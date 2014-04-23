package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.logic.DrawingObject.Orientation;
import textanalyzer.logic.DrawingObject.Shape;
import textanalyzer.util.EnumUtils;

import java.util.List;
import java.util.Random;

/**
 * A kromoszómák mutálásáért felelõs osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class Mutator {
    private enum MutationType {
	/**
	 * Új objektum hozzáadása véletlenszerû pozíción.
	 */
	ADD,
	/**
	 * Egy objektum törlése véletlenszerû pozíción.
	 */
	REMOVE,
	/**
	 * Egy objektum irányának megfordítása.
	 */
	INVERT,
	/**
	 * Egy objektum alakjának megváltoztatása.
	 */
	TRANSSHAPE,
	/**
	 * Egy objektum orientációjának megváltoztatása.
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
     * A kromoszóma mutálása.
     * 
     * @return true, ha történt mutáció, egyébként false
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
     * Egy gén mutálása.
     */
    private void mutate(Gene gene) {
	List<DrawingObject> buildingElements = gene.getBuildingElements();

	// a mutáció típusa
	// egy elemet tartalmazó génbõl nem törlünk
	MutationType mutationType = buildingElements.size() == 1 ? EnumUtils
		.randomValue(MutationType.class, MutationType.REMOVE)
		: EnumUtils.randomValue(MutationType.class);
	// melyik pozíción
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
