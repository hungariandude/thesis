package textanalyzer.test;

import org.junit.Before;
import org.junit.Test;

import textanalyzer.logic.algorithm.Chromosome;
import textanalyzer.logic.algorithm.GeneticAlgorithm;
import textanalyzer.logic.algorithm.Population;
import textanalyzer.util.ResourceLoader;

import java.util.logging.Logger;

public class GeneticAlgorithmTest {

    private String text;
    private GeneticAlgorithm ga;

    private static Logger LOGGER = Logger.getLogger(GeneticAlgorithmTest.class
	    .getName());

    @Before
    public void init() {
	text = ResourceLoader.getInstance("textanalyzer/test/")
		.loadStringFromResource("alkotmany.txt", "UTF-8");
	LOGGER.info("Successfully read " + text.length() + " characters.");
	ga = new GeneticAlgorithm(text, 8);
    }

    @Test
    public void test() {
	for (int i = 0; i < 10000; ++i) {
	    Population pop = ga.evolvePopulation();

	    double maxFitness = 0;
	    for (Chromosome chrom : pop) {
		if (chrom.getFitnessScore() > maxFitness) {
		    maxFitness = chrom.getFitnessScore();
		}
	    }

	    System.out.println(maxFitness);
	}
    }

}
