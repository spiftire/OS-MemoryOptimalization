package no.hials.page.replacement;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimal Replacement algorithm
 * Fill in your code in this class!
 */
public class OptimalReplacement extends ReplacementAlgorithm {

    private int frameToReset;
    private int numberOfPhysicalFrames;

    @Override
    protected void reset() {
        // TODO - do preparation/initilization here, if needed
        frameToReset = 0;
    }

    @Override
    public int process(String referenceString) {
        List<Integer> pageReferences = Tools.stringToArray(referenceString);
        if (pageReferences == null) return 0;

        int replacements = 0; // How many page replacements made

        // TODO - process the reference string here. You can see FIFOReplacement
        // as an example. But remember, that FIFO uses a different algorithm.
        // This class should use Optimal Replacement algorithm, described
        // in Section 9.4.
        // Get the reference list as an array

        List<Integer> listOfFrames = convertToList(frames);

        int frame = 0;
        for (int ref : pageReferences) {
            boolean placed = false;
            if (!isLoaded(ref)) {
                listOfFrames.set(frame, ref);
                placed = true;
            }

            while (!placed) {
                while (!placed && frame < numberOfPhysicalFrames) {
                    if (frames[frame] == -1) {
                        frames[frame] = pageReferences.get(ref);
                        placed = true;
                        pageReferences.remove(0); // todo reset loop
                    }
                    frame++;
                }

                // no free space must replace
                //var arrayOfNextStrings = pageReferences.subList(ref, ref+numberOfPhysicalFrames);
                int indexOfFrameToDelete = 0;
                Integer valueToReplace = null;
                for (int fram :
                        frames) {
                    int longestDistance = 0;
                    if (!pageReferences.contains(fram)) {
                        valueToReplace = fram;
                    }
                    if (pageReferences.contains(fram)) {
                        var indexOfFrame = pageReferences.indexOf(fram);
                        if (indexOfFrame > longestDistance) {
                            longestDistance = indexOfFrame;
                            valueToReplace = fram;
                        }
                    }
                }

                if (valueToReplace != null) {
                    indexOfFrameToDelete = listOfFrames.indexOf(valueToReplace);
                }

                frames[indexOfFrameToDelete] = ref;
                replacements++;

            }
            frame = frame % numberOfPhysicalFrames; // wrap around the frames.
        }

        return replacements;
    }

    private List<Integer> convertToList(int[] frames) {
        var result = new ArrayList<Integer>();
        for (int frame :
                frames) {
            result.add(frame);
        }
        return result;
    }

    // TODO - create any helper methods here if you need any


    @Override
    public void setup(int numberOfPhysicalFrames) {
        super.setup(numberOfPhysicalFrames);
        this.numberOfPhysicalFrames = numberOfPhysicalFrames;
    }
}
