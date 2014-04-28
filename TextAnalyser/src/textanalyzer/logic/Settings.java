package textanalyzer.logic;

/**
 * A beállítható paramétereket tartalmazó statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class Settings {

    /**
     * A populáció mérete. Alapértelmezett: 32
     */
    public static int populationSize = 32;
    /**
     * A genetikus algoritmus lépései közötti szüneteltetési idõ
     * ezredmásodpercben. Alapértelmezett: 0
     */
    public static long sleepTime = 0;

    private Settings() {
	// statikus osztály
    }

}
