package textanalyzer.logic.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A genetikus algoritmusban szereplõ kromoszóma.
 * 
 * @author Istvánfi Zsolt
 */
public class Chromosome {
    public static final float CROSSOVER_RATE = 0.7f;
    public static final float MUTATION_RATE = 0.001f;

    private static final Random random = new Random();

    /**
     * Két kromoszóma keresztezése.
     */
    public static void crossover(Chromosome chrom1, Chromosome chrom2) {
	// kell?
	if (random.nextFloat() > CROSSOVER_RATE) {
	    return;
	}

	// kromoszómaméretek ellenõrzése
	if (chrom1.geneMap.size() != chrom2.geneMap.size()) {
	    throw new IllegalArgumentException("Chromosome sizes do not match.");
	}

	// elvégezzük a keresztezést
	int swappingIndex = random.nextInt(chrom1.geneMap.size());
	int index = 0;
	Character charAtIndex = null;
	for (Iterator<Character> it = chrom1.geneMap.navigableKeySet()
		.iterator(); index <= swappingIndex && it.hasNext();) {
	    charAtIndex = it.next();
	}
	SortedMap<Character, Gene> headMapFrom1 = chrom1.geneMap
		.headMap(charAtIndex);
	SortedMap<Character, Gene> tailMapFrom2 = chrom2.geneMap
		.tailMap(charAtIndex);
	chrom1.geneMap.keySet().retainAll(headMapFrom1.keySet());
	chrom1.geneMap.putAll(tailMapFrom2);
	chrom2.geneMap.keySet().retainAll(tailMapFrom2.keySet());
	chrom2.geneMap.putAll(headMapFrom1);
    }

    private TreeMap<Character, Gene> geneMap;
    private int fitnessScore;

    /**
     * Létrehoz egy véletlenszerû kromoszómát a paraméterek alapján.
     */
    public Chromosome(Collection<Character> chars, int maxGeneLength) {
	this.geneMap = new TreeMap<>();
	for (Character ch : chars) {
	    Gene gene = new Gene(random.nextInt(maxGeneLength + 1));
	    geneMap.put(ch, gene);
	}
    }

    public Chromosome(TreeMap<Character, Gene> geneMap) {
	this.geneMap = geneMap;
    }

    public void calculateFitnessScore() {
	fitnessScore = 0;
    }

    public int getFitnessScore() {
	return fitnessScore;
    }

    /**
     * A kromoszóma mutálása, kicsi valószínûséggel.
     */
    public void mutate() {
	for (Gene gene : geneMap.values()) {
	    if (random.nextFloat() <= MUTATION_RATE) {
		gene.mutate();
	    }
	}
    }

    public void setFitnessScore(int score) {
	fitnessScore = score;
    }
}
