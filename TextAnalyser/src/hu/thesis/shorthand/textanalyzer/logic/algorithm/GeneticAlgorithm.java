package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.util.RandomUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * A genetikus algoritmus megvalósítása.
 * 
 * @author Istvánfi Zsolt
 */
public class GeneticAlgorithm {

    protected Population population;
    protected final int populationSize;

    protected Mutator mutator = new Mutator();
    protected FitnessTester fitnessTester;
    /**
     * Hányadik generációnál tart az evolúció.
     */
    protected long generationCounter = 0L;

    public GeneticAlgorithm(String sourceText, int populationSize) {
	if (populationSize < 2 || populationSize % 2 != 0) {
	    throw new IllegalArgumentException(
		    "Population size must be positive and even.");
	}

	this.populationSize = populationSize;
	population = new Population(generationCounter);
	fitnessTester = new FitnessTester(sourceText);

	generateRandomPopulation();

	// if(Parameters.debugMode) {
	// LOGGER.info("Initial population:\n" + population.toString());
	// }
    }

    // private static final Logger LOGGER = Logger
    // .getLogger(GeneticAlgorithm.class.getName());

    /**
     * Az evolúció egy lépése. Új populációt hoz létre.
     */
    public Population evolvePopulation() {
	// új generáció születik
	++generationCounter;
	// az eddigi legjobb kromoszóma
	Chromosome top = population.first();
	// az új generáció populációja
	Population newGeneration = new Population(generationCounter);

	for (int i = 0; i < populationSize; i += 2) {
	    // 1. lépés: két szülő kromószóma kiválasztása
	    Chromosome parent1 = selectMember(true);
	    Chromosome parent2 = selectMember(true);

	    // 2. lépés: szülők keresztezése
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

	// Ha nem keletkezett jobb fitnesz értékű kromoszóma az eddigi
	// legnagyobbnál, akkor visszarakjuk a korábbit és eltávolítjuk az új
	// generáció legrosszabb elemét.
	Chromosome newTop = newGeneration.first();
	if (top.compareTo(newTop) > 0) {
	    newGeneration.remove(newGeneration.last());
	    newGeneration.add(top);
	}

	// frissítjük a populációnkat
	// itt az eredeti populáció már üres, mert az előző generáció már
	// "elpusztult"
	population = newGeneration;

	return newGeneration;
    }

    /**
     * Létrehoz véletlenszerű kromoszómákat a szövegben megtalált karaktereknek.
     */
    protected void generateRandomPopulation() {
	// lekérjük a fitneszkiértékelőtől a megtalált karaktereket
	Set<Character> characterSet = fitnessTester.characterSet();
	int numberOfFoundCharacters = characterSet.size();
	// a maximum génhossz a legkisebb olyan alakzatszám, amellyel
	// biztos, hogy minden megtalált karakter különbözően kirajzolható
	int maximumGeneLength = 1;
	int differentObjects = Segment.NUMBER_OF_OBJECT_DRAWING_WAYS;
	while (differentObjects < numberOfFoundCharacters) {
	    differentObjects += (int) Math.pow(
		    Segment.NUMBER_OF_OBJECT_DRAWING_WAYS, ++maximumGeneLength);
	}

	for (int i = 0; i < populationSize; ++i) {
	    Chromosome chrom = new Chromosome(characterSet, maximumGeneLength);
	    fitnessTester.scoreFitness(chrom);
	    population.add(chrom);
	}
    }

    public Population getPopulation() {
	return population;
    }

    /**
     * Szelekció. Nagyobb fitnesz érték nagyobb kiválasztási valószínűséget
     * jelent.
     * 
     * @param remove
     *            A kromoszóma törlése a kiválasztás után.
     */
    protected Chromosome selectMember(boolean remove) {
	//
	// RULETTKERÉK ALGORITMUS
	//

	// 1. lépés: összegezzük a fitnesz értékeket
	double totalFitness = 0;
	for (Chromosome chrom : population) {
	    totalFitness += chrom.getFitnessScore();
	}

	// 2. lépés: kiválasztunk egy pontot a skálán
	double selectedPoint = totalFitness * RandomUtils.random.nextDouble();

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
