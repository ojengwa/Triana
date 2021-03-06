package signalproc.converters;

import org.trianacode.taskgraph.Unit;
import triana.types.Histogram;
import triana.types.VectorType;

/**
 * A HistTo2D unit to convert a histogram into a 2D data set, mainly for drawing a graph of the histogram.  The 2D data
 * set represents the corners of a "step" graph of the histogram. If either of the end-bins is an overspill bin
 * extending to infinity (or minus infinity), a finite-width bin is substituted with the same height, provided the value
 * of the histogram for that bin is non-zero.
 *
 * @author B F Schutz and Ian Taylor
 * @version 1.0 alpha 06 Aug 1997
 */
public class HistToVect extends Unit {

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Converts a Histogram TrianaType into a VectorType TrianaType";
    }

    /**
     * ********************************************* ** USER CODE of HistTo2D goes here    ***
     * *********************************************
     */
    public void process() {
        Histogram hist = (Histogram) getInputAtNode(0);
        VectorType histout = convert(hist);
        output(histout);
    }

    public static VectorType convert(Histogram hist) {
        hist.x = hist.getXArray();
        hist.y = hist.getData();

        hist.x = hist.getFiniteDelimiters(0);

//        hist.setIndependentLabels(0,labelx);
        //      hist.setDependentLabels(0,labely);

        return hist;
    }

/*        int size = hist.hsize();
int bins = hist.bsize();
int delimitersOffset = 0;
int xOffset = 1;
int dataIndexStart = 0;
int dataIndexEnd = size - 1;
int dsize = 2 * size + 2;
boolean upperSpillBinFull = false;
boolean lowerSpillBinFull = false;
if (bins < size) { // both upper and lower spill bins exist
    delimitersOffset = -1;
    dataIndexStart = 1;
    dataIndexEnd = size - 2;
    if (hist.data[0] == 0.0) { // lower spill bin empty
        xOffset = -1;
        dsize = dsize - 2;
        }
    else lowerSpillBinFull = true;
    if (hist.data[size - 1] == 0.0) { // upper spill bin empty
        dsize = dsize - 2;
        }
    else upperSpillBinFull = true;
    }
else if (bins == size) {  // only upper spill bin exists
    dataIndexEnd = size - 2;
    if (hist.data[size - 1] == 0.0) { // upper spill bin empty
        dsize = dsize - 2;
        }
    else upperSpillBinFull = true;
    }
double[] x = new double[dsize];
double[] y = new double[dsize];
y[0] = 0.0;
y[dsize - 1] = 0.0;
if (lowerSpillBinFull) {
    x[0] = 2. * hist.delimiters[0] - hist.delimiters[1];
    x[1] = x[0];
    x[2] = hist.delimiters[0];
    y[1] = hist.data[0];
    y[2] = y[1];
    }
else {
    x[0] = hist.delimiters[0];
    }
if (upperSpillBinFull) {
    x[dsize - 1] = 2.* hist.delimiters[bins - 1] - hist.delimiters[bins - 2];
    x[dsize - 2] = x[dsize - 1];
    x[dsize - 3] = hist.delimiters[bins - 1];
    y[dsize - 2] = hist.data[size - 1];
    y[dsize - 3] = y[dsize - 2];
    }
else {
    x[dsize - 1] = hist.delimiters[bins - 1];
    }
int delimitersIndex = dataIndexStart + delimitersOffset;
int xyIndex = 2 * dataIndexStart + xOffset;
for (int i = dataIndexStart; i <= dataIndexEnd; i++) {
    x[xyIndex] = hist.delimiters[delimitersIndex];
    y[xyIndex] = hist.data[i];
    delimitersIndex++;
    xyIndex++;
    x[xyIndex] = hist.delimiters[delimitersIndex];
    y[xyIndex] = hist.data[i];
    xyIndex++;
    }

VectorType v = new VectorType(x,y);
v.setIndependentLabels(0,hist.binLabel);
v.setDependentLabels(0,hist.hLabel);

return v;
} */


    /**
     * Initialises information specific to HistTo2D.
     */
    public void init() {
        super.init();

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
     * Reset's HistTo2D
     */
    public void reset() {
        super.reset();
    }

    /**
     * Saves parameters
     */
    public void saveParameters() {
    }

    /**
     * Sets parameters
     */
    public void setParameter(String name, String value) {
    }

    /**
     * @return a string containing the names of the types allowed to be input to HistTo2D, each separated by a white
     *         space.
     */
//    public String inputTypes() {
//        return "Histogram";
//    }
//
//    /**
//     * @return a string containing the names of the types output from HistTo2D, each separated by a white space.
//     */
//    public String outputTypes() {
//        return "VectorType";
//    }

    public String[] getInputTypes() {
        return new String[]{"triana.types.Histogram"};
    }

    public String[] getOutputTypes() {
        return new String[]{"triana.types.VectorType"};
    }

    /**
     *
     * @returns the location of the help file for this unit.  
     */
    public String getHelpFile() {
        return "HistTo2D.html";
    }
}














