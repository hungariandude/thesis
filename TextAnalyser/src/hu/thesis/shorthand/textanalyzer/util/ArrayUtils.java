package hu.thesis.shorthand.textanalyzer.util;

/**
 * Tömbökkel kapcsolatos műveletek elvégzését segítő statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class ArrayUtils {
    /**
     * Megfordítja egy integer tömb elemeinek sorrendjét.
     */
    public static void reverse(int[] array) {
	for (int i = 0; i < array.length / 2; i++) {
	    int temp = array[i];
	    int latterIndex = array.length - i - 1;
	    array[i] = array[latterIndex];
	    array[latterIndex] = temp;
	}
    }

    private ArrayUtils() {
	// statikus osztály
    }
}
