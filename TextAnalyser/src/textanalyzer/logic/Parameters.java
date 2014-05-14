package textanalyzer.logic;

/**
 * A beállítható paramétereket tartalmazó statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class Parameters {

    /**
     * A populáció mérete. Alapértelmezett: 32
     */
    public static int populationSize = 32;
    /**
     * A genetikus algoritmus lépései közötti szüneteltetési idő
     * ezredmásodpercben. Alapértelmezett: 0
     */
    public static long sleepTime = 0;
    /**
     * A rajzolási terület horizontális mérete egységekben.
     */
    public static double drawingAreaSizeX = 4.0;
    /**
     * A rajzolási terület vertikális mérete egységekben.
     */
    public static double drawingAreaSizeY = 4.0;

    private Parameters() {
	// statikus osztály
    }

}
