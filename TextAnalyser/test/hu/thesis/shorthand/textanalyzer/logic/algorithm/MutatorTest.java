package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.util.RandomUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MutatorTest extends FitnessTesterTest {

    Mutator mutator;

    @Override
    @Before
    public void init() {
	super.init();
	mutator = new Mutator();
    }

    @Override
    @Test
    public void test() {
	for (Gene gene : chrom.genes()) {
	    // minél hosszabb a gén, annál valószínűbb, hogy mutálásra kerül
	    if (RandomUtils.random.nextFloat() <= mutator.mutationRate
		    * gene.length()) {
		Gene copy = new Gene(gene);
		mutator.mutate(gene);

		// nem szabad egyeznie a mutáció után a két génnek
		Assert.assertNotEquals(gene, copy);
	    }
	}
    }

}
