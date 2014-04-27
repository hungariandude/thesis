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
 * A genetikus algoritmusban szerepl� fitnesz f�ggv�nyt biztos�t� oszt�ly.
 * 
 * @author Istv�nfi Zsolt
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
	    double sizeScore = scoreDrawingSize(ch, gene);
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
     * ]0;100]
     */
    private double scoreGeneLength(Character ch, Gene gene) {
	// mennyire t�r el a g�n hossza a neki aj�nlott hossz�s�gt�l
	int deviation = Math.abs(gene.length() - recommendedLengthMap.get(ch));

	// m�sodfok� az �rt�kcs�kkent�s (100, 50, 25...)
	double lengthScore = 2.0 / Math.pow(2, deviation + 1) * 100.0;

	return lengthScore;
    }

    /**
     * [0;1]
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
