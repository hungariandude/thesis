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
 * A genetikus algoritmusban szereplõ fitnesz függvényt biztosító osztály.
 * 
 * @author Istvánfi Zsolt
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
     * A megtalált karakterek halmaza.
     */
    public Set<Character> characterSet() {
	return charCountMap.keySet();
    }

    /**
     * Megszámolja az egyes karakterek elõfordulását a szövegben.
     */
    private void countCharOccurrences() {
	// 1. lépés: megszámoljuk a karaktereket
	for (int beginIndex = 0; beginIndex < sourceText.length(); ++beginIndex) {
	    char currentChar = sourceText.charAt(beginIndex);
	    Integer value = charCountMap.get(currentChar);
	    if (value == null) {
		// még nem számoltuk meg az elõfordulásait, tehát most ezt
		// elvégezzük
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

	// 2. lépés: meghatározzuk az ajánlott génhosszakat az egyes
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
		// ebben az esetben a dx negatív
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
     * Értékeljük azt, hogy mennyire távolodik el a kiindulóponttól a
     * kirajzolandó alakzat végpontja.
     * 
     * @return [0;100]
     */
    private double scoreDrawingSize(Gene gene) {
	Point2D size = measureGeneDrawingSize(gene);

	double absX = Math.abs(size.getX());
	double absY = Math.abs(size.getY());

	if (absX > DEFAULT_DRAWING_AREA_SIZE.getX()
		|| absY > DEFAULT_DRAWING_AREA_SIZE.getY()) {
	    // biztos, kifut a rajzolási területrõl
	    return 0.0;
	}

	if (absX <= 1 && absY <= 1) {
	    // 1 egységen belül végzõdik az objektum
	    return 100.0;
	}

	double xScore = 50.0;
	double yScore = 50.0;

	// ha 1 egységnél távolabb végzõdik az objektum, akkor az értékelés a
	// fordított relatív távolság lesz
	if (absX > 1) {
	    xScore *= 1 - (absX - 1) / (DEFAULT_DRAWING_AREA_SIZE.getX() - 1);
	}
	if (absY > 1) {
	    yScore *= 1 - (absY - 1) / (DEFAULT_DRAWING_AREA_SIZE.getY() - 1);
	}

	return xScore + yScore;
    }

    /**
     * Meghatározza és beállítja a kromoszóma fitnesz értékét. Az érték 0 és 100
     * közötti valós szám.
     */
    public void scoreFitness(Chromosome chrom) {
	double fitnessScore = 0.0;

	Map<Character, Gene> geneMap = chrom.geneMap();
	for (Entry<Character, Gene> geneEntry : geneMap.entrySet()) {
	    Character ch = geneEntry.getKey();
	    Gene gene = geneEntry.getValue();

	    // 1. lépés: értékeljük a gén hosszát
	    double lengthScore = scoreGeneLength(ch, gene);
	    // 2. lépés: értékeljük a gén méretét
	    double sizeScore = scoreDrawingSize(gene);
	    // 3. lépés: értékeljük a gén kapcsolatát a többi génnel
	    double connectionScore = scoreConnections(ch, gene);

	    fitnessScore += (lengthScore + sizeScore + connectionScore) / 3;
	}
	fitnessScore = fitnessScore / geneMap.size();

	// a karakterek egyediségi értékének négyzetével szorozzuk a
	// végeredményt, ezzel büntetve a megegyezõ géneket tartalmazó
	// kromoszómákat
	double uniquenessScore = scoreGeneUniqueness(chrom.genes());
	fitnessScore *= uniquenessScore * uniquenessScore;

	chrom.setFitnessScore(fitnessScore);
    }

    /**
     * @return ]0;100]
     */
    private double scoreGeneLength(Character ch, Gene gene) {
	// mennyire tér el a gén hossza a neki ajánlott hosszúságtól
	int deviation = Math.abs(gene.length() - recommendedLengthMap.get(ch));

	// másodfokú az értékcsökkentés (100, 50, 25...)
	double lengthScore = 2.0 / Math.pow(2, deviation + 1) * 100.0;

	return lengthScore;
    }

    /**
     * @return [0;1]
     */
    private double scoreGeneUniqueness(Collection<Gene> genes) {
	if (genes.isEmpty()) {
	    // üres halmaz elemei teljesen "egyediek"
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
