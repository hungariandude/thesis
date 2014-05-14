package textanalyzer.logic.algorithm;

import textanalyzer.logic.algorithm.Shape.Form;
import textanalyzer.logic.algorithm.Shape.Rotation;
import textanalyzer.util.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A kromoszómák mutálásáért felelős osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class Mutator {

    private enum MutationType {
	/**
	 * Új objektum hozzáadása véletlenszerű pozíción.
	 */
	ADD,
	/**
	 * Egy objektum törlése véletlenszerű pozíción.
	 */
	REMOVE,
	/**
	 * Egy objektum alakjának megváltoztatása.
	 */
	BEND,
	/**
	 * Egy objektum orientációjának megváltoztatása.
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
	this.mutationWeightMap.put(MutationType.ADD, 1.0);
	this.mutationWeightMap.put(MutationType.REMOVE, 1.0);
	this.mutationWeightMap.put(MutationType.BEND, 1.2);
	this.mutationWeightMap.put(MutationType.ROTATE, 20.0);
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
	List<Shape> buildingElements = gene.getBuildingElements();

	// a mutáció típusa
	// egy elemet tartalmazó génből nem törlünk
	MutationType mutationType = buildingElements.size() == 1 ? RandomUtils
		.randomValue(mutationWeightMap, MutationType.REMOVE)
		: RandomUtils.randomValue(mutationWeightMap);
	// melyik pozíción
	int index = random.nextInt(buildingElements.size());

	switch (mutationType) {
	case ADD:
	    Shape shape = new Shape();
	    buildingElements.add(index, shape);
	    break;
	case REMOVE:
	    buildingElements.remove(index);
	    break;
	case ROTATE:
	    shape = buildingElements.get(index);
	    shape.setRotation(RandomUtils.randomValue(Rotation.values(),
		    shape.getRotation()));
	    break;
	case BEND:
	    shape = buildingElements.get(index);
	    shape.setForm(RandomUtils.randomValue(Form.values(),
		    shape.getForm()));
	    break;
	}

	gene.recalculateDrawingSize();
    }
}
