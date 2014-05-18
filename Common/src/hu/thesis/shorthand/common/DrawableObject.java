
package hu.thesis.shorthand.common;

/**
 * Egy kirajzolható alakzat. Van alakja, orientációja, iránya.
 */
public interface DrawableObject {
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
        NONE(0), DEGREE_45(45), DEGREE_90(90), DEGREE_135(135), DEGREE_180(180), DEGREE_225(225), DEGREE_270(
                270), DEGREE_315(315);

        public static final int[] intValues = new int[] {
                0, 45, 90, 135, 180, 225, 270, 315
        };

        private final int degrees;

        private Rotation(int degrees) {
            this.degrees = degrees;
        }

        public int getDegrees() {
            return degrees;
        }
    }

    Form getForm();

    Rotation getRotation();

    void setForm(Form form);

    void setRotation(Rotation rotation);
}
