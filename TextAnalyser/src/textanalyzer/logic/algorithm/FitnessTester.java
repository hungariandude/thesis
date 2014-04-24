package textanalyzer.logic.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A genetikus algoritmusban szerepl� fitnesz f�ggv�nyt reprezent�l� oszt�ly.
 * 
 * @author Istv�nfi Zsolt
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
     * A megtal�lt karakterek halmaza.
     */
    public Set<Character> characterSet() {
	return charCountMap.keySet();
    }

    /**
     * Megsz�molja az egyes karakterek el�fordul�s�t a sz�vegben.
     */
    private void countCharOccurrences() {
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

	averageCharCount = sourceText.length() / (double) charCountMap.size();

	LOGGER.info("Count of found characters: " + charCountMap.size());
    }

    /**
     * Meghat�rozza �s be�ll�tja a kromosz�ma fitnesz �rt�k�t.
     */
    public void scoreFitness(Chromosome chrom) {
	double fitnessScore = 0.0;

	Map<Character, Gene> geneMap = chrom.geneMap();
	for (Entry<Character, Gene> geneEntry : geneMap.entrySet()) {
	    fitnessScore += scoreGeneEntry(geneEntry);
	}
	fitnessScore = fitnessScore / geneMap.size();

	// a karakterek egyedis�g�nek �rt�k�vel szorozzuk a v�geredm�nyt
	fitnessScore *= scoreGeneUniqeness(chrom.genes());
	chrom.setFitnessScore(fitnessScore);
    }

    /**
     * (0;100]
     */
    private double scoreGeneEntry(Entry<Character, Gene> geneEntry) {
	Character ch = geneEntry.getKey();
	Gene gene = geneEntry.getValue();

	// 1. l�p�s: �rt�kelj�k a g�n hossz�t
	double lengthScore = scoreGeneLength(ch, gene);
	// 2. l�p�s: �rt�kelj�k a g�n kapcsolat�t a t�bbi g�nnel

	return lengthScore / 1.0;
    }

    /**
     * (0;100]
     */
    private double scoreGeneLength(Character ch, Gene gene) {
	double lengthBaseScore = 2.0 / Math.pow(2, gene.length()) * 100.0;

	// min�l t�bbsz�r fordul el� a karakter a sz�vegben, ann�l el�ny�sebb,
	// ha r�videbb a g�nje
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
