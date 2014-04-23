package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A genetikus algoritmus megval�s�t�sa.
 * 
 * @author Istv�nfi Zsolt
 */
public class GeneticAlgorithm {

    private Population population;
    private final int populationSize;
    private Mutator mutator = new Mutator();
    private FitnessTester fitnessTester;

    /**
     * H�nyadik gener�ci�n�l tart az evol�ci�.
     */
    private int generationCounter;

    private final Random random = new Random();

    public GeneticAlgorithm(String sourceText, int populationSize) {
	if (populationSize < 2 || populationSize % 2 != 0) {
	    throw new IllegalArgumentException(
		    "Population size must be positive and even.");
	}

	this.populationSize = populationSize;
	population = new Population(populationSize, generationCounter);
	fitnessTester = new FitnessTester(sourceText);

	generateRandomPopulation();
	calculateFitnessScores();
    }

    /**
     * Kezdeti fitnesz �rt�k sz�m�t�s.
     */
    private void calculateFitnessScores() {
	for (Chromosome chrom : population) {
	    fitnessTester.scoreFitness(chrom);
	}
    }

    /**
     * Az evol�ci� egy l�p�se. �j popul�ci�t hoz l�tre.
     */
    public List<Chromosome> evolvePopulation() {
	// �j gener�ci� sz�letik
	++generationCounter;
	// az �j gener�ci� popul�ci�ja
	Population newPopulation = new Population(populationSize,
		generationCounter);

	for (int i = 0; i < populationSize; i += 2) {
	    // 1. l�p�s: k�t krom�sz�ma kiv�laszt�s
	    Chromosome parent1 = selectMember();
	    Chromosome parent2 = selectMember();

	    // 2. l�p�s: ezek keresztez�se
	    Chromosome.crossover(parent1, parent2);

	    // 3. l�p�s: ezek mut�l�sa
	    mutator.mutate(parent1);
	    mutator.mutate(parent2);

	    // 4. l�p�s: ezek fitnesz �rt�keinek �jrasz�m�t�sa
	    fitnessTester.scoreFitness(parent1);
	    fitnessTester.scoreFitness(parent2);

	    // 5. l�p�s: ezek hozz�d�sa az �j popul�ci�hoz
	    newPopulation.add(parent1);
	    newPopulation.add(parent2);
	}

	// friss�tj�k a popul�ci�nkat
	population = newPopulation;

	return newPopulation;
    }

    /**
     * L�trehoz v�letlenszer� kromosz�m�kat a sz�vegben megtal�lt karaktereknek.
     */
    private void generateRandomPopulation() {
	// lek�rj�k a fitneszki�rt�kel�t�l a megtal�lt karaktereket
	Set<Character> characterSet = fitnessTester.characterSet();
	int numberOfFoundCharacters = characterSet.size();
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
	    population.add(new Chromosome(characterSet, maximumGeneLength));
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
