package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

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
    public Population evolvePopulation() {
	// új generáció születik
	++generationCounter;
	// az új generáció populációja
	Population newGeneration = new Population(populationSize,
		generationCounter);

	for (int i = 0; i < populationSize; i += 2) {
	    // 1. lépés: két szülõ kromószóma kiválasztása
	    Chromosome parent1 = selectMember(true);
	    Chromosome parent2 = selectMember(true);

	    // 2. lépés: szülõk keresztezése
	    Chromosome[] childs = parent1.crossOverWith(parent2);
	    Chromosome child1 = childs[0];
	    Chromosome child2 = childs[1];

	    // 3. lépés: gyerekek mutálása
	    mutator.mutate(child1);
	    mutator.mutate(child2);

	    // 4. lépés: gyerekek fitnesz értékeinek számítása
	    fitnessTester.scoreFitness(child1);
	    fitnessTester.scoreFitness(child2);

	    // 5. lépés: gyerekek hozzádása az új populációhoz
	    newGeneration.add(child1);
	    newGeneration.add(child2);
	}

	// frissítjük a populációnkat
	// itt az eredeti populáció már üres, mert az elõzõ generáció már
	// "elpusztult"
	population = newGeneration;

	return newGeneration;
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
     * 
     * @param remove
     *            A kromoszóma törlése a kiválasztás után.
     */
    private Chromosome selectMember(boolean remove) {
	//
	// RULETTKERÉK ALGORITMUS
	//

	// 1. lépés: összegezzük a fitnesz értékeket
	double totalFitness = 0;
	for (Chromosome chrom : population) {
	    totalFitness += chrom.getFitnessScore();
	}

	// 2. lépés: kiválasztunk egy pontot a skálán
	double selectedPoint = totalFitness * random.nextDouble();

	// 3. lépés: megkeressük, hogy melyik egyed fitnesz intervallumába
	// találtunk bele
	double totalFitnessSoFar = 0;
	for (Iterator<Chromosome> it = population.iterator(); it.hasNext();) {
	    Chromosome chrom = it.next();
	    totalFitnessSoFar += chrom.getFitnessScore();
	    if (totalFitnessSoFar >= selectedPoint) {
		// megtaláltuk
		if (remove) {
		    it.remove();
		}
		return chrom;
	    }
	}

	return null;
    }
}
