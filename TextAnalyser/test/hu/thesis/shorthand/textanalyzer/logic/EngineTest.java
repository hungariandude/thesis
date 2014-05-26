package hu.thesis.shorthand.textanalyzer.logic;

import hu.thesis.shorthand.common.CharMappingSaveData;
import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Chromosome;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Gene;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.GeneticAlgorithm;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Population;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Segment;
import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EngineTest {

    Chromosome chrom;
    File file;

    @After
    public void clean() {
	if (this.file.exists()) {
	    this.file.delete();
	}
    }

    @Test
    public void exportChromosomeTest() {
	try {
	    Map<Character, Gene> geneMap = chrom.getGeneMap();
	    CharMappingSaveData[] exportSaveData = new CharMappingSaveData[geneMap
		    .size()];
	    int i = 0;
	    for (Entry<Character, Gene> entry : geneMap.entrySet()) {
		Character ch = entry.getKey();
		Gene gene = entry.getValue();
		List<Segment> segments = gene.getSegments();
		Form[] forms = new Form[segments.size()];
		Rotation[] rotations = new Rotation[segments.size()];
		exportSaveData[i++] = new CharMappingSaveData(ch, forms,
			rotations);
		for (int j = 0; j < segments.size(); ++j) {
		    Segment segment = segments.get(j);
		    forms[j] = segment.getForm();
		    rotations[j] = segment.getRotation();
		}
	    }

	    try (FileOutputStream fos = new FileOutputStream(file)) {
		try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
		    oos.writeObject(exportSaveData);
		}
	    }

	    try (FileInputStream fis = new FileInputStream(file)) {
		try (ObjectInputStream is = new ObjectInputStream(fis)) {
		    try {
			CharMappingSaveData[] inputSaveData = (CharMappingSaveData[]) is
				.readObject();

			Assert.assertEquals(
				Arrays.equals(exportSaveData, inputSaveData),
				true);
		    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		    }
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
	this.chrom = pop.first();
	this.file = new File("test.ser");
    }
}
