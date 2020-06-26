package utils;

import java.util.List;

public class BinarySearchUtils {

    /**
     * Binary search to find by key
     *
     * @param list
     * @param key
     * @return
     */
    public static int binarySearch(List<String> list, String key) {
        // define lower limit
        int low = 0;
        // define upper limit
        int high = list.size() - 1;
        // define mid
        int mid = (low + high) / 2;
        // loop through when low <= high and mid value of list is not the key
        while (low <= high && !list.get(mid).equalsIgnoreCase(key)) {
            // compare mid value of the list with the key
            if (list.get(mid).compareTo(key) < 0) {
                // set low to mid + 1 if mid value is less than the key
                low = mid + 1;
            } else {
                // set high to mid - 1 if mid value is greater than the key
                high = mid - 1;
            }
            // get new mid for new low and new high
            mid = (low + high) / 2;
            // if low > high then set mid to -1
            if (low > high) {
                mid = -1;
            }
        }
        // return mid
        return mid;
    }
}
