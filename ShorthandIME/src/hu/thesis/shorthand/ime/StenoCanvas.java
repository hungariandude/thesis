
package hu.thesis.shorthand.ime;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.gesture.GesturePoint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * A gyorsíró beviteli eszköz rajzoló felülete.
 * 
 * @author Istvánfi Zsolt
 */
public class StenoCanvas extends GestureOverlayView {

    private boolean clearing = false;

    private GesturePoint[] mDebugPoints;
    private Paint mDebugPaint;

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
            mDebugPaint = new Paint();
            mDebugPaint.setColor(Color.BLUE);
            mDebugPaint.setAntiAlias(true);
            mDebugPaint.setStrokeWidth(5);
            mDebugPaint.setStyle(Paint.Style.STROKE);
            mDebugPaint.setStrokeJoin(Paint.Join.ROUND);
            mDebugPaint.setStrokeCap(Paint.Cap.ROUND);
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

        if (ShorthandIME.DEBUG && this.mDebugPoints != null) {
            for (GesturePoint point : this.mDebugPoints) {
                canvas.drawPoint(point.x, point.y, mDebugPaint);
            }
        }
    }

    public void reset() {
        clearing = true;
        invalidate();
    }

    public void setDebugPoints(GesturePoint[] points) {
        this.mDebugPoints = points;
    }
}
