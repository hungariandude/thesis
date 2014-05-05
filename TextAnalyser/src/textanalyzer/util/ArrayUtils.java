package textanalyzer.util;

/**
 * T�mb�kkel kapcsolatos m�veletek elv�gz�s�t seg�t� statikus oszt�ly.
 * 
 * @author Istv�nfi Zsolt
 */
public class ArrayUtils {
    /**
     * Megford�tja egy integer t�mb elemeinek sorrendj�t.
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
	// statikus oszt�ly
    }
}
