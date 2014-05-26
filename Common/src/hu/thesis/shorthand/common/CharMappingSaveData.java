
package hu.thesis.shorthand.common;

import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;

import java.io.Serializable;
import java.util.Arrays;

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
        CharMappingSaveData other = (CharMappingSaveData) obj;
        if (ch != other.ch) {
            return false;
        }
        if (!Arrays.equals(forms, other.forms)) {
            return false;
        }
        if (!Arrays.equals(rotations, other.rotations)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ch;
        result = prime * result + Arrays.hashCode(forms);
        result = prime * result + Arrays.hashCode(rotations);
        return result;
    }

}
