package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.Arrays;
import java.util.List;

/**
 * A genetikus algoritmusban szereplõ kromoszóma.
 * 
 * @author Istvánfi Zsolt
 */
public class Chromosome {

    private List<DrawingObject> buildingElements;

    public Chromosome(DrawingObject[] buildingElements) {
	if (buildingElements.length == 0) {
	    throw new IllegalArgumentException("Parameter array is empty.");
	}

	this.buildingElements = Arrays.asList(buildingElements);
    }

    public Chromosome(List<DrawingObject> buildingElements) {
	if (buildingElements.isEmpty()) {
	    throw new IllegalArgumentException("Parameter list is empty.");
	}

	this.buildingElements = buildingElements;
    }
}
