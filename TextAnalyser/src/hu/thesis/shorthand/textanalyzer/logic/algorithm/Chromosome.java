package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.util.RandomUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A genetikus algoritmusban szereplő kromoszóma.
 * 
 * @author Istvánfi Zsolt
 */
public class Chromosome implements Comparable<Chromosome> {
    private static final float CROSSOVER_RATE = 0.7f;

    private final TreeMap<Character, Gene> geneMap;
    private double fitnessScore;

    /**
     * Deep copy constructor.
     */
    public Chromosome(Chromosome sample) {
	this(sample.geneMap);
    }

    /**
     * Létrehoz egy véletlenszerű kromoszómát a paraméterek alapján.
     */
    public Chromosome(Collection<Character> chars, int maxGeneLength) {
	this.geneMap = new TreeMap<>();
	for (Character ch : chars) {
	    // 0 hosszúságú gént nem szeretnénk
	    Gene gene = new Gene(RandomUtils.random.nextInt(maxGeneLength) + 1);
	    geneMap.put(ch, gene);
	}
    }

    /**
     * A paraméterként megkapott géntérképminta alapján létrehoz egy
     * kromoszómát.
     */
    public Chromosome(Map<Character, Gene> sampleGeneMap) {
	this.geneMap = new TreeMap<>();

	for (Entry<Character, Gene> entry : sampleGeneMap.entrySet()) {
	    this.geneMap.put(entry.getKey(), new Gene(entry.getValue()));
	}
    }

    @Override
    public int compareTo(Chromosome o) {
	return Double.compare(this.fitnessScore, o.fitnessScore);
    }

    /**
     * A kromoszóma keresztezése egy másikkal, aminek az eredménye két gyermek
     * kromoszóma.
     */
    public Chromosome[] crossOverWith(Chromosome other) {
	// kell?
	if (RandomUtils.random.nextFloat() > CROSSOVER_RATE) {
	    return new Chromosome[] { new Chromosome(this),
		    new Chromosome(other) };
	}

	// kromoszómaméretek ellenőrzése
	if (this.geneMap.size() != other.geneMap.size()) {
	    throw new IllegalArgumentException("Chromosome sizes do not match.");
	}

	// elvégezzük a keresztezést
	// Választunk egy pontot, amin kettéosztjuk a génállományt (a ponton
	// lévő elem a második részhez tartozik). A pont nem lehet a legelső
	// elem, mert akkor csak szimpla génállománycsere történne az utódokban.
	int swappingIndex = RandomUtils.random.nextInt(this.geneMap.size() - 1) + 1;
	int index = 1;
	Character charAtIndex = null;
	for (Iterator<Character> it = this.geneMap.navigableKeySet().iterator(); index <= swappingIndex
		&& it.hasNext();) {
	    charAtIndex = it.next();
	}
	SortedMap<Character, Gene> headMapFrom1 = this.geneMap
		.headMap(charAtIndex);
	SortedMap<Character, Gene> tailMapFrom2 = other.geneMap
		.tailMap(charAtIndex);

	Map<Character, Gene> child1Map = new HashMap<>(headMapFrom1);
	child1Map.putAll(tailMapFrom2);
	Map<Character, Gene> child2Map = new HashMap<>(tailMapFrom2);
	child2Map.putAll(headMapFrom1);

	Chromosome child1 = new Chromosome(child1Map);
	Chromosome child2 = new Chromosome(child2Map);

	return new Chromosome[] { child1, child2 };
    }

    /**
     * A kromoszómában szereplő gének (csak olvasható).
     */
    public Collection<Gene> genes() {
	return geneMap.values();
    }

    public double getFitnessScore() {
	return fitnessScore;
    }

    /**
     * A kromoszóma géntérképe.
     */
    public Map<Character, Gene> getGeneMap() {
	return geneMap;
    }

    public void setFitnessScore(double score) {
	fitnessScore = score;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	DecimalFormat df = new DecimalFormat("#.0000");
	df.setRoundingMode(RoundingMode.HALF_UP);
	sb.append(", fitness: ").append(df.format(fitnessScore));
	sb.append(" [");
	if (!geneMap.isEmpty()) {
	    for (Entry<Character, Gene> entry : geneMap.entrySet()) {
		sb.append('\'');
		char ch = entry.getKey();
		String unescaped;
		if (ch == '\n') {
		    unescaped = "\\n";
		} else if (ch == '\t') {
		    unescaped = "\\t";
		} else if (ch == '\r') {
		    unescaped = "\\r";
		} else {
		    unescaped = String.valueOf(ch);
		}

		sb.append(unescaped).append("' ");
		sb.append(entry.getValue()).append(", ");
	    }
	    sb.delete(sb.length() - 2, sb.length());
	    sb.append("]");
	}

	return sb.toString();
    }
}
