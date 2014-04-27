package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

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

    /**
     * ]0;100]
     */
    private double scoreConnections(Character ch, Gene gene) {
	// TODO Auto-generated method stub
	return 100;
    }

    /**
     * [0;100]
     */
    private double scoreDrawingSize(Character ch, Gene gene) {
	// TODO Auto-generated method stub
	return 100;
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
	    double sizeScore = scoreDrawingSize(ch, gene);
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
     * ]0;100]
     */
    private double scoreGeneLength(Character ch, Gene gene) {
	// mennyire tér el a gén hossza a neki ajánlott hosszúságtól
	int deviation = Math.abs(gene.length() - recommendedLengthMap.get(ch));

	// másodfokú az értékcsökkentés (100, 50, 25...)
	double lengthScore = 2.0 / Math.pow(2, deviation + 1) * 100.0;

	return lengthScore;
    }

    /**
     * [0;1]
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
