package textanalyzer.logic;

import textanalyzer.logic.algorithm.GeneticAlgorithm;
import textanalyzer.logic.algorithm.Population;
import textanalyzer.util.ArrayUtils;
import textanalyzer.util.IntValueChangeListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    public static List<File> fileList() {
	return Collections.unmodifiableList(fileList);
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
    public static void initAlgorithm() {
	StringBuilder sb = new StringBuilder();
	for (File file : fileList) {
	    try {
		byte[] bytes = Files.readAllBytes(file.toPath());
		sb.append(new String(bytes, DEFAULT_CHARSET));
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}

	ga = new GeneticAlgorithm(sb.toString(), Settings.populationSize);
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
	    IntValueChangeListener listener) {
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
	    listener.valueChange(population.getGenerationNumber());
	    ++stepsDone;

	    if (Settings.sleepTime > 0) {
		try {
		    Thread.sleep(Settings.sleepTime);
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
