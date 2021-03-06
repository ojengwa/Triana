package common.output;

import org.trianacode.gui.panels.UnitPanel;
import org.trianacode.gui.util.Env;
import org.trianacode.gui.windows.ErrorDialog;
import org.trianacode.taskgraph.Unit;
import org.trianacode.taskgraph.util.FileUtils;
import triana.types.AsciiComm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A TypeExportPanel which exports to file any type which implements the AsciiComm interface
 *
 * @author Ian Taylor
 * @version 1.0 1st Decmeber 1999
 * @see UnitPanel
 */
public class TypeExportPanel extends UnitPanel implements ActionListener {
    boolean anAsciiType = false;
    PrintWriter bw = null;

    /**
     * The number of this particular type loaded in.
     */
    JTextField numberSaved;

    JTextField fileName;

    JLabel lab2, lab3;

    JButton chooseFile;
    JButton rewind;
    JButton closeFile;

    boolean beginning = true;

    /**
     * Creates a new TypeImporterPanel for BTImporter.
     */
    public TypeExportPanel() {
        super();
    }

    public void setObject(Unit unit) {
        super.setObject(unit);

        createWidgets();

        layoutPanel();
    }

    public void createWidgets() {
        numberSaved = new JTextField("0", 10);
        fileName = new JTextField(Env.getString("None"), 10);

        lab2 = new JLabel("Number Saved So Far", JLabel.CENTER);
        lab3 = new JLabel("File Name", JLabel.CENTER);

        closeFile = new JButton("Close File");
        closeFile.addActionListener(this);
        rewind = new JButton(Env.getString("Rewind"));
        rewind.addActionListener(this);
        chooseFile = new JButton(Env.getString("Browse"));
        chooseFile.addActionListener(this);
    }


    /**
     * The layout of the RawToGen window.
     */
    public void layoutPanel() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gb);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10, 0, 0, 0);

        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.RELATIVE;
        gb.setConstraints(lab2, c);
        add(lab2);

        c.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(numberSaved, c);
        add(numberSaved);

        gb.setConstraints(lab3, c);
        add(lab3);

        c.fill = GridBagConstraints.BOTH;
        gb.setConstraints(fileName, c);
        add(fileName);

        c.insets = new Insets(10, 5, 5, 5);

        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        gb.setConstraints(chooseFile, c);
        add(chooseFile);

        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(rewind, c);
        add(rewind);

        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(closeFile, c);
        add(closeFile);
    }

    /**
     * Output the type of data importer into this unit i.e. the actual name of the class.
     */
    public void sendType(AsciiComm type) {
        bw.println(type.getClass().getName());
    }


    /**
     * Outputs the inpput data.  If this is unsuccessful then we return -1, 0 otherwise
     */
    public int sendNextPacket(Object type) {

        Class interfaces[] = type.getClass().getInterfaces();
        anAsciiType = false;

        for (int i = 0; i < interfaces.length; ++i) {
            if (interfaces[i].getName().equals("triana.types.AsciiComm")) {
                anAsciiType = true;
            }
        }

        if (!anAsciiType) {
            new ErrorDialog(null, Env.getString("NotAnAsciiComm"));
            return -1;
        }

        if (bw == null) {
            openFile();
            if (bw == null) {
                fileName.setText("none");
                ErrorDialog.show(null, Env.getString("FileError"));
                return -1;
            }
        }

        if (anAsciiType) {
            sendType((AsciiComm) type);
            try {
                ((AsciiComm) type).outputToStream(bw);
            }
            catch (IOException ee) {
                ErrorDialog.show(null, Env.getString("CantWrite") + " " + fileName.getText());
            }
        }


        return 0;
    }

    public String getHelpFile() {
        return "TypeImporter.html";
    }

    /**
     * Closes the current file
     */
    public void closeFile() {
        if (bw != null) {
            bw.close();
        }
        bw = null;
//        GUIUtils.getParentJFrameFor(this).setVisible(false);
    }

    public void openFile() {
        closeFile();

        if (fileName.getText().equals(Env.getString("None"))) {
            return;
        }

        numberSaved.setText("0");

        bw = FileUtils.createWriter(fileName.getText());

        if (bw == null) {
            fileName.setText(Env.getString("None"));
            ErrorDialog.show(null, Env.getString("FilleError"));
        }

        updateParameter("file", fileName.getText());
        beginning = true;
    }


    /**
     * Checks for the next, previous and load buttons
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (e.getSource() instanceof JTextField) {
            openFile();
        }

        if (command.equals(Env.getString("Rewind"))) {
            openFile();
        }

        if (command.equals(Env.getString("CloseFile"))) {
            closeFile();
        }

        if (command.equals(Env.getString("Browse"))) {
            /*int r = TFileDialog.showDialog(this,
                    Env.getString("TypeExportDial"),
                                         TFileDialog.SAVE);
            if (r!=TFileDialog.APPROVE) return;
            if (TFileDialog.getPathAndFile() != null)
                fileName.setText(TFileDialog.getPathAndFile());
            openFile();*/
        }
    }
}















