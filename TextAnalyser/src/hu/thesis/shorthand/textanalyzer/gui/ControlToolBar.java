package hu.thesis.shorthand.textanalyzer.gui;

import hu.thesis.shorthand.textanalyzer.gui.components.AdvancedFileChooser;
import hu.thesis.shorthand.textanalyzer.gui.components.RadioPanel;
import hu.thesis.shorthand.textanalyzer.logic.Engine;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Population;
import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;
import hu.thesis.shorthand.textanalyzer.util.ValueChangeListener;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

/**
 * A menüsor alatt megjelenő eszköztár, ami a genetikus algoritmus vezérlését
 * lehetővé tevő gombokat tartalmazza.
 * 
 * @author Istvánfi Zsolt
 */
public class ControlToolBar extends JToolBar {
    /**
     * A genetikus algoritmust elindító SwingWorker.
     * 
     * @author Istvánfi Zsolt
     */
    class GAWorker extends SwingWorker<Void, Population> implements
	    ValueChangeListener<Population> {
	private final int stepsToDo;

	GAWorker(int stepsToDo) {
	    this.stepsToDo = stepsToDo;
	}

	@Override
	protected Void doInBackground() throws Exception {
	    Engine.startAlgorithm(stepsToDo, this);
	    return null;
	}

	@Override
	protected void done() {
	    try {
		get();
	    } catch (InterruptedException | ExecutionException e) {
		e.printStackTrace();
		String cause = e.getCause().getMessage() != null ? e.getCause()
			.getMessage() : e.getMessage();
		JOptionPane.showMessageDialog(mainFrame,
			"A genetikus algoritmus futása váratlanul befejeződött!\n\nA hiba oka: "
				+ cause, "Hiba", JOptionPane.ERROR_MESSAGE);
	    }
	    if (!stopped) {
		pauseButton.setEnabled(false);
		startButton.setEnabled(true);
		stepSelector.setEnabled(true);
		stopButton.setEnabled(false);
	    }
	    mainFrame.getContentPanel().getGaPanel().enableExport();
	    mainFrame.getStatusBar().setText(
		    "A genetikus algoritmus futása befejeződött.");
	}

	@Override
	protected void process(List<Population> chunks) {
	    // A genetikus algoritmus futása nagyon gyors is lehet, ezért a
	    // chunks lista több elemből állhat. Nekünk csak a legutóbbi kell
	    // és azt adjuk tovább a felületnek.
	    Population max = Collections.max(chunks);
	    mainFrame.getContentPanel().getGaPanel().setPopulation(max);
	}

	@Override
	public void valueChange(Population newValue) {
	    publish(newValue);
	}
    }

    private static final long serialVersionUID = 9141379367752119608L;

    private final JButton openButton, resetButton, startButton, pauseButton,
	    stopButton, fullStopButton;
    private final RadioPanel stepSelector;
    private final MainFrame mainFrame;

    private final AdvancedFileChooser fileChooser;
    private boolean stopped = true;

