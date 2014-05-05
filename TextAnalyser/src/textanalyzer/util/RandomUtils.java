package textanalyzer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Random műveletek elvégzését segítő statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class RandomUtils {
    private static final Random random = new Random();

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott
     * kollekcióból.
     */
    public static <E> E randomValue(Collection<E> collection) {
	int randomIndex = random.nextInt(collection.size());
	int i = 0;
	for (E element : collection) {
	    if (i == randomIndex) {
		return element;
	    }
	    ++i;
	}
	return null;
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott
     * kollekcióból, amit nem tartalmaz a kivétel lista.
     */
    public static <E> E randomValue(Collection<E> collection,
	    Collection<E> exceptions) {
	ArrayList<E> list = new ArrayList<>(collection);
	list.removeAll(exceptions);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott
     * kollekcióból, ami nem egyenlő a kivétellel.
     */
    public static <E> E randomValue(Collection<E> collection, E exception) {
	ArrayList<E> list = new ArrayList<>(collection);
	list.remove(exception);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott tömbből.
     */
    public static <E> E randomValue(E[] array) {
	return array[random.nextInt(array.length)];
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott tömbből,
     * amit nem tartalmaz a kivétel lista.
     */
    public static <E> E randomValue(E[] array, Collection<E> exceptions) {
	ArrayList<E> list = new ArrayList<>(Arrays.asList(array));
	list.removeAll(exceptions);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott tömbből, ami
     * nem egyenlő a kivétellel.
     */
    public static <E> E randomValue(E[] array, E exception) {
	ArrayList<E> list = new ArrayList<>(Arrays.asList(array));
	list.remove(exception);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott súlyozási
     * térkép alapján.
     */
    public static <E> E randomValue(Map<E, ? extends Number> weightMap) {
	double totalWeight = 0.0;
	for (Number n : weightMap.values()) {
	    totalWeight += n.doubleValue();
	}
	double rand = random.nextDouble() * totalWeight;
	double totalWeightSoFar = 0.0;
	for (Entry<E, ? extends Number> n : weightMap.entrySet()) {
	    totalWeightSoFar += n.getValue().doubleValue();
	    if (totalWeightSoFar >= rand) {
		return n.getKey();
	    }
	}
	return null;
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott súlyozási
     * térkép alapján, amit nem tartalmaz a kivétel lista.
     */
    public static <E> E randomValue(Map<E, ? extends Number> weightMap,
	    Collection<E> exceptions) {
	HashMap<E, ? extends Number> map = new HashMap<>(weightMap);
	map.keySet().removeAll(exceptions);
	return randomValue(map);
    }

    /**
     * Visszaad egy véletlenszerű értéket a paraméterként megkapott súlyozási
     * térkép alapján, ami nem egyenlő a kivétellel.
     */
    public static <E> E randomValue(Map<E, ? extends Number> weightMap,
	    E exception) {
	HashMap<E, ? extends Number> map = new HashMap<>(weightMap);
	map.remove(exception);
	return randomValue(map);
    }

    private RandomUtils() {
	// statikus osztály
    }
}
