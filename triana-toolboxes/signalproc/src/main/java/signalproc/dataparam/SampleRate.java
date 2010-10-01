package signalproc.dataparam;

import org.trianacode.taskgraph.Unit;
import triana.types.Const;
import triana.types.Signal;

/**
 * A SampleRate unit to extract the sampling rate from a data set.
 *
 * @author B F Schutz
 * @version 1.0 26 Feb 2001
 */
public class SampleRate extends Unit {

    /**
     * ********************************************* ** USER CODE of SampleRate goes here    ***
     * *********************************************
     */
    public void process() throws Exception {
        Signal input = (Signal) getInputAtNode(0);
        output(new Const(input.getSamplingRate()));


    }


    /**
     * Initialses information specific to SampleRate.
     */
    public void init() {
        super.init();

        // set these to true if your unit can process double-precision
        // arrays
//        setRequireDoubleInputs(false);
//        setCanProcessDoubleArrays(false);
//
//        setResizableInputs(false);
//        setResizableOutputs(true);
        setDefaultInputNodes(1);
        setMinimumInputNodes(1);
        setMaximumInputNodes(Integer.MAX_VALUE);

        setDefaultOutputNodes(1);
        setMinimumOutputNodes(1);
        setMaximumOutputNodes(Integer.MAX_VALUE);

    }

    /**
     * Called when the reset button is pressed within the MainTriana Window
     */
    public void reset() {
        super.reset();
    }

    /**
     * Called when the stop button is pressed within the MainTriana Window
     */
    public void stopping() {
        super.stopping();
    }

    /**
     * Called when the start button is pressed within the MainTriana Window
     */
//    public void starting() {
//        super.starting();
//    }

    /**
     * Saves SampleRate's parameters.
     */
    public void saveParameters() {
    }

    /**
     * Used to set each of SampleRate's parameters. This should NOT be used to update this unit's user interface
     */
    public void setParameter(String name, String value) {
    }

    /**
     * Used to update the widget in this unit's user interface that is used to control the given parameter name.
     */
    public void updateWidgetFor(String name) {
    }

    /**
     * @return a string containing the names of the types allowed to be input to SampleRate, each separated by a white
     *         space.
     */
//    public String inputTypes() {
//        return "Signal";
//    }
//
//    /**
//     * @return a string containing the names of the types output from SampleRate, each separated by a white space.
//     */
//    public String outputTypes() {
//        return "Const";
//    }


    public String[] getInputTypes() {
        return new String[]{"triana.types.Signal"};
    }

    public String[] getOutputTypes() {
        return new String[]{"triana.types.Const"};
    }

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Get the sampling rate of a signal data set";
    }

    /**
     *
     * @returns the location of the help file for this unit.  
     */
    public String getHelpFile() {
        return "SampleRate.html";
    }
}



