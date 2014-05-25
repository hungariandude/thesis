
package hu.thesis.shorthand.ime.recognizer;

import android.content.Context;
import android.gesture.GesturePoint;
import android.graphics.Path;
import android.util.Log;

import hu.thesis.shorthand.common.CharMappingSaveData;
import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;
import hu.thesis.shorthand.ime.Parameters;
import hu.thesis.shorthand.ime.util.ShorthandUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    private static final String TAG = Recognizer.class.getSimpleName();

    private final Context mContext;
    private final Segmenter mSegmenter;

    private HashMap<ComplexShape, Character> mCharMap = new HashMap<>();

    public Recognizer(Context context, float unit) {
        mContext = context;
        mSegmenter = new Segmenter(unit);
    }

    /**
     * A deszerializált CharMappingSaveData tömböt betölti a saját HashMap-jébe.
     */
    private void createMapFromSaveData(CharMappingSaveData[] saveData) {
        mCharMap.clear();

        for (CharMappingSaveData data : saveData) {
            char ch = data.ch;
            Form[] forms = data.forms;
            Rotation[] rotations = data.rotations;

            ComplexShape cs = new ComplexShape(forms.length);
            for (int i = 0; i < forms.length; ++i) {
                Segment segment = new Segment(forms[i], rotations[i]);
                cs.add(segment);
            }

            mCharMap.put(cs, ch);
        }
    }

    public List<Path> getDebugPaths() {
        return mSegmenter.getDebugPaths();
    }

    /**
     * Betölti az alkalmazás csomagfájlában eltárolt ábécét.
     */
    public void loadDefaultCharMapping() {
        CharMappingSaveData[] saveData = ShorthandUtils.readCharMappingFromResource(mContext);
        createMapFromSaveData(saveData);
    }

    /**
     * Felismeri a paraméterként megkapott <code>GesturePoint</code> tömb által
     * leírt alakzatot.
     * 
     * @return A legvalószínűbb szöveges megfeleltetés.
     */
    public String recognize(GesturePoint[] gesture) {
        mSegmenter.clear();

        List<GesturePoint[]> charComponents = splitGestureIntoCharComponents(gesture);
        if (Parameters.getInstance().isDebugEnabled()) {
            Log.d(TAG, "Number of found char components: " + charComponents.size());
        }

        StringBuilder sb = new StringBuilder();
        for (GesturePoint[] component : charComponents) {
            Character ch = recognizeCharComponent(component);
            if (ch != 0) {
                sb.append(ch.charValue());
            }
        }

        return sb.toString();
    }

    /**
     * @return A felismert komponens karakter megfelelője (0, ha nem ismerhető
     *         fel).
     */
    private char recognizeCharComponent(GesturePoint[] component) {
        ComplexShape cs = mSegmenter.segment(component);
        if (cs != null && !cs.isEmpty()) {
            if (Parameters.getInstance().isDebugEnabled()) {
                Log.d(TAG, "Found segments: " + cs.size() + ' ' + cs.toString());
            }
            Character ch = mCharMap.get(cs);
            if (ch != null) {
                return ch;
            }
        }

        return 0;
    }

    /**
     * Feldarabolja a ponttömböt a karakterek mentén.
     * 
     * @return A karakterek listája, ahol a karakter pontok tömbje.
     */
    private List<GesturePoint[]> splitGestureIntoCharComponents(GesturePoint[] gesture) {
        List<GesturePoint[]> components = new ArrayList<>();
        int componentStartIndex = 0;
        long lastTimeStamp = 0;
        for (int i = 0; i < gesture.length; ++i) {
            if (i == gesture.length - 1) {
                GesturePoint[] component = Arrays.copyOfRange(gesture, componentStartIndex,
                        gesture.length);
                components.add(component);
            } else {
                GesturePoint actualPoint = gesture[i];
                if (actualPoint.timestamp - lastTimeStamp >= Parameters.getInstance()
                        .getPauseBetweenChars()) {
                    if (i > 0 && i - componentStartIndex > 1) {
                        GesturePoint[] component = Arrays.copyOfRange(gesture, componentStartIndex,
                                i);
                        components.add(component);
                        componentStartIndex = i - 1;
                    }
                }
                lastTimeStamp = actualPoint.timestamp;
            }
        }

        return components;
    }

}
