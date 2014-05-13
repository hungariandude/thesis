package textanalyzer.logic.algorithm;

import java.util.ArrayList;

/**
 * A genetikus algoritmusban szereplő populáció.
 * 
 * @author Istvánfi Zsolt
 */
public class Population extends ArrayList<Chromosome> implements
	Comparable<Population> {

    private static final long serialVersionUID = 5968120913954570795L;

    /**
     * Hányadik generáció.
     */
    private final long generationNumber;

    public Population(int size, long generationNumber) {
	super(size);
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
