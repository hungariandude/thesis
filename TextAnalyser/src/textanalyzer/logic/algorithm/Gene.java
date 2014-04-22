package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.util.EnumUtils;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A kromoszómát felépítõ gén.
 * 
 * @author Istvánfi Zsolt
 */
public class Gene {
    private enum MutationType {
	INVERT, ADD, REMOVE, TRANSSHAPE, ROTATE
    }

    private List<DrawingObject> buildingElements;

    /**
     * Létrehoz egy paraméterben megkapott hosszúságú, véletlenszerû gént.
     */
    public Gene(int length) {
	this.buildingElements = new ArrayList<>(length);

	for (int i = 0; i < length; ++i) {
	    Point2D startingPoint = i == 0 ? new Point()
		    : (Point2D) buildingElements.get(i - 1).getEndingPoint()
			    .clone();

	    buildingElements.add(new DrawingObject(startingPoint));
	}
    }

    public Gene(List<DrawingObject> buildingElements) {
	if (buildingElements.isEmpty()) {
	    throw new IllegalArgumentException("Parameter list is empty.");
	}

	this.buildingElements = buildingElements;
    }

    /**
     * A gén mutálása.
     */
    public void mutate() {
	MutationType mutationType = EnumUtils.randomValue(MutationType.class);
	switch (mutationType) {
	case ADD:
	    break;
	case REMOVE:
	    break;
	case INVERT:
	    break;
	case ROTATE:
	    break;
	case TRANSSHAPE:
	    break;
	}
    }
}
