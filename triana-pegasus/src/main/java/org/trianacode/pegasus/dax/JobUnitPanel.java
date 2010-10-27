package org.trianacode.pegasus.dax;

import org.apache.commons.logging.Log;
import org.trianacode.enactment.logging.Loggers;
import org.trianacode.gui.panels.ParameterPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * User: ian
 * Date: Sep 10, 2010
 * Time: 2:25:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class JobUnitPanel extends ParameterPanel {
    private static final int AUTO_CONNECT = 0;
    private static final int SCATTER_CONNECT = 1;
    private static final int ONE2ONE_CONNECT = 2;
    private static final int SPREAD_CONNECT = 3;

    private JPanel upperPanel = new JPanel(new GridLayout(3,2,5,5));
    private JPanel lowerPanel = new JPanel();
    private JTextField nameField = new JTextField("");
    private JTextField argsField = new JTextField("");
    JLabel collectLabel = new JLabel("");

    private int numberOfJobs = 1;
    private int fileInputsPerJob = 1;
    private boolean collection = false;
    private boolean autoConnect = true;
    private int connectPattern = AUTO_CONNECT;



    //  public boolean isAutoCommitByDefault(){return true;}

    @Override
    public void init() {
        getParams();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        upperPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Job"));
        JLabel nameLabel = new JLabel("Job Name :");
        upperPanel.add(nameLabel);

        changeToolName(getTask().getToolName());
        upperPanel.add(nameField);

        JLabel argsLabel = new JLabel("Job Args :");
        upperPanel.add(argsLabel);

        argsField.setText((String) getParameter("args"));
        upperPanel.add(argsField);

        final JCheckBox collectionBox = new JCheckBox("Collection", collection);
        collectionBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
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
        });
        upperPanel.add(collectionBox);

        if(collection){
            collectLabel.setText("Collection of jobs.");
        }else{
            collectLabel.setText("Not a collection");
        }
        upperPanel.add(collectLabel);

        add(upperPanel);

        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
        lowerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Job Collection Options"));
        JPanel lowerPanel1 = new JPanel(new GridLayout(3, 1, 5, 5));
        final JPanel lowerPanelAuto = new JPanel(new GridLayout(1, 2, 5, 5));
        final JPanel lowerPanelScatter = new JPanel(new GridLayout(1, 2, 5, 5));
        final JPanel lowerPanelOne2One = new JPanel(new GridLayout(1, 2, 5, 5));

        final JCheckBox autoCheck = new JCheckBox("Auto (Fully connect all incoming nodes)", autoConnect);
        final JCheckBox scatterCheck = new JCheckBox("Scatter (Each job takes n-files as input) ", !autoConnect);
        final JCheckBox spreadCheck = new JCheckBox("Spread (Connect incoming files to n-jobs) ", !autoConnect);
        final JCheckBox one2oneCheck = new JCheckBox("One-2-one (Job duplicated to number of input files)", !autoConnect);


        autoCheck.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (autoCheck.isSelected()) {
                    connectPattern = AUTO_CONNECT;
                    setEnabling(lowerPanelAuto, true);
                    setEnabling(lowerPanelScatter, false);
                    setEnabling(lowerPanelOne2One, false);
                    spreadCheck.setSelected(false);
                    scatterCheck.setSelected(false);
                    one2oneCheck.setSelected(false);
                }
            }
        });
        scatterCheck.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (scatterCheck.isSelected()) {
                    connectPattern = SCATTER_CONNECT;
                    setEnabling(lowerPanelAuto, false);
                    setEnabling(lowerPanelScatter, true);
                    setEnabling(lowerPanelOne2One, false);
                    autoCheck.setSelected(false);
                    spreadCheck.setSelected(false);
                    one2oneCheck.setSelected(false);
                }
            }
        });
        spreadCheck.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (spreadCheck.isSelected()) {
                    connectPattern = SPREAD_CONNECT;
                    setEnabling(lowerPanelAuto, true);
                    setEnabling(lowerPanelScatter, false);
                    setEnabling(lowerPanelOne2One, false);
                    autoCheck.setSelected(false);
                    scatterCheck.setSelected(false);
                    one2oneCheck.setSelected(false);
                }
            }
        });
        one2oneCheck.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                if (one2oneCheck.isSelected()) {
                    connectPattern = ONE2ONE_CONNECT;
                    setEnabling(lowerPanelAuto, false);
                    setEnabling(lowerPanelScatter, false);
                    setEnabling(lowerPanelOne2One, true);
                    autoCheck.setSelected(false);
                    scatterCheck.setSelected(false);
                    spreadCheck.setSelected(false);
                }
            }
        });

        lowerPanel1.add(autoCheck);
        lowerPanel1.add(scatterCheck);
        lowerPanel1.add(spreadCheck);
        lowerPanel1.add(one2oneCheck);

        final JLabel numberJobsLabel = new JLabel("No. jobs : " + numberOfJobs);
