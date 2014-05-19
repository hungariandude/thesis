
package hu.thesis.shorthand.ime.recognizer;

import android.content.Context;
import android.gesture.GesturePoint;
import android.graphics.Path;
import android.util.Log;

import hu.thesis.shorthand.common.CharMappingSaveData;
import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;
import hu.thesis.shorthand.ime.ShorthandIME;
import hu.thesis.shorthand.ime.util.RecognizerUtils;

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

    private static final int PAUSE_BETWEEN_CHARS = 300;
    // private static final float THRESHOLD = 0.3f;
    /**
     * Vonal tűréshatár, fokokban.
     */
    private static final int THRESHOLD = 10;

    private static final String TAG = Recognizer.class.getSimpleName();

    private final Context mContext;
    private HashMap<ComplexShape, Character> mCharMap = new HashMap<>();
    private List<Path> mDebugPaths = new ArrayList<>();

    public Recognizer(Context context) {
        mContext = context;
    }

    private double calculateDegreesBetween(GesturePoint p1, GesturePoint p2) {
        // az Android sajátos koordináta-rendszere miatt a pontjaink y
        // koordinátájának ellentettjét kell venni
        float lineX1 = p1.x;
        float lineY1 = -p1.y;
        float lineX2 = p2.x;
        float lineY2 = -p2.y;

        float dx = lineX2 - lineX1;
        float dy = lineY2 - lineY1;

        int quarter;
        if (dx >= 0) {
            if (dy >= 0) {
                quarter = 1;
            } else {
                quarter = 4;
            }
        } else {
            if (dy >= 0) {
                quarter = 2;
            } else {
                quarter = 3;
            }
        }

        float quotient = dx / dy;
        double atan = Math.atan(quotient);
        double degrees = Math.toDegrees(atan);

        switch (quarter) {
            case 1:
            case 2:
                return 90 - degrees;
            case 3:
                return 270 - degrees;
            case 4:
                return -degrees - 90;
        }

        return degrees;
    }

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
        return mDebugPaths;
    }

    private Rotation getRotationFromFloatValue(double floatValue) {
        if (floatValue < 0) {
            floatValue += 360;
        }
        int[] intValues = Rotation.intValues;
        for (int i = 0; i <= intValues.length; ++i) {
            boolean last = i == intValues.length;
            int intValue = last ? 360 : intValues[i];
            if (floatValue < intValue) {
                int previousValue = intValues[i - 1];
                float between = previousValue + (intValue - previousValue) / 2.0f;
                if (floatValue < between) {
                    return Rotation.values()[i - 1];
                } else if (last) {
                    return Rotation.NONE;
                } else {
                    return Rotation.values()[i];
                }
            }
        }

        return null;
    }

    public void loadDefaultCharMapping() {
        CharMappingSaveData[] saveData = RecognizerUtils.readCharMappingFromResource(mContext);
        createMapFromSaveData(saveData);
    }

    /**
     * Felismeri a paraméterként megkapott <code>GesturePoint</code> tömb által
     * leírt alakzatot.
     * 
     * @return A legvalószínűbb szöveges megfeleltetés.
     */
    public String recognize(GesturePoint[] gesture) {
        if (ShorthandIME.DEBUG) {
            mDebugPaths.clear();
        }
        List<GesturePoint[]> charComponents = splitGestureIntoCharComponents(gesture);
        if (ShorthandIME.DEBUG) {
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
        ComplexShape cs = splitCharComponentIntoSegments(component);
        if (cs != null && !cs.isEmpty()) {
            if (ShorthandIME.DEBUG) {
                Log.d(TAG, "Found segments: " + cs.size() + ' ' + cs.toString());
            }
            Character ch = mCharMap.get(cs);
            if (ch != null) {
                return ch;
            }
        }

        return 0;
    }

    private ComplexShape splitCharComponentIntoSegments(GesturePoint[] component) {
        ComplexShape cs = null;
        Form form = Form.LINE;
        // float rotValue = 0.0f;
        // int startingIndex = 0;
        GesturePoint startingPoint = component[0];
        GesturePoint currentPoint = component[1];
        // float lastDx = 0.0f, lastDy = 0.0f;
        // float lastTg = 0.0f;
        // double atan = calculateAtan(startingPoint, currentPoint);
        double degrees = calculateDegreesBetween(startingPoint, currentPoint);

        // float avgTg = tg;
        // double lastAtan = atan;
        double lastDegrees = degrees;

        if (ShorthandIME.DEBUG) {
            // Log.d(TAG, "atan of 0-1: " + atan);
            Log.d(TAG, "degrees of 0-1: " + degrees);
            Path path = new Path();
            path.moveTo(startingPoint.x, startingPoint.y);
            path.lineTo(currentPoint.x, currentPoint.y);
            mDebugPaths.add(path);
        }

        if (component.length == 2) {
            // double rot = Math.toDegrees(atan);
            cs = new ComplexShape();
            // Rotation rotation = getRotationFromFloatValue(rot);
            Rotation rotation = getRotationFromFloatValue(degrees);
            cs.add(new Segment(form, rotation));
        }

        for (int i = 2; i < component.length; ++i) {
            currentPoint = component[i];
            // startingPoint = component[startingIndex];
            GesturePoint lastPoint = component[i - 1];

            // tg = calculateTangent(startingPoint, currentPoint);
            // atan = calculateAtan(lastPoint, currentPoint);
            degrees = calculateDegreesBetween(lastPoint, currentPoint);
            // if (Math.abs(avgTg - tg) > THRESHOLD) {
            // double diff = Math.abs(lastAtan - atan);
            double diff = Math.abs(lastDegrees - degrees);
            if (ShorthandIME.DEBUG) {
                // Log.d(TAG, "atan of " + (i - 1) + "-" + i + ": " + atan);
                // Log.d(TAG, "atan diff of " + (i - 2) + "-" + (i - 1) +
                // " and " + (i - 1) + "-" + i
                // + ": " + diff);
                Log.d(TAG, "degrees of " + (i - 1) + "-" + i + ": " + degrees);
                Log.d(TAG, "degrees diff of " + (i - 2) + "-" + (i - 1) + " and " + (i - 1) + "-"
                        + i + ": " + diff);
            }
            if (diff > THRESHOLD) {
                // double finalAtan = calculateAtan(startingPoint, lastPoint);
                double finalDegrees = calculateDegreesBetween(startingPoint, lastPoint);
                // double rot = Math.toDegrees(Math.atan(lastTg));
                // double rot = Math.toDegrees(finalAtan);
                if (cs == null) {
                    cs = new ComplexShape();
                }
                // Rotation rotation = getRotationFromFloatValue(rot);
                Rotation rotation = getRotationFromFloatValue(finalDegrees);
                // if (!cs.isEmpty()) {
                // Segment lastSegment = cs.get(cs.size() - 1);
                // if (lastSegment.getForm() != Form.LINE ||
                // lastSegment.getRotation() != rotation) {
                // // nem adunk hozzá ugyanolyan irányú vonalat
                // cs.add(new Segment(form, rotation));
                // }
                // } else {
                cs.add(new Segment(form, rotation));
                // }

                // if (ShorthandIME.DEBUG) {
                // RecognizerUtils.logGesturePoints(cs.size() +
                // ". segment found: ",
                // Arrays.copyOfRange(component, startingIndex, i));
                // }

                // startingIndex = i - 1;
                // avgTg = calculateTangent(startingPoint, currentPoint);
                // lastAtan = calculateAtan(startingPoint, currentPoint);
                startingPoint = lastPoint;
                lastDegrees = degrees;

                if (i == component.length - 1) {
                    finalDegrees = calculateDegreesBetween(startingPoint, currentPoint);
                    rotation = getRotationFromFloatValue(finalDegrees);
                    cs.add(new Segment(form, rotation));
                }

                if (ShorthandIME.DEBUG) {
                    Path path = new Path();
                    path.moveTo(startingPoint.x, startingPoint.y);
                    path.lineTo(currentPoint.x, currentPoint.y);
                    mDebugPaths.add(path);
                }
            } else {
                if (i == component.length - 1) {
                    // double finalAtan = calculateAtan(startingPoint,
                    // currentPoint);
                    double finalDegrees = calculateDegreesBetween(startingPoint, currentPoint);
                    // double rot = Math.toDegrees(finalAtan);
                    if (cs == null) {
                        cs = new ComplexShape();
                    }
                    // Rotation rotation = getRotationFromFloatValue(rot);
                    Rotation rotation = getRotationFromFloatValue(finalDegrees);
                    cs.add(new Segment(form, rotation));

                    // if (ShorthandIME.DEBUG) {
                    // RecognizerUtils.logGesturePoints(cs.size() +
                    // ". segment found: ",
                    // Arrays.copyOfRange(component, startingIndex, i));
                    // }
                } else {
                    // int count = i - startingIndex;
                    // avgTg = (avgTg * count + tg) / (count + 1);
                    // lastAtan = atan;
                    lastDegrees = degrees;
                }

                if (ShorthandIME.DEBUG) {
                    mDebugPaths.get(mDebugPaths.size() - 1).lineTo(currentPoint.x, currentPoint.y);
                }
            }
        }

        return cs;
    }

    private List<GesturePoint[]> splitGestureIntoCharComponents(GesturePoint[] gesture) {
        List<GesturePoint[]> components = new ArrayList<>();
        int componentStartIndex = 0;
        long lastTimeStamp = 0;
        for (int i = 0; i < gesture.length; ++i) {
            if (i == gesture.length - 1) {
                GesturePoint[] component = Arrays.copyOfRange(gesture, componentStartIndex,
                        gesture.length);
                components.add(component);

                // if (ShorthandIME.DEBUG) {
                // RecognizerUtils
                // .logGesturePoints(components.size() + ". component: ",
                // component);
                // }
            } else {
                GesturePoint actualPoint = gesture[i];
                if (actualPoint.timestamp - lastTimeStamp >= PAUSE_BETWEEN_CHARS) {
                    if (i > 0 && i - componentStartIndex > 1) {
                        GesturePoint[] component = Arrays.copyOfRange(gesture, componentStartIndex,
                                i);
                        components.add(component);

                        // if (ShorthandIME.DEBUG) {
                        // RecognizerUtils.logGesturePoints(components.size() +
                        // ". component: ",
                        // component);
                        // }

                        componentStartIndex = i - 1;
                    }
                }
                lastTimeStamp = actualPoint.timestamp;
            }
        }

        return components;
    }

}
