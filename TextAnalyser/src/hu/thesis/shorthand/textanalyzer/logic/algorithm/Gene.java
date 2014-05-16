package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import java.awt.geom.Rectangle2D;
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
	ArrayList<Segment> newSegments = new ArrayList<>(
		firstPart.segments.size() + secondPart.segments.size());
	newSegments.addAll(firstPart.segments);
	newSegments.addAll(secondPart.segments);

	return new Gene(newSegments);
    }

    private ArrayList<Segment> segments;
    private Rectangle2D bounds;

    /**
     * Üres gén.
     */
    public Gene() {
	this.segments = new ArrayList<>();
	this.bounds = new Rectangle2D.Double();
    }

    /**
     * A megadott építőelemek alapján létrehoz egy új gént.
     */
    public Gene(ArrayList<Segment> segments) {
	this.segments = new ArrayList<>(segments.size());

	for (Segment segment : segments) {
	    this.segments.add(new Segment(segment));
	}

	recalculateBounds();
    }

    /**
     * Deep copy constructor.
     */
    public Gene(Gene sample) {
	this.segments = new ArrayList<>(sample.segments.size());

	for (Segment segment : sample.segments) {
	    this.segments.add(new Segment(segment));
	}

	this.bounds = new Rectangle2D.Double(sample.bounds.getX(),
		sample.bounds.getY(), sample.bounds.getWidth(),
		sample.bounds.getHeight());
    }

    /**
     * Létrehoz egy paraméterben megkapott hosszúságú, véletlenszerű gént.
     */
    public Gene(int length) {
	this.segments = new ArrayList<>(length);

	for (int i = 0; i < length; ++i) {
	    segments.add(new Segment());
	}

	recalculateBounds();
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
	if (segments == null) {
	    if (other.segments != null) {
		return false;
	    }
	} else if (!segments.equals(other.segments)) {
	    return false;
	}
	return true;
    }

    public Rectangle2D getBounds() {
	return bounds;
    }

    public List<Segment> getSegments() {
	return segments;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((segments == null) ? 0 : segments.hashCode());
	return result;
    }

    /**
     * A gén hossza (az építőelemek száma).
     */
    public int length() {
	return this.segments.size();
    }

    /**
     * Újraszámolja a gén rajzolási méretét.
     */
    public void recalculateBounds() {
	double minX, minY, maxX, maxY;
	minX = minY = maxX = maxY = 0.0;

	for (Segment segment : segments) {
	    Rectangle2D segmentBounds = segment.getBounds2D();
	    if (segmentBounds.getMinX() < minX) {
		minX = segmentBounds.getMinX();
	    }
	    if (segmentBounds.getMinY() < minY) {
		minY = segmentBounds.getMinY();
	    }
	    if (segmentBounds.getMaxX() < maxX) {
		maxX = segmentBounds.getMaxX();
	    }
	    if (segmentBounds.getMaxY() < maxY) {
		maxY = segmentBounds.getMaxY();
	    }
	}

	this.bounds = new Rectangle2D.Double(minX, minY, maxX - minX, maxY
		- minY);
    }

    public void setSegments(ArrayList<Segment> segments) {
	this.segments = segments;

	recalculateBounds();
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder('[');
	if (!segments.isEmpty()) {
	    for (Segment segment : segments) {
		sb.append(segment.toString()).append(", ");
	    }
	    sb.delete(sb.length() - 2, sb.length());
	}
	sb.append(']');

	return sb.toString();
    }
}
