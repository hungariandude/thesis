package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.logic.DrawingObject.Direction;
import textanalyzer.logic.DrawingObject.Orientation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * A genetikus algoritmusban szerepl� fitnesz f�ggv�nyt biztos�t� oszt�ly.
 * 
 * @author Istv�nfi Zsolt
 */
public class FitnessTester {

    private String sourceText;
    private Map<Character, Integer> charCountMap = new HashMap<>();
    private Map<Character, Integer> recommendedLengthMap = new HashMap<>();

    private static final Point2D DEFAULT_DRAWING_AREA_SIZE = new Point2D.Double(
	    4.0, 3.0);

    private static final Logger LOGGER = Logger.getLogger(FitnessTester.class
	    .getName());

    public FitnessTester(String sourceText) {
	this.sourceText = sourceText;
	countCharOccurrences();
    }

    /**
     * A megtal�lt karakterek halmaza.
     */
    public Set<Character> characterSet() {
	return charCountMap.keySet();
    }

    /**
     * Megsz�molja az egyes karakterek el�fordul�s�t a sz�vegben.
     */
    private void countCharOccurrences() {
	// 1. l�p�s: megsz�moljuk a karaktereket
	for (int beginIndex = 0; beginIndex < sourceText.length(); ++beginIndex) {
	    char currentChar = sourceText.charAt(beginIndex);
	    Integer value = charCountMap.get(currentChar);
	    if (value == null) {
		// m�g nem sz�moltuk meg az el�fordul�sait, teh�t most ezt
		// elv�gezz�k
		int count = 1;
		for (int i = beginIndex + 1; i < sourceText.length(); ++i) {
		    if (sourceText.charAt(i) == currentChar) {
			++count;
		    }
		}
		charCountMap.put(currentChar, count);
	    }
	}
	LOGGER.info("Count of found characters: " + charCountMap.size());

	// 2. l�p�s: meghat�rozzuk az aj�nlott g�nhosszakat az egyes
	// karakterekhez
	TreeMap<Integer, ArrayList<Character>> orderedMap = new TreeMap<>();
	for (Entry<Character, Integer> entry : charCountMap.entrySet()) {
	    Character ch = entry.getKey();
	    Integer count = entry.getValue();
	    ArrayList<Character> value = orderedMap.get(count);
	    if (value == null) {
		orderedMap.put(count, value = new ArrayList<>());
	    }
	    value.add(ch);
	}

	int occupiedSlotsSoFar = 0;
	int differentObjects = DrawingObject.NUMBER_OF_OBJECT_DRAWING_WAYS;
	int actualGeneLength = 1;
	for (ArrayList<Character> list : orderedMap.values()) {
	    if (differentObjects <= occupiedSlotsSoFar) {
		differentObjects += (int) Math.pow(
			DrawingObject.NUMBER_OF_OBJECT_DRAWING_WAYS,
			++actualGeneLength);
	    }
	    occupiedSlotsSoFar += list.size();

	    for (Character ch : list) {
		recommendedLengthMap.put(ch, actualGeneLength);
	    }
	}
    }

    private Point2D measureDrawingObjectSize(DrawingObject object) {
	double dx, dy;

	if (object.getOrientation() == Orientation.OBLIQUE_RIGHT
		|| object.getOrientation() == Orientation.OBLIQUE_LEFT) {
	    dx = dy = Math.sin(Math.toRadians(45));
	    if (object.getOrientation() == Orientation.OBLIQUE_LEFT) {
		// ebben az esetben a dx negat�v
		dx *= -1;
	    }
	} else {
	    if (object.getOrientation() == Orientation.HORIZONTAL) {
		dx = 0;
		dy = 1;
	    } else {
		dx = 1;
		dy = 0;
	    }
	}
	if (object.getDirection() == Direction.REVERSE) {
	    dx *= -1;
	    dy *= -1;
	}

	return new Point2D.Double(dx, dy);
    }

    private Point2D measureGeneDrawingSize(Gene gene) {
	double dx = 0.0, dy = 0.0;

	for (DrawingObject object : gene.getBuildingElements()) {
	    Point2D size = measureDrawingObjectSize(object);
	    dx += size.getX();
	    dy += size.getY();
	}

	return new Point2D.Double(dx, dy);
    }

