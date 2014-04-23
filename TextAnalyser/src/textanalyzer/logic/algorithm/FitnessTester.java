package textanalyzer.logic.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A genetikus algoritmusban szereplõ fitnesz függvényt reprezentáló osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class FitnessTester {

    private String sourceText;
    private Map<Character, Integer> charCountMap = new HashMap<>();

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

	// negatív értéket adunk vissza
	return -(count / (double) charCountMap.size());
    }

    /**
     * Meghatározza és beállítja a kromoszóma fitnesz értékét.
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
	// minél többször fordul elõ a karakter a szövegben, annál elõnyösebb,
	// ha rövidebb a génje
	double relativeFrequencyBaseScore = charCountMap.get(ch)
		/ (double) sourceText.length();
	double lengthTotalScore = lengthBaseScore
		/ (1 - relativeFrequencyBaseScore);

	return lengthBaseScore + lengthTotalScore;
    }
}
