package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

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
	ArrayList<DrawingObject> newBuildingElements = new ArrayList<>(
		firstPart.buildingElements.size()
			+ secondPart.buildingElements.size());
	newBuildingElements.addAll(firstPart.buildingElements);
	newBuildingElements.addAll(secondPart.buildingElements);

	return new Gene(newBuildingElements);
    }

    private ArrayList<DrawingObject> buildingElements;

    private Point2D drawingSize;

    /**
     * Üres gén.
     */
    public Gene() {
	this.buildingElements = new ArrayList<>();

	this.drawingSize = new Point2D.Double();
    }

    /**
     * A megadott építőelemek alapján létrehoz egy új gént.
     */
    public Gene(ArrayList<DrawingObject> buildingElements) {
	this.buildingElements = new ArrayList<>(buildingElements.size());

	for (DrawingObject object : buildingElements) {
	    this.buildingElements.add(new DrawingObject(object));
	}

	recalculateDrawingSize();
    }

    /**
     * Deep copy constructor.
     */
    public Gene(Gene sample) {
	this.buildingElements = new ArrayList<>(sample.buildingElements.size());

	for (DrawingObject object : sample.buildingElements) {
	    this.buildingElements.add(new DrawingObject(object));
	}

	this.drawingSize = new Point2D.Double(sample.drawingSize.getX(),
		sample.drawingSize.getY());
    }

    /**
     * Létrehoz egy paraméterben megkapott hosszúságú, véletlenszerű gént.
     */
    public Gene(int length) {
	this.buildingElements = new ArrayList<>(length);

	for (int i = 0; i < length; ++i) {
	    buildingElements.add(new DrawingObject());
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

    public List<DrawingObject> getBuildingElements() {
	return buildingElements;
    }

    public Point2D getDrawingSize() {
	return drawingSize;
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
     * Újraszámolja a gép rajzolási méretét.
     */
    public void recalculateDrawingSize() {
	double dx = 0.0, dy = 0.0;

	for (DrawingObject object : buildingElements) {
	    Point2D size = object.getDrawingSize();
	    dx += size.getX();
	    dy += size.getY();
	}

	this.drawingSize = new Point2D.Double(dx, dy);
    }

    public void setBuildingElements(ArrayList<DrawingObject> buildingElements) {
	this.buildingElements = buildingElements;

	recalculateDrawingSize();
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder("[");
	if (!buildingElements.isEmpty()) {
	    for (DrawingObject object : buildingElements) {
		sb.append(object.toString()).append(", ");
	    }
	    sb.delete(sb.length() - 2, sb.length());
	}
	sb.append("]");

	return sb.toString();
    }
}
