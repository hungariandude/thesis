package textanalyzer.logic;

import textanalyzer.logic.algorithm.GeneticAlgorithm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * A logika mûködését vezérlõ statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class Engine {
    private static ArrayList<File> fileList = new ArrayList<>();
    private static TreeSet<File> fileSet = new TreeSet<>();

    private static GeneticAlgorithm ga;

    private static Thread thread;
    private static boolean paused = false;
    private static boolean running = false;

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static void addFiles(File[] files) {
	fileSet.addAll(Arrays.asList(files));
	fileList.clear();
	fileList.addAll(fileSet);
    }

    private static void doAlgorithmStep() {
	if (!running || paused) {
	    return;
	}
	ga.evolvePopulation();
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

    public static void pauseAlgorithm() {
	if (!running || paused) {
	    return;
	}
	paused = true;
    }

    public static void resumeAlgorithm() {
	if (!running || !paused) {
	    return;
	}
	paused = false;
	thread.notify();
    }

    public static void startAlgorithm(final int stepsToDo) {
	if (running) {
	    return;
	}
	running = true;

	thread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (running) {
		    if (stepsToDo >= 0) {
			for (int i = 1; i <= stepsToDo; ++i) {
			    doAlgorithmStep();
			}
			running = false;
		    } else {
			doAlgorithmStep();
		    }

		    if (Settings.sleepTime > 0) {
			try {
			    Thread.sleep(Settings.sleepTime);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    }

		    if (paused) {
			try {
			    this.wait();
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	});
	thread.start();
    }

    public static void stopAlgorithm() {
	if (!running) {
	    return;
	}
	running = false;
	if (paused) {
	    paused = false;
	    thread.notify();
	}
    }

    private Engine() {
	// statikus osztály
    }
}
