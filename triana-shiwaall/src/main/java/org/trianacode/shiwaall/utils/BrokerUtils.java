package org.trianacode.shiwaall.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.shiwa.desktop.data.description.core.WorkflowImplementation;
import org.shiwa.desktop.data.description.workflow.SHIWAProperty;
import org.trianacode.enactment.logging.stampede.StampedeLog;
import org.trianacode.taskgraph.Task;
import org.trianacode.taskgraph.TaskGraph;
import org.trianacode.taskgraph.TaskGraphManager;
import org.trianacode.taskgraph.service.Scheduler;
import org.trianacode.taskgraph.service.SchedulerInterface;
import org.trianacode.taskgraph.service.TrianaServer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * Created by IntelliJ IDEA.
 * User: Ian Harvey
 * Date: 05/05/2012
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class BrokerUtils {

    /**
     * Gets the time stamp.
     *
     * @return the time stamp
     */
    public static String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_HH-mm-ss-SS_z");
        return dateFormat.format(new Date());
    }

    /**
     * Gets the result bundle.
     *
     * @param url the url
     * @param key the key
     * @return the result bundle
     */
    public static File getResultBundle(String url, String key) {

        System.out.println("Requesting " + key + " from " + url);

        try {
            boolean tryAgain = true;

            HttpResponse response = null;
            HttpClient client = null;
            while (tryAgain) {
                client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url + "?action=file&key=" + key);

                response = client.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    tryAgain = false;
                } else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            File bundle = writeFile(response);

            client.getConnectionManager().shutdown();

            return bundle;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Write file.
     *
     * @param response the response
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File writeFile(HttpResponse response) throws IOException {
        InputStream input = response.getEntity().getContent();

        File bundle = File.createTempFile("received", ".zip");
        OutputStream out = new FileOutputStream(bundle);
        byte buf[] = new byte[1024];
        int len;
        while ((len = input.read(buf)) > 0)
            out.write(buf, 0, len);
        out.close();
        input.close();

        System.out.println("Got bundle at : " + bundle.getAbsolutePath());
        return bundle;
    }


    /**
     * Wait for exec.
     *
     * @param url the url
     * @param i the i
     * @param uuid the uuid
     * @param execBundleName the exec bundle name
     * @return the string
     */
    public static String waitForExec(String url, int i, String uuid, String execBundleName) {
        while (i > 0) {
            String key = getJSONReply(url, uuid, execBundleName);
            if (key != null) {
                return key;
            } else {
                try {
                    System.out.println("No key, sleeping. " + i + " remaining");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i -= 1000;
            }
        }
        return null;
    }


    /**
     * Gets the jSON reply.
     *
     * @param url the url
     * @param uuid the uuid
     * @param execBundleName the exec bundle name
     * @return the jSON reply
     */
    public static String getJSONReply(String url, String uuid, String execBundleName) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url + "?action=json");
//            HttpGet httpGet = new HttpGet(url + "?action=byid&&uuid=" + uuid);

            HttpResponse response = client.execute(httpGet);
            System.out.println(response.getEntity().getContentLength() + " from json");
            InputStream input = response.getEntity().getContent();
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);

            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                System.out.printf(line);
                stringBuilder.append(line);
            }
            client.getConnectionManager().shutdown();

            JSONTokener jsonTokener = new JSONTokener(stringBuilder.toString());
            JSONArray jsonArray = new JSONArray(jsonTokener);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString("name");

                if (name.equals(execBundleName)) {
                    String key = jsonObject.getString("key");
                    System.out.println("Name = " + execBundleName + " key = " + key);
                    return key;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Post bundle.
     *
     * @param hostAddress the host address
     * @param routingKey the routing key
     * @param execBundleName the exec bundle name
     * @param tempBundleFile the temp bundle file
     * @return the string
     */
    public static String postBundle(
            String hostAddress, String routingKey, String execBundleName, File tempBundleFile) {

        try {
            FileBody fileBody = new FileBody(tempBundleFile);
            StringBody routing = new StringBody(routingKey);
            StringBody numtasks = new StringBody("1");

            StringBody name = new StringBody(execBundleName);

            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("file", fileBody);
            multipartEntity.addPart("routingkey", routing);
            multipartEntity.addPart("numtasks", numtasks);
            multipartEntity.addPart("name", name);

            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(hostAddress);
            httpPost.setEntity(multipartEntity);
            System.out.println("Sending " + httpPost.getEntity().getContentLength()
                    + " bytes to " + hostAddress);
            HttpResponse response = client.execute(httpPost);

            InputStream input = response.getEntity().getContent();
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);

            String line = null;
            StringBuilder keyString = new StringBuilder();
            while ((line = br.readLine()) != null) {
                System.out.printf("\n%s", line);
                keyString.append(line);
            }
            client.getConnectionManager().shutdown();


            return findKey(keyString.toString());

        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Find key.
     *
     * @param s the s
     * @return the string
     */
    private static String findKey(String s) {

        int openTag = s.indexOf("<uuid>");
        int closeTag = s.indexOf("</uuid>");

        if (openTag != -1 && closeTag != -1 && closeTag > openTag) {
            return s.substring(openTag + 6, closeTag);
        }
        return null;
    }

    /**
     * Adds the parent details to sub workflow.
     *
     * @param impl the impl
     * @param runUUID the run uuid
     * @param parentID the parent id
     * @param jobID the job id
     * @param jobInstID the job inst id
     */
    public static void addParentDetailsToSubWorkflow(
            WorkflowImplementation impl, UUID runUUID, UUID parentID, String jobID, String jobInstID) {

        impl.addProperty(new SHIWAProperty(StampedeLog.PARENT_UUID_STRING,
                parentID.toString()));

        impl.addProperty(new SHIWAProperty(StampedeLog.RUN_UUID_STRING,
                runUUID.toString()));

        impl.addProperty(new SHIWAProperty(StampedeLog.JOB_ID, jobID));

        impl.addProperty(new SHIWAProperty(StampedeLog.JOB_INST_ID, jobInstID));
    }

    /**
     * Gets the scheduler for task graph.
     *
     * @param taskGraph the task graph
     * @return the scheduler for task graph
     */
    public static Scheduler getSchedulerForTaskGraph(TaskGraph taskGraph) {
        TrianaServer server = TaskGraphManager.getTrianaServer(taskGraph);
        SchedulerInterface sched = server.getSchedulerInterface();
        if (sched instanceof Scheduler) {
            return (Scheduler) sched;
        }
        return null;
    }

    /**
     * Prepare subworkflow.
     *
     * @param task the task
     * @param runUUID the run uuid
     * @param workflowImplementation the workflow implementation
     */
    public static void prepareSubworkflow(Task task, UUID runUUID, WorkflowImplementation workflowImplementation) {
        Scheduler scheduler = getSchedulerForTaskGraph(task.getParent());

        cleanBundleProperties(workflowImplementation);

        addParentDetailsToSubWorkflow(
                workflowImplementation,
                runUUID,
                scheduler.stampedeLog.getRunUUID(),
                "unit:" + task.getQualifiedToolName(),
                scheduler.stampedeLog.getTaskNumber(task).toString()
        );
    }

    /**
     * Clean bundle properties.
     *
     * @param workflowImplementation the workflow implementation
     */
    private static void cleanBundleProperties(WorkflowImplementation workflowImplementation) {
        ArrayList<SHIWAProperty> toRemove = new ArrayList<SHIWAProperty>();

        for (SHIWAProperty property : workflowImplementation.getProperties()) {
            String title = property.getTitle();
            if (title.equals(StampedeLog.PARENT_UUID_STRING) ||
                    title.equals(StampedeLog.JOB_ID) ||
                    title.equals(StampedeLog.JOB_INST_ID) ||
                    title.equals(StampedeLog.RUN_UUID_STRING)) {
                toRemove.add(property);
            }
        }

        for (SHIWAProperty removing : toRemove) {
            workflowImplementation.getProperties().remove(removing);
        }
    }
}
