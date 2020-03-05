package no.hials.page.replacement;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimal Replacement algorithm
 * Fill in your code in this class!
 */
public class OptimalReplacement extends ReplacementAlgorithm {

    private int frameToReset;
    int numberOfInserts = 0;
    private int numberOfAviodedReplacements = 0;
    private int numberOfPhysicalFrames;
    List<Integer> pageReferences;
    int workingValue;

    @Override
    protected void reset() {
        // TODO - do preparation/initilization here, if needed
        frameToReset = 0;
    }

    @Override
    public int process(String referenceString) {
        pageReferences = Tools.stringToArray(referenceString);
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
            workingValue = ref;
            boolean placed = false;
            if (!isLoaded(ref)) {
                var index = getIndexToReplace();
                replacements = isReplacing(replacements, index);
                insertIntoFrames(ref, index);
                placed = true;
                frame++;
            } else {
                numberOfAviodedReplacements++;
            }

//            while (!placed) {
//                while (!placed && frame < numberOfPhysicalFrames) {
//                    if (isFrameEmpty(listOfFrames.get(frame))) {
//                        insertIntoFrames(pageReferences.get(ref), frame);
//                        placed = true;
//                        pageReferences.remove(0); // todo reset loop
//                    }
//                    frame++;
//                }

                // no free space must replace
                //var arrayOfNextStrings = pageReferences.subList(ref, ref+numberOfPhysicalFrames);

//                frames[indexOfFrameToDelete] = ref;
//                replacements++;

            }
//            frame = frame % numberOfPhysicalFrames; // wrap around the frames.
//        }

        return replacements;
    }

    /**
     * if the value at index is not like -1
     * it means that something is getting replaced
     * @param replacements the number of replacements all ready done
     * @param index the index of the frame to check
     * @return
     */
    private int isReplacing(int replacements, int index) {
        if(frames[index] != -1) {

            replacements++;
        }
        return replacements;
    }

    private void insertIntoFrames(int valueToBeInserted, int indexOfPlacement) {
        System.out.printf("%d: The value %d is replacing %d \n", numberOfInserts
                , valueToBeInserted
                , frames[indexOfPlacement]
        );
        frames[indexOfPlacement] = valueToBeInserted;
        numberOfInserts++;
    }

    private int getIndexToReplace() {
        int index = checkForEmptySpace();
        if (index == -1) {
            index = findIndexOfLongestGap(workingValue);
        }

        return index;
    }

    private int findIndexOfLongestGap(int valueToReplace) {
        int indexOfFrameToDelete = -1;
        int longestDistance = 0;
        var listOfFrames = convertToList(frames);

        var shortenedReferenceList = shortenReferenceList();
        for (int frame : listOfFrames) {
            if (!shortenedReferenceList.contains(frame)) {
                indexOfFrameToDelete = listOfFrames.indexOf(frame);
//                longestDistance = pageReferences.size();
                break;
            }

            var i = shortenedReferenceList.indexOf(frame);
            if (i > longestDistance) {
                longestDistance = i;
            }
        }

        if (indexOfFrameToDelete == -1) {
            var valueToBeReplaced = shortenedReferenceList.get(longestDistance);
            indexOfFrameToDelete = listOfFrames.indexOf(valueToBeReplaced);
        }


//        indexOfFrameToDelete = listOfFrames.indexOf(valueToReplace);


        return indexOfFrameToDelete;
    }

    private ArrayList<Integer> shortenReferenceList() {
        var result = pageReferences.subList(numberOfInserts+numberOfAviodedReplacements, pageReferences.size());
        return new ArrayList<Integer>(result);
    }

    /**
     * Checks for empty space
     *
     * @return index of empty, -1 if not found.
     */
    private int checkForEmptySpace() {
        var result = -1;
        var index = 0;
        boolean foundIndex = false;
        while (!foundIndex && index < frames.length) {
            if (isFrameEmpty(frames[index])) {
                result = index;
                foundIndex = true;
            } else {
                index++;
            }
        }
        return result;
    }

    private boolean isFrameEmpty(int frame) {
        return frame == -1;
    }

    private List<Integer> convertToList(int[] frames) {
        var result = new ArrayList<Integer>();
        for (int frame :
                frames) {
            result.add(frame);
        }
        return result;
    }

    @Override
    public void setup(int numberOfPhysicalFrames) {
        super.setup(numberOfPhysicalFrames);
        this.numberOfPhysicalFrames = numberOfPhysicalFrames;
    }
}
