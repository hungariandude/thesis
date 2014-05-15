package textanalyzer.logic.algorithm;

import textanalyzer.util.RandomUtils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * A gén szegmense, ami egy kirajzolható alakzat. Van alakja, orientációja,
 * iránya.
 * 
 * @author Istvánfi Zsolt
 */
public class Segment extends Path2D.Double {

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

    private static final long serialVersionUID = -8067923710886726795L;

    /**
     * Hányféleképpen lehet kirajzolni egy alakzatot.
     */
    public static final int NUMBER_OF_OBJECT_DRAWING_WAYS = Form.values().length
	    * Rotation.values().length;

    private Form form;
    private Rotation rotation;

    /**
     * Az objektum alakját, orientációját és irányát véletlenszerűen határozza
     * meg.
     */
    public Segment() {
	this(RandomUtils.randomValue(Form.values()), RandomUtils
		.randomValue(Rotation.values()));
    }

    public Segment(Form form, Rotation rotation) {
	this.form = form;
	this.rotation = rotation;

	recalculatePath();
    }

    /**
     * Copy constructor.
     */
    public Segment(Segment sample) {
	super(sample);
	this.form = sample.form;
	this.rotation = sample.rotation;
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
	Segment other = (Segment) obj;
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
     * Kiszámítja az alakzat útját.
     */
    private void recalculatePath() {
	reset();
	moveTo(0, 0);
	double x = 1.0;
	double y = 0;
	if (form != Form.LINE) {
	    double halfX = x / 2;
	    double halfY = y / 2;
	    double controlX, controlY;
	    if (form == Form.CREST_CURVE) {
		controlX = halfX - y * 0.6;
		controlY = halfY + x * 0.6;
	    } else {
		controlX = halfX + y * 0.6;
		controlY = halfY - x * 0.6;
	    }
	    quadTo(controlX, controlY, x, y);
	} else {
	    lineTo(x, y);
	}

	transform(AffineTransform.getRotateInstance(Math
		.toRadians(rotation.degrees)));
    }

    public void setForm(Form form) {
	this.form = form;

	recalculatePath();
    }

    public void setRotation(Rotation rotation) {
	this.rotation = rotation;

	recalculatePath();
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
