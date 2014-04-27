package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

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
    private int generationCounter = 0;

    private final Random random = new Random();

    private static final Logger LOGGER = Logger
	    .getLogger(GeneticAlgorithm.class.getName());

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

	// LOGGER.info("Initial population:\n" + population.toString());
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
    public Population evolvePopulation() {
	// �j gener�ci� sz�letik
	++generationCounter;
	// az �j gener�ci� popul�ci�ja
	Population newGeneration = new Population(populationSize,
		generationCounter);

	for (int i = 0; i < populationSize; i += 2) {
	    // 1. l�p�s: k�t sz�l� krom�sz�ma kiv�laszt�sa
	    Chromosome parent1 = selectMember(true);
	    Chromosome parent2 = selectMember(true);

	    // 2. l�p�s: sz�l�k keresztez�se
	    Chromosome[] childs = parent1.crossOverWith(parent2);
	    Chromosome child1 = childs[0];
	    Chromosome child2 = childs[1];

	    // 3. l�p�s: gyerekek mut�l�sa
	    mutator.mutate(child1);
	    mutator.mutate(child2);

	    // 4. l�p�s: gyerekek fitnesz �rt�keinek sz�m�t�sa
	    fitnessTester.scoreFitness(child1);
	    fitnessTester.scoreFitness(child2);

	    // 5. l�p�s: gyerekek hozz�d�sa az �j popul�ci�hoz
	    newGeneration.add(child1);
	    newGeneration.add(child2);
	}

	// friss�tj�k a popul�ci�nkat
	// itt az eredeti popul�ci� m�r �res, mert az el�z� gener�ci� m�r
	// "elpusztult"
	population = newGeneration;

	return newGeneration;
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
     * 
     * @param remove
     *            A kromosz�ma t�rl�se a kiv�laszt�s ut�n.
     */
    private Chromosome selectMember(boolean remove) {
	//
	// RULETTKER�K ALGORITMUS
	//

	// 1. l�p�s: �sszegezz�k a fitnesz �rt�keket
	double totalFitness = 0;
	for (Chromosome chrom : population) {
	    totalFitness += chrom.getFitnessScore();
	}

	// 2. l�p�s: kiv�lasztunk egy pontot a sk�l�n
	double selectedPoint = totalFitness * random.nextDouble();

	// 3. l�p�s: megkeress�k, hogy melyik egyed fitnesz intervallum�ba
	// tal�ltunk bele
	double totalFitnessSoFar = 0;
	for (Iterator<Chromosome> it = population.iterator(); it.hasNext();) {
	    Chromosome chrom = it.next();
	    totalFitnessSoFar += chrom.getFitnessScore();
	    if (totalFitnessSoFar >= selectedPoint) {
		// megtal�ltuk
		if (remove) {
		    it.remove();
		}
		return chrom;
	    }
	}

	return null;
    }
}
