package math.functions;

import org.trianacode.taskgraph.Unit;
import triana.types.ComplexSampleSet;
import triana.types.ComplexSpectrum;
import triana.types.Const;
import triana.types.EmptyingType;
import triana.types.GraphType;
import triana.types.SampleSet;
import triana.types.Spectrum;
import triana.types.util.FlatArray;
import triana.types.util.Str;

/**
 * An Exp unit to apply the exponential function to the elements of an input data array. The array can be real or
 * complex. The returned data type will be real or complex as appropriate.
 * <p/>
 * This Unit obeys the conventions of Triana Type 2 data types.
 *
 * @author Bernard Schutz
 * @version 2.1 13 January 2001
 */
public class Exp extends Unit {

    /**
     * Offset parameters will be added to each element of the input array.
     */
    double offsetReal = 0.0;
    double offsetImag = 0.0;
    boolean complex, complexInput, complexOffset;

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Applies the exponential function to the elements of the input data.";
    }

    /**
     * ********************************************* ** USER CODE of Exp goes here    ***
     * *********************************************
     */
    public void process() {

        Object input, output;

        output = null;
        input = getInputAtNode(0);
        if (input instanceof EmptyingType) {
            return;
        }
        complexOffset = (offsetImag != 0.0);
        if ((input instanceof SampleSet) && complexOffset) {
            output = new ComplexSampleSet((SampleSet) input);
        } else if ((input instanceof Spectrum) && complexOffset) {
            output = new ComplexSpectrum((Spectrum) input);
        } else {
            output = input;
        }
        Class outputClass = output.getClass();
        //setOutputType(outputClass);

        if (input instanceof GraphType) {
            FlatArray tempR, tempI;
            int dv, j;
            double d, offsetCos, offsetSin;
            double[] inputdataR, inputdataI;
            for (dv = 0; dv < ((GraphType) input).getDependentVariables(); dv++) {
                if (((GraphType) input).isArithmeticArray(dv)) {
                    tempR = new FlatArray(((GraphType) input).getDataArrayReal(dv));
                    inputdataR = (double[]) tempR.getFlatArray();
                    complexInput = ((GraphType) input).isDependentComplex(dv);
                    complex = (complexOffset || complexInput);
                    if (complex) {
                        if (complexInput) {
                            tempI = new FlatArray(((GraphType) input).getDataArrayImag(dv));
                            inputdataI = (double[]) tempI.getFlatArray();
                            for (j = 0; j < inputdataI.length; j++) {
                                d = Math.exp(inputdataR[j] + offsetReal);
                                inputdataR[j] = d * Math.cos(inputdataI[j] + offsetImag);
                                inputdataI[j] = d * Math.sin(inputdataI[j] + offsetImag);
                            }
                            ((GraphType) output).setDataArrayReal(tempR.restoreArray(false), dv);
                            ((GraphType) output).setDataArrayImag(tempI.restoreArray(false), dv);
                        } else {
                            offsetCos = Math.cos(offsetImag);
                            offsetSin = Math.sin(offsetImag);
                            inputdataI = new double[inputdataR.length];
                            for (j = 0; j < inputdataI.length; j++) {
                                d = Math.exp(inputdataR[j] + offsetReal);
                                inputdataR[j] = d * offsetCos;
                                inputdataI[j] = d * offsetSin;
                            }
                            ((GraphType) output).setDataArrayReal(tempR.restoreArray(false), dv);
                            tempR.setFlatArray(inputdataI);
                            ((GraphType) output).setDataArrayImag(tempR.restoreArray(true), dv);
                        }
                    } else {
                        for (j = 0; j < inputdataR.length; j++) {
                            inputdataR[j] = Math.exp(inputdataR[j] + offsetReal);
                        }
                        ((GraphType) output).setDataArrayReal(tempR.restoreArray(false), dv);
                    }
                }
            }
        } else if (input instanceof Const) {
            double r, i, d;
            r = ((Const) input).getReal();
            complexInput = ((Const) input).isComplex();
            complex = (complexOffset || complexInput);
            if (complex) {
                if (complexInput) {
                    i = ((Const) input).getImag();
                    d = Math.exp(r + offsetReal);
                    r = d * Math.cos(i + offsetImag);
                    i = d * Math.sin(i + offsetImag);
                } else {
                    d = Math.exp(r + offsetReal);
                    r = d * Math.cos(offsetImag);
                    i = d * Math.sin(offsetImag);
                }
                ((Const) output).setImag(i);
                ((Const) output).setReal(r);
            } else {
                r = Math.exp(r + offsetReal);
                ((Const) output).setReal(r);
            }
        }

        output(output);

    }


    /**
     * Initialses information specific to Exp.
     */
    public void init() {
        super.init();

//        setUseGUIBuilder(true);
//
//        setResizableInputs(false);
//        setResizableOutputs(true);
//
//        // This is to ensure that we receive arrays containing double-precision numbers
//        setRequireDoubleInputs(true);
//        setCanProcessDoubleArrays(true);

        setDefaultInputNodes(1);
        setMinimumInputNodes(1);
        setMaximumInputNodes(Integer.MAX_VALUE);

        setDefaultOutputNodes(1);
        setMinimumOutputNodes(1);
        setMaximumOutputNodes(Integer.MAX_VALUE);


        String guilines = "";
        guilines += "Set real part of offset of argument $title offsetReal Scroller -1.0 1.0 0.0\n";
        guilines += "Set imaginary part of offset of argument $title offsetImag Scroller -1.0 1.0 0.0\n";
        setGUIBuilderV2Info(guilines);


    }

    /**
     * @return the GUI information for this unit. It uses the addGUILine function to add lines to the GUI interface.
     *         Such lines must in the specified GUI text format.
     */
//    public void setGUIInformation() {
//        addGUILine("Set real part of offset of argument $title offsetReal Scroller -1.0 1.0 0.0");
//        addGUILine("Set imaginary part of offset of argument $title offsetImag Scroller -1.0 1.0 0.0");
//    }


    /**
     * Resets Exp
     */
    public void reset() {
        super.reset();
    }


    /**
     * Saves Exp's parameters.
     */
//    public void saveParameters() {
//        saveParameter("offsetReal", offsetReal);
//        saveParameter("offsetImag", offsetImag);
//    }

    /**
     * Used to set each of Exp's parameters.
     */
    public void parameterUpdate(String name, Object value) {
        //updateGUIParameter(name, value);

        if (name.equals("offsetReal")) {
            offsetReal = Str.strToDouble((String) value);
        }
        if (name.equals("offsetImag")) {
            offsetImag = Str.strToDouble((String) value);
        }
    }


    /**
     * @return a string containing the names of the types allowed to be input to Exp, each separated by a white space.
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
        return "Exp.html";
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


}



















