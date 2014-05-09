
package hu.thesis.shorthand.ime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * A gyorsíró beviteli eszköz rajzoló felülete.
 * 
 * @author Istvánfi Zsolt
 */
public class StenoCanvas extends SurfaceView {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mDrawPaint, mBgPaint;
    private float mX, mY;

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
        mPath = new Path();

        mBgPaint = new Paint(Paint.DITHER_FLAG);
        mBgPaint.setColor(getResources().getColor(R.color.canvas_background));

        mDrawPaint = new Paint();
        mDrawPaint.setColor(Color.BLUE);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(20);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBgPaint);
        canvas.drawPath(mPath, mDrawPaint);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void reset() {
        mCanvas.drawPaint(mBgPaint);
        invalidate();
    }

    private void touchMove(float x, float y) {
        // float dx = Math.abs(x - mX);
        // float dy = Math.abs(y - mY);
        // if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
        mX = x;
        mY = y;
        // }
    }

    private void touchStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mDrawPaint);
        mPath.reset();
    }
}
