package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class GeneticAlgorithmTest {

    private String text;
    private GeneticAlgorithm ga;

    @Before
    public void init() {
	text = ResourceUtils.loadStringFromResource("alkotmany.txt", "UTF-8");
	ga = new GeneticAlgorithm(text, 32);
    }

    @Test
    public void test() {
	for (int i = 1; i <= 1000; ++i) {
	    Population pop = ga.evolvePopulation();
	    Collections.sort(pop, Collections.reverseOrder());

	    StringBuilder sb = new StringBuilder();
	    for (Chromosome chrom : pop) {
		sb.append(Math.round(chrom.getFitnessScore())).append(',');
	    }
	    sb.delete(sb.length() - 1, sb.length());

	    System.out.println(i + ". gen: " + sb.toString());

	    // try {
	    // Thread.sleep(50);
	    // } catch (InterruptedException e) {
	    // e.printStackTrace();
	    // }
	}
    }

}
