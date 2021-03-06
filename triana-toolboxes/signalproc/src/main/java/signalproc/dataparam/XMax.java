package signalproc.dataparam;

import org.trianacode.taskgraph.Unit;
import triana.types.Const;
import triana.types.VectorType;

/**
 * A XMax unit to return the value at the start of the range of the independent variable.
 *
 * @author B F Schutz
 * @version 2.0 28 Feb 2001
 */
public class XMax extends Unit {

    /**
     * ********************************************* ** USER CODE of XMax goes here    ***
     * *********************************************
     */
    public void process() throws Exception {
        VectorType input = (VectorType) getInputAtNode(0);
        if (input.isTriplet()) {
            output(new Const(input.getXTriplet().getLast()));
        } else {
            double[] x = input.getXArray();
            int length = x.length;
            if (input.isIndependentComplex(0)) {
                double[] xim = input.getXImag();
                output(new Const(x[length - 1], xim[length - 1]));
            } else {
                output(new Const(x[length - 1]));
            }

        }
    }

    /**
     * Initialses information specific to XMax.
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
        setMaximumInputNodes(1);

        setDefaultOutputNodes(1);
        setMinimumOutputNodes(0);
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
     * Saves XMax's parameters.
     */
    public void saveParameters() {
    }

    /**
     * Used to set each of XMax's parameters. This should NOT be used to update this unit's user interface
     */
    public void setParameter(String name, String value) {
    }

    /**
     * Used to update the widget in this unit's user interface that is used to control the given parameter name.
     */
    public void updateWidgetFor(String name) {
    }

    /**
     * @return a string containing the names of the types allowed to be input to XMax, each separated by a white space.
     */
//    public String inputTypes() {
//        return "VectorType";
//    }
//
//    /**
//     * @return a string containing the names of the types output from XMax, each separated by a white space.
//     */
//    public String outputTypes() {
//        return "Const";
//    }

    public String[] getInputTypes() {
        return new String[]{"triana.types.VectorType"};
    }

    public String[] getOutputTypes() {
        return new String[]{"triana.types.Const"};
    }

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Returns the value at the upper end of the range of the independent variable";
    }

    /**
     *
     * @returns the location of the help file for this unit.  
     */
    public String getHelpFile() {
        return "XMax.html";
    }
}



