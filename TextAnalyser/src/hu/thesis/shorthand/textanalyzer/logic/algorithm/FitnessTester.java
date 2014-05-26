package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.logic.Parameters;
import hu.thesis.shorthand.textanalyzer.util.MutableInteger;
import hu.thesis.shorthand.textanalyzer.util.RandomUtils;

import java.awt.geom.Point2D;
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
    protected String sourceText;
    /**
     * Az egyes karakterek száma a szövegben.
     */
    protected Map<Character, MutableInteger> charCountMap = new HashMap<>();
    /**
     * Az egyes karakterekhez rendelt kirajzolható objektumok ajánlott
     * elemszáma.
     */
    protected Map<Character, Integer> recommendedLengthMap = new HashMap<>();

    /**
     * Egymáshoz hasonlító karakterek.
     */
    protected static final String[] similarChars = new String[] { "aá", "eé",
	    "ií", "oóöő", "uúüű" };
    /**
     * A karakterek és a hozzájuk hasonlító karakterek összerendelése.
     */
    protected static Map<Character, char[]> similarityMap = new HashMap<>();
    static {
	for (String str : similarChars) {
	    for (int i = 0; i < str.length(); ++i) {
		char ch = str.charAt(i);
		str = str.replace(String.valueOf(ch), "");
		similarityMap.put(ch, str.toCharArray());
	    }
	}
    }

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
    protected void countCharOccurrences() {
	// 1. lépés: megszámoljuk a karaktereket
	for (int index = 0; index < sourceText.length(); ++index) {
	    char currentChar = sourceText.charAt(index);

	    MutableInteger value = charCountMap.get(currentChar);
	    if (value == null) {
		charCountMap.put(currentChar, value = new MutableInteger());
	    }
	    value.add(1);
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
    }

    /**
     * Értékeljük azt, hogy mennyire távolodik el a kiindulóponttól a
     * kirajzolandó alakzat végpontja.
     * 
     * @return [0;1]
     */
    protected double scoreDrawingSize(Gene gene) {
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

	Map<Character, Gene> geneMap = chrom.getGeneMap();
	for (Entry<Character, Gene> geneEntry : geneMap.entrySet()) {
	    Character ch = geneEntry.getKey();
	    Gene gene = geneEntry.getValue();

	    // 1. lépés: értékeljük a gén hosszát
	    double lengthScore = scoreLength(ch, gene);
	    // 2. lépés: értékeljük a gén méretét
	    double sizeScore = scoreDrawingSize(gene);
	    // 3. lépés: értékeljük a gén hasonlóságát a karakterek szerinti
	    // hasonlóságokhoz (pl. u-ú-ü-ű)
	    double similarityScore = scoreSimilarity(ch, gene,
		    chrom.getGeneMap());

	    fitnessScore += (lengthScore + sizeScore + similarityScore) / 3;
	}
	// a fentiek 3/10-ed arányban számítódnak bele a végső pontszámba
	fitnessScore = fitnessScore / geneMap.size() * 30;

	// végigíratjuk a bemeneti szövegünket a kromoszómával. Az értékelés
	// szempontja az, hogy az írás során hányszor futunk le a rajzolási
	// képernyőről
	double writingTestScore = scoreWritingTest(chrom);
	// ez pedig 7/10-ed arányban számítódik bele a fitness pontszámba
	fitnessScore += writingTestScore * 70;

	// a karakterek egyediségi értékével hatványozzuk a végeredményt, ezzel
	// büntetve a megegyező géneket tartalmazó kromoszómákat
	double uniquenessScore = scoreGeneUniqueness(chrom.genes());
	fitnessScore = Math.pow(fitnessScore, uniquenessScore);

	chrom.setFitnessScore(fitnessScore);
    }

    /**
     * @return [0;1]
     */
    protected double scoreGeneUniqueness(Collection<Gene> genes) {
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
     * @return ]0;1]
     */
    protected double scoreLength(Character ch, Gene gene) {
	// mennyire tér el a gén hossza a neki ajánlott hosszúságtól
	int deviation = Math.abs(gene.length() - recommendedLengthMap.get(ch));

	// másodfokú az értékcsökkentés (1, 0.5, 0.25, ...)
	double lengthScore = 2.0 / Math.pow(2, deviation + 1);

	return lengthScore;
    }

    /**
     * @return [0;1]
     */
    protected double scoreSimilarity(Character ch, Gene gene,
	    Map<Character, Gene> geneMap) {
	char[] value = similarityMap.get(ch);
	if (value == null || value.length == 0) {
	    return 1.0;
	} else {
	    double totalScore = 0.0;
	    int found = 0;
	    for (char c : value) {
		double score;
		Gene similarGene = geneMap.get(c);
		if (similarGene == null) {
		    continue;
		}
		found++;
		if (gene.length() != similarGene.length()) {
		    score = 0.0;
		} else {
		    score = 0.0;
		    for (int i = 0; i < gene.length(); ++i) {
			Segment thisSegment = gene.getSegments().get(i);
			Segment otherSegment = similarGene.getSegments().get(i);
			if (thisSegment.getForm() == otherSegment.getForm()) {
			    score += 1;
			}
		    }
		    score /= gene.length();
		}
		totalScore += score;
	    }
	    if (found == 0) {
		return 1.0;
	    }
	    return totalScore / found;
	}
    }

    /**
     * Végigmegy egy forrásszövegen és megszámolja, hogy hányszor fut le az írás
     * a rajzoló felületről és ezután ad értékelést.
     * 
     * @return [0;1]
     */
    protected double scoreWritingTest(Chromosome chrom) {
	double x = 0.0, y = 0.0;

	int runOutCount = 0;
	int start = 0, end = sourceText.length();
	if (end > 1000) {
	    start = RandomUtils.random.nextInt(end - 999);
	    end = start + 1000;
	}
	for (int i = start; i < end; ++i) {
	    char ch = sourceText.charAt(i);
	    Gene gene = chrom.getGeneMap().get(ch);
	    // Rectangle2D size = gene.getBounds();
	    // x += size.getWidth();
	    // y += size.getHeight();
	    for (Segment segment : gene.getSegments()) {
		Point2D endingPoint = segment.getCurrentPoint();
		x += endingPoint.getX();
		y += endingPoint.getY();
		if (Math.abs(x) > Parameters.drawingAreaSizeX / 2
			|| Math.abs(y) > Parameters.drawingAreaSizeY / 2) {
		    // kifutottunk
		    runOutCount++;
		    // visszaugrunk középre
		    x = y = 0.0;
		    // új karaktert kezdünk
		    break;
		}
	    }
	}

	return 1 - runOutCount / 1000.0;
    }

    /**
     * Egy <T, MutableInteger> Map-ből egy <Integer, List<T>> TreeMap-et készít.
     */
    protected <T> TreeMap<Integer, List<T>> sortMutableIntegerValueMap(
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
