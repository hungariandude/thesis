package textanalyzer.logic.algorithm;

import textanalyzer.logic.Parameters;
import textanalyzer.util.MutableInteger;
import textanalyzer.util.Pair;

import java.awt.geom.Rectangle2D;
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
 * A genetikus algoritmusban szereplő fitnesz függvényt biztosító osztály.
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
     * Az egyes karakterek bal- és jobbszomszédainak előfordulási számát követi.
     */
    private Map<Character, Pair<Map<Character, MutableInteger>, Map<Character, MutableInteger>>> charNeighborCountMap = new HashMap<>();
    /**
     * Az egyes karakterekhez tartozó bal- és jobbszomszédok, amelyeknek az
     * előfordulási száma a maximummal egyenlő.
     */
    private Map<Character, Pair<Entry<Integer, List<Character>>, Entry<Integer, List<Character>>>> maxCountNeighborMap = new HashMap<>();
    /**
     * Az egyes karakterekhez rendelt kirajzolható objektumok ajánlott
     * elemszáma.
     */
    private Map<Character, Integer> recommendedLengthMap = new HashMap<>();

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
     * Megszámolja az egyes karakterek előfordulását a szövegben.
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

	if (Parameters.debugMode) {
	    LOGGER.info("Count of found characters: " + charCountMap.size());
	}

	// 2. lépés: meghatározzuk az ajánlott génhosszakat az egyes
	// karakterekhez
	TreeMap<Integer, List<Character>> orderedMap = sortMutableIntegerValueMap(charCountMap);

	int occupiedSlotsSoFar = 0;
	int differentObjects = Segment.NUMBER_OF_OBJECT_DRAWING_WAYS;
	int actualGeneLength = 1;
	for (List<Character> list : orderedMap.values()) {
	    if (differentObjects <= occupiedSlotsSoFar) {
		differentObjects += (int) Math.pow(
			Segment.NUMBER_OF_OBJECT_DRAWING_WAYS,
			++actualGeneLength);
	    }
	    occupiedSlotsSoFar += list.size();

	    for (Character ch : list) {
		recommendedLengthMap.put(ch, actualGeneLength);
	    }
	}

	// 3. lépés: megkeressük a legnagyobb előfordulási számú bal- és
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

    /**
     * @return [0;1]
     */
    private double scoreConnections(Character key, Map<Character, Gene> map) {
	Pair<Entry<Integer, List<Character>>, Entry<Integer, List<Character>>> entry = maxCountNeighborMap
		.get(key);
	Gene mainGene = map.get(key);
	List<Character> leftList = entry.getLeft() == null ? null : entry
		.getLeft().getValue();
	List<Character> rightList = entry.getRight() == null ? null : entry
		.getRight().getValue();
	double leftPoints = 0.0, rightPoints = 0.0;

	if (leftList == null) {
	    leftPoints = 0.5;
	} else {
	    int leftSize = leftList.size();
	    if (leftSize > 0) {
		for (Character ch : leftList) {
		    leftPoints += scoreDrawingSize(Gene.concatenate(
			    map.get(ch), mainGene));
		}
		leftPoints = leftPoints / leftSize / 2;
	    } else {
		leftPoints = 0.5;
	    }
	}

	if (rightList == null) {
	    rightPoints = 0.5;
	} else {
	    int rightSize = rightList.size();
	    if (rightSize > 0) {
		for (Character ch : rightList) {
		    rightPoints += scoreDrawingSize(Gene.concatenate(mainGene,
			    map.get(ch)));
		}
		rightPoints = rightPoints / rightSize / 2;
	    } else {
		rightPoints = 0.5;
	    }
	}

	return leftPoints + rightPoints;
    }

    /**
     * Értékeljük azt, hogy mennyire távolodik el a kiindulóponttól a
     * kirajzolandó alakzat végpontja.
     * 
     * @return [0;1]
     */
    private double scoreDrawingSize(Gene gene) {
	Rectangle2D bounds = gene.getBounds();

	if (bounds.getWidth() > Parameters.drawingAreaSizeX
		|| bounds.getHeight() > Parameters.drawingAreaSizeY) {
	    // biztos, kifut a rajzolási területről
	    return 0.0;
	}

	if (bounds.getWidth() <= 1 && bounds.getHeight() <= 1) {
	    // 1 egységen belül végződik az objektum
	    return 1.0;
	}

	double xScore = 0.5;
	double yScore = 0.5;

	// ha 1 egységnél távolabb végződik az objektum, akkor az értékelés a
	// fordított relatív távolság lesz
	if (bounds.getWidth() > 1) {
	    xScore *= 1 - (bounds.getWidth() - 1)
		    / (Parameters.drawingAreaSizeX - 1);
	}
	if (bounds.getHeight() > 1) {
	    yScore *= 1 - (bounds.getHeight() - 1)
		    / (Parameters.drawingAreaSizeY - 1);
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
	    double connectionScore = scoreConnections(ch, chrom.geneMap());

	    fitnessScore += (lengthScore + sizeScore + connectionScore) / 3;
	}
	// a fentiek 1/10-ed arányban számítanak bele a fitnesz pontszámba
	fitnessScore = fitnessScore / geneMap.size() * 20;

	// végigíratjuk a bemeneti szövegünket a kromoszómával. Az értékelés
	// szempontja az, hogy az írás során hányszor futunk le a rajzolási
	// képernyőről
	double writingTestScore = scoreWritingTest(chrom);
	// ez pedig 9/10-ed arányban számítódik bele a fitness pontszámba
	fitnessScore += writingTestScore * 80;

	// a karakterek egyediségi értékének négyzetével szorozzuk a
	// végeredményt, ezzel büntetve a megegyező géneket tartalmazó
	// kromoszómákat
	double uniquenessScore = scoreGeneUniqueness(chrom.genes());
	fitnessScore *= uniquenessScore * uniquenessScore;

	chrom.setFitnessScore(fitnessScore);
    }

    /**
     * @return ]0;1]
     */
    private double scoreGeneLength(Character ch, Gene gene) {
	// mennyire tér el a gén hossza a neki ajánlott hosszúságtól
	int deviation = Math.abs(gene.length() - recommendedLengthMap.get(ch));

	// másodfokú az értékcsökkentés (1, 0.5, 0.25, ...)
	double lengthScore = 2.0 / Math.pow(2, deviation + 1);

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
     * Végigmegy a forrásszövegen és megszámolja, hogy hányszor fut le az írás a
     * rajzoló felületről és ezután ad értékelést.
     * 
     * return [0;1]
     */
    private double scoreWritingTest(Chromosome chrom) {
	double x = 0.0, y = 0.0;

	int runOutCount = 0;
	for (int i = 0; i < sourceText.length(); ++i) {
	    char ch = sourceText.charAt(i);
	    Rectangle2D size = chrom.geneMap().get(ch).getBounds();
	    x += size.getWidth();
	    y += size.getHeight();
	    if (Math.abs(x) > Parameters.drawingAreaSizeX / 2
		    || Math.abs(y) > Parameters.drawingAreaSizeY / 2) {
		// kifutottunk
		runOutCount++;
		// visszaugrunk középre
		x = y = 0.0;
	    }
	}

	return 1 - (double) runOutCount / sourceText.length();
    }

    /**
     * Egy <T, MutableInteger> Map-ből egy <Integer, List<T>> TreeMap-et készít.
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
