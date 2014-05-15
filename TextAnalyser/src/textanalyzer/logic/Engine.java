package textanalyzer.logic;

import textanalyzer.logic.algorithm.Chromosome;
import textanalyzer.logic.algorithm.Gene;
import textanalyzer.logic.algorithm.GeneticAlgorithm;
import textanalyzer.logic.algorithm.Population;
import textanalyzer.logic.algorithm.Segment;
import textanalyzer.util.ArrayUtils;
import textanalyzer.util.ValueChangeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public static boolean exportChromosome(Chromosome chrom, File target) {
	Map<Character, Gene> geneMap = chrom.getGeneMap();
	CharMappingSaveData[] saveData = new CharMappingSaveData[geneMap.size()];
	int i = 0;
	for (Entry<Character, Gene> entry : geneMap.entrySet()) {
	    Character ch = entry.getKey();
	    Gene gene = entry.getValue();
	    List<Segment> segments = gene.getSegments();
	    String[] forms = new String[segments.size()];
	    int[] rotations = new int[segments.size()];
	    saveData[i] = new CharMappingSaveData(ch, forms, rotations);
	    for (int j = 0; j < segments.size(); ++j) {
		Segment segment = segments.get(j);
		forms[j] = segment.getForm().toString();
		rotations[j] = segment.getRotation().getDegrees();
	    }
	}

	try (FileOutputStream fos = new FileOutputStream(target)) {
	    try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
		oos.writeObject(saveData);
	    } catch (IOException e) {
		throw e;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}

	return true;
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

	ga = new GeneticAlgorithm(sb.toString(), Parameters.populationSize);
	Population sortedPopulation = new Population(ga.getPopulation());
	Collections.sort(sortedPopulation, Collections.reverseOrder());
	return sortedPopulation;
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

	    Population sortedPopulation = new Population(population);
	    Collections.sort(sortedPopulation, Collections.reverseOrder());
	    listener.valueChange(sortedPopulation);

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
