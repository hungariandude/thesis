package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.util.EnumUtils;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A kromosz�m�t fel�p�t� g�n.
 * 
 * @author Istv�nfi Zsolt
 */
public class Gene {
    private enum MutationType {
	INVERT, ADD, REMOVE, TRANSSHAPE, ROTATE
    }

    private List<DrawingObject> buildingElements;

    /**
     * L�trehoz egy param�terben megkapott hossz�s�g�, v�letlenszer� g�nt.
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
     * A g�n mut�l�sa.
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
