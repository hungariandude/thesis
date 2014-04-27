package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.logic.DrawingObject.Orientation;
import textanalyzer.logic.DrawingObject.Shape;
import textanalyzer.util.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A kromosz�m�k mut�l�s��rt felel�s oszt�ly.
 * 
 * @author Istv�nfi Zsolt
 */
public class Mutator {
    private enum MutationType {
	/**
	 * �j objektum hozz�ad�sa v�letlenszer� poz�ci�n.
	 */
	ADD,
	/**
	 * Egy objektum t�rl�se v�letlenszer� poz�ci�n.
	 */
	REMOVE,
	/**
	 * Egy objektum ir�ny�nak megford�t�sa.
	 */
	INVERT,
	/**
	 * Egy objektum alakj�nak megv�ltoztat�sa.
	 */
	TRANSSHAPE,
	/**
	 * Egy objektum orient�ci�j�nak megv�ltoztat�sa.
	 */
	ROTATE;
    }

    private static final float DEFAULT_MUATION_RATE = 0.01f;

    private final Map<MutationType, Double> mutationWeightMap;

    private float mutationRate;
    private final Random random = new Random();

    public Mutator() {
	this(DEFAULT_MUATION_RATE);
    }

    public Mutator(float mutationRate) {
	this.mutationRate = mutationRate;

	this.mutationWeightMap = new HashMap<>();
	this.mutationWeightMap.put(MutationType.ADD, 10.0);
	this.mutationWeightMap.put(MutationType.REMOVE, 10.0);
	this.mutationWeightMap.put(MutationType.INVERT, 1.0);
	this.mutationWeightMap.put(MutationType.TRANSSHAPE, 5.0);
	this.mutationWeightMap.put(MutationType.ROTATE, 5.0);
    }

    /**
     * A kromosz�ma mut�l�sa.
     * 
     * @return true, ha t�rt�nt mut�ci�, egy�bk�nt false
     */
    public boolean mutate(Chromosome chrom) {
	boolean wasMutation = false;
	for (Gene gene : chrom.genes()) {
	    if (random.nextFloat() <= mutationRate) {
		mutate(gene);
		wasMutation = true;
	    }
	}
	return wasMutation;
    }

    /**
     * Egy g�n mut�l�sa.
     */
    private void mutate(Gene gene) {
	List<DrawingObject> buildingElements = gene.getBuildingElements();

	// a mut�ci� t�pusa
	// egy elemet tartalmaz� g�nb�l nem t�rl�nk
	MutationType mutationType = buildingElements.size() == 1 ? RandomUtils
		.randomValue(mutationWeightMap, MutationType.REMOVE)
		: RandomUtils.randomValue(mutationWeightMap);
	// melyik poz�ci�n
	int index = random.nextInt(buildingElements.size());

	switch (mutationType) {
	case ADD:
	    DrawingObject object = new DrawingObject();
	    buildingElements.add(index, object);
	    break;
	case REMOVE:
	    buildingElements.remove(index);
	    break;
	case INVERT:
	    buildingElements.get(index).invertDirection();
	    break;
	case ROTATE:
	    object = buildingElements.get(index);
	    object.setOrientation(RandomUtils.randomValue(Orientation.values(),
		    object.getOrientation()));
	    break;
	case TRANSSHAPE:
	    object = buildingElements.get(index);
	    object.setShape(RandomUtils.randomValue(Shape.values(),
		    object.getShape()));
	    break;
	}
    }
}
