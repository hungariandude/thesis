
package hu.thesis.shorthand.ime;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.android.inputmethodcommon.InputMethodSettingsFragment;

/**
 * Megjeleníti az alkalmazás beállításait az Android beviteli eszközök
 * beállításaiban.
 * 
 * @author Istvánfi Zsolt
 */
public class IMESettingsActivity extends PreferenceActivity {

    public static class Settings extends InputMethodSettingsFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Betöltjük a preferenciákat egy XML resource-ból
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    public Intent getIntent() {
        final Intent modIntent = new Intent(super.getIntent());
        modIntent.putExtra(EXTRA_SHOW_FRAGMENT, Settings.class.getName());
        modIntent.putExtra(EXTRA_NO_HEADERS, true);
        return modIntent;
    }

    @Override
    public boolean isValidFragment(String fragment) {
        if (Settings.class.getName().equals(fragment)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Felülírjuk az alapértelmezett activity címét
        setTitle(R.string.settings_name);
    }
}
