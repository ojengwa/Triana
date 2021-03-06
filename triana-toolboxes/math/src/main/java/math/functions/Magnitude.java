package math.functions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import org.trianacode.taskgraph.Unit;
import triana.types.ComplexSampleSet;
import triana.types.ComplexSpectrum;
import triana.types.Const;
import triana.types.EmptyingType;
import triana.types.GraphType;
import triana.types.SampleSet;
import triana.types.Spectrum;
import triana.types.TrianaType;
import triana.types.util.FlatArray;

/**
 * A Magnitude unit to compute the magnitudes of the elements of a data array.  The input array can be real or complex.
 * The magnitude is the length in the Argand diagram, or the absolute value if the input is real.
 * <p/>
 * This Unit obeys the conventions of Triana Type 2 data types.
 *
 * @author Bernard Schutz
 * @version 2.1 13 January 2001
 * @see TrianaType
 */
public class Magnitude extends Unit {

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Computes the magnitudes (absolute values) of the input data set";
    }

    /**
     * ********************************************* ** USER CODE of Magnitude goes here    ***
     * *********************************************
     */
    public void process() {


        Object input, output;

        input = getInputAtNode(0);
        if (input instanceof EmptyingType) {
            return;
        }
        if (input instanceof ComplexSampleSet) {
            output = new SampleSet((ComplexSampleSet) input);
        } else if (input instanceof ComplexSpectrum) {
            output = new Spectrum((ComplexSpectrum) input);
        } else if (input instanceof TrianaType) {
            output = ((TrianaType) input).copyMe();
        } else {
            output = input;
        }

        Class outputClass = output.getClass();
        //setOutputType(outputClass);


        if (input instanceof GraphType) {
            FlatArray tempR, tempI;
            int dv, j;
            double d;
            double[] inputdataR, inputdataI;
            for (dv = 0; dv < ((GraphType) input).getDependentVariables(); dv++) {
                if (((GraphType) input).isArithmeticArray(dv)) {
                    tempR = new FlatArray(((GraphType) input).getDataArrayReal(dv));
                    inputdataR = (double[]) tempR.getFlatArray();
                    if (((GraphType) input).isDependentComplex(dv)) {
                        tempI = new FlatArray(((GraphType) input).getDataArrayImag(dv));
                        inputdataI = (double[]) tempI.getFlatArray();
                        for (j = 0; j < inputdataI.length; j++) {
                            inputdataR[j] = Math.sqrt(inputdataR[j] * inputdataR[j] + inputdataI[j] * inputdataI[j]);
                        }
                        if (!(output instanceof SampleSet) && !(output instanceof Spectrum)) {
                            ((GraphType) output).setDataArrayImag(null, dv);
                        }
                    } else {
                        for (j = 0; j < inputdataR.length; j++) {
                            inputdataR[j] = Math.abs(inputdataR[j]);
                        }
                    }
                    ((GraphType) output).setDataArrayReal(tempR.restoreArray(false), dv);
                }
            }
        } else if (input instanceof Const) {
            double r, i, d;
            r = ((Const) input).getReal();
            if (((Const) input).isComplex()) {
                i = ((Const) input).getImag();
                r = Math.sqrt(r * r + i * i);
                ((Const) output).setImag(0.0);
            } else {
                r = Math.abs(r);
            }
            ((Const) output).setReal(r);

        }

        if (input instanceof GraphType && ((GraphType) input).getTitle() != null) {
            ((GraphType) output).setTitle(((GraphType) input).getTitle());
        }
        output(output);


    }


    /**
     * Initialses information specific to Magnitude.
     */
    public void init() {
        super.init();

//        setResizableInputs(false);
//        setResizableOutputs(true);
//        // This is to ensure that we receive arrays containing double-precision numbers
//        setRequireDoubleInputs(true);
//        setCanProcessDoubleArrays(true);
        setDefaultInputNodes(1);
        setMinimumInputNodes(1);
        setMaximumInputNodes(1);

        setDefaultOutputNodes(1);
        setMinimumOutputNodes(1);
        setMaximumOutputNodes(Integer.MAX_VALUE);


    }

    /**
     * Resets Magnitude
     */
    public void reset() {
        super.reset();
    }

    /**
     * Saves Magnitude's parameters to the parameter file.
     */
    public void saveParameters() {
    }

    /**
     * Loads Magnitude's parameters of from the parameter file.
     */
    public void setParameter(String name, String value) {
    }

    /**
     * @return a string containing the names of the types allowed to be input to Magnitude, each separated by a white
     *         space.
     */
    public String[] getInputTypes() {
        return new String[]{"triana.types.GraphType", "triana.types.Const"};
    }

    public String[] getOutputTypes() {
        return new String[]{"triana.types.GraphType", "triana.types.Const"};
    }

    /**
     *
     * @returns the location of the help file for this unit.
     */
    public String getHelpFile() {
        return "Magnitude.html";
    }


    /**
     * @return Magnitude's parameter window sp that Triana can move and display it.
     */
    public Window getParameterWindow() {
        return null;
    }


    /**
     * Captures the events thrown out by Magnitude.
     */
//    public void actionPerformed(ActionEvent e) {
//        super.actionPerformed(e);   // we need this
//
//    }
}




















