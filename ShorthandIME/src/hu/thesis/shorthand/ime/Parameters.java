
package hu.thesis.shorthand.ime;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

/**
 * A beállításokat tartalmazó singleton osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class Parameters implements OnSharedPreferenceChangeListener {
    private static final String DEBUG_MODE = "debug_mode";
    private static final String SHOW_POPUPS = "show_popups";
    private static final String PAUSE_BETWEEN_CHARS = "pause_between_chars";

    private boolean mDebugEnabled;
    private boolean mPopupsEnabled;
    private int mPauseBetweenChars;

    private static String TAG = Parameters.class.getSimpleName();
    private static Parameters instance;

    /**
     * @throws NullPointerException Ha a paraméterek még nincsenek
     *             inicializálva.
     */
    public static Parameters getInstance() {
        return instance;
    }

    public static void initInstance(SharedPreferences prefs) {
        Log.d(TAG, "Initializing parameters...");
        int pauseBetweenChars = Integer.parseInt(prefs.getString(PAUSE_BETWEEN_CHARS, "300"));
        Log.d(TAG, PAUSE_BETWEEN_CHARS + '=' + pauseBetweenChars);
        boolean popupsEnabled = prefs.getBoolean(SHOW_POPUPS, true);
        Log.d(TAG, SHOW_POPUPS + '=' + popupsEnabled);
        boolean debugEnabled = prefs.getBoolean(DEBUG_MODE, false);
        Log.d(TAG, DEBUG_MODE + '=' + debugEnabled);
        instance = new Parameters(debugEnabled, popupsEnabled, pauseBetweenChars);
        Log.d(TAG, "...done.");
    }

    /**
     * Singleton osztály.
     */
    private Parameters(boolean debugEnabled, boolean popupsEnabled, int pauseBetweenChars) {
        mDebugEnabled = debugEnabled;
        mPopupsEnabled = popupsEnabled;
        mPauseBetweenChars = pauseBetweenChars;
    }

    public int getPauseBetweenChars() {
        return mPauseBetweenChars;
    }

    public boolean isDebugEnabled() {
        return mDebugEnabled;
    }

    public boolean isPopupsEnabled() {
        return mPopupsEnabled;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        switch (key) {
            case PAUSE_BETWEEN_CHARS: {
                mPauseBetweenChars = Integer.parseInt(prefs.getString(
                        PAUSE_BETWEEN_CHARS, "300"));
                break;
            }
            case SHOW_POPUPS: {
                mPopupsEnabled = prefs.getBoolean(key, true);
                break;
            }
            case DEBUG_MODE: {
                mDebugEnabled = prefs.getBoolean(key, false);
                break;
            }
        }
        if (mDebugEnabled) {
            Log.d(TAG, "onSharedPreferenceChanged() called, changed preference: " + key);
        }
    }
}
