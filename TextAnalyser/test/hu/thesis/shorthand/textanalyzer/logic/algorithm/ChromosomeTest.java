package hu.thesis.shorthand.textanalyzer.logic.algorithm;

import hu.thesis.shorthand.textanalyzer.util.RandomUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

public class ChromosomeTest extends FitnessTesterTest {

    Chromosome other;

    @Override
    @Before
    public void init() {
	super.init();
	other = new Chromosome(fitnessTester.characterSet(), 3);
    }

    @Override
    @Test
    public void test() {
	// kromoszómaméretek ellenőrzése
	if (chrom.geneMap.size() != other.geneMap.size()) {
	    throw new IllegalArgumentException("Chromosome sizes do not match.");
	}

	// elvégezzük a keresztezést
	// Választunk egy pontot, amin kettéosztjuk a génállományt (a ponton
	// lévő elem a második részhez tartozik). A pont nem lehet a legelső
	// elem, mert akkor csak szimpla génállománycsere történne az utódokban.
	int swappingIndex = RandomUtils.random.nextInt(chrom.getGeneMap()
		.size() - 1) + 1;
	int index = 1;
	Character charAtIndex = null;
	for (Iterator<Character> it = chrom.geneMap.navigableKeySet()
		.iterator(); index <= swappingIndex && it.hasNext();) {
	    charAtIndex = it.next();
	}
	SortedMap<Character, Gene> headMapFrom1 = chrom.geneMap
		.headMap(charAtIndex);
	SortedMap<Character, Gene> tailMapFrom2 = other.geneMap
		.tailMap(charAtIndex);

	Map<Character, Gene> child1Map = new HashMap<>(headMapFrom1);
	child1Map.putAll(tailMapFrom2);
	Map<Character, Gene> child2Map = new HashMap<>(tailMapFrom2);
	child2Map.putAll(headMapFrom1);

	Chromosome child1 = new Chromosome(child1Map);
	Chromosome child2 = new Chromosome(child2Map);

	Assert.assertEquals(child1.geneMap.navigableKeySet(),
		chrom.geneMap.navigableKeySet());
	Assert.assertEquals(child2.geneMap.navigableKeySet(),
		other.geneMap.navigableKeySet());

	Assert.assertNotEquals(child1.geneMap, chrom.geneMap);
	Assert.assertNotEquals(child2.geneMap, other.geneMap);
    }

}
