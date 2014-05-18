
package hu.thesis.shorthand.ime;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.gesture.GesturePoint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.List;

/**
 * A gyorsíró beviteli eszköz rajzoló felülete.
 * 
 * @author Istvánfi Zsolt
 */
public class StenoCanvas extends GestureOverlayView {

    private boolean clearing = false;

    private GesturePoint[] mDebugPoints;
    private List<Path> mDebugPaths;
    private Paint mDebugPointPaint, mDebugPathPaint0, mDebugPathPaint1;

    public StenoCanvas(Context context) {
        super(context);
        init();
    }

    public StenoCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StenoCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (ShorthandIME.DEBUG) {
            mDebugPointPaint = new Paint();
            mDebugPointPaint.setColor(Color.RED);
            mDebugPointPaint.setAntiAlias(true);
            mDebugPointPaint.setStrokeWidth(5);
            mDebugPointPaint.setStyle(Paint.Style.STROKE);
            mDebugPointPaint.setStrokeJoin(Paint.Join.ROUND);
            mDebugPointPaint.setStrokeCap(Paint.Cap.ROUND);

            mDebugPathPaint0 = new Paint();
            mDebugPathPaint0.setColor(Color.CYAN);
            mDebugPathPaint0.setAntiAlias(true);
            mDebugPathPaint0.setStrokeWidth(5);
            mDebugPathPaint0.setStyle(Paint.Style.STROKE);
            mDebugPathPaint0.setStrokeJoin(Paint.Join.ROUND);
            mDebugPathPaint0.setStrokeCap(Paint.Cap.ROUND);

            mDebugPathPaint1 = new Paint();
            mDebugPathPaint1.setColor(Color.WHITE);
            mDebugPathPaint1.setAntiAlias(true);
            mDebugPathPaint1.setStrokeWidth(5);
            mDebugPathPaint1.setStyle(Paint.Style.STROKE);
            mDebugPathPaint1.setStrokeJoin(Paint.Join.ROUND);
            mDebugPathPaint1.setStrokeCap(Paint.Cap.ROUND);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (clearing) {
            canvas.drawColor(R.color.canvas_background);
            clearing = false;
            return;
        }

        if (ShorthandIME.DEBUG) {
            if (this.mDebugPaths != null) {
                for (int i = 0; i < mDebugPaths.size(); ++i) {
                    Path path = mDebugPaths.get(i);
                    Paint paint = i % 2 == 0 ? mDebugPathPaint0 : mDebugPathPaint1;
                    canvas.drawPath(path, paint);
                }
            }
            if (this.mDebugPoints != null) {
                for (GesturePoint point : this.mDebugPoints) {
                    canvas.drawPoint(point.x, point.y, mDebugPointPaint);
                }
            }
        }
    }

    public void reset() {
        clearing = true;
        invalidate();
    }

    public void setDebugPaths(List<Path> debugPaths) {
        this.mDebugPaths = debugPaths;
    }

    public void setDebugPoints(GesturePoint[] points) {
        this.mDebugPoints = points;
    }
}
