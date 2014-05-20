package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import java.util.Collections;
import java.util.TreeSet;

/**
 * A genetikus algoritmusban szereplő populáció. A kromoszómák a fitnesz értékük
 * szerinti csökkenő sorrendben szerepelnek benne.
 * 
 * @author Istvánfi Zsolt
 */
public class Population extends TreeSet<Chromosome> implements
	Comparable<Population> {

    private static final long serialVersionUID = 5968120913954570795L;

    /**
     * Hányadik generáció.
     */
    private final long generationNumber;

    public Population(long generationNumber) {
	super(Collections.<Chromosome> reverseOrder());
	this.generationNumber = generationNumber;
    }

    /**
     * Shallow copy constructor.
     */
    public Population(Population sample) {
	super(sample);
	this.generationNumber = sample.generationNumber;
    }

    @Override
    public int compareTo(Population o) {
	return Long.compare(this.generationNumber, o.generationNumber);
    }

    public long getGenerationNumber() {
	return generationNumber;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(generationNumber).append(". generation; Size: ")
		.append(size());
	if (!isEmpty()) {
	    sb.append("\nMembers:\n");
	    for (Chromosome chrom : this) {
		sb.append('\t').append(chrom.toString()).append("\n");
	    }
	}

	return sb.toString();
    }
}
