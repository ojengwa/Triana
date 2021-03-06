package org.trianacode.shiwaall.dax;

import org.apache.commons.logging.Log;
import org.trianacode.annotation.CustomGUIComponent;
import org.trianacode.annotation.Parameter;
import org.trianacode.annotation.Tool;
import org.trianacode.enactment.logging.Loggers;
import org.trianacode.shiwaall.string.PatternCollection;
import org.trianacode.taskgraph.Task;
import org.trianacode.taskgraph.annotation.TaskConscious;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * Created by IntelliJ IDEA.
 * User: Ian Harvey
 * Date: Aug 19, 2010
 * Time: 11:08:09 AM
 * To change this template use File | Settings | File Templates.
 */

@Tool //(renderingHints = {"DAX File"})
public class FileUnit implements TaskConscious, Displayer {

    /** The Constant PHYSICAL_FILE. */
    public static final String PHYSICAL_FILE = "physicalFile";
    
    /** The Constant FILE_URL. */
    public static final String FILE_URL = "locationString";

    /** The location string. */
    @Parameter
    public String locationString = "";   //wtf?

    /** The physical file. */
    @Parameter
    public boolean physicalFile = false;
    
    /** The file protocol. */
    @Parameter
    public String fileProtocol = "";
    
    /** The number of files. */
    @Parameter
    public int numberOfFiles = 1;


    /** The naming pattern. */
    @Parameter
    public PatternCollection namingPattern = null;

    /** The file name. */
    public String fileName = "a.txt";
    
    /** The collection. */
    public boolean collection = false;
    
    /** The one2one. */
    public boolean one2one = false;

    /** The task. */
    public Task task;
    
    /** The dev log. */
    private static Log devLog = Loggers.DEV_LOGGER;

    /**
     * File unit process.
     *
     * @param in the in
     * @return the uuid
     */
    @org.trianacode.annotation.Process(gather = true)
    public UUID fileUnitProcess(List in) {
        fileName = task.getToolName();

        devLog.debug("File : " + fileName + " Collection = " + collection + " Number of files : " + numberOfFiles);
        DaxFileChunk thisFile = new DaxFileChunk();

        thisFile.setFilename(fileName);
        thisFile.setPhysicalFile(physicalFile);
        thisFile.setFileLocation(locationString);
//        System.out.println("File location " + thisFile.getFileLocation());
        thisFile.setFileProtocol(fileProtocol);
        thisFile.setUuid(UUID.randomUUID());
        thisFile.setCollection(collection);
        thisFile.setNumberOfFiles(numberOfFiles);
        thisFile.setNamePattern(namingPattern);
        devLog.debug("setting files one2one as " + one2one);
        thisFile.setOne2one(one2one);

        DaxRegister register = DaxRegister.getDaxRegister();
        register.addFile(thisFile);

        devLog.debug("\nList in is size: " + in.size() + " contains : " + in.toString() + ".\n ");

        for (Object object : in) {
            if (object instanceof DaxSettingObject) {
                devLog.debug("Found settings object");
                DaxSettingObject dso = (DaxSettingObject) object;
                int number = dso.getNumberFiles();
                devLog.debug("Found number of files from settings object : " + number);
                thisFile.setNumberOfFiles(number);
                numberOfFiles = number;
            } else if (object instanceof DaxJobChunk) {
                DaxJobChunk jobChunk = (DaxJobChunk) object;

                devLog.debug("Previous job was : " + jobChunk.getJobName());

                devLog.debug("Adding : " + thisFile.getFilename() + " as an output to job : " + jobChunk.getJobName());
                jobChunk.addOutFileChunk(thisFile);

                devLog.debug("Adding : " + jobChunk.getJobName() + " as an input to file : " + thisFile.getFilename());
                thisFile.addInJobChunk(jobChunk);

            } else if (object instanceof UUID) {
                UUID uuid = (UUID) object;
                DaxJobChunk jobChunk = register.getJobChunkFromUUID(uuid);

                if (jobChunk != null) {

                    devLog.debug("\nPrevious job was : " + jobChunk.getJobName() + "\n");

//                    devLog.debug("Adding : " + thisFile.getFilename() + " as an output to job : " + jobChunk.getJobName());
                    jobChunk.addOutFileChunk(thisFile);
                    jobChunk.getArgBuilder().addOutputFile(fileName);

//                    devLog.debug("Adding : " + jobChunk.getJobName() + " as an input to file : " + thisFile.getFilename());
                    thisFile.addInJobChunk(jobChunk);
                } else {
                    devLog.debug("jobChunk not found in register");
                }
            } else {
                devLog.debug("Cannot handle input : " + object.getClass().getName());
            }

        }

        if (in.size() == 0) {
            devLog.debug("No jobs enter fileUnit : " + thisFile.getFilename());
        }

        return thisFile.getUuid();
    }

    /**
     * Sets the params.
     */
    public void setParams() {
        if (task != null) {
            task.setParameter("numberOfFiles", numberOfFiles);
            task.setParameter("collection", collection);
            task.setParameter("one2one", one2one);

            task.setParameter(PHYSICAL_FILE, physicalFile);
            task.setParameter(FILE_URL, locationString);
            task.setParameter("fileProtocol", fileProtocol);

            if (namingPattern != null) {
                task.setParameter("namingPattern", namingPattern);
            }
        }
    }

