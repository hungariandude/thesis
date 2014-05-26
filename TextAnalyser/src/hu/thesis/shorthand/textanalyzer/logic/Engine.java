package hu.thesis.shorthand.textanalyzer.logic;

import hu.thesis.shorthand.common.CharMappingSaveData;
import hu.thesis.shorthand.common.DrawableObject.Form;
import hu.thesis.shorthand.common.DrawableObject.Rotation;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Chromosome;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Gene;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.GeneticAlgorithm;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Population;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Segment;
import hu.thesis.shorthand.textanalyzer.util.ArrayUtils;
import hu.thesis.shorthand.textanalyzer.util.ValueChangeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 * A logika működését vezérlő statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class Engine {
    private static final ArrayList<File> fileList = new ArrayList<>();

    private static GeneticAlgorithm ga;

    private static Thread thread;
    private static boolean paused = false;
    private static boolean running = false;

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static synchronized void addFiles(File[] files) {
	TreeSet<File> fileSet = new TreeSet<>(fileList);
	fileSet.addAll(Arrays.asList(files));
	fileList.clear();
	fileList.addAll(fileSet);
    }

    /**
     * Exportálja a kromószámban található karekter-alakzat párosításokat egy
     * fájlba.
     */
    public static void exportChromosome(Chromosome chrom, File target)
	    throws IOException {
	Map<Character, Gene> geneMap = chrom.getGeneMap();
	CharMappingSaveData[] saveData = new CharMappingSaveData[geneMap.size()];
	int i = 0;
	for (Entry<Character, Gene> entry : geneMap.entrySet()) {
	    Character ch = entry.getKey();
	    Gene gene = entry.getValue();
	    List<Segment> segments = gene.getSegments();
	    Form[] forms = new Form[segments.size()];
	    Rotation[] rotations = new Rotation[segments.size()];
	    saveData[i++] = new CharMappingSaveData(ch, forms, rotations);
	    for (int j = 0; j < segments.size(); ++j) {
		Segment segment = segments.get(j);
		forms[j] = segment.getForm();
		rotations[j] = segment.getRotation();
	    }
	}

	try (FileOutputStream fos = new FileOutputStream(target)) {
	    try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
		oos.writeObject(saveData);
	    }
	}
    }

    public static List<File> fileList() {
	return fileList;
    }

    public static String[] getFileNames() {
	String[] fileNames = new String[fileList.size()];
	for (int i = 0; i < fileNames.length; ++i) {
	    fileNames[i] = fileList.get(i).getName();
	}

	return fileNames;
    }

    /**
     * SwingWorkerrel vagy új szállal kell használni!
     */
    public static Population initAlgorithm() {
	StringBuilder sb = new StringBuilder();
	for (File file : fileList) {
	    try {
		byte[] bytes = Files.readAllBytes(file.toPath());
		sb.append(new String(bytes, DEFAULT_CHARSET));
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}

	if (sb.length() == 0) {
	    throw new IllegalArgumentException("A megnyitott fájlok üresek.");
	}

	ga = new GeneticAlgorithm(sb.toString(), Parameters.populationSize);
	Population clonePopulation = new Population(ga.getPopulation());
	return clonePopulation;
    }

    public static boolean isPaused() {
	return paused;
    }

    public static boolean isRunning() {
	return running;
    }

    public static void pauseAlgorithm() {
	if (!running || paused) {
	    return;
	}
	paused = true;
    }

    public static synchronized void removeFilesAtIndices(int[] indices) {
	int[] clone = indices.clone();
	Arrays.sort(clone);
	ArrayUtils.reverse(clone);
	for (int index : clone) {
	    fileList.remove(index);
	}
    }

    public static void resumeAlgorithm() {
	if (!running || !paused) {
	    return;
	}
	synchronized (thread) {
	    paused = false;
	    thread.notify();
	}
    }

    /**
     * SwingWorkerrel vagy új szállal kell használni!
     */
    public static void startAlgorithm(final int stepsToDo,
	    ValueChangeListener<Population> listener) {
	if (running) {
	    return;
	}
	running = true;
	thread = Thread.currentThread();

	int stepsDone = 0;
	while (running) {
	    if (stepsDone == stepsToDo) {
		running = false;
		break;
	    }
	    Population population = ga.evolvePopulation();
	    Population clonePopulation = new Population(population);
	    listener.valueChange(clonePopulation);

	    ++stepsDone;

	    if (Parameters.sleepTime > 0) {
		try {
		    Thread.sleep(Parameters.sleepTime);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }

	    synchronized (thread) {
		try {
		    if (paused) {
			thread.wait();
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
	thread = null;
    }

    /**
     * Az algoritmus futásának leállítását kéri.
     */
    public static void stopAlgorithm() {
	if (!running) {
	    return;
	}
	running = false;
	if (paused) {
	    synchronized (thread) {
		paused = false;
		thread.notify();
	    }
	}
    }

    private Engine() {
	// statikus osztály
    }
}
