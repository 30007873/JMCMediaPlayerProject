package utils;

public class MergeSortUtils {
    /**
     * Merge sort a String array for given from and to indices
     * @param stringArray
     * @param indexFrom
     * @param indexTo
     */
    public static void mergeSort(String[] stringArray, int indexFrom, int indexTo) {
        if (indexFrom == indexTo) {
            return;
        }
        // get mid position
        int mid = (indexFrom + indexTo) / 2;
        // merge-sort first half of string array
        mergeSort(stringArray, indexFrom, mid);
        // merge-sort second half og the string array
        mergeSort(stringArray, mid + 1, indexTo);
        // merge all
        merge(stringArray, indexFrom, mid, indexTo);
    }

    /**
     * Merge two intervals
     * @param stringArray
     * @param indexFrom
     * @param mid
     * @param indexTo
     */
    private static void merge(String[] stringArray, int indexFrom, int mid, int indexTo) {
        // get range to be merged
        int range = indexTo - indexFrom + 1;
        // merge all and store in String array
        String[] temp = new String[range];
        // get from
        int from = indexFrom;
        // get start of second half
        int index = mid + 1;
        int j = 0;
        // loop through and fill temp with values less than from value
        while (from <= mid && index <= indexTo) {
            if (stringArray[from].compareTo(stringArray[index]) < 0) {
                temp[j] = stringArray[from];
                from++;
            } else {
                temp[j] = stringArray[index];
                index++;
            }
            j++;
        }

        // loop through and add remaining to the first half
        while (from <= mid) {
            temp[j] = stringArray[from];
            from++;
            j++;
        }

        // loop through and add remaining to the second half
        while (index <= indexTo) {
            temp[j] = stringArray[index];
            index++;
            j++;
        }

        // copy back indexFrom the temporary array
        for (j = 0; j < range; j++) {
            stringArray[indexFrom + j] = temp[j];
        }
    }
}
