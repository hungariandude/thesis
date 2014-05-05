package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A kromoszómát felépítő gén.
 * 
 * @author Istvánfi Zsolt
 */
public class Gene {

    private ArrayList<DrawingObject> buildingElements;

    /**
     * Üres gén.
     */
    public Gene() {
	this.buildingElements = new ArrayList<>();
    }

    /**
     * Copy constructor.
     */
    public Gene(Gene sample) {
	this.buildingElements = new ArrayList<>(sample.buildingElements.size());

	for (DrawingObject object : sample.buildingElements) {
	    this.buildingElements.add(new DrawingObject(object));
	}
    }

    /**
     * Létrehoz egy paraméterben megkapott hosszúságú, véletlenszerű gént.
     */
    public Gene(int length) {
	this.buildingElements = new ArrayList<>(length);

	for (int i = 0; i < length; ++i) {
	    buildingElements.add(new DrawingObject());
	}
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

    public void setBuildingElements(ArrayList<DrawingObject> buildingElements) {
	this.buildingElements = buildingElements;
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
