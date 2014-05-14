package textanalyzer.logic.algorithm;

import textanalyzer.util.RandomUtils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Egy kirajzolható alakzat. Van alakja, orientációja, iránya.
 * 
 * @author Istvánfi Zsolt
 */
public class Shape {

    /** Forma. */
    public enum Form {
	/** Egyenes. */
	LINE,
	/** Süllyedt görbe. */
	SAG_CURVE,
	/** Emelkedett görbe. */
	CREST_CURVE;
    }

    /** Elforgatás. */
    public enum Rotation {
	NONE(0), DEGREE_45(45), DEGREE_90(90), DEGREE_135(135), DEGREE_180(180), DEGREE_225(
		225), DEGREE_270(270), DEGREE_315(315);

	private final int degrees;

	private Rotation(int degrees) {
	    this.degrees = degrees;
	}

	public int getDegrees() {
	    return degrees;
	}
    }

    /**
     * Hányféleképpen lehet kirajzolni egy alakzatot.
     */
    public static final int NUMBER_OF_OBJECT_DRAWING_WAYS = Form.values().length
	    * Rotation.values().length;

    private Form form;
    private Rotation rotation;

    private Point2D vector;

    /**
     * Az objektum alakját, orientációját és irányát véletlenszerűen határozza
     * meg.
     */
    public Shape() {
	this(RandomUtils.randomValue(Form.values()), RandomUtils
		.randomValue(Rotation.values()));
    }

    public Shape(Form form, Rotation rotation) {
	this.form = form;
	this.rotation = rotation;

	recalculateVector();
    }

    /**
     * Copy constructor.
     */
    public Shape(Shape sample) {
	this.form = sample.form;
	this.rotation = sample.rotation;
	this.vector = sample.vector;
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
	if (rotation != other.rotation) {
	    return false;
	}
	if (form != other.form) {
	    return false;
	}
	return true;
    }

    public Form getForm() {
	return form;
    }

    public Rotation getRotation() {
	return rotation;
    }

    /**
     * Az alakzat nyomvektora, légvonalban.
     */
    public Point2D getVector() {
	return vector;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((rotation == null) ? 0 : rotation.hashCode());
	result = prime * result + ((form == null) ? 0 : form.hashCode());
	return result;
    }

    /**
     * Az alakzat nyomvektorának újraszámolása.
     */
    public void recalculateVector() {
	vector = new Point2D.Double(1.0, 0);
	AffineTransform at;
	at = AffineTransform
		.getRotateInstance(Math.toRadians(rotation.degrees));
	at.transform(vector, vector);
    }

    public void setForm(Form form) {
	this.form = form;
    }

    public void setRotation(Rotation rotation) {
	this.rotation = rotation;

	recalculateVector();
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
	    if (form == Form.CREST_CURVE) {
		controlX = halfX - y * 0.6;
		controlY = halfY + x * 0.6;
	    } else {
		controlX = halfX + y * 0.6;
		controlY = halfY - x * 0.6;
	    }
	    path.quadTo(controlX, controlY, x, y);
	} else {
	    path.lineTo(x, y);
	}
	return path;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder("(Form: ");
	sb.append(form);
	sb.append("; Rotation: ");
	sb.append(rotation.degrees);
	// sb.append("; Vector: ");
	// sb.append(getVector());
	sb.append(')');

	return sb.toString();
    }
}