//        final JSlider jobSlide = new JSlider(1, 999, 1);
//        jobSlide.setValue(numberOfJobs);
//        jobSlide.setMajorTickSpacing(100);
//        jobSlide.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent changeEvent) {
//                numberOfJobs = jobSlide.getValue();
//                numberJobsLabel.setText("No. jobs : " + numberOfJobs);
//            }
//        });
//        numberJobsLabel.setText("No. Jobs : " + numberOfJobs);
//        lowerPanelAuto.add(numberLabel);
//        lowerPanelAuto.add(jobSlide);
//
        final JLabel numberInputFilesLabel = new JLabel("No. Files/job : " + fileInputsPerJob);
//        final JSlider fileSlide = new JSlider(1, 999, 1);
//        fileSlide.setMajorTickSpacing(100);
//        fileSlide.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent changeEvent) {
//                fileInputsPerJob = fileSlide.getValue();
//                numberInputFilesLabel.setText("No. Files/job : " + fileInputsPerJob);
//            }
//        });
//        lowerPanelScatter.add(numberInputFilesLabel);
//        lowerPanelScatter.add(fileSlide);
        final String[] numbers = new String[100];
        for(int i = 1; i < 100; i++){
            numbers[i] = "" + i;
        }

        final JComboBox jobsCombo = new JComboBox(numbers);
        jobsCombo.setSelectedItem("" + numberOfJobs);
        jobsCombo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                numberOfJobs = Integer.parseInt((String)jobsCombo.getSelectedItem());
                numberJobsLabel.setText("No. files : " + numberOfJobs);
            }
        });
        lowerPanelAuto.add(numberJobsLabel);
        lowerPanelAuto.add(jobsCombo);

        final JComboBox filesPerJobCombo = new JComboBox(numbers);
        filesPerJobCombo.setSelectedItem("" + fileInputsPerJob);
        filesPerJobCombo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                fileInputsPerJob = Integer.parseInt((String)filesPerJobCombo.getSelectedItem());
                numberInputFilesLabel.setText("No. files : " + fileInputsPerJob);
            }
        });
        lowerPanelScatter.add(numberInputFilesLabel);
        lowerPanelScatter.add(filesPerJobCombo);

        lowerPanel.add(lowerPanel1);
        lowerPanel.add(lowerPanelAuto);
        lowerPanel.add(lowerPanelScatter);
        add(lowerPanel);

        setEnabling(lowerPanel, collection);
        if(collection){
            setEnabling(lowerPanelAuto, autoConnect);
            setEnabling(lowerPanelScatter, !autoConnect);
        }
    }


    public void getParams(){
        collection = isCollection();
        numberOfJobs = getNumberOfJobs();
        connectPattern = getConnectPattern();
        fileInputsPerJob = getFileInputsPerJob();
    }

    private void setParams(){
        getTask().setParameter("args", argsField.getText());
        getTask().setParameter("numberOfJobs", numberOfJobs);
        getTask().setParameter("fileInputsPerJob", fileInputsPerJob);
        getTask().setParameter("collection", collection);
        getTask().setParameter("connectPattern", connectPattern);
    }

    public void changeToolName(String name){
        nameField.setText(name);
        log("Changing tool " + getTask().getToolName() + " to : " + name);
        getTask().setParameter("jobName", name);
        getTask().setToolName(name);
    }

    public void applyClicked(){ apply();}
    public void okClicked(){ apply();};
    public void apply(){
        changeToolName(nameField.getText());
        setParams();
    }

    public void setEnabling(Component c,boolean enable) {
        c.setEnabled(enable);
        if (c instanceof Container)
        {
            Component [] arr = ((Container) c).getComponents();
            for (int j=0;j<arr.length;j++) { setEnabling(arr[j],enable); }
        }
    }

    private boolean isCollection(){
        Object o = getParameter("collection");
        //   System.out.println("Returned object from param *collection* : " + o.getClass().getCanonicalName() + " : " + o.toString());
        if(o.equals(true)){
            return true;
        }else{
            return false;
        }
    }

    private int getNumberOfJobs(){
        Object o = getParameter("numberOfJobs");
        //   System.out.println("Returned object from param *numberOfJobs* : " + o.getClass().getCanonicalName() + " : " + o.toString());
        if(o != null){
            int value = (Integer)o;
            if(value > 1 ){
                return value;
            }
            return 1;
        }
        return 1;
    }
    private int getFileInputsPerJob(){
        Object o = getParameter("fileInputsPerJob");
        //   System.out.println("Returned object from param *numberOfJobs* : " + o.getClass().getCanonicalName() + " : " + o.toString());
        if(o != null){
            int value = (Integer)o;
            if(value > 1 ){
                return value;
            }
            return 1;
        }
        return 1;
    }
    private int getConnectPattern(){
        Object o = getParameter("connectPattern");
        if(o != null){
            int value = (Integer)o;
            switch(value){
                case 0 : return AUTO_CONNECT;
                case 1 : return SCATTER_CONNECT;
                case 2 : return ONE2ONE_CONNECT;
                case 3 : return SPREAD_CONNECT;
            }
        }
        return AUTO_CONNECT;
    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    private void log(String s){
        Log log = Loggers.DEV_LOGGER;
        log.debug(s);
        //System.out.println(s);
    }
}
