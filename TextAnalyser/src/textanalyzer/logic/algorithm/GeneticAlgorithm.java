package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A genetikus algoritmus megval�s�t�sa.
 * 
 * @author Istv�nfi Zsolt
 */
public class GeneticAlgorithm {

    private List<Chromosome> population;
    private final int populationSize;

    private Map<Character, Integer> charCountMap = new HashMap<>();
    private String sourceText;

    private final Random random = new Random();
    private boolean running = true;

    public GeneticAlgorithm(String sourceText, int populationSize) {
	if (populationSize < 2 || populationSize % 2 != 0) {
	    throw new IllegalArgumentException(
		    "Population size must be positive and even.");
	}

	this.sourceText = sourceText;
	this.populationSize = populationSize;
	population = new ArrayList<>(populationSize);

	countCharOccurrences();
	generateRandomPopulation();
	calculateFitnessScores();
    }

    /**
     * Kezdeti fitnesz �rt�k sz�m�t�s.
     */
    private void calculateFitnessScores() {
	for (Chromosome chrom : population) {
	    chrom.calculateFitnessScore();
	}
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
     * L�trehoz v�letlenszer� kromosz�m�kat a sz�vegben megtal�lt karaktereknek.
     */
    private void generateRandomPopulation() {
	int numberOfFoundCharacters = charCountMap.size();
	// a maximum g�nhossz a legkisebb olyan alakzatsz�m, amellyel
	// biztos, hogy minden megtal�lt karakter k�l�nb�z�en kirajzolhat�
	int maximumGeneLength = 1;
	int differentObjects = DrawingObject.NUMBER_OF_OBJECT_DRAWING_WAYS;
	while (differentObjects < numberOfFoundCharacters) {
	    differentObjects += (int) Math.pow(
		    DrawingObject.NUMBER_OF_OBJECT_DRAWING_WAYS,
		    ++maximumGeneLength);
	}

	for (int i = 0; i < populationSize; ++i) {
	    population.add(new Chromosome(charCountMap.keySet(),
		    maximumGeneLength));
	}
    }

    private void run() {
	// az �j gener�ci� popul�ci�ja
	List<Chromosome> newPopulation = new ArrayList<>(populationSize);
	// �ppen melyik gener�ci� sz�letik
	int generation = 0;

	while (running) {
	    // �j gener�ci� sz�letik
	    ++generation;
	    newPopulation.clear();

	    for (int i = 0; i < populationSize; i += 2) {
		// 1. l�p�s: k�t krom�sz�ma kiv�laszt�s
		Chromosome parent1 = selectMember();
		Chromosome parent2 = selectMember();

		// 2. l�p�s: ezek keresztez�se
		Chromosome.crossover(parent1, parent2);

		// 3. l�p�s: ezek mut�l�sa
		parent1.mutate();
		parent2.mutate();

		// 4. l�p�s: ezek fitnesz �rt�keinek �jrasz�m�t�sa
		parent1.calculateFitnessScore();
		parent2.calculateFitnessScore();

		// 5. l�p�s: ezek hozz�d�sa az �j popul�ci�hoz
		newPopulation.add(parent1);
		newPopulation.add(parent2);
	    }

	    // friss�tj�k a popul�ci�nkat
	    population.addAll(newPopulation);
	}
    }

    /**
     * Szelekci�. Nagyobb fitnesz �rt�k nagyobb kiv�laszt�si val�sz�n�s�get
     * jelent.
     */
    private Chromosome selectMember() {
	// 1. l�p�s: �sszegezz�k a fitnesz �rt�keket
	int totalFitness = 0;
	for (Chromosome chrom : population) {
	    totalFitness += chrom.getFitnessScore();
	}

	// 2. l�p�s: kiv�lasztunk egy pontot a sk�l�n
	float selectedPoint = totalFitness * random.nextFloat();

	// 3. l�p�s: megkeress�k, hogy melyik egyed fitnesz intervallum�ba
	// tal�ltunk bele
	int totalFitnessSoFar = 0;
	for (Iterator<Chromosome> it = population.iterator(); it.hasNext();) {
	    Chromosome chrom = it.next();
	    totalFitnessSoFar += chrom.getFitnessScore();
	    if (totalFitnessSoFar >= selectedPoint) {
		// megtal�ltuk
		it.remove();
		return chrom;
	    }
	}

	return null;
    }
}
