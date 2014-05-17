
package hu.thesis.shorthand.common;

import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;

import java.io.Serializable;

/**
 * Egy karakterekhez párosított alakzatok szerializálásához használható
 * objektum.
 * 
 * @author Istvánfi Zsolt
 */
public class CharMappingSaveData implements Serializable {

    private static final long serialVersionUID = 1985471333094078446L;

    public char ch;
    public Form[] forms;
    public Rotation[] rotations;

    public CharMappingSaveData() {
        // szerializáláshoz
    }

    public CharMappingSaveData(char ch, Form[] forms, Rotation[] rotations) {
        this.ch = ch;
        this.forms = forms;
        this.rotations = rotations;
    }

}
