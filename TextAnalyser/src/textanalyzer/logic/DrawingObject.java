package textanalyzer.logic;

import java.awt.geom.Point2D;

/**
 * Egy kirajzolható alakzat. Van formája, mérete, pozíciója, iránya.
 * 
 * @author Istvánfi Zsolt
 */
public class DrawingObject {

    public enum Direction {
	FORWARD, REVERSE
    }

    public enum Orientation {
	VERTICAL, HORIZONTAL, OBLIQUE_RIGHT, OBLIQUE_LEFT
    }

    public enum Shape {
	LINE, CURVE_RIGHT, CURVE_LEFT
    }

    private Point2D startingPoint, endingPoint;

    private Shape shape;
    private Orientation orientation;
    private Direction direction;

    /**
     * Alapértelmezett hossz (görbék esetén a két végpont között)
     */
    private final static int DEFAULT_LENGTH = 1;

    /**
     * A végpontot a többi adatból határozza meg.
     */
    public DrawingObject(Point2D startingPoint, Shape shape,
	    Orientation orientation, Direction direction) {
	this.startingPoint = startingPoint;
	this.shape = shape;
	this.orientation = orientation;
	this.direction = direction;

	this.endingPoint = calculateEndingPoint(DEFAULT_LENGTH);
    }

    private Point2D calculateEndingPoint(int length) {
	double dx, dy;

	if (orientation == Orientation.OBLIQUE_RIGHT
		|| orientation == Orientation.OBLIQUE_LEFT) {
	    dx = dy = Math.sin(Math.toRadians(45));
	    if (orientation == Orientation.OBLIQUE_LEFT) {
		// ebben az esetben a dx az negatív
		dx *= -1;
	    }
	} else {
	    if (orientation == Orientation.HORIZONTAL) {
		dx = 0;
		dy = 1;
	    } else {
		dx = 1;
		dy = 0;
	    }
	}
	if (direction == Direction.REVERSE) {
	    dx *= -1;
	    dy *= -1;
	}

	return new Point2D.Double(startingPoint.getX() + dx,
		startingPoint.getY() + dy);
    }

    public Direction getDirection() {
	return direction;
    }

    public Point2D getEndingPoint() {
	return endingPoint;
    }

    public Orientation getOrientation() {
	return orientation;
    }

    public Shape getShape() {
	return shape;
    }

    public Point2D getStartingPoint() {
	return startingPoint;
    }

    public void setDirection(Direction direction) {
	this.direction = direction;
    }

    public void setEndingPoint(Point2D endingPoint) {
	this.endingPoint = endingPoint;
    }

    public void setOrientation(Orientation orientation) {
	this.orientation = orientation;
    }

    public void setShape(Shape shape) {
	this.shape = shape;
    }

    public void setStartingPoint(Point2D startingPoint) {
	this.startingPoint = startingPoint;
    }
}
