package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.ProgramTest;
import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneticAlgorithmTest {

    GeneticAlgorithm ga;

    @Before
    public void init() {
	StringBuilder sb = new StringBuilder();
	for (String file : ProgramTest.files) {
	    sb.append(ResourceUtils.loadStringFromResource(file, "UTF-8"));
	}
	ga = new GeneticAlgorithm(sb.toString(), 128);
    }

    @SuppressWarnings("null")
    @Test
    public void test() {
	Population pop = null;
	int steps = 10000;
	for (int i = 1; i <= steps; ++i) {
	    pop = ga.evolvePopulation();

	    System.out.println(i + ". gen max fitness: "
		    + pop.first().getFitnessScore());
	}

	Assert.assertEquals(pop.getGenerationNumber(), steps);
    }

}
