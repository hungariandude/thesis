
package hu.thesis.shorthand.ime.util;

import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Segítő metódusokat tartalmazó statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class RecognizerUtils {

    /**
     * Kinyeri a <code>GesturePoint</code> objektumokat a paraméterként
     * megkapott <code>GestureStroke</code> objektumból.
     */
    public static GesturePoint[] extractGesturePointsFromStroke(GestureStroke stroke) {
        // a timestamp mező privát a GestureStroke osztályban, ezért
        // reflekcióval nyerjük ki belőle
        long[] timestamps = null;
        try {
            Field f = stroke.getClass().getDeclaredField("timestamps");
            f.setAccessible(true);
            timestamps = (long[]) f.get(stroke);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            Log.e(RecognizerUtils.class.getSimpleName(),
                    "Failed to read timestamps field from GestureStroke.", ex);
            return null;
        }

        GesturePoint[] points = new GesturePoint[stroke.points.length / 2];
        for (int i = 0; i < points.length; ++i) {
            int doubledIndex = i * 2;
            points[i] = new GesturePoint(stroke.points[doubledIndex],
                    stroke.points[doubledIndex + 1],
                    timestamps[i]);
        }

        return points;
    }

    private RecognizerUtils() {
        // statikus osztály
    }

}
