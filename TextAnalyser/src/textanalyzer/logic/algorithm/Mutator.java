package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.logic.DrawingObject.Orientation;
import textanalyzer.logic.DrawingObject.Shape;
import textanalyzer.util.EnumUtils;

import java.util.List;
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
	ROTATE
    }

    private static final float DEFAULT_MUATION_RATE = 0.001f;

    private float mutationRate;
    private final Random random = new Random();

    public Mutator() {
	mutationRate = DEFAULT_MUATION_RATE;
    }

    public Mutator(float mutationRate) {
	this.mutationRate = mutationRate;
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
	MutationType mutationType = buildingElements.size() == 1 ? EnumUtils
		.randomValue(MutationType.class, MutationType.REMOVE)
		: EnumUtils.randomValue(MutationType.class);
	// melyik poz�ci�n
	int index = random.nextInt(buildingElements.size());

	switch (mutationType) {
	case ADD:
	    // Point2D startingPoint = index == 0 ? new Point() :
	    // buildingElements.get(index -1).getEndingPoint();
	    // DrawingObject object = new DrawingObject(startingPoint);
	    // if(index != buildingElements.size() - 1)
	    // {
	    // buildingElements.get(index).sets
	    // }
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
	    object.setOrientation(EnumUtils.randomValue(Orientation.class,
		    object.getOrientation()));
	    break;
	case TRANSSHAPE:
	    object = buildingElements.get(index);
	    object.setShape(EnumUtils.randomValue(Shape.class,
		    object.getShape()));
	    break;
	}
    }
}
