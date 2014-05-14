package textanalyzer.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Karakterek lokálspecifikus rendezése.
 * 
 * @author Istvánfi Zsolt
 */
public class CharCollator implements Comparator<Character> {

    private Collator collator;

    public CharCollator(Locale locale) {
	collator = Collator.getInstance(locale);
	collator.setStrength(Collator.PRIMARY);
    }

    @Override
    public int compare(Character o1, Character o2) {
	int result = collator.compare(o1.toString(), o2.toString());
	if (result == 0) {
	    result = Character.compare(o1, o2);
	}
	return result;
    }

}
