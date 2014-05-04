package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.logic.DrawingObject.Direction;
import textanalyzer.logic.DrawingObject.Orientation;
import textanalyzer.util.MutableInteger;
import textanalyzer.util.Pair;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    /**
     * A forrás szöveg.
     */
    private String sourceText;
    /**
     * Az egyes karakterek száma a szövegben.
     */
    private Map<Character, MutableInteger> charCountMap = new HashMap<>();
    /**
     * Az egyes karakterek bal- és jobbszomszédainak elõfordulási számát követi.
     */
    private Map<Character, Pair<Map<Character, MutableInteger>, Map<Character, MutableInteger>>> charNeighborCountMap = new HashMap<>();
    /**
     * Az egyes karakterekhez tartozó bal- és jobbszomszédok, amelyeknek az
     * elõfordulási száma a maximummal egyenlõ.
     */
    private Map<Character, Pair<Entry<Integer, List<Character>>, Entry<Integer, List<Character>>>> maxCountNeighborMap = new HashMap<>();
    /**
     * Az egyes karakterekhez rendelt kirajzolható objektumok ajánlott
     * elemszáma.
     */
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
	for (int index = 0; index < sourceText.length(); ++index) {
	    char currentChar = sourceText.charAt(index);

	    MutableInteger value = charCountMap.get(currentChar);
	    if (value == null) {
		charCountMap.put(currentChar, value = new MutableInteger());
	    }
	    value.add(1);

	    Pair<Map<Character, MutableInteger>, Map<Character, MutableInteger>> pairValue = charNeighborCountMap
		    .get(currentChar);
	    Map<Character, MutableInteger> left, right;
	    if (pairValue == null) {
		charNeighborCountMap.put(currentChar, pairValue = new Pair<>(
			left = new HashMap<>(), right = new HashMap<>()));
	    } else {
		if (pairValue.getLeft() == null) {
		    left = new HashMap<>();
		} else {
		    left = pairValue.getLeft();
		}
		if (pairValue.getRight() == null) {
		    right = new HashMap<>();
		} else {
		    right = pairValue.getRight();
		}

		if (index > 1) {
		    char previousChar = sourceText.charAt(index - 1);
		    value = left.get(previousChar);
		    if (value == null) {
			left.put(previousChar, value = new MutableInteger());
		    }
		    value.add(1);
		}
		if (index < sourceText.length() - 1) {
		    char nextChar = sourceText.charAt(index + 1);
		    value = right.get(nextChar);
		    if (value == null) {
			right.put(nextChar, value = new MutableInteger());
		    }
		    value.add(1);
		}
	    }
	}

	LOGGER.info("Count of found characters: " + charCountMap.size());

	// 2. lépés: meghatározzuk az ajánlott génhosszakat az egyes
	// karakterekhez
	TreeMap<Integer, List<Character>> orderedMap = sortMutableIntegerValueMap(charCountMap);

	int occupiedSlotsSoFar = 0;
	int differentObjects = DrawingObject.NUMBER_OF_OBJECT_DRAWING_WAYS;
	int actualGeneLength = 1;
	for (List<Character> list : orderedMap.values()) {
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

	// 3. lépés: megkeressük a legnagyobb elõfordulási számú bal- és
	// jobbszomszédokat az egyes karakterekhez
	for (Entry<Character, Pair<Map<Character, MutableInteger>, Map<Character, MutableInteger>>> entry : charNeighborCountMap
		.entrySet()) {
	    Character key = entry.getKey();
	    Pair<Map<Character, MutableInteger>, Map<Character, MutableInteger>> value = entry
		    .getValue();
	    TreeMap<Integer, List<Character>> leftTreeMap = sortMutableIntegerValueMap(value
		    .getLeft());
	    TreeMap<Integer, List<Character>> rightTreeMap = sortMutableIntegerValueMap(value
		    .getRight());
	    maxCountNeighborMap.put(key, new Pair<>(leftTreeMap.lastEntry(),
		    rightTreeMap.lastEntry()));
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

    private Point2D measureDrawingSize(List<DrawingObject> objects) {
	double dx = 0.0, dy = 0.0;

	for (DrawingObject object : objects) {
	    Point2D size = measureDrawingObjectSize(object);
	    dx += size.getX();
	    dy += size.getY();
	}

	return new Point2D.Double(dx, dy);
    }

    /**
     * @return [0;100]
     */
    private double scoreConnections(Character key, Map<Character, Gene> map) {
	Pair<Entry<Integer, List<Character>>, Entry<Integer, List<Character>>> entry = maxCountNeighborMap
		.get(key);
	List<DrawingObject> mainObjects = map.get(key).getBuildingElements();
	List<Character> leftList = entry.getLeft() == null ? null : entry
		.getLeft().getValue();
	List<Character> rightList = entry.getRight() == null ? null : entry
		.getRight().getValue();
	double leftPoints = 0.0, rightPoints = 0.0;

	if (leftList == null) {
	    leftPoints = 50.0;
	} else {
	    int leftSize = leftList.size();
	    if (leftSize > 0) {
		for (Character ch : leftList) {
		    ArrayList<DrawingObject> objects = new ArrayList<>(map.get(
			    ch).getBuildingElements());
		    objects.addAll(mainObjects);
		    leftPoints += scoreDrawingSize(objects);
		}
		leftPoints = leftPoints / leftSize / 2;
	    } else {
		leftPoints = 50.0;
	    }
	}

	if (rightList == null) {
	    rightPoints = 50.0;
	} else {
	    int rightSize = rightList.size();
	    if (rightSize > 0) {
		for (Character ch : rightList) {
		    ArrayList<DrawingObject> objects = new ArrayList<>(
			    mainObjects);
		    objects.addAll(map.get(ch).getBuildingElements());
		    rightPoints += scoreDrawingSize(objects);
		}
		rightPoints = rightPoints / rightSize / 2;
	    } else {
		rightPoints = 50.0;
	    }
	}

	return leftPoints + rightPoints;
    }

    /**
     * Értékeljük azt, hogy mennyire távolodik el a kiindulóponttól a
     * kirajzolandó alakzat végpontja.
     * 
     * @return [0;100]
     */
    private double scoreDrawingSize(List<DrawingObject> objects) {
	Point2D size = measureDrawingSize(objects);

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
	    double sizeScore = scoreDrawingSize(gene.getBuildingElements());
	    // 3. lépés: értékeljük a gén kapcsolatát a többi génnel
	    double connectionScore = scoreConnections(ch, chrom.geneMap());

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

    /**
     * Egy <T, MutableInteger> Map-bõl egy <Integer, List<T>> TreeMap-et készít.
     */
    private <T> TreeMap<Integer, List<T>> sortMutableIntegerValueMap(
	    Map<T, MutableInteger> inputMap) {
	TreeMap<Integer, List<T>> treeMap = new TreeMap<>();
	for (Entry<T, MutableInteger> entry : inputMap.entrySet()) {
	    T ch = entry.getKey();
	    int count = entry.getValue().getValue();
	    List<T> value = treeMap.get(count);
	    if (value == null) {
		treeMap.put(count, value = new ArrayList<>());
	    }
	    value.add(ch);
	}

	return treeMap;
    }
}
