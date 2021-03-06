package org.trianacode.shiwaall.gui.guiUnits;

import org.apache.commons.logging.Log;
import org.trianacode.annotation.CustomGUIComponent;
import org.trianacode.annotation.Tool;
import org.trianacode.enactment.logging.Loggers;
import org.trianacode.gui.hci.GUIEnv;
import org.trianacode.shiwaall.dax.Displayer;
import org.trianacode.shiwaall.dax.JobUnit;
import org.trianacode.shiwaall.gui.models.DaxJobComponentModel;
import org.trianacode.taskgraph.annotation.TaskConscious;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * Created by IntelliJ IDEA.
 * User: Ian Harvey
 * Date: 23/05/2011
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
@Tool(renderingHints = {DaxJobComponentModel.DAX_JOB_RENDERING_HINT}, minimumInputs = 1, minimumOutputs = 1)
public class DaxJob extends JobUnit implements Displayer, TaskConscious, ItemListener, ActionListener {

    /** The dev log. */
    private static Log devLog = Loggers.DEV_LOGGER;
    
    /** The upper panel. */
    JPanel upperPanel; // = new JPanel(new GridLayout(4, 2, 5, 5));
    
    /** The lower panel. */
    JPanel lowerPanel; // = new JPanel();
    
    /** The lower panel1. */
    JPanel lowerPanel1; // = new JPanel(new GridLayout(3, 1, 5, 5));
    
    /** The lower panel auto. */
    JPanel lowerPanelAuto; // = new JPanel(new GridLayout(1, 2, 5, 5));
    
    /** The lower panel scatter. */
    JPanel lowerPanelScatter; // = new JPanel(new GridLayout(1, 2, 5, 5));
    
    /** The lower panel one2 one. */
    JPanel lowerPanelOne2One; // = new JPanel(new GridLayout(1, 2, 5, 5));

    /** The name field. */
    JTextField nameField;
    
    /** The exec field. */
    JTextField execField;
    
    /** The args field. */
    JTextField argsField;
    
    /** The collect label. */
    JLabel collectLabel;
    
    /** The number jobs label. */
    JLabel numberJobsLabel;
    
    /** The number input files label. */
    JLabel numberInputFilesLabel;

    /** The files per job combo. */
    JComboBox filesPerJobCombo;
    
    /** The jobs combo. */
    JComboBox jobsCombo;
    
    /** The collection box. */
    JCheckBox collectionBox;
    
    /** The auto check. */
    JRadioButton autoCheck;
    
    /** The scatter check. */
    JRadioButton scatterCheck;
    
    /** The spread check. */
    JRadioButton spreadCheck;
    
    /** The one2one check. */
    JRadioButton one2oneCheck;
    
    /** The input switch field. */
    private JTextField inputSwitchField;
    
    /** The output switch field. */
    private JTextField outputSwitchField;

    /**
     * Fake process.
     *
     * @param list the list
     * @return the uuid
     */
    @org.trianacode.annotation.Process(gather = true)
    public UUID fakeProcess(List list) {
        return this.process(list);
    }

    /* (non-Javadoc)
     * @see org.trianacode.shiwaall.dax.JobUnit#getComponent()
     */
    @CustomGUIComponent
    public Component getComponent() {
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));

//        JobActionPerformer actionPerformer = new JobActionPerformer();
//        JobItemPerformer itemPerformer = new JobItemPerformer();

        upperPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        upperPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Job"));

        JLabel nameLabel = new JLabel("Job Name :");
        upperPanel.add(nameLabel);
        nameField = new JTextField(jobName);

