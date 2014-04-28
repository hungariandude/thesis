package textanalyzer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Random m�veletek elv�gz�s�t seg�t� statikus oszt�ly.
 * 
 * @author Istv�nfi Zsolt
 */
public final class RandomUtils {
    private static final Random random = new Random();

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott
     * kollekci�b�l.
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
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott
     * kollekci�b�l, amit nem tartalmaz a kiv�tel lista.
     */
    public static <E> E randomValue(Collection<E> collection,
	    Collection<E> exceptions) {
	ArrayList<E> list = new ArrayList<>(collection);
	list.removeAll(exceptions);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott
     * kollekci�b�l, ami nem egyenl� a kiv�tellel.
     */
    public static <E> E randomValue(Collection<E> collection, E exception) {
	ArrayList<E> list = new ArrayList<>(collection);
	list.remove(exception);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott t�mbb�l.
     */
    public static <E> E randomValue(E[] array) {
	return array[random.nextInt(array.length)];
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott t�mbb�l,
     * amit nem tartalmaz a kiv�tel lista.
     */
    public static <E> E randomValue(E[] array, Collection<E> exceptions) {
	ArrayList<E> list = new ArrayList<>(Arrays.asList(array));
	list.removeAll(exceptions);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott t�mbb�l, ami
     * nem egyenl� a kiv�tellel.
     */
    public static <E> E randomValue(E[] array, E exception) {
	ArrayList<E> list = new ArrayList<>(Arrays.asList(array));
	list.remove(exception);
	return list.get(random.nextInt(list.size()));
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott s�lyoz�si
     * t�rk�p alapj�n.
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
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott s�lyoz�si
     * t�rk�p alapj�n, amit nem tartalmaz a kiv�tel lista.
     */
    public static <E> E randomValue(Map<E, ? extends Number> weightMap,
	    Collection<E> exceptions) {
	HashMap<E, ? extends Number> map = new HashMap<>(weightMap);
	map.keySet().removeAll(exceptions);
	return randomValue(map);
    }

    /**
     * Visszaad egy v�letlenszer� �rt�ket a param�terk�nt megkapott s�lyoz�si
     * t�rk�p alapj�n, ami nem egyenl� a kiv�tellel.
     */
    public static <E> E randomValue(Map<E, ? extends Number> weightMap,
	    E exception) {
	HashMap<E, ? extends Number> map = new HashMap<>(weightMap);
	map.remove(exception);
	return randomValue(map);
    }

    private RandomUtils() {
	// statikus oszt�ly
    }
}
