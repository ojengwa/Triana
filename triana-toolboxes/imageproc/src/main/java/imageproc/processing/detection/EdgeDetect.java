package imageproc.processing.detection;

import org.trianacode.taskgraph.Unit;
import triana.types.TrianaPixelMap;
import triana.types.image.PixelMap;

/**
 * A EdgeDetect unit to ..
 *
 * @author Melanie Rhianna Lewis
 * @version 1.0 alpha 04 Sep 1997
 */
public class EdgeDetect extends Unit {
    /**
     * The UnitWindow for EdgeDetect
     */

    PixelMap sourceMap, destMap;

    /**
     * ********************************************* ** USER CODE of EdgeDetect goes here    ***
     * *********************************************
     */
    public void process() {
        TrianaPixelMap sourceGPM = (TrianaPixelMap) getInputAtNode(0);
        sourceMap = sourceGPM.getPixelMap();
        destMap = new PixelMap(sourceMap);
        processImage();
        output(new TrianaPixelMap(destMap));
    }

    /**
     * Initialses information specific to EdgeDetect.
     */
    public void init() {
        super.init();

        setDefaultInputNodes(1);
        setMinimumInputNodes(1);
        setMaximumInputNodes(Integer.MAX_VALUE);

        setDefaultOutputNodes(1);
        setMinimumOutputNodes(1);
        setMaximumOutputNodes(Integer.MAX_VALUE);
    }

    /**
     * Reset's EdgeDetect
     */
    public void reset() {
        super.reset();
    }

    /**
     * Saves EdgeDetect's parameters to the parameter file.
     */
    public void saveParameters() {
    }

    /**
     * Loads EdgeDetect's parameters of from the parameter file.
     */
    public void setParameter(String name, String value) {
    }

    /**
     * @return a string containing the names of the types allowed to be input to EdgeDetect, each separated by a white
     *         space.
     */
    public String[] getInputTypes() {
        return new String[]{"triana.types.TrianaPixelMap"};
    }

    public String[] getOutputTypes() {
        return new String[]{"triana.types.TrianaPixelMap"};
    }

    /**
     * This returns a <b>brief!</b> description of what the unit does. The text here is shown in a pop up window when
     * the user puts the mouse over the unit icon for more than a second.
     */
    public String getPopUpDescription() {
        return "Performs a Sobel Edge detection on a TrianaPixelMap.";
    }

    /**
     *
     * @returns the location of the help file for this unit.  
     */
    public String getHelpFile() {
        return "EdgeDetect.html";
    }

    void processImage() {
        int iA, iB, iC, iD, iE, iF, iG, iH, iI; // pixel interators
        int pA, pB, pC, pD, pE, pF, pG, pH, pI; // pixel values (32 bit)
        int rA, rB, rC, rD, rE, rF, rG, rH, rI; // red channel
        int gA, gB, gC, gD, gE, gF, gG, gH, gI; // green channel
        int bA, bB, bC, bD, bE, bF, bG, bH, bI; // blue channel
        int red, green, blue;                   // results
        int w, x, y, z;
        int width = sourceMap.getWidth();
        int[] source = sourceMap.getPixels();
        int[] dest = destMap.getPixels();

        System.out.println("Processing...");

        for (y = 1; y < sourceMap.getHeight() - 1; y++) {
            iE = 1 + y * width;
            iD = iE - 1;
            iF = iE + 1;
            iA = iD - width;
            iB = iE - width;
            iC = iF - width;
            iG = iD + width;
            iH = iE + width;
            iI = iF + width;

            for (x = 1; x < width - 1; x++) {
                pA = source[iA];
                pB = source[iB];
                pC = source[iC];
                pD = source[iD];
                pE = source[iE];
                pF = source[iF];
                pG = source[iG];
                pH = source[iH];
                pI = source[iI];

                rA = (pA >> 16) & 0xff;
                rB = (pB >> 16) & 0xff;
                rC = (pC >> 16) & 0xff;
                rD = (pD >> 16) & 0xff;
                rE = (pE >> 16) & 0xff;
                rF = (pF >> 16) & 0xff;
                rG = (pG >> 16) & 0xff;
                rH = (pH >> 16) & 0xff;
                rI = (pI >> 16) & 0xff;

                w = rA + (rB << 1) + rC;
                x = rG + (rH << 1) + rI;
                y = rA + (rD << 1) + rG;
                z = rC + (rF << 1) + rI;

                red = ((((w - x) > 0) ? w - x : x - w) +
                        (((y - z) > 0) ? y - z : z - y)) / 6;

                gA = (pA >> 8) & 0xff;
                gB = (pB >> 8) & 0xff;
                gC = (pC >> 8) & 0xff;
                gD = (pD >> 8) & 0xff;
                gE = (pE >> 8) & 0xff;
                gF = (pF >> 8) & 0xff;
                gG = (pG >> 8) & 0xff;
                gH = (pH >> 8) & 0xff;
                gI = (pI >> 8) & 0xff;

                w = gA + (gB << 1) + gC;
                x = gG + (gH << 1) + gI;
                y = gA + (gD << 1) + gG;
                z = gC + (gF << 1) + gI;

                green = ((((w - x) > 0) ? w - x : x - w) +
                        (((y - z) > 0) ? y - z : z - y)) / 6;

                bA = pA & 0xff;
                bB = pB & 0xff;
                bC = pC & 0xff;
                bD = pD & 0xff;
                bE = pE & 0xff;
                bF = pF & 0xff;
                bG = pG & 0xff;
                bH = pH & 0xff;
                bI = pI & 0xff;

                w = bA + (bB << 1) + bC;
                x = bG + (bH << 1) + bI;
                y = bA + (bD << 1) + bG;
                z = bC + (bF << 1) + bI;

                blue = ((((w - x) > 0) ? w - x : x - w) +
                        (((y - z) > 0) ? y - z : z - y)) / 6;

                dest[iE] = (pE & 0xff000000) | red << 16 | green << 8 | blue;

                iA++;
                iB++;
                iC++;
                iD++;
                iE++;
                iF++;
                iG++;
                iH++;
                iI++;
            }
        }
    }
}














