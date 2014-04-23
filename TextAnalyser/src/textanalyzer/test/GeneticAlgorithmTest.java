package textanalyzer.test;

import org.junit.Before;
import org.junit.Test;

import textanalyzer.logic.algorithm.GeneticAlgorithm;
import textanalyzer.util.ResourceLoader;

import java.util.logging.Logger;

public class GeneticAlgorithmTest {

    String text;
    GeneticAlgorithm ga;

    private static Logger LOGGER = Logger.getLogger(GeneticAlgorithmTest.class
	    .getName());

    @Before
    public void init() {
	text = ResourceLoader.getInstance("textanalyzer/test/")
		.loadStringFromResource("alkotmany.txt", "UTF-8");
	LOGGER.info("Successfully read " + text.length() + " characters.");
	ga = new GeneticAlgorithm(text, 10);
    }

    @Test
    public void test() {

    }

}
