package textanalyzer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * Enum t�pusokkal kapcsolatos m�veleteket seg�t� statikus oszt�ly.
 * 
 * @author Istv�nfi Zsolt
 */
public class EnumUtils {
    private static final Random random = new Random();

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott enumb�l.
     */
    public static <E extends Enum<?>> E randomValue(Class<E> enumClass) {
	E[] enumConstants = enumClass.getEnumConstants();
	return enumConstants[random.nextInt(enumConstants.length)];
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott enumb�l,
     * amit nem tartalmaz a kiv�tel lista.
     */
    public static <E extends Enum<?>> E randomValue(Class<E> enumClass,
	    Collection<E> exceptions) {
	E[] enumConstants = enumClass.getEnumConstants();
	ArrayList<E> list = new ArrayList<>(Arrays.asList(enumConstants));
	list.removeAll(exceptions);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott enumb�l, ami
     * nem egyenl� a kiv�tellel.
     */
    public static <E extends Enum<?>> E randomValue(Class<E> enumClass,
	    E exception) {
	E[] enumConstants = enumClass.getEnumConstants();
	ArrayList<E> list = new ArrayList<>(Arrays.asList(enumConstants));
	list.remove(exception);
	return list.get(random.nextInt(list.size()));
    }

    private EnumUtils() {
	// statikus oszt�ly
    }
}
