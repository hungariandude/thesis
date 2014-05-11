
package hu.thesis.shorthand.ime.recognizer;

import android.gesture.GesturePoint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * A megrajzolt alakzatokat felismerő osztály.
 * </p>
 * A bevitt alakzatokat először komponensekre bontja, majd azokat a
 * szabályrendszer szerint próbálja megfeleltetni karaktereknek.
 * 
 * @author Istvánfi Zsolt
 */
public class Recognizer {

    private static final int PAUSE_BETWEEN_CHARS = 300;

    /**
     * Felismeri a paraméterként megkapott <code>GesturePoint</code> tömb által
     * leírt alakzatot.
     * 
     * @return A legvalószínűbb szöveges megfeleltetés.
     */
    public String recognize(GesturePoint[] gesture) {
        List<GesturePoint[]> charComponents = splitGestureIntoCharComponents(gesture);
        Log.d(Recognizer.class.getSimpleName(), "Number of found char components: "
                + charComponents.size());

        StringBuilder sb = new StringBuilder();
        for (GesturePoint[] component : charComponents) {
            Character ch = recognizeCharComponent(component);
            if (ch != null) {
                sb.append(ch.charValue());
            }
        }

        return sb.toString();
    }

    private Character recognizeCharComponent(GesturePoint[] component) {
        return null;
    }

    private List<GesturePoint[]> splitGestureIntoCharComponents(GesturePoint[] gesture) {
        List<GesturePoint[]> components = new ArrayList<>();
        int componentStartIndex = 0;
        long lastTimeStamp = 0;
        for (int i = 0; i < gesture.length; ++i) {
            if (i == gesture.length - 1) {
                components.add(Arrays.copyOfRange(gesture, componentStartIndex, gesture.length));
            } else {
                GesturePoint actualPoint = gesture[i];
                if (actualPoint.timestamp - lastTimeStamp >= PAUSE_BETWEEN_CHARS) {
                    if (i > 0) {
                        components.add(Arrays.copyOfRange(gesture, componentStartIndex, i));
                        componentStartIndex = i;
                    }
                }
                lastTimeStamp = actualPoint.timestamp;
            }
        }

        return components;
    }

}
