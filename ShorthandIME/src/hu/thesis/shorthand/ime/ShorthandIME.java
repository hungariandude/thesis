
package hu.thesis.shorthand.ime;

import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import hu.thesis.shorthand.ime.recognizer.Recognizer;
import hu.thesis.shorthand.ime.util.RecognizerUtils;

/**
 * A gyorsíró beviteli eszköz implementációja.
 * 
 * @author Istvánfi Zsolt
 */
public class ShorthandIME extends InputMethodService implements OnGesturePerformedListener {

    public static final boolean DEBUG = true;

    private View mContainerView;
    private StenoCanvas mStenoCanvas;
    private int mLastDisplayWidth;
    private StringBuilder mComposingText = new StringBuilder();
    private Recognizer mRecognizer;

    /**
     * Itt inicializáljuk a beviteli eszközt.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mRecognizer = new Recognizer(getApplicationContext());
        mRecognizer.loadDefaultCharMapping();
    }

    /**
     * Itt hozhatjuk létre a jelölteket mutató mezőt, ami alapesetben a beviteli
     * mező felett található.
     */
    @Override
    public View onCreateCandidatesView() {
        super.onCreateCandidatesView();

        // TODO nincs implementálva
        return null;
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
        // Log.d(ShorthandIME.class.getSimpleName(), "Strokes size: " +
        // gesture.getStrokesCount());

        InputConnection ic = getCurrentInputConnection();

        for (GestureStroke stroke : gesture.getStrokes()) {
            GesturePoint[] points = RecognizerUtils.extractGesturePointsFromStroke(stroke);
            String result = mRecognizer.recognize(points);
            if (DEBUG) {
                StenoCanvas canvas = (StenoCanvas) overlay;
                canvas.setDebugPaths(mRecognizer.getDebugPaths());
                canvas.setDebugPoints(points);
            }
            if (result != null) {
                mComposingText.append(result);
                ic.commitText(result, 1);
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

        // Megnézzük, hogy változott-e a rendelkezésünkre álló szélesség. Ha
        // igen, akkor ahhoz képest kell újraméreteznünk a rajzolófelületünket.
        int displayWidth = getMaxWidth();
        if (displayWidth == mLastDisplayWidth) {
            return;
        }
        mLastDisplayWidth = displayWidth;
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

        // Ha az aktuális kijelölés változik a szöveges mezőben, akkor törölnünk
        // kell az ajánlást
        if (mComposingText.length() > 0
                && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
            mComposingText.setLength(0);
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * Alaphelyzetbe hozza a beviteli eszközt.
     */
    private void resetState() {
        // Töröljük az éppen képzett szöveget.
        mComposingText.setLength(0);

        if (mStenoCanvas != null) {
            // Visszaállítjuk alaphelyzetbe a rajzoló felületet.
            mStenoCanvas.reset();
        }
    }
}
