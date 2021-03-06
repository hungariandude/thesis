package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;
import hu.thesis.shorthand.textanalyzer.util.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A kromoszómák mutálásáért felelős osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class Mutator {

    protected enum MutationType {
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

    public static final float DEFAULT_MUATION_RATE = 0.01f;

    protected final Map<MutationType, Double> mutationWeightMap;
    protected final Map<Form, Double> bendMutatationWeightMap;

    protected float mutationRate;

    public Mutator() {
	this(DEFAULT_MUATION_RATE);
    }

    public Mutator(float mutationRate) {
	this.mutationRate = mutationRate;

	this.mutationWeightMap = new HashMap<>();
	this.mutationWeightMap.put(MutationType.ADD, 0.5);
	this.mutationWeightMap.put(MutationType.REMOVE, 1.0);
	this.mutationWeightMap.put(MutationType.BEND, 1.2);
	this.mutationWeightMap.put(MutationType.ROTATE, 20.0);

	this.bendMutatationWeightMap = new HashMap<>();
	this.bendMutatationWeightMap.put(Form.CREST_CURVE, 5.0);
	this.bendMutatationWeightMap.put(Form.LINE, 10.0);
	this.bendMutatationWeightMap.put(Form.SAG_CURVE, 5.0);
    }

    /**
     * A kromoszóma mutálása.
     * 
     * @return true, ha történt mutáció, egyébként false
     */
    public boolean mutate(Chromosome chrom) {
	boolean wasMutation = false;
	for (Gene gene : chrom.genes()) {
	    // minél hosszabb a gén, annál valószínűbb, hogy mutálásra kerül
	    if (RandomUtils.random.nextFloat() <= mutationRate * gene.length()) {
		mutate(gene);
		wasMutation = true;
	    }
	}
	return wasMutation;
    }

    /**
     * Egy gén mutálása.
     */
    protected void mutate(Gene gene) {
	List<Segment> segments = gene.getSegments();

	// a mutáció típusa
	// egy elemet tartalmazó génből nem törlünk
	MutationType mutationType = segments.size() == 1 ? RandomUtils
		.randomValue(mutationWeightMap, MutationType.REMOVE)
		: RandomUtils.randomValue(mutationWeightMap);
	// melyik pozíción
	int index = RandomUtils.random.nextInt(segments.size());

	switch (mutationType) {
	case ADD:
	    Segment segment = new Segment();
	    segments.add(index, segment);
	    break;
	case REMOVE:
	    segments.remove(index);
	    break;
	case ROTATE:
	    segment = segments.get(index);
	    segment.setRotation(RandomUtils.randomValue(Rotation.values(),
		    segment.getRotation()));
	    break;
	case BEND:
	    segment = segments.get(index);
	    segment.setForm(RandomUtils.randomValue(bendMutatationWeightMap,
		    segment.getForm()));
	    break;
	}

	gene.recalculateBounds();
    }
}
