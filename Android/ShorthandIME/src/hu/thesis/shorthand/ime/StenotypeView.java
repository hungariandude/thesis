
package hu.thesis.shorthand.ime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * A gyorsíró beviteli eszköz látható felülete, ami tartalmazza a karakterek
 * megrajzolását biztosító felületet is.
 * 
 * @author Istvánfi Zsolt
 */
public class StenotypeView extends View {

    // public StenotypeView(Context context) {
    // super(context);
    // }

    public StenotypeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StenotypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void closing() {
        // TODO Auto-generated method stub

    }

    // @Override
    // public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // // Round up a little
    // if (mKeyboard == null) {
    // setMeasuredDimension(mPaddingLeft + mPaddingRight, mPaddingTop +
    // mPaddingBottom);
    // } else {
    // int width = mKeyboard.getMinWidth() + mPaddingLeft + mPaddingRight;
    // if (MeasureSpec.getSize(widthMeasureSpec) < width + 10) {
    // width = MeasureSpec.getSize(widthMeasureSpec);
    // }
    // setMeasuredDimension(width, mKeyboard.getHeight() + mPaddingTop +
    // mPaddingBottom);
    // }
    // }

}