    private final AbstractAction openAction = new AbstractAction() {
	private static final long serialVersionUID = 7849531221976556017L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    int returnInt = fileChooser
		    .showOpenDialog(ControlToolBar.this.mainFrame);
	    if (returnInt == JFileChooser.APPROVE_OPTION) {
		addFiles(fileChooser.getSelectedFiles());
	    }
	}
    };
    private final AbstractAction resetAction = new AbstractAction() {
	private static final long serialVersionUID = 7808890696584471336L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    openButton.setEnabled(false);
	    resetButton.setEnabled(false);
	    ControlToolBar.this.mainFrame.getContentPanel()
		    .disableFileListEdit();

	    mainFrame.getStatusBar().setText(
		    "A genetikus algoritmus inicializálása...");
	    mainFrame.getContentPanel().getGaPanel().reset();

	    SwingWorker<Population, Void> worker = new SwingWorker<Population, Void>() {
		@Override
		protected Population doInBackground() throws Exception {
		    Population population = Engine.initAlgorithm();
		    return population;
		}

		@Override
		protected void done() {
		    try {
			mainFrame.getContentPanel().getGaPanel()
				.setPopulation(get());
			startButton.setEnabled(true);
			stepSelector.setEnabled(true);
			fullStopButton.setEnabled(true);
			stopped = false;
			mainFrame.getContentPanel().getGaPanel()
				.setEnabled(true);
			mainFrame
				.getStatusBar()
				.setText(
					"A genetikus algoritmus inicializálása elkészült.");
		    } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			String cause = e.getCause().getMessage() != null ? e
				.getCause().getMessage() : e.getMessage();
			JOptionPane.showMessageDialog(mainFrame,
				"A genetikus algoritmus inicializálása nem sikerült!\n\nA hiba oka: "
					+ cause, "Hiba",
				JOptionPane.ERROR_MESSAGE);
			openButton.setEnabled(true);
			resetButton.setEnabled(true);
			ControlToolBar.this.mainFrame.getContentPanel()
				.enableFileListEdit();
			mainFrame
				.getStatusBar()
				.setText(
					"A genetikus algoritmus inicializálása nem sikerült.");
		    }
		}
	    };
	    worker.execute();
	}
    };
    private final AbstractAction startAction = new AbstractAction() {
	private static final long serialVersionUID = 7808890696584471336L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    openButton.setEnabled(false);
	    resetButton.setEnabled(false);
	    startButton.setEnabled(false);
	    stepSelector.setEnabled(false);
	    mainFrame.getContentPanel().getGaPanel().disableExport();

	    mainFrame.getStatusBar().setText("A genetikus algoritmus fut...");

	    if (Engine.isPaused()) {
		Engine.resumeAlgorithm();
	    } else {
		String selectedString = stepSelector.getSelectedValue();
		int stepsToDo;
		try {
		    stepsToDo = Integer.parseInt(selectedString);
		} catch (NumberFormatException ex) {
		    stepsToDo = -1;
		}
		GAWorker worker = new GAWorker(stepsToDo);
		worker.execute();
	    }

	    pauseButton.setEnabled(true);
	    stopButton.setEnabled(true);
	}
    };
    private final AbstractAction pauseAction = new AbstractAction() {
	private static final long serialVersionUID = 7808890696584471336L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    Engine.pauseAlgorithm();
	    mainFrame.getStatusBar().setText(
		    "A genetikus algoritmus szüneteltetve.");
	    startButton.setEnabled(true);
	    pauseButton.setEnabled(false);
	    mainFrame.getContentPanel().getGaPanel().enableExport();
	}
    };
    private final AbstractAction stopAction = new AbstractAction() {
	private static final long serialVersionUID = 7808890696584471336L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    Engine.stopAlgorithm();
	    stopButton.setEnabled(false);
	    pauseButton.setEnabled(false);
	    startButton.setEnabled(true);
	    stepSelector.setEnabled(true);
	}
    };

    private final AbstractAction fullStopAction = new AbstractAction() {
	private static final long serialVersionUID = 7808890696584471336L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    Engine.stopAlgorithm();

	    stopped = true;

	    fullStopButton.setEnabled(false);
	    startButton.setEnabled(false);
	    stepSelector.setEnabled(false);
	    pauseButton.setEnabled(false);
	    stopButton.setEnabled(false);

	    openButton.setEnabled(true);
	    resetButton.setEnabled(true);

	    mainFrame.getContentPanel().getGaPanel().setEnabled(false);
	    mainFrame.getContentPanel().enableFileListEdit();
	}
    };

    public ControlToolBar(MainFrame mainFrame) {
	this.mainFrame = mainFrame;
	fileChooser = new AdvancedFileChooser();
	fileChooser.setMultiSelectionEnabled(true);

	setFloatable(false);
	setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	openButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("open.png")));
	openButton.setToolTipText("Szöveges fájl(ok) megnyitása");

	resetButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("population.png")));
	resetButton.setToolTipText("Új populáció generálása");
	resetButton.setEnabled(false);

	startButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("start.png")));
	startButton.setToolTipText("Indítás/folytatás");
	startButton.setEnabled(false);
	stepSelector = new RadioPanel(new String[] { "1", "10", "100", "1000",
		"10000", "∞" }, true);
	stepSelector.setEnabled(false);

	pauseButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("pause.png")));
	pauseButton.setToolTipText("Szünet");
	pauseButton.setEnabled(false);

	stopButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("stop.png")));
	stopButton.setToolTipText("Leállítás");
	stopButton.setEnabled(false);

	fullStopButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("stop_cross.png")));
	fullStopButton.setToolTipText("Az algoritmus befejezése");
	fullStopButton.setEnabled(false);

	openButton.addActionListener(openAction);
	resetButton.addActionListener(resetAction);
	startButton.addActionListener(startAction);
	pauseButton.addActionListener(pauseAction);
	stopButton.addActionListener(stopAction);
	fullStopButton.addActionListener(fullStopAction);

	add(openButton);
	add(resetButton);
	add(stepSelector);
	add(startButton);
	add(pauseButton);
	add(stopButton);
	add(fullStopButton);
    }

    public void addFiles(File[] files) {
	Engine.addFiles(files);
	mainFrame.getContentPanel().setFileNames(Engine.getFileNames());
	if (files.length > 0) {
	    resetButton.setEnabled(true);
	}
    }

    public void notifyFileListChanged() {
	if (Engine.fileList().isEmpty()) {
	    resetButton.setEnabled(false);
	}
    }
}
