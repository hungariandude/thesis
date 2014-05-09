
package hu.thesis.shorthand.ime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * A gyorsíró beviteli eszköz rajzoló felülete.
 * 
 * @author Istvánfi Zsolt
 */
public class ShorthandCanvas extends SurfaceView {

    public ShorthandCanvas(Context context) {
        super(context);
        init();
    }

    public ShorthandCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShorthandCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // public void closing() {

    // }

    private void init() {
        // TODO Auto-generated method stub

    }

    // @Override
    // public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // int width = mKeyboard.getMinWidth() + mPaddingLeft + mPaddingRight;
    // if (MeasureSpec.getSize(widthMeasureSpec) < width + 10) {
    // width = MeasureSpec.getSize(widthMeasureSpec);
    // }
    // setMeasuredDimension(width, mKeyboard.getHeight() + mPaddingTop +
    // mPaddingBottom);
    // }

}
