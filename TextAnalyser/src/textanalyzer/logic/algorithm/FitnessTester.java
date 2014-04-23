package textanalyzer.logic.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A genetikus algoritmusban szerepl� fitnesz f�ggv�nyt reprezent�l� oszt�ly.
 * 
 * @author Istv�nfi Zsolt
 */
public class FitnessTester {

    private String sourceText;
    private Map<Character, Integer> charCountMap = new HashMap<>();

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
    }

    /**
     * [-1;0]
     */
    private double scoreChromosomeUniqeness(Chromosome chrom) {
	int count = 0;
	HashSet<Gene> duplicationSearchSet = new HashSet<>();
	for (Gene gene : chrom.genes()) {
	    if (duplicationSearchSet.contains(gene)) {
		count += 1;
	    } else {
		duplicationSearchSet.add(gene);
	    }
	}

	// negat�v �rt�ket adunk vissza
	return -(count / (double) charCountMap.size());
    }

    /**
     * Meghat�rozza �s be�ll�tja a kromosz�ma fitnesz �rt�k�t.
     */
    public void scoreFitness(Chromosome chrom) {
	double fitnessScore = 0.0;

	for (Entry<Character, Gene> geneEntry : chrom.geneMap().entrySet()) {
	    fitnessScore += scoreGeneEntry(geneEntry);
	}
	fitnessScore += scoreChromosomeUniqeness(chrom);

	chrom.setFitnessScore(fitnessScore);
    }

    private double scoreGeneEntry(Entry<Character, Gene> geneEntry) {
	Character ch = geneEntry.getKey();
	Gene gene = geneEntry.getValue();

	double lengthBaseScore = 2 / Math.pow(2, gene.length());
	// min�l t�bbsz�r fordul el� a karakter a sz�vegben, ann�l el�ny�sebb,
	// ha r�videbb a g�nje
	double relativeFrequencyBaseScore = charCountMap.get(ch)
		/ (double) sourceText.length();
	double lengthTotalScore = lengthBaseScore
		/ (1 - relativeFrequencyBaseScore);

	return lengthBaseScore + lengthTotalScore;
    }
}