//        if(task != null){
//            changeToolName(task.getToolName());
//        }
        upperPanel.add(nameField);

        JLabel jobExecLabel = new JLabel("Executable location");
        upperPanel.add(jobExecLabel);
        execField = new JTextField("");
        execField.setText(exec);
        upperPanel.add(execField);


        JLabel argsLabel = new JLabel("Job Args :");
        upperPanel.add(argsLabel);
        argsField = new JTextField("");
        if (task != null) {
            argsField.setText((String) task.getParameter("args"));
        }
        upperPanel.add(argsField);


        collectionBox = new JCheckBox("Collection", collection);
        collectionBox.addItemListener(this);
        upperPanel.add(collectionBox);

        collectLabel = new JLabel("");
        if (collection) {
            collectLabel.setText("Collection of jobs.");
        } else {
            collectLabel.setText("Not a collection");
        }
        upperPanel.add(collectLabel);

        mainPane.add(upperPanel);

        JPanel switchArgsPanel = new JPanel(new GridLayout(2,1));

        JPanel inputSwitchPanel = new JPanel(new BorderLayout());
        JLabel inputSwitchLabel = new JLabel("Input switch : ");
        inputSwitchField = new JTextField("");
        inputSwitchField.setText(inputSwitch);
        inputSwitchPanel.add(inputSwitchLabel, BorderLayout.WEST);
        inputSwitchPanel.add(inputSwitchField, BorderLayout.CENTER);

        JPanel outputSwitchPanel = new JPanel(new BorderLayout());
        JLabel outputSwitchLabel = new JLabel("Output switch : ");
        outputSwitchField = new JTextField("");
        outputSwitchField.setText(outputSwitch);
        outputSwitchPanel.add(outputSwitchLabel, BorderLayout.WEST);
        outputSwitchPanel.add(outputSwitchField, BorderLayout.CENTER);

        switchArgsPanel.add(inputSwitchPanel);
        switchArgsPanel.add(outputSwitchPanel);

        mainPane.add(switchArgsPanel);

        lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
        lowerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Job Collection Options"));

        lowerPanel1 = new JPanel(new GridLayout(3, 1, 5, 5));
        lowerPanelAuto = new JPanel(new GridLayout(1, 2, 5, 5));
        lowerPanelScatter = new JPanel(new GridLayout(1, 2, 5, 5));
        lowerPanelOne2One = new JPanel(new GridLayout(1, 2, 5, 5));

        autoCheck = new JRadioButton("Auto (Fully connect all incoming nodes)", autoConnect);
        scatterCheck = new JRadioButton("Scatter (Each job takes n-files as input) ", !autoConnect);
        spreadCheck = new JRadioButton("Spread (Connect incoming files to n-jobs) ", !autoConnect);
        one2oneCheck = new JRadioButton("One-2-one (Job duplicated to number of input files)", !autoConnect);

        ButtonGroup radios = new ButtonGroup();
        radios.add(autoCheck);
        radios.add(scatterCheck);
        radios.add(spreadCheck);
        radios.add(one2oneCheck);

        autoCheck.addItemListener(this);
        scatterCheck.addItemListener(this);
        spreadCheck.addItemListener(this);
        one2oneCheck.addItemListener(this);

        lowerPanel1.add(autoCheck);
        lowerPanel1.add(scatterCheck);
        lowerPanel1.add(spreadCheck);
        lowerPanel1.add(one2oneCheck);

        numberJobsLabel = new JLabel("No. jobs : " + numberOfJobs);

        numberInputFilesLabel = new JLabel("No. Files/job : " + fileInputsPerJob);

        final String[] numbers = new String[100];
        for (int i = 1; i < 100; i++) {
            numbers[i] = "" + i;
        }

        jobsCombo = new JComboBox(numbers);
        jobsCombo.setActionCommand("jobsCombo");
        jobsCombo.setSelectedItem("" + numberOfJobs);
        jobsCombo.addActionListener(this);

        lowerPanelAuto.add(numberJobsLabel);
        lowerPanelAuto.add(jobsCombo);

        filesPerJobCombo = new JComboBox(numbers);
        filesPerJobCombo.setActionCommand("filesPerJobCombo");
        filesPerJobCombo.setSelectedItem("" + fileInputsPerJob);
        filesPerJobCombo.addActionListener(this);

        lowerPanelScatter.add(numberInputFilesLabel);
        lowerPanelScatter.add(filesPerJobCombo);


        JButton argsFromFileButton = new JButton("Job arguments");
        argsFromFileButton.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = chooser.showDialog(GUIEnv.getApplicationFrame(), "File");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    if (f != null) {
                        getArgs(f);
                    }
                }
            }
        });
        lowerPanel.add(argsFromFileButton);

        lowerPanel.add(lowerPanel1);
        lowerPanel.add(lowerPanelAuto);
        lowerPanel.add(lowerPanelScatter);
        mainPane.add(lowerPanel);


        setEnabling(lowerPanel, collection);
        if (collection) {
            setEnabling(lowerPanelAuto, autoConnect);
            setEnabling(lowerPanelScatter, !autoConnect);
        }

        String word = "no";
        if (task != null) {
            word = task.getQualifiedToolName();
        }
        mainPane.add(new JLabel("Task? " + word));

        JButton apply = new JButton("Apply");
        apply.setActionCommand("apply");
        apply.addActionListener(this);
        mainPane.add(apply);
        return mainPane;
    }

    /**
     * Gets the args.
     *
     * @param file the file
     * @return the args
     */
    private void getArgs(File file) {
        argsStringArray = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                argsStringArray.add(line);
            }
            System.out.printf("Will try to make " + argsStringArray.size() + " jobs.");
            numberOfJobs = argsStringArray.size();
            apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Apply.
     */
    public void apply() {
        changeToolName(nameField.getText());
        exec = execField.getText();
        jobName = nameField.getText();
        args = argsField.getText();
        if (!collection) {
            connectPattern = DaxJob.AUTO_CONNECT;
        }
        inputSwitch = inputSwitchField.getText();
        outputSwitch = outputSwitchField.getText();
        setParams();
    }

    /**
     * Sets the enabling.
     *
     * @param c the c
     * @param enable the enable
     */
    public void setEnabling(Component c, boolean enable) {
        c.setEnabled(enable);
        if (c instanceof Container) {
            Component[] arr = ((Container) c).getComponents();
            for (Component anArr : arr) {
                setEnabling(anArr, enable);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.trianacode.shiwaall.dax.JobUnit#displayMessage(java.lang.String)
     */
    @Override
    public void displayMessage(String string) {
        devLog.debug(string);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("apply")) {
            apply();
        }
        if (ae.getActionCommand().equals("jobsCombo")) {
            numberOfJobs = Integer.parseInt((String) jobsCombo.getSelectedItem());
            numberJobsLabel.setText("No. files : " + numberOfJobs);
        }
        if (ae.getActionCommand().equals("filesPerJobCombo")) {
            fileInputsPerJob = Integer.parseInt((String) filesPerJobCombo.getSelectedItem());
            numberInputFilesLabel.setText("No. files : " + fileInputsPerJob);
        }
    }


    /* (non-Javadoc)
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == collectionBox) {
            if (collectionBox.isSelected()) {
                collectLabel.setText("Collection of jobs.");
                collection = true;
                setEnabling(lowerPanel, true);
            } else {
                collectLabel.setText("Not a collection");
                collection = false;
                setEnabling(lowerPanel, false);
            }
        }
        if (itemEvent.getSource() == autoCheck) {
            if (autoCheck.isSelected()) {
                connectPattern = JobUnit.AUTO_CONNECT;
                setEnabling(lowerPanelAuto, true);
                setEnabling(lowerPanelScatter, false);
                setEnabling(lowerPanelOne2One, false);
            }
        }

        if (itemEvent.getSource() == scatterCheck) {
            if (scatterCheck.isSelected()) {
                connectPattern = JobUnit.SCATTER_CONNECT;
                setEnabling(lowerPanelAuto, false);
                setEnabling(lowerPanelScatter, true);
                setEnabling(lowerPanelOne2One, false);
            }
        }
        if (itemEvent.getSource() == spreadCheck) {
            if (spreadCheck.isSelected()) {
                connectPattern = JobUnit.SPREAD_CONNECT;
                setEnabling(lowerPanelAuto, true);
                setEnabling(lowerPanelScatter, false);
                setEnabling(lowerPanelOne2One, false);
            }
        }
        if (itemEvent.getSource() == one2oneCheck) {
            if (one2oneCheck.isSelected()) {
                connectPattern = JobUnit.ONE2ONE_CONNECT;
                setEnabling(lowerPanelAuto, false);
                setEnabling(lowerPanelScatter, false);
                setEnabling(lowerPanelOne2One, true);
            }
        }
    }

}
