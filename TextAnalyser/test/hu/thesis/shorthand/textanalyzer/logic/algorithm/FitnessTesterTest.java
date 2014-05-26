package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Map.Entry;

public class FitnessTesterTest {

    FitnessTester fitnessTester;
    Chromosome chrom;

    @Before
    public void init() {
	fitnessTester = new FitnessTester(ResourceUtils.loadStringFromResource(
		"egri_csillagok.txt", "UTF-8"));
	chrom = new Chromosome(fitnessTester.characterSet(), 3);
    }

    @Test
    public void test() {
	double fitnessScore = 0.0;

	Map<Character, Gene> geneMap = chrom.getGeneMap();
	for (Entry<Character, Gene> geneEntry : geneMap.entrySet()) {
	    Character ch = geneEntry.getKey();
	    Gene gene = geneEntry.getValue();

	    // 1. lépés: értékeljük a gén hosszát
	    double lengthScore = fitnessTester.scoreLength(ch, gene);
	    Assert.assertEquals(lengthScore > 0 && lengthScore <= 1, true);

	    // 2. lépés: értékeljük a gén méretét
	    double sizeScore = fitnessTester.scoreDrawingSize(gene);
	    Assert.assertEquals(sizeScore >= 0 && sizeScore <= 1, true);

	    // 3. lépés: értékeljük a gén hasonlóságát a karakterek szerinti
	    // hasonlóságokhoz (pl. u-ú-ü-ű)
	    double similarityScore = fitnessTester.scoreSimilarity(ch, gene,
		    chrom.getGeneMap());
	    Assert.assertEquals(similarityScore >= 0 && similarityScore <= 1,
		    true);

	    fitnessScore += (lengthScore + sizeScore + similarityScore) / 3;
	}
	// a fentiek 3/10-ed arányban számítódnak bele a végső pontszámba
	fitnessScore = fitnessScore / geneMap.size() * 30;

	// végigíratjuk a bemeneti szövegünket a kromoszómával. Az értékelés
	// szempontja az, hogy az írás során hányszor futunk le a rajzolási
	// képernyőről
	double writingTestScore = fitnessTester.scoreWritingTest(chrom);
	Assert.assertEquals(writingTestScore >= 0 && writingTestScore <= 1,
		true);

	// ez pedig 7/10-ed arányban számítódik bele a fitness pontszámba
	fitnessScore += writingTestScore * 70;

	// a karakterek egyediségi értékével hatványozzuk a végeredményt, ezzel
	// büntetve a megegyező géneket tartalmazó kromoszómákat
	double uniquenessScore = fitnessTester.scoreGeneUniqueness(chrom
		.genes());
	Assert.assertEquals(uniquenessScore >= 0 && writingTestScore <= 1, true);

	fitnessScore = Math.pow(fitnessScore, uniquenessScore);

	System.out.println(fitnessScore);
	Assert.assertEquals(fitnessScore >= 0 && writingTestScore <= 100, true);
    }

}
