package textanalyzer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * Enum típusokkal kapcsolatos mûveleteket segítõ statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class EnumUtils {
    private static final Random random = new Random();

    /**
     * Visszaad egy véletlenszerû értéket a paraméterként megkapott enumból.
     */
    public static <E extends Enum<?>> E randomValue(Class<E> enumClass) {
	E[] enumConstants = enumClass.getEnumConstants();
	return enumConstants[random.nextInt(enumConstants.length)];
    }

    /**
     * Visszaad egy véletlenszerû értéket a paraméterként megkapott enumból,
     * amit nem tartalmaz a kivétel lista.
     */
    public static <E extends Enum<?>> E randomValue(Class<E> enumClass,
	    Collection<E> exceptions) {
	E[] enumConstants = enumClass.getEnumConstants();
	ArrayList<E> list = new ArrayList<>(Arrays.asList(enumConstants));
	list.removeAll(exceptions);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy véletlenszerû értéket a paraméterként megkapott enumból, ami
     * nem egyenlõ a kivétellel.
     */
    public static <E extends Enum<?>> E randomValue(Class<E> enumClass,
	    E exception) {
	E[] enumConstants = enumClass.getEnumConstants();
	ArrayList<E> list = new ArrayList<>(Arrays.asList(enumConstants));
	list.remove(exception);
	return list.get(random.nextInt(list.size()));
    }

    private EnumUtils() {
	// statikus osztály
    }
}
