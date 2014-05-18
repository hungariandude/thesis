
package hu.thesis.shorthand.ime.util;

import android.content.Context;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.util.Log;

import hu.thesis.shorthand.common.CharMappingSaveData;
import hu.thesis.shorthand.ime.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;

/**
 * Segítő metódusokat tartalmazó statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class RecognizerUtils {

    private static final String TAG = RecognizerUtils.class.getSimpleName();

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
            Log.e(TAG, "Failed to read timestamps field from GestureStroke.", ex);
            return null;
        }

        GesturePoint[] points = new GesturePoint[stroke.points.length / 2];
        for (int i = 0; i < points.length; ++i) {
            int doubledIndex = i * 2;
            points[i] = new GesturePoint(stroke.points[doubledIndex],
                    stroke.points[doubledIndex + 1], timestamps[i]);
        }

        return points;
    }

    public static String gesturePointToString(GesturePoint point) {
        return "(x:" + point.x + ";y:" + point.y + ";t:" + point.timestamp + ')';
    }

    public static void logGesturePoints(String label, GesturePoint[] points) {
        StringBuilder sb = new StringBuilder();
        if (label != null) {
            sb.append(label);
        }
        sb.append('[');
        for (GesturePoint point : points) {
            sb.append(RecognizerUtils.gesturePointToString(point));
            sb.append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), "]");
        Log.d(TAG, sb.toString());
    }

    public static CharMappingSaveData[] readCharMappingFromResource(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.albhabet);
        CharMappingSaveData[] saveData = null;

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(is);
            saveData = (CharMappingSaveData[]) ois.readObject();
        } catch (IOException ex) {
            Log.e(TAG, "Failed to read char mapping from resource.", ex);
        } catch (ClassNotFoundException ex) {
            Log.e(TAG, "Failed to read char mapping from resource.", ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
        }

        return saveData;
    }

    private RecognizerUtils() {
        // statikus osztály
    }
}