    /**
     * @return ]0;100]
     */
    private double scoreConnections(Character ch, Gene gene) {
	// TODO Not yet implemented
	return 100.0;
    }

    /**
     * �rt�kelj�k azt, hogy mennyire t�volodik el a kiindul�pontt�l a
     * kirajzoland� alakzat v�gpontja.
     * 
     * @return [0;100]
     */
    private double scoreDrawingSize(Gene gene) {
	Point2D size = measureGeneDrawingSize(gene);

	double absX = Math.abs(size.getX());
	double absY = Math.abs(size.getY());

	if (absX > DEFAULT_DRAWING_AREA_SIZE.getX()
		|| absY > DEFAULT_DRAWING_AREA_SIZE.getY()) {
	    // biztos, kifut a rajzol�si ter�letr�l
	    return 0.0;
	}

	if (absX <= 1 && absY <= 1) {
	    // 1 egys�gen bel�l v�gz�dik az objektum
	    return 100.0;
	}

	double xScore = 50.0;
	double yScore = 50.0;

	// ha 1 egys�gn�l t�volabb v�gz�dik az objektum, akkor az �rt�kel�s a
	// ford�tott relat�v t�vols�g lesz
	if (absX > 1) {
	    xScore *= 1 - (absX - 1) / (DEFAULT_DRAWING_AREA_SIZE.getX() - 1);
	}
	if (absY > 1) {
	    yScore *= 1 - (absY - 1) / (DEFAULT_DRAWING_AREA_SIZE.getY() - 1);
	}

	return xScore + yScore;
    }

    /**
     * Meghat�rozza �s be�ll�tja a kromosz�ma fitnesz �rt�k�t. Az �rt�k 0 �s 100
     * k�z�tti val�s sz�m.
     */
    public void scoreFitness(Chromosome chrom) {
	double fitnessScore = 0.0;

	Map<Character, Gene> geneMap = chrom.geneMap();
	for (Entry<Character, Gene> geneEntry : geneMap.entrySet()) {
	    Character ch = geneEntry.getKey();
	    Gene gene = geneEntry.getValue();

	    // 1. l�p�s: �rt�kelj�k a g�n hossz�t
	    double lengthScore = scoreGeneLength(ch, gene);
	    // 2. l�p�s: �rt�kelj�k a g�n m�ret�t
	    double sizeScore = scoreDrawingSize(gene);
	    // 3. l�p�s: �rt�kelj�k a g�n kapcsolat�t a t�bbi g�nnel
	    double connectionScore = scoreConnections(ch, gene);

	    fitnessScore += (lengthScore + sizeScore + connectionScore) / 3;
	}
	fitnessScore = fitnessScore / geneMap.size();

	// a karakterek egyedis�gi �rt�k�nek n�gyzet�vel szorozzuk a
	// v�geredm�nyt, ezzel b�ntetve a megegyez� g�neket tartalmaz�
	// kromosz�m�kat
	double uniquenessScore = scoreGeneUniqueness(chrom.genes());
	fitnessScore *= uniquenessScore * uniquenessScore;

	chrom.setFitnessScore(fitnessScore);
    }

    /**
     * @return ]0;100]
     */
    private double scoreGeneLength(Character ch, Gene gene) {
	// mennyire t�r el a g�n hossza a neki aj�nlott hossz�s�gt�l
	int deviation = Math.abs(gene.length() - recommendedLengthMap.get(ch));

	// m�sodfok� az �rt�kcs�kkent�s (100, 50, 25...)
	double lengthScore = 2.0 / Math.pow(2, deviation + 1) * 100.0;

	return lengthScore;
    }

    /**
     * @return [0;1]
     */
    private double scoreGeneUniqueness(Collection<Gene> genes) {
	if (genes.isEmpty()) {
	    // �res halmaz elemei teljesen "egyediek"
	    return 1.0;
	}

	int count = 0;
	HashSet<Gene> duplicationSearchSet = new HashSet<>();
	for (Gene gene : genes) {
	    if (duplicationSearchSet.contains(gene)) {
		count += 1;
	    } else {
		duplicationSearchSet.add(gene);
	    }
	}

	return 1.0 - count / (double) charCountMap.size();
    }
}
