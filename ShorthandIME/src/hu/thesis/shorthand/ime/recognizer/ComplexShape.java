
package hu.thesis.shorthand.ime.recognizer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Egy összetett alakzat, ami szegmensekből áll.
 * 
 * @author Istvánfi Zsolt
 * @see Segment
 */
public class ComplexShape extends ArrayList<Segment> {

    private static final long serialVersionUID = 9184171225788664568L;

    public ComplexShape() {
        super();
    }

    public ComplexShape(Collection<? extends Segment> collection) {
        super(collection);
    }

    public ComplexShape(int capacity) {
        super(capacity);
    }

}
