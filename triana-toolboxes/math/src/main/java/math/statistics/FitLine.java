package math.statistics;

import org.trianacode.taskgraph.Unit;
import triana.types.VectorType;

/**
 * A FitLine unit to do linear least-squares fit to the input data. Only the real parts of the independent and dependent
 * data are used in the fit. The unit outputs a VectorType of the same length whose data are replaced by the fitted
 * function, plus Constants for the slope and intercept.
 *
 * @author B F Schutz
 * @version 1.0 28 Feb 2001
 */
public class FitLine extends Unit {

    String m = "(auto)";
    String b = "(auto)";


    /**
     * ********************************************* ** USER CODE of FitLine goes here    ***
     * *********************************************
     */
    public void process() throws Exception {
        VectorType input = (VectorType) getInputAtNode(0);

        double[] x = input.getXReal();
        double[] y = input.getDataReal();

        double sumX = 0;
        double sumXY = 0;
        double sumX2 = 0;
        double sumY = 0;
        double n = x.length;

        double slope = 0;
        double intercept = 0;
        double denom;
        int k;

        for (k = 0; k < n; k++) {
            sumX += x[k];
            sumXY += x[k] * y[k];
            sumX2 += x[k] * x[k];
            sumY += y[k];
        }

        if (m.indexOf("auto") != -1) {

            if (b.indexOf("auto") != -1) {
                denom = sumX * sumX - n * sumX2;
                slope = (sumY * sumX - n * sumXY) / denom;
                intercept = (sumXY * sumX - sumY * sumX2) / denom;
            } else {
                intercept = Double.parseDouble(b.trim());
                slope = (sumXY - intercept * sumX) / sumX2;
            }

        } else {

            if (b.indexOf("auto") != -1) {
                slope = Double.parseDouble(m.trim());
                intercept = (sumY - slope * sumX) / n;
            } else {
                intercept = Double.parseDouble(b.trim());
                slope = Double.parseDouble(m.trim());
            }

        }

        for (k = 0; k < n; k++) {
            y[k] = slope * x[k] + intercept;
        }

        String newTitle = "Linear fit to data";
        if ((input.getTitle() != null) && !input.getTitle().equals("")) {
            newTitle += " of " + input.getTitle();
        }
        input.setTitle(newTitle);

        output(input);

    }


    /**
     * Initialses information specific to FitLine.
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
        guilines += "Give slope ( auto = fit the slope ) $title m TextField (auto)\n";
        guilines += "Give y-intercept ( auto = fit the intercept ) $title b TextField (auto)\n";
        setGUIBuilderV2Info(guilines);
    }

    /**
     * @return the GUI information for this unit. It uses the addGUILine function to add lines to the GUI interface.
     *         Such lines must in the specified GUI text format.
     */
//    public void setGUIInformation() {
//        addGUILine("Give slope ( auto = fit the slope ) $title m TextField (auto)");
//        addGUILine("Give y-intercept ( auto = fit the intercept ) $title b TextField (auto)");
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
//     * Saves FitLine's parameters.
//     */
//    public void saveParameters() {
//        saveParameter("m", m);
//        saveParameter("b", b);
//    }


    /**
     * Used to set each of FitLine's parameters.
     */
    public void parameterUpdate(String name, Object value) {
        //updateGUIParameter(name, value);

        if (name.equals("m")) {
            m = (String) value;
        }
        if (name.equals("b")) {
            b = (String) value;
        }
    }

    /**
     * Don't need to use this for GUI Builder units as everthing is updated by triana automatically
     */
    public void updateWidgetFor(String name) {
    }

    /**
     * @return a string containing the names of the types allowed to be input to FitLine, each separated by a white
     *         space.
     */
    public String[] getInputTypes() {
        return new String[]{"triana.types.VectorType"};
    }

    public String[] getOutputTypes() {
        return new String[]{"triana.types.VectorType"};
    }

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Fits a straight line to data";
    }

    /**
     *
     * @returns the location of the help file for this unit.  
     */
    public String getHelpFile() {
        return "FitLine.html";
    }
}




