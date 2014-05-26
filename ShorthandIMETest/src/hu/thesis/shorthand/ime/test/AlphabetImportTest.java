package hu.thesis.shorthand.ime.test;

import android.test.AndroidTestCase;

import hu.thesis.shorthand.common.CharMappingSaveData;
import hu.thesis.shorthand.ime.util.ShorthandUtils;

public class AlphabetImportTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
	super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void testLoadDefaultCharMapping() {
	CharMappingSaveData[] saveData = ShorthandUtils
		.readCharMappingFromResource(mContext);
	if (saveData == null) {
	    fail("Imported data is null");
	} else if (saveData.length == 0) {
	    fail("Imported data is empty");
	}
    }
}
