package textanalyzer.logic.algorithm;

import java.util.ArrayList;

/**
 * A genetikus algoritmusban szereplő populáció.
 * 
 * @author Istvánfi Zsolt
 */
public class Population extends ArrayList<Chromosome> {

    private static final long serialVersionUID = 5968120913954570795L;

    /**
     * Hányadik generáció.
     */
    private final int generationNumber;

    public Population(int size, int generationNumber) {
	super(size);
	this.generationNumber = generationNumber;
    }

    public int getGenerationNumber() {
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
