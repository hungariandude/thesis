package textanalyzer.logic.algorithm;

import java.util.ArrayList;

/**
 * A genetikus algoritmusban szerepl� popul�ci�.
 * 
 * @author Istv�nfi Zsolt
 */
public class Population extends ArrayList<Chromosome> {

    private static final long serialVersionUID = 2547878875132494045L;

    /**
     * H�nyadik gener�ci�.
     */
    private final int generationNumber;

    public Population(int size, int generationNumber) {
	super(size);
	this.generationNumber = generationNumber;
    }

    public int getGenerationNumber() {
	return generationNumber;
    }

}
