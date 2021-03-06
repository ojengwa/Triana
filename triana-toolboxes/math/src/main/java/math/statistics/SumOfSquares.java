package math.statistics;

import org.trianacode.taskgraph.Unit;
import triana.types.Const;
import triana.types.GraphType;
import triana.types.util.FlatArray;
import triana.types.util.Str;

/**
 * A SumOfSquares unit to compute sum of squares, average of squares, or rms (square root of average of squares) of any
 * data set. The mean is not subtracted before doing the calculation. For complex data the squared magnitude of each
 * point is used.
 *
 * @author B F Schutz
 * @version 1.01 28 Feb 2001
 */
public class SumOfSquares extends Unit {

    int dv = 0;
    String type = "SumOfSquares";


    /**
     * ********************************************* ** USER CODE of SumOfSquares goes here    ***
     * *********************************************
     */
    public void process() throws Exception {
        GraphType input = (GraphType) getInputAtNode(0);

        FlatArray flatR, flatI;
        double[] dataR, dataI;
        double yI, yR;
        int k, len;
        double answer = Double.NaN;

        if (input.isArithmeticArray(dv)) {
            if (input.isDependentComplex(dv)) {
                flatR = new FlatArray(input.getDataArrayReal(dv));
                flatI = new FlatArray(input.getDataArrayImag(dv));
                dataR = (double[]) flatR.getFlatArray();
                dataI = (double[]) flatI.getFlatArray();
                len = dataR.length;
                answer = 0;
                for (k = 0; k < len; k++) {
                    yR = dataR[k];
                    yI = dataI[k];
                    answer += yR * yR + yI * yI;
                }
            } else {
                flatR = new FlatArray(input.getDataArrayReal(dv));
                dataR = (double[]) flatR.getFlatArray();
                len = dataR.length;
                answer = 0;
                for (k = 0; k < len; k++) {
                    yR = dataR[k];
                    answer += yR * yR;
                }
            }
            if (type.equals("AvgOfSquares")) {
                answer /= len;
            } else if (type.equals("RMS")) {
                answer = Math.sqrt(answer / len);
            }

        }

        output(new Const(answer));

    }


    /**
     * Initialses information specific to SumOfSquares.
     */
    public void init() {
        super.init();

//        setUseGUIBuilder(true);
//
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

        String guilines = "";
        guilines += "Which dependent variable do you want the sum of squares of? $title dv IntScroller 0 5 0\n";
        guilines += "Choose value to be computed: $title type Choice SumOfSquares AvgOfSquares RMS\n";
        setGUIBuilderV2Info(guilines);

    }

    /**
     * @return the GUI information for this unit. It uses the addGUILine function to add lines to the GUI interface.
     *         Such lines must in the specified GUI text format.
     */
//    public void setGUIInformation() {
//        addGUILine("Which dependent variable do you want the sum of squares of? $title dv IntScroller 0 5 0");
//        addGUILine("Choose value to be computed: $title type Choice SumOfSquares AvgOfSquares RMS");
//    }

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
//
//    /**
//     * Saves SumOfSquares's parameters.
//     */
//    public void saveParameters() {
//        saveParameter("dv", dv);
//        saveParameter("type", type);
//    }


    /**
     * Used to set each of SumOfSquares's parameters.
     */
    public void parameterUpdate(String name, Object value) {
        //updateGUIParameter(name, value);

        if (name.equals("dv")) {
            dv = Str.strToInt((String) value);
        }
        if (name.equals("type")) {
            type = (String) value;
        }
    }

    /**
     * Don't need to use this for GUI Builder units as everthing is updated by triana automatically
     */
    public void updateWidgetFor(String name) {
    }

    /**
     * @return a string containing the names of the types allowed to be input to SumOfSquares, each separated by a white
     *         space.
     */
    public String[] getInputTypes() {
        return new String[]{"triana.types.GraphType"};
    }

    public String[] getOutputTypes() {
        return new String[]{"triana.types.Const"};
    }

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Compute sum of squares, average of squares, or rms of data set";
    }

    /**
     *
     * @returns the location of the help file for this unit.  
     */
    public String getHelpFile() {
        return "SumOfSquares.html";
    }
}




