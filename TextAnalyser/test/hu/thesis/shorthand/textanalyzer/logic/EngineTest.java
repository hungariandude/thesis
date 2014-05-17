package hu.thesis.shorthand.textanalyzer.logic;

import hu.thesis.shorthand.common.CharMappingSaveData;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Chromosome;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.GeneticAlgorithm;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Population;
import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class EngineTest {

    private Chromosome chrom;
    private File file;

    @After
    public void clean() {
	if (this.file.exists()) {
	    this.file.delete();
	}
    }

    @Test
    public void exportChromosomeTest() {
	Engine.exportChromosome(chrom, file);

	try (FileInputStream fis = new FileInputStream(file)) {
	    try (ObjectInputStream is = new ObjectInputStream(fis)) {
		try {
		    CharMappingSaveData[] saveData = (CharMappingSaveData[]) is
			    .readObject();

		    Assert.assertEquals(saveData.length, 99);

		    for (CharMappingSaveData data : saveData) {
			Assert.assertNotEquals(data, null);
		    }

		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
	    }
	} catch (IOException ex) {
	    ex.printStackTrace();
	}

    }

    @Before
    public void init() {
	String text = ResourceUtils.loadStringFromResource(
		"egri_csillagok.txt", "UTF-8");
	GeneticAlgorithm ga = new GeneticAlgorithm(text, 2);
	Population pop = ga.getPopulation();
	this.chrom = pop.get(0);
	this.file = new File("test.ser");
    }
}
