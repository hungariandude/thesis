package textanalyzer.util;

/**
 * Karakterekkel kapcsolatos műveletek elvégzését segítő statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class CharacterUtils {

    public static String escapeChar(char ch) {
	String str;
	switch (ch) {
	case '\n':
	    str = "\\n";
	    break;
	case '\t':
	    str = "\\t";
	    break;
	case '\r':
	    str = "\\r";
	    break;
	default:
	    str = String.valueOf(ch);
	}
	return str;
    }

    private CharacterUtils() {
	// statikus osztály
    }
}
