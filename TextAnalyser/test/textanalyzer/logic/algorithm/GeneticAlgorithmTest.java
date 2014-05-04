package textanalyzer.logic.algorithm;

import org.junit.Before;
import org.junit.Test;

import textanalyzer.util.ResourceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class GeneticAlgorithmTest {

    private String text;
    private GeneticAlgorithm ga;

    private static Logger LOGGER = Logger.getLogger(GeneticAlgorithmTest.class
	    .getName());

    @Before
    public void init() {
	text = ResourceUtils.loadStringFromResource("alkotmany.txt", "UTF-8");
	LOGGER.info("Successfully read " + text.length() + " characters.");
	ga = new GeneticAlgorithm(text, 32);
    }

    @Test
    public void test() {
	for (int i = 1; i <= 100; ++i) {
	    Population pop = ga.evolvePopulation();

	    ArrayList<Double> scores = new ArrayList<>();
	    for (Chromosome chrom : pop) {
		scores.add(chrom.getFitnessScore());
	    }

	    Collections.sort(scores, Collections.reverseOrder());
	    StringBuilder sb = new StringBuilder();
	    for (double d : scores) {
		sb.append(Math.round(d)).append(',');
	    }
	    sb.delete(sb.length() - 1, sb.length());

	    System.out.println(i + ". gen: " + sb.toString());

	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

}
