
package hu.thesis.shorthand.ime;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Megjeleníti az alkalmazás beállításait az Android beviteli eszközök
 * beállításaiban.
 * 
 * @author Istvánfi Zsolt
 */
public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Betöltjük a preferenciákat egy XML resource-ból
        addPreferencesFromResource(R.xml.preferences);
    }
}
