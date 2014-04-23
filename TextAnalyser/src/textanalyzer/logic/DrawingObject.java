package textanalyzer.logic;

import textanalyzer.util.EnumUtils;

/**
 * Egy kirajzolhat� alakzat. Van alakja, orient�ci�ja, ir�nya.
 * 
 * @author Istv�nfi Zsolt
 */
public class DrawingObject {

    public enum Direction {
	FORWARD, REVERSE;
    }

    public enum Orientation {
	VERTICAL, HORIZONTAL, OBLIQUE_RIGHT, OBLIQUE_LEFT
    }

    public enum Shape {
	LINE, CURVE_RIGHT, CURVE_LEFT
    }

    /**
     * H�nyf�lek�ppen lehet kirajzolni egy alakzatot.
     */
    public static final int NUMBER_OF_OBJECT_DRAWING_WAYS = Shape.values().length
	    * Orientation.values().length * Direction.values().length;

    private Shape shape;
    private Orientation orientation;
    private Direction direction;

    /**
     * Alap�rtelmezett hossz (g�rb�k eset�n a k�t v�gpont k�z�tt, l�gvonalban)
     */
    public static final int DEFAULT_LENGTH = 1;

    /**
     * Az objektum alakj�t, orient�ci�j�t �s ir�ny�t v�letlenszer�en hat�rozza
     * meg.
     */
    public DrawingObject() {
	this(EnumUtils.randomValue(Shape.class), EnumUtils
		.randomValue(Orientation.class), EnumUtils
		.randomValue(Direction.class));
    }

    public DrawingObject(Shape shape, Orientation orientation,
	    Direction direction) {
	this.shape = shape;
	this.orientation = orientation;
	this.direction = direction;
    }

    // private Point2D calculateEndingPoint(int length) {
    // double dx, dy;
    //
    // if (orientation == Orientation.OBLIQUE_RIGHT
    // || orientation == Orientation.OBLIQUE_LEFT) {
    // dx = dy = Math.sin(Math.toRadians(45));
    // if (orientation == Orientation.OBLIQUE_LEFT) {
    // // ebben az esetben a dx az negat�v
    // dx *= -1;
    // }
    // } else {
    // if (orientation == Orientation.HORIZONTAL) {
    // dx = 0;
    // dy = 1;
    // } else {
    // dx = 1;
    // dy = 0;
    // }
    // }
    // if (direction == Direction.REVERSE) {
    // dx *= -1;
    // dy *= -1;
    // }
    //
    // return new Point2D.Double(startingPoint.getX() + dx,
    // startingPoint.getY() + dy);
    // }

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
	DrawingObject other = (DrawingObject) obj;
	if (direction != other.direction) {
	    return false;
	}
	if (orientation != other.orientation) {
	    return false;
	}
	if (shape != other.shape) {
	    return false;
	}
	return true;
    }

    public Direction getDirection() {
	return direction;
    }

    public Orientation getOrientation() {
	return orientation;
    }

    public Shape getShape() {
	return shape;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((direction == null) ? 0 : direction.hashCode());
	result = prime * result
		+ ((orientation == null) ? 0 : orientation.hashCode());
	result = prime * result + ((shape == null) ? 0 : shape.hashCode());
	return result;
    }

    public void invertDirection() {
	this.direction = this.direction == Direction.FORWARD ? Direction.REVERSE
		: Direction.FORWARD;
    }

    public void setDirection(Direction direction) {
	this.direction = direction;
    }

    public void setOrientation(Orientation orientation) {
	this.orientation = orientation;
    }

    public void setShape(Shape shape) {
	this.shape = shape;
    }

}
