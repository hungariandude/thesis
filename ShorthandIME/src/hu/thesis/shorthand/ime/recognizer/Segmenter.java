
package hu.thesis.shorthand.ime.recognizer;

import android.gesture.GesturePoint;
import android.graphics.Matrix;
import android.graphics.Path;
import android.util.Log;

import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;
import hu.thesis.shorthand.ime.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy pontokból álló alakzat szegmenseire történő bontásához használható
 * osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class Segmenter {

    private static final String TAG = Segmenter.class.getSimpleName();

    /**
     * Vonal tűréshatár, fokokban.
     */
    private static final int THRESHOLD = 10;
    /**
     * Két pont közötti minimum távolság.
     */
    private static final int MINIMUM_DISTANCE = 5;

    private ComplexShape complexShape;
    private final List<Path> mDebugPaths = new ArrayList<>();

    /**
     * Egy szegmens átlagos hossza.
     */
    // private final float mUnit;
    private final float mMinimumLength, mMaximumLength;

    public Segmenter(float unit) {
        // mUnit = unit;
        mMinimumLength = unit / 2;
        mMaximumLength = unit * 1.5f;
    }

    private void appendCurve(List<GesturePoint> points) {
        GesturePoint startingPoint = points.get(0);
        GesturePoint endingPoint = points.get(points.size() - 1);
        double degrees = calculateDegreesBetween(startingPoint, endingPoint);
        Rotation rotation = getRotationFromFloatValue(degrees);
        Form form = calculateCurveForm(points, (float) degrees);
        complexShape.add(new Segment(form, rotation));

        if (Parameters.getInstance().isDebugEnabled()) {
            Path path = new Path();
            path.moveTo(startingPoint.x, startingPoint.y);
            for (int i = 1; i < points.size(); ++i) {
                GesturePoint point = points.get(i);
                path.lineTo(point.x, point.y);
            }
            mDebugPaths.add(path);
        }
    }

    private void appendLine(GesturePoint startingPoint, GesturePoint endingPoint) {
        double degrees = calculateDegreesBetween(startingPoint, endingPoint);
        Rotation rotation = getRotationFromFloatValue(degrees);
        complexShape.add(new Segment(Form.LINE, rotation));

        if (Parameters.getInstance().isDebugEnabled()) {
            Path path = new Path();
            path.moveTo(startingPoint.x, startingPoint.y);
            path.lineTo(endingPoint.x, endingPoint.y);
            mDebugPaths.add(path);
        }
    }

    /**
     * Feldarabolja a két pont közzötti hosszú vonalat és megkapott darabokat
     * hozzáadja a szegmensekhez.
     */
    private void appendLongLine(GesturePoint startingPoint, GesturePoint endingPoint) {
        double distance = calculateDistanceBetween(startingPoint, endingPoint);
        double degrees = calculateDegreesBetween(startingPoint, endingPoint);
        Rotation rotation = getRotationFromFloatValue(degrees);
        int count = 2;
        double splitLength = distance / 2;
        while (splitLength > mMaximumLength) {
            splitLength = distance / ++count;
        }
        for (int i = 0; i < count; ++i) {
            complexShape.add(new Segment(Form.LINE, rotation));
        }

        if (Parameters.getInstance().isDebugEnabled()) {
            Path path = new Path();
            path.moveTo(startingPoint.x, startingPoint.y);
            path.lineTo(endingPoint.x, endingPoint.y);
            mDebugPaths.add(path);
        }
    }

    /**
     * Meghatározza a paraméterek alapján, hogy egy görbe emelkedő vagy lejtő-e.
     */
    private Form calculateCurveForm(List<GesturePoint> curvePoints, float rotation) {
        Matrix matrix = new Matrix();
        GesturePoint startingPoint = curvePoints.get(0);
        matrix.setTranslate(-startingPoint.x, startingPoint.y);
        int length = (curvePoints.size() - 2) * 2;
        float[] points = new float[length];
        for (int i = 1; i < curvePoints.size() - 1; ++i) {
            GesturePoint point = curvePoints.get(i);
            points[(i - 1) * 2] = point.x;
            points[(i - 1) * 2 + 1] = -point.y;
        }
        matrix.mapPoints(points);
        matrix.reset();
        matrix.setRotate(-rotation);

        int plus = 0, minus = 0;

        for (int i = 0; i < length; i += 2) {
            float x = points[i];
            float y = points[i + 1];

            float[] point = new float[2];
            point[0] = x;
            point[1] = y;
            matrix.mapPoints(point);

            if (point[1] < 0) {
                minus++;
            } else if (point[1] > 0) {
                plus++;
            }
        }

        if (plus > minus) {
            return Form.CREST_CURVE;
        } else if (minus > plus) {
            return Form.SAG_CURVE;
        }

        return Form.LINE;
    }

    /**
     * @return A két pont közötti vektor foka.
     */
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

    /**
     * @return A két pont közötti távolság.
     */
    private double calculateDistanceBetween(GesturePoint p1, GesturePoint p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;

        double distance = Math.pow(dx * dx + dy * dy, 0.5);
        return distance;
    }

    /**
     * Alaphelyzetbe hozza az objektumot.
     */
    public void clear() {
        complexShape = null;

        if (Parameters.getInstance().isDebugEnabled()) {
            mDebugPaths.clear();
        }
    }

    public List<Path> getDebugPaths() {
        return mDebugPaths;
    }

    /**
     * Az általunk definiált fokokhoz közelíti a lebegőpontosságú értéket.
     */
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

    /**
     * Pontok tömbjét egy összetett alakzattá alakítja.
     */
    public ComplexShape segment(GesturePoint[] points) {
        complexShape = new ComplexShape();
        List<GesturePoint> curvePoints = new ArrayList<>();
        boolean isCurve = false;
        GesturePoint startingPoint = points[0];
        GesturePoint currentPoint = points[1];

        // ha csak két pontunk van, akkor egyértelműen csak egy vonalunk van
        if (points.length == 2) {
            appendLine(startingPoint, currentPoint);
            return complexShape;
        }

        double degrees = calculateDegreesBetween(startingPoint, currentPoint);
        double lastDegrees = degrees;

        if (Parameters.getInstance().isDebugEnabled()) {
            Log.d(TAG, "degrees of 0-1: " + degrees);
        }

        for (int i = 2; i < points.length; ++i) {
            currentPoint = points[i];
            GesturePoint lastPoint = points[i - 1];

            degrees = calculateDegreesBetween(lastPoint, currentPoint);
            double diff = Math.abs(lastDegrees - degrees);
            if (Parameters.getInstance().isDebugEnabled()) {
                Log.d(TAG, "degrees of " + (i - 1) + "-" + i + ": " + degrees);
                Log.d(TAG, "degrees diff of " + (i - 2) + "-" + (i - 1) + " and " + (i - 1) + "-"
                        + i + ": " + diff);
            }
            if (diff > THRESHOLD) {
                double distance = calculateDistanceBetween(startingPoint, lastPoint);
                if (distance < MINIMUM_DISTANCE) {
                    continue;
                }
                if (distance < mMinimumLength) {
                    if (!isCurve) {
                        isCurve = true;
                        curvePoints.add(startingPoint);
                    }
                    curvePoints.add(lastPoint);
                } else {
                    if (isCurve) {
                        if (distance > mMaximumLength) {
                            curvePoints.add(lastPoint);
                            appendCurve(curvePoints);
                            isCurve = false;
                            curvePoints.clear();
                            startingPoint = lastPoint;
                        } else {
                            curvePoints.add(lastPoint);
                        }
                    } else {
                        if (distance > mMaximumLength) {
                            appendLongLine(startingPoint, lastPoint);
                        } else {
                            appendLine(startingPoint, lastPoint);
                        }
                        startingPoint = lastPoint;
                    }
                }

                lastDegrees = degrees;

                if (i == points.length - 1) {
                    distance = calculateDistanceBetween(startingPoint, currentPoint);
                    if (distance > mMinimumLength) {
                        if (isCurve) {
                            curvePoints.add(currentPoint);
                            appendCurve(curvePoints);
                        } else {
                            appendLine(startingPoint, currentPoint);
                        }
                    }
                }
            } else {
                if (i == points.length - 1) {
                    double distance = calculateDistanceBetween(startingPoint, currentPoint);
                    if (distance >= mMinimumLength) {
                        if (isCurve) {
                            if (distance > mMaximumLength) {
                                curvePoints.add(lastPoint);
                                appendCurve(curvePoints);
                                curvePoints.clear();
                            }
                        } else {
                            if (distance > mMaximumLength) {
                                appendLongLine(startingPoint, lastPoint);
                            } else {
                                appendLine(startingPoint, lastPoint);
                            }
                        }
                    }
                } else {
                    lastDegrees = degrees;
                }
            }
        }

        return complexShape;
    }
}
