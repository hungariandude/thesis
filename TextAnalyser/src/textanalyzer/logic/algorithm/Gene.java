package textanalyzer.logic.algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A kromoszómát felépítő gén.
 * 
 * @author Istvánfi Zsolt
 */
public class Gene {

    /**
     * Összefűz két gént és az eredményt új génként adja vissza.
     */
    public static Gene concatenate(Gene firstPart, Gene secondPart) {
	ArrayList<Shape> newBuildingElements = new ArrayList<>(
		firstPart.buildingElements.size()
			+ secondPart.buildingElements.size());
	newBuildingElements.addAll(firstPart.buildingElements);
	newBuildingElements.addAll(secondPart.buildingElements);

	return new Gene(newBuildingElements);
    }

    private ArrayList<Shape> buildingElements;

    private Point2D endingPoint;

    /**
     * Üres gén.
     */
    public Gene() {
	this.buildingElements = new ArrayList<>();

	this.endingPoint = new Point2D.Double();
    }

    /**
     * A megadott építőelemek alapján létrehoz egy új gént.
     */
    public Gene(ArrayList<Shape> buildingElements) {
	this.buildingElements = new ArrayList<>(buildingElements.size());

	for (Shape object : buildingElements) {
	    this.buildingElements.add(new Shape(object));
	}

	recalculateDrawingSize();
    }

    /**
     * Deep copy constructor.
     */
    public Gene(Gene sample) {
	this.buildingElements = new ArrayList<>(sample.buildingElements.size());

	for (Shape object : sample.buildingElements) {
	    this.buildingElements.add(new Shape(object));
	}

	this.endingPoint = new Point2D.Double(sample.endingPoint.getX(),
		sample.endingPoint.getY());
    }

    /**
     * Létrehoz egy paraméterben megkapott hosszúságú, véletlenszerű gént.
     */
    public Gene(int length) {
	this.buildingElements = new ArrayList<>(length);

	for (int i = 0; i < length; ++i) {
	    buildingElements.add(new Shape());
	}

	recalculateDrawingSize();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Gene other = (Gene) obj;
	if (buildingElements == null) {
	    if (other.buildingElements != null) {
		return false;
	    }
	} else if (!buildingElements.equals(other.buildingElements)) {
	    return false;
	}
	return true;
    }

    public List<Shape> getBuildingElements() {
	return buildingElements;
    }

    public Point2D getEndingPoint() {
	return endingPoint;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime
		* result
		+ ((buildingElements == null) ? 0 : buildingElements.hashCode());
	return result;
    }

    /**
     * A gén hossza (az építőelemek száma).
     */
    public int length() {
	return this.buildingElements.size();
    }

    /**
     * Újraszámolja a gén rajzolási méretét.
     */
    public void recalculateDrawingSize() {
	double dx = 0.0, dy = 0.0;

	for (Shape shape : buildingElements) {
	    Point2D size = shape.getVector();
	    dx += size.getX();
	    dy += size.getY();
	}

	this.endingPoint = new Point2D.Double(dx, dy);
    }

    public void setBuildingElements(ArrayList<Shape> buildingElements) {
	this.buildingElements = buildingElements;

	recalculateDrawingSize();
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder('[');
	if (!buildingElements.isEmpty()) {
	    for (Shape shape : buildingElements) {
		sb.append(shape.toString()).append(", ");
	    }
	    sb.delete(sb.length() - 2, sb.length());
	}
	sb.append(']');

	return sb.toString();
    }
}
