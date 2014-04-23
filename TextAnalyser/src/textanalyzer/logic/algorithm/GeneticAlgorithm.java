package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A genetikus algoritmus megvalósítása.
 * 
 * @author Istvánfi Zsolt
 */
public class GeneticAlgorithm {

    private Population population;
    private final int populationSize;
    private Mutator mutator = new Mutator();
    private FitnessTester fitnessTester;

    /**
     * Hányadik generációnál tart az evolúció.
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
     * Kezdeti fitnesz érték számítás.
     */
    private void calculateFitnessScores() {
	for (Chromosome chrom : population) {
	    fitnessTester.scoreFitness(chrom);
	}
    }

    /**
     * Az evolúció egy lépése. Új populációt hoz létre.
     */
    public List<Chromosome> evolvePopulation() {
	// új generáció születik
	++generationCounter;
	// az új generáció populációja
	Population newPopulation = new Population(populationSize,
		generationCounter);

	for (int i = 0; i < populationSize; i += 2) {
	    // 1. lépés: két kromószóma kiválasztás
	    Chromosome parent1 = selectMember();
	    Chromosome parent2 = selectMember();

	    // 2. lépés: ezek keresztezése
	    Chromosome.crossover(parent1, parent2);

	    // 3. lépés: ezek mutálása
	    mutator.mutate(parent1);
	    mutator.mutate(parent2);

	    // 4. lépés: ezek fitnesz értékeinek újraszámítása
	    fitnessTester.scoreFitness(parent1);
	    fitnessTester.scoreFitness(parent2);

	    // 5. lépés: ezek hozzádása az új populációhoz
	    newPopulation.add(parent1);
	    newPopulation.add(parent2);
	}

	// frissítjük a populációnkat
	population = newPopulation;

	return newPopulation;
    }

    /**
     * Létrehoz véletlenszerû kromoszómákat a szövegben megtalált karaktereknek.
     */
    private void generateRandomPopulation() {
	// lekérjük a fitneszkiértékelõtõl a megtalált karaktereket
	Set<Character> characterSet = fitnessTester.characterSet();
	int numberOfFoundCharacters = characterSet.size();
	// a maximum génhossz a legkisebb olyan alakzatszám, amellyel
	// biztos, hogy minden megtalált karakter különbözõen kirajzolható
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
     * Szelekció. Nagyobb fitnesz érték nagyobb kiválasztási valószínûséget
     * jelent.
     */
    private Chromosome selectMember() {
	// 1. lépés: összegezzük a fitnesz értékeket
	int totalFitness = 0;
	for (Chromosome chrom : population) {
	    totalFitness += chrom.getFitnessScore();
	}

	// 2. lépés: kiválasztunk egy pontot a skálán
	float selectedPoint = totalFitness * random.nextFloat();

	// 3. lépés: megkeressük, hogy melyik egyed fitnesz intervallumába
	// találtunk bele
	int totalFitnessSoFar = 0;
	for (Iterator<Chromosome> it = population.iterator(); it.hasNext();) {
	    Chromosome chrom = it.next();
	    totalFitnessSoFar += chrom.getFitnessScore();
	    if (totalFitnessSoFar >= selectedPoint) {
		// megtaláltuk
		it.remove();
		return chrom;
	    }
	}

	return null;
    }
}
