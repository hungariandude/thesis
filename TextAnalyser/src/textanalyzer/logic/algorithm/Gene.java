package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A kromosz�m�t fel�p�t� g�n.
 * 
 * @author Istv�nfi Zsolt
 */
public class Gene {
    private List<DrawingObject> buildingElements;

    /**
     * L�trehoz egy param�terben megkapott hossz�s�g�, v�letlenszer� g�nt.
     */
    public Gene(int length) {
	this.buildingElements = new ArrayList<>(length);

	for (int i = 0; i < length; ++i) {
	    buildingElements.add(new DrawingObject());
	}
    }

    public Gene(List<DrawingObject> buildingElements) {
	if (buildingElements.isEmpty()) {
	    throw new IllegalArgumentException("Parameter list is empty.");
	}

	this.buildingElements = buildingElements;
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
     * A g�n hossza (az �p�t�elemek sz�ma).
     */
    public int length() {
	return this.buildingElements.size();
    }

    public void setBuildingElements(List<DrawingObject> buildingElements) {
	this.buildingElements = buildingElements;
    }
}
