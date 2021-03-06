package org.trianacode.shiwaall.handler;

import org.shiwa.desktop.data.transfer.WorkflowEngineHandler;
import org.shiwa.desktop.gui.SHIWADesktop;
import org.shiwa.desktop.gui.util.InterfaceUtils;
import org.trianacode.TrianaInstance;
import org.trianacode.enactment.AddonUtils;
import org.trianacode.enactment.addon.ConversionAddon;
import org.trianacode.gui.action.ActionDisplayOptions;
import org.trianacode.gui.action.files.ImageAction;
import org.trianacode.gui.hci.ApplicationFrame;
import org.trianacode.gui.hci.GUIEnv;
import org.trianacode.gui.panels.DisplayDialog;
import org.trianacode.taskgraph.TaskGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;


// TODO: Auto-generated Javadoc
/**
* Created by IntelliJ IDEA.
* User: Ian Harvey
* Date: 22/02/2011
* Time: 14:36
* To change this template use File | Settings | File Templates.
*/
public class PublishWorkflow extends AbstractAction implements ActionDisplayOptions {

    /**
     * Instantiates a new publish workflow.
     */
    public PublishWorkflow() {
        this(DISPLAY_BOTH);
    }

    /**
     * Instantiates a new publish workflow.
     *
     * @param displayOption the display option
     */
    public PublishWorkflow(int displayOption) {
        putValue(SHORT_DESCRIPTION, "Publish");
        putValue(NAME, "Publish");
        if ((displayOption == DISPLAY_ICON) || (displayOption == DISPLAY_BOTH)) {
//            putValue(SMALL_ICON, GUIEnv.getIcon("upload_small.png"));
//            InterfaceUtils.initImages();
            Icon icon = InterfaceUtils.X16_ICON;
            putValue(SMALL_ICON, icon);
        }

    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent actionEvent) {


        final ApplicationFrame frame = GUIEnv.getApplicationFrame();
        final TaskGraph tg = frame.getSelectedTaskgraph();

        WorkflowEngineHandler handler = buildHandler(tg);
        if (handler != null) {
            System.out.println("Handler built " + handler.getClass().getCanonicalName());
            publish(handler, frame.getEngine());
        }
    }

    /**
     * Builds the handler.
     *
     * @param tg the tg
     * @return the workflow engine handler
     */
    public static WorkflowEngineHandler buildHandler(TaskGraph tg) {
        System.out.println("Publishing Workflow");

        if (tg == null || tg.getTasks(false).length == 0) {
            JOptionPane.showMessageDialog(null, "No taskgraph selected," +
                    " or currently selected taskgraph has no tasks");
            return null;
        } else {
            System.out.println(tg.getQualifiedTaskName());



            ArrayList<ConversionAddon> converters = new ArrayList<ConversionAddon>();
            Set<Object> addons = AddonUtils.getCLIaddons(GUIEnv.getApplicationFrame().getEngine());
            for (Object addon : addons) {
                if (addon instanceof ConversionAddon) {
                    converters.add((ConversionAddon) addon);
                }
            }

            Object[] choices = new Object[converters.size() + 1];
            choices[0] = "taskgraph";
            for (int i = 0; i < converters.size(); i++) {
                ConversionAddon addon = converters.get(i);
//                choices[i] = addon.getShortOption();
                choices[i + 1] = addon;
            }
            Object addon = JOptionPane.showInputDialog(GUIEnv.getApplicationFrame(),
                    "Please select the format for the workflow definition to be submitted in.",
                    "Select Definition Type",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    choices,
                    choices[0]);

            if (addon != null) {
                InputStream displayStream = getImageStream();
//                TransferSignature signature = new TransferSignature();
                if (!(addon instanceof ConversionAddon)) {
                    return new TrianaEngineHandler(tg, tg.getProperties().getEngine(), displayStream);
                } else if (addon.toString().equals("IWIR")) {

                    return new TrianaIWIRHandler(tg, displayStream);
                } else {
//                    InputStream definitionStream = ((ConversionAddon) addon).toolToWorkflowFileInputStream(tg);
//                    GenericWorkflowHandler handler = new GenericWorkflowHandler(definitionStream, displayStream);
//                    handler.setSignature(signature);
//                    handler.setLanguage(addon.toString());
//                    handler.setDefinitionName(tg.getToolName());
//                    return handler;
                    System.out.printf("Handler not found");
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the image stream.
     *
     * @return the image stream
     */
    public static InputStream getImageStream() {
        InputStream displayStream = null;
        try {
            File imageFile = File.createTempFile("image", ".jpg");
            ImageAction.save(imageFile, 1, "jpg");
            if (imageFile.length() > 0) {
                displayStream = new FileInputStream(imageFile);
                System.out.println("Display image created : " + imageFile.toURI());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return displayStream;
    }

    /**
     * Publish.
     *
     * @param handler the handler
     * @param engine the TrianaInstange "engine"
     */
    public static void publish(WorkflowEngineHandler handler, TrianaInstance engine) {
        SHIWADesktop shiwaDesktop = new SHIWADesktop(handler, SHIWADesktop.ButtonOption.SHOW_TOOLBAR);
        DisplayDialog dialog = null;
        shiwaDesktop.addExecutionListener(new TrianaShiwaListener(engine));
        Image icon = InterfaceUtils.X16_ICON.getImage();
        dialog = new DisplayDialog(shiwaDesktop.getPanel(), "SHIWA Desktop", icon);
        shiwaDesktop = null;

    }
}
