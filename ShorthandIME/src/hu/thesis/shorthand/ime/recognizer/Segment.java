
package hu.thesis.shorthand.ime.recognizer;

/**
 * Egy rajzolt alakzat jól meghatozható darabja. Van alakja, iránya,
 * orientációja.
 * 
 * @author Istvánfi Zsolt
 */
public class Segment {
    /** Irány. */
    public enum Direction {
        /** Előre. */
        FORWARD,
        /** Hátra. */
        REVERSE;
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

    /** Alak. */
    public enum Shape {
        /** Egyenes. */
        LINE,
        /** Süllyedt görbe. */
        SAG_CURVE,
        /** Emelkedett görbe. */
        CREST_CURVE;
    }

    private final Shape shape;
    private final Orientation orientation;
    private final Direction direction;

    public Segment(Shape shape, Orientation orientation, Direction direction) {
        super();
        this.shape = shape;
        this.orientation = orientation;
        this.direction = direction;
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
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
        result = prime * result + ((shape == null) ? 0 : shape.hashCode());
        return result;
    }

}
