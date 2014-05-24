
package hu.thesis.shorthand.ime;

import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.inputmethodservice.InputMethodService;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import hu.thesis.shorthand.ime.recognizer.Recognizer;
import hu.thesis.shorthand.ime.util.ShorthandUtils;

/**
 * A gyorsíró beviteli eszköz implementációja.
 * 
 * @author Istvánfi Zsolt
 */
public class ShorthandIME extends InputMethodService implements OnGesturePerformedListener,
        OnClickListener {

    private View mContainerView;
    private StenoCanvas mStenoCanvas;

    private Recognizer mRecognizer;
    private Context mContext;
    private Parameters mParameters;

    // private static final String TAG = ShorthandIME.class.getSimpleName();

    @Override
    public void onClick(View view) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.deleteSurroundingText(1, 0);
        }
    }

    /**
     * Itt inicializáljuk a beviteli eszközt.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        float drawingAreaHeight = ShorthandUtils.dpToPx(mContext, 250.0f);
        mRecognizer = new Recognizer(mContext, drawingAreaHeight / 3);
        mRecognizer.loadDefaultCharMapping();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Parameters.initInstance(sharedPrefs);
        mParameters = Parameters.getInstance();
        sharedPrefs.registerOnSharedPreferenceChangeListener(mParameters);
    }

    /**
     * Itt hozhatjuk létre a jelölteket mutató mezőt, ami alapesetben a beviteli
     * mező felett található.
     */
    @Override
    public View onCreateCandidatesView() {
        return super.onCreateCandidatesView();
    }

    /**
     * Itt kéri tőlünk a framework azt, hogy hozzuk létre a beviteli eszközünk
     * nézetét.
     */
    @Override
    public View onCreateInputView() {
        super.onCreateInputView();

        mContainerView = getLayoutInflater().inflate(R.layout.input, null);
        mStenoCanvas = (StenoCanvas) mContainerView.findViewById(R.id.canvas);

        mStenoCanvas.addOnGesturePerformedListener(this);
        mStenoCanvas.addOnClickListener(this);

        return mContainerView;
    }

    /**
     * Ez akkor hívódik meg, amikor a felhasználó befejezte egy mező
     * szerkesztését. Arra használhatjuk, hogy visszaállítsuk alaphelyzetbe az
     * állapotunkat.
     */
    @Override
    public void onFinishInput() {
        super.onFinishInput();

        resetState();
    }

    /**
     * Ez hívódik meg, amikor az input mező eltűnik a felhasználó elől.
     */
    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        // Log.d(TAG, "Strokes size: " + gesture.getStrokesCount());

        InputConnection ic = getCurrentInputConnection();

        for (GestureStroke stroke : gesture.getStrokes()) {
            GesturePoint[] points = ShorthandUtils.extractGesturePointsFromStroke(stroke);
            String result = mRecognizer.recognize(points);
            if (mParameters.isDebugEnabled()) {
                StenoCanvas canvas = (StenoCanvas) overlay;
                canvas.setDebugPaths(mRecognizer.getDebugPaths());
                canvas.setDebugPoints(points);
            }
            if (result != null && !result.isEmpty()) {
                ic.commitText(result, 1);
            } else if (mParameters.isPopupsEnabled()) {
                Toast.makeText(mContext, R.string.not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Itt végezhetünk el mindenféle UI inicializációt. Az {@code onCreate()}
     * metódus után és konfigurációs módosítások után hívódik meg.
     */
    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
    }

    /**
     * Ez az a fő pont, ahol elvégezhetjük a beviteli eszközünk inicializálását,
     * hogy elkezdhessen dolgozni az alkalmazáson. Itt már kapcsolódva vagyunk a
     * klienshez és már megkapunk minden részletes információt a cél szerkesztő
     * mezőről.
     */
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        // Erre azért van szükség, mert az alattunk lévő beviteli mező állapota
        // megváltozhatott.
        resetState();
    }

    /**
     * Ez hívódik meg, amikor a beviteli mező megjelent és elkezdhetünk írni a
     * szöveges mezőbe.
     */
    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
    }

    /**
     * Ez hívódik meg, amikor megszűnik a kapcsolat a kliens mezővel.
     */
    @Override
    public void onUnbindInput() {
        super.onUnbindInput();
    }

    /**
     * Ez akkor hívódik meg, amikor a szerkesztett mezőben elmozdul a kurzor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd,
            int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart,
                candidatesEnd);
    }

    /**
     * Alaphelyzetbe hozza a beviteli eszközt.
     */
    private void resetState() {
        if (mStenoCanvas != null) {
            // Visszaállítjuk alaphelyzetbe a rajzoló felületet.
            mStenoCanvas.reset();
        }
    }
}
