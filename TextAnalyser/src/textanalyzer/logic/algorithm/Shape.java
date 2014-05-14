package textanalyzer.logic.algorithm;

import textanalyzer.util.RandomUtils;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Egy kirajzolható alakzat. Van alakja, orientációja, iránya.
 * 
 * @author Istvánfi Zsolt
 */
public class Shape {

    /** Irány. */
    public enum Direction {
	/** Előre. */
	FORWARD,
	/** Hátra. */
	REVERSE;
    }

    /** Forma. */
    public enum Form {
	/** Egyenes. */
	LINE,
	/** Süllyedt görbe. */
	SAG_CURVE,
	/** Emelkedett görbe. */
	CREST_CURVE;
    }

    /** Orientáció. */
    public enum Orientation {
	/** Függőleges. */
	VERTICAL,
	/** Vízszintes. */
	HORIZONTAL,
	/** Jobbra dőlt. */
	OBLIQUE_RIGHT,
	/** Balra dőlt. */
	OBLIQUE_LEFT;
    }

    /**
     * Hányféleképpen lehet kirajzolni egy alakzatot.
     */
    public static final int NUMBER_OF_OBJECT_DRAWING_WAYS = Form.values().length
	    * Orientation.values().length * Direction.values().length;

    private Form form;
    private Orientation orientation;
    private Direction direction;

    /**
     * Az objektum alakját, orientációját és irányát véletlenszerűen határozza
     * meg.
     */
    public Shape() {
	this(RandomUtils.randomValue(Form.values()), RandomUtils
		.randomValue(Orientation.values()), RandomUtils
		.randomValue(Direction.values()));
    }

    public Shape(Form form, Orientation orientation, Direction direction) {
	this.form = form;
	this.orientation = orientation;
	this.direction = direction;
    }

    /**
     * Copy constructor.
     */
    public Shape(Shape sample) {
	this.form = sample.form;
	this.orientation = sample.orientation;
	this.direction = sample.direction;
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
	Shape other = (Shape) obj;
	if (direction != other.direction) {
	    return false;
	}
	if (orientation != other.orientation) {
	    return false;
	}
	if (form != other.form) {
	    return false;
	}
	return true;
    }

    public Direction getDirection() {
	return direction;
    }

    public Form getForm() {
	return form;
    }

    public Orientation getOrientation() {
	return orientation;
    }

    /**
     * Az alakzat mérete, légvonalban.
     */
    public Point2D getVector() {
	double dx, dy;

	if (orientation == Orientation.OBLIQUE_RIGHT
		|| orientation == Orientation.OBLIQUE_LEFT) {
	    dx = dy = Math.sin(Math.toRadians(45));
	    if (orientation == Orientation.OBLIQUE_LEFT) {
		// ebben az esetben a dx negatív
		dx *= -1;
	    }
	} else {
	    if (orientation == Orientation.HORIZONTAL) {
		dx = 0.0;
		dy = 1.0;
	    } else {
		dx = 1.0;
		dy = 0.0;
	    }
	}
	if (direction == Direction.REVERSE) {
	    dx *= -1;
	    dy *= -1;
	}

	return new Point2D.Double(dx, dy);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((direction == null) ? 0 : direction.hashCode());
	result = prime * result
		+ ((orientation == null) ? 0 : orientation.hashCode());
	result = prime * result + ((form == null) ? 0 : form.hashCode());
	return result;
    }

    public void invertDirection() {
	this.direction = this.direction == Direction.FORWARD ? Direction.REVERSE
		: Direction.FORWARD;
    }

    public void setDirection(Direction direction) {
	this.direction = direction;
    }

    public void setForm(Form form) {
	this.form = form;
    }

    public void setOrientation(Orientation orientation) {
	this.orientation = orientation;
    }

    /**
     * Visszadaja az alakzatot az origóból kiinduló útként.
     */
    public Path2D toPath2D() {
	Path2D path = new Path2D.Double();
	path.moveTo(0, 0);
	Point2D endingPoint = getVector();
	double x = endingPoint.getX();
	double y = endingPoint.getY();
	if (form != Form.LINE) {
	    double halfX = endingPoint.getX() / 2;
	    double halfY = endingPoint.getY() / 2;
	    double controlX, controlY;
	    if (direction == Direction.FORWARD) {
		if (form == Form.CREST_CURVE) {
		    controlX = halfX - y * 0.6;
		    controlY = halfY + x * 0.6;
		} else {
		    controlX = halfX + y * 0.6;
		    controlY = halfY - x * 0.6;
		}
	    } else {
		if (form == Form.CREST_CURVE) {
		    controlX = halfX + y * 0.6;
		    controlY = halfY - x * 0.6;
		} else {
		    controlX = halfX - y * 0.6;
		    controlY = halfY + x * 0.6;
		}
	    }
	    path.quadTo(controlX, controlY, x, y);
	} else {
	    path.lineTo(x, y);
	}
	return path;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(direction);
	sb.append(' ');
	sb.append(orientation);
	sb.append(' ');
	sb.append(form);

	return sb.toString();
    }
}
