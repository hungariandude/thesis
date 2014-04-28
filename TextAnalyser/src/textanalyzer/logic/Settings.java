package textanalyzer.logic;

/**
 * A be�ll�that� param�tereket tartalmaz� statikus oszt�ly.
 * 
 * @author Istv�nfi Zsolt
 */
public final class Settings {

    /**
     * A popul�ci� m�rete. Alap�rtelmezett: 32
     */
    public static int populationSize = 32;
    /**
     * A genetikus algoritmus l�p�sei k�z�tti sz�neteltet�si id�
     * ezredm�sodpercben. Alap�rtelmezett: 0
     */
    public static long sleepTime = 0;

    private Settings() {
	// statikus oszt�ly
    }

}
