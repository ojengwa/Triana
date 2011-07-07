package org.trianacode.shiwa;

import org.shiwa.desktop.gui.SHIWADesktopPanel;
import org.trianacode.gui.hci.ApplicationFrame;
import org.trianacode.gui.hci.GUIEnv;
import org.trianacode.taskgraph.TaskGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: ian
 * Date: 22/02/2011
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class PublishWorkflow extends AbstractAction {

    public PublishWorkflow() {
        putValue(SHORT_DESCRIPTION, "Publish");
        putValue(NAME, "Publish");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Publishing Workflow");

        ApplicationFrame frame = GUIEnv.getApplicationFrame();
        TaskGraph tg = frame.getSelectedTaskgraph();

        if (tg != null) {

            TrianaEngineHandler teh = new TrianaEngineHandler(tg, frame.getEngine());


            JPanel popup = new SHIWADesktopPanel(teh);
            ((SHIWADesktopPanel) popup).addSHIWADesktopListener(new TrianaShiwaListener(frame.getEngine()));
            DisplayDialog dialog = new DisplayDialog(popup);

        } else {
            JOptionPane.showMessageDialog(frame, "No taskgraph selected");
        }
    }
}
