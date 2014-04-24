package textanalyzer.logic.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A genetikus algoritmusban szereplõ fitnesz függvényt reprezentáló osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class FitnessTester {

    private String sourceText;
    private Map<Character, Integer> charCountMap = new HashMap<>();
    private double averageCharCount;

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

	averageCharCount = sourceText.length() / (double) charCountMap.size();

	LOGGER.info("Count of found characters: " + charCountMap.size());
    }

    /**
     * Meghatározza és beállítja a kromoszóma fitnesz értékét.
     */
    public void scoreFitness(Chromosome chrom) {
	double fitnessScore = 0.0;

	Map<Character, Gene> geneMap = chrom.geneMap();
	for (Entry<Character, Gene> geneEntry : geneMap.entrySet()) {
	    fitnessScore += scoreGeneEntry(geneEntry);
	}
	fitnessScore = fitnessScore / geneMap.size();

	// a karakterek egyediségének értékével szorozzuk a végeredményt
	fitnessScore *= scoreGeneUniqeness(chrom.genes());
	chrom.setFitnessScore(fitnessScore);
    }

    /**
     * (0;100]
     */
    private double scoreGeneEntry(Entry<Character, Gene> geneEntry) {
	Character ch = geneEntry.getKey();
	Gene gene = geneEntry.getValue();

	// 1. lépés: értékeljük a gén hosszát
	double lengthScore = scoreGeneLength(ch, gene);
	// 2. lépés: értékeljük a gén kapcsolatát a többi génnel

	return lengthScore / 1.0;
    }

    /**
     * (0;100]
     */
    private double scoreGeneLength(Character ch, Gene gene) {
	double lengthBaseScore = 2.0 / Math.pow(2, gene.length()) * 100.0;

	// minél többször fordul elõ a karakter a szövegben, annál elõnyösebb,
	// ha rövidebb a génje
	int count = charCountMap.get(ch);
	double relativeFrequencyScore;
	if (count >= averageCharCount) {
	    relativeFrequencyScore = 1.0;
	} else {
	    relativeFrequencyScore = count / averageCharCount;
	}
	double lengthTotalScore = lengthBaseScore * relativeFrequencyScore;

	return lengthTotalScore;
    }

    /**
     * [0;1]
     */
    private double scoreGeneUniqeness(Collection<Gene> genes) {
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