    /**
     * Gets the params.
     *
     * @return the params
     */
    public void getParams() {
        if (task != null) {
            fileName = getFileName();
            collection = isCollection();
            numberOfFiles = getNumberOfFiles();
            namingPattern = getNamingPattern();
            one2one = isOne2one();
            physicalFile = isPhysicalFile();
            locationString = getFileLocation();
            fileProtocol = getFileProtocol();
        }
    }

    /**
     * Various getting and setting of parameters.
     *
     * @param name the name
     */
    public void changeToolName(String name) {
        fileName = name;
        if (task != null) {
            devLog.debug("Changing tool " + task.getToolName() + " to : " + name);
            task.setParameter("fileName", name);
            task.setToolName(name);

        }
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    private String getFileName() {
        Object o = task.getParameter("fileName");
        if (o != null && !((String) o).equals("")) {
            return (String) o;
        }
        return fileName;
    }

    /**
     * Gets the naming pattern.
     *
     * @return the naming pattern
     */
    public PatternCollection getNamingPattern() {
        Object o = task.getParameter("namingPattern");
        if (o instanceof PatternCollection) {
            devLog.debug("Found : " + o.toString());
            return (PatternCollection) o;
        }
        return null;
    }

    /**
     * Checks if is collection.
     *
     * @return true, if is collection
     */
    public boolean isCollection() {
        Object o = task.getParameter("collection");
        if (o != null) {
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
        }
        return false;
    }

    /**
     * Checks if is one2one.
     *
     * @return true, if is one2one
     */
    public boolean isOne2one() {
        Object o = task.getParameter("one2one");
        if (o != null) {
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
        }
        return false;
    }

    /**
     * Gets the number of files.
     *
     * @return the number of files
     */
    public int getNumberOfFiles() {
        Object o = task.getParameter("numberOfFiles");
        if (o != null) {
            int value = (Integer) o;
            if (value > 1) {
                return value;
            }
            return 1;
        }
        return 1;
    }

    /**
     * Checks if is physical file.
     *
     * @return true, if is physical file
     */
    private boolean isPhysicalFile() {
        Object o = task.getParameter(PHYSICAL_FILE);
        if (o != null) {
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
        }
        return false;
    }

    /**
     * Gets the file location.
     *
     * @return the file location
     */
    public String getFileLocation() {
        Object o = task.getParameter(FILE_URL);
        if (o != null && !((String) o).equals("")) {
            return (String) o;
        }
        return "";
    }

    /**
     * Gets the file protocol.
     *
     * @return the file protocol
     */
    public String getFileProtocol() {
        Object o = task.getParameter("fileProtocol");
        if (o != null && !((String) o).equals("")) {
            return (String) o;
        }
        return "";
    }

    /**
     * Gets the component.
     *
     * @return the component
     */
    @CustomGUIComponent
    public Component getComponent() {
        return new JLabel("This is a non-gui tool. Use the triana-pegasus-gui toolbox for more options.");
    }

    /* (non-Javadoc)
     * @see org.trianacode.taskgraph.annotation.TaskConscious#setTask(org.trianacode.taskgraph.Task)
     */
    @Override
    public void setTask(Task task) {
        this.task = task;
        getParams();
    }

    /* (non-Javadoc)
     * @see org.trianacode.shiwaall.dax.Displayer#displayMessage(java.lang.String)
     */
    @Override
    public void displayMessage(String string) {
        devLog.debug(string);
    }
}

//
//        List<DaxJobChunk> jcl = new ArrayList<DaxJobChunk>();
//
//
//            List<List> inList = (List<List>)in;
//            for(int i = 0; i < inList.size(); i++){
//                Object o = inList.get(i);
//                if(o instanceof List){
//                    List<DaxJobChunk> innerList = (List)o;
//
//                    for(int j = 0; j < innerList.size(); j++){
//                        Object o2 = innerList.get(j);
//                        if(o2 instanceof DaxJobChunk){
//                            devLog.debug("Found a DaxJobChunk");
//                            if(j == (innerList.size() - 1)){
//                                ((DaxJobChunk)o2).addOutFile(fileName);
//                                ((DaxJobChunk)o2).addOutFileChunk(thisFile);
//                                devLog.debug("Added output file to job " + (i+1) + " of " + inList.size() + ".");
//                                ((DaxJobChunk) o2).setOutputFilename(fileName);
//                                ((DaxJobChunk) o2).setOutputFileChunk(thisFile);
//                                devLog.debug("Telling the jobs before and after this fileUnit that this file was in between them");
//                            }
//                            jcl.add((DaxJobChunk) o2);
//                        }
//                        else{
//                            devLog.debug("Found " + o2.getClass().toString() + " instead of a DaxJobChunk.");
//                        }
//                    }
//
//
//                }
//                else{
//                    devLog.debug("Incoming list didn't contain a list, contains : " + o.getClass().toString());
//                }
//            }
//            if(in.size() == 0){
//                devLog.debug("No jobs handed to this one. Creating job stub with this filename");
//                DaxJobChunk jc = new DaxJobChunk();
//                jc.setOutputFilename(fileName);
//                jc.setStub(true);
//                jcl.add(jc);
//            }
//        return jcl;
//
//

//     register.listAll();