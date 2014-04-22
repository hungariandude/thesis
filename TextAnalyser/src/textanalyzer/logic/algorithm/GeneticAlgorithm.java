package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A genetikus algoritmus megvalósítása.
 * 
 * @author Istvánfi Zsolt
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
     * Kezdeti fitnesz érték számítás.
     */
    private void calculateFitnessScores() {
	for (Chromosome chrom : population) {
	    chrom.calculateFitnessScore();
	}
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
     * Létrehoz véletlenszerû kromoszómákat a szövegben megtalált karaktereknek.
     */
    private void generateRandomPopulation() {
	int numberOfFoundCharacters = charCountMap.size();
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
	    population.add(new Chromosome(charCountMap.keySet(),
		    maximumGeneLength));
	}
    }

    private void run() {
	// az új generáció populációja
	List<Chromosome> newPopulation = new ArrayList<>(populationSize);
	// éppen melyik generáció születik
	int generation = 0;

	while (running) {
	    // új generáció születik
	    ++generation;
	    newPopulation.clear();

	    for (int i = 0; i < populationSize; i += 2) {
		// 1. lépés: két kromószóma kiválasztás
		Chromosome parent1 = selectMember();
		Chromosome parent2 = selectMember();

		// 2. lépés: ezek keresztezése
		Chromosome.crossover(parent1, parent2);

		// 3. lépés: ezek mutálása
		parent1.mutate();
		parent2.mutate();

		// 4. lépés: ezek fitnesz értékeinek újraszámítása
		parent1.calculateFitnessScore();
		parent2.calculateFitnessScore();

		// 5. lépés: ezek hozzádása az új populációhoz
		newPopulation.add(parent1);
		newPopulation.add(parent2);
	    }

	    // frissítjük a populációnkat
	    population.addAll(newPopulation);
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
