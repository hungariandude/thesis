
package hu.thesis.shorthand.ime.recognizer;

import hu.thesis.shorthand.common.DrawableObject;

/**
 * Egy kirajzolt nagyobb alakzat szegmense.
 * 
 * @author Istvánfi Zsolt
 */
public class Segment implements DrawableObject {

    private Form mForm;
    private Rotation mRotation;

    public Segment(Form form, Rotation rotation) {
        mForm = form;
        mRotation = rotation;
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
        if (mForm != other.mForm) {
            return false;
        }
        if (mRotation != other.mRotation) {
            return false;
        }
        return true;
    }

    @Override
    public Form getForm() {
        return mForm;
    }

    @Override
    public Rotation getRotation() {
        return mRotation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mForm == null) ? 0 : mForm.hashCode());
        result = prime * result + ((mRotation == null) ? 0 : mRotation.hashCode());
        return result;
    }

    @Override
    public void setForm(Form form) {
        mForm = form;
    }

    @Override
    public void setRotation(Rotation rotation) {
        mRotation = rotation;
    }

    @Override
    public String toString() {
        String str = '(' + mForm.toString() + ';' + mRotation.getDegrees() + ')';
        return str;
    }

}
