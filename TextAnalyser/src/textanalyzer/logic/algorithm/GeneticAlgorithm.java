package textanalyzer.logic.algorithm;

import textanalyzer.logic.DrawingObject;
import textanalyzer.logic.DrawingObject.Direction;
import textanalyzer.logic.DrawingObject.Orientation;
import textanalyzer.logic.DrawingObject.Shape;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A genetikus algoritmus megval�s�t�sa.
 * 
 * @author Istv�nfi Zsolt
 */
public class GeneticAlgorithm {

    private Map<Character, Chromosome> chromosomeMap = new HashMap<>();
    private Map<Character, Integer> fitMap = new HashMap<>();
    private Map<Character, Integer> charCountMap = new HashMap<>();
    private String sourceText;

    private Random random = new Random();
    private boolean running = true;
    private int chromosomeMaxLength;

    public GeneticAlgorithm(String sourceText) {
	this.sourceText = sourceText;

	countCharOccurrences();
	generateRandomChromosomes();
    }

    private void calculateFitnessScores() {

    }

    /**
     * Megsz�molja az egyes karakterek el�fordul�s�t a sz�vegben.
     */
    private void countCharOccurrences() {
	for (int beginIndex = 0; beginIndex < sourceText.length(); ++beginIndex) {
	    char currentChar = sourceText.charAt(beginIndex);
	    Integer value = charCountMap.get(currentChar);
	    if (value == null) {
		// m�g nem sz�moltuk meg az el�fordul�sait, teh�t most ezt
		// elv�gezz�k
		int count = 1;
		for (int i = beginIndex + 1; i < sourceText.length(); ++i) {
		    if (sourceText.charAt(i) == currentChar) {
			++count;
		    }
		}
		charCountMap.put(currentChar, count);
	    }
	}
    }

    private int fitnessFunction(Chromosome ch) {
	return 0;
    }

    private Chromosome generateRandomChromosome(int length) {
	Shape[] shapes = Shape.values();
	Orientation[] orientations = Orientation.values();
	Direction[] directions = Direction.values();

	DrawingObject[] buildingElements = new DrawingObject[length];

	for (int i = 0; i < length; ++i) {
	    Shape theShape = shapes[random.nextInt(shapes.length)];
	    Orientation theOrientation = orientations[random
		    .nextInt(orientations.length)];
	    Direction theDirection = directions[random
		    .nextInt(directions.length)];

	    Point2D startingPoint = i == 0 ? new Point()
		    : (Point2D) buildingElements[i - 1].getEndingPoint()
			    .clone();

	    buildingElements[i] = new DrawingObject(startingPoint, theShape,
		    theOrientation, theDirection);
	}

	return new Chromosome(buildingElements);
    }

    /**
     * L�trehoz v�letlenszer� kromosz�m�kat a sz�vegben megtal�lt karaktereknek.
     */
    private void generateRandomChromosomes() {
	// h�nyf�lek�ppen lehet kirajzolni egy alakzatot
	int numberOfObjectDrawingWays = Shape.values().length
		* Orientation.values().length * Direction.values().length;

	// az alap�rtelmezett kromosz�masz�m a legkisebb olyan alakzatsz�m,
	// amellyel minden megtal�lt karakter k�l�nb�z�en kirajzolhat�
	int numberOfFoundCharacters = charCountMap.size();
	int defaultChromosomeLength = 1;
	while (Math.pow(numberOfObjectDrawingWays, defaultChromosomeLength) < numberOfFoundCharacters) {
	    ++defaultChromosomeLength;
	}

	for (Character ch : charCountMap.keySet()) {
	    Chromosome value = generateRandomChromosome(defaultChromosomeLength);
	    chromosomeMap.put(ch, value);
	}
    }

    private void run() {
	while (running) {
	    // 1. l�p�s: fitnessz�rt�kek sz�m�t�sa
	    calculateFitnessScores();

	    // 2. l�p�s: k�t kromosz�ma keresztez�se

	    // 3. l�p�s:
	}
    }
}
