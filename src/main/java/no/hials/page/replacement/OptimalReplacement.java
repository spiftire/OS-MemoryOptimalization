package no.hials.page.replacement;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimal Replacement algorithm
 * Fill in your code in this class!
 */
public class OptimalReplacement extends ReplacementAlgorithm {

    int numberOfInserts = 0;
    private int numberOfAviodedReplacements = 0;
    List<Integer> pageReferences;
    int workingValue;

    @Override
    protected void reset() {
        // TODO - do preparation/initilization here, if needed
        // did not see the use of this
    }

    @Override
    public int process(String referenceString) {
        pageReferences = Tools.stringToArray(referenceString);
        if (pageReferences == null) return 0;

        int replacements = 0; // How many page replacements made

        // process the reference string here. You can see FIFOReplacement
        // as an example. But remember, that FIFO uses a different algorithm.
        // This class should use Optimal Replacement algorithm, described
        // in Section 9.4.
        // Get the reference list as an array

        for (int ref : pageReferences) {
            workingValue = ref;
            if (!isLoaded(ref)) {
                var index = getIndexToReplace();
                replacements = isReplacing(replacements, index);
                insertIntoFrames(ref, index);
            } else {
                numberOfAviodedReplacements++;
            }
        }
        return replacements;
    }

    /**
     * if the value at index is not like -1
     * it means that something is getting replaced
     *
     * @param replacements the number of replacements all ready done
     * @param index        the index of the frame to check
     * @return
     */
    private int isReplacing(int replacements, int index) {
        if (frames[index] != -1) {
            replacements++;
        }
        return replacements;
    }

    /**
     * Inserts a value into a frame
     *
     * @param valueToBeInserted
     * @param indexOfPlacement
     */
    private void insertIntoFrames(int valueToBeInserted, int indexOfPlacement) {
        // logging for debugging
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
            index = findIndexOfLongestGap();
        }

        return index;
    }

    /**
     * Finds the index of the reference with the longest gap, aka the longest until used again.
     *
     * @return the index of that reference
     */
    private int findIndexOfLongestGap() {
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

    /**
     * Shortens the reference list so we don't have to iterate over the whole thing
     *
     * @return
     */
    private ArrayList<Integer> shortenReferenceList() {
        var result = pageReferences.subList(numberOfInserts + numberOfAviodedReplacements, pageReferences.size());
        return new ArrayList<>(result);
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

    /**
     * Converts an `int[]` into `List<Integer>`
     *
     * @param frames
     * @return
     */
    private List<Integer> convertToList(int[] frames) {
        var result = new ArrayList<Integer>();
        result.ensureCapacity(frames.length);
        for (int frame :
                frames) {
            result.add(frame);
        }
        return result;
    }
}
