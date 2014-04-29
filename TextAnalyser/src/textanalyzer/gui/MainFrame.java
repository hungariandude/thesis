package textanalyzer.gui;

import textanalyzer.logic.Engine;
import textanalyzer.util.ResourceUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

/**
 * Az alkalmazás fõablaka, ami a program indításakor azonnal megjelenik.
 * 
 * @author Istvánfi Zsolt
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = -9021902498585692548L;

    private final ContentPanel contentPanel;
    private final StatusBar statusBar;
    private final JFileChooser fileChooser;

    private JButton openButton, resetButton, startButton, pauseButton,
	    stopButton, fullStopButton;

    public MainFrame() throws HeadlessException {
	super("Szövegelemzõ");

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLayout(new BorderLayout());

	Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	final int windowWidth = (int) (screenDim.width * 0.75f);
	final int windowHeight = (int) (screenDim.height * 0.75f);
	setSize(windowWidth, windowHeight);
	setMinimumSize(new Dimension(440, 300));

	// int posX = screenDim.width / 2 - windowWidth / 2;
	// int posY = screenDim.height / 2 - windowHeight / 2;
	// setLocation(posX, posY);
	setLocationRelativeTo(null);

	fileChooser = new JFileChooser();
	fileChooser.setMultiSelectionEnabled(true);

	setJMenuBar(createMenuBar());

	add(createToolBar(), BorderLayout.NORTH);
	contentPanel = new ContentPanel(this);
	add(contentPanel, BorderLayout.CENTER);
	statusBar = new StatusBar(20);
	statusBar.setText("Kész.");
	add(statusBar, BorderLayout.SOUTH);
    }

    public void addFiles(File[] files) {
	Engine.addFiles(files);
	contentPanel.setFileNames(Engine.getFileNames());
	resetButton.setEnabled(true);
    }

    /**
     * Létrehozza az ablak menüsorát.
     * 
     * @return <code>JMenuBar</code>
     */
    private JMenuBar createMenuBar() {
	JMenuBar menuBar = new JMenuBar();

	JMenu fileMenu = new JMenu("Fájl");
	menuBar.add(fileMenu);
	fileMenu.add(new JMenuItem(new AbstractAction("Beállítások") {
	    private static final long serialVersionUID = -2355160966436405427L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		new SettingsDialog().show();
	    }
	}));
	fileMenu.add(new JMenuItem(new AbstractAction("Kilépés") {
	    private static final long serialVersionUID = -2355160966436405427L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		MainFrame.this.dispatchEvent(new WindowEvent(MainFrame.this,
			WindowEvent.WINDOW_CLOSING));
	    }
	}));

	JMenu aboutMenu = new JMenu("Súgó");
	menuBar.add(aboutMenu);
	aboutMenu.add(new JMenuItem(new AbstractAction("Névjegy") {
	    private static final long serialVersionUID = -3219128113154876190L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		String body = "Készítette: Istvánfi Zsolt\n\nELTE, 2014";
		JOptionPane.showMessageDialog(MainFrame.this, body, "Névjegy",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}));

	return menuBar;
    }

    /**
     * Létrehozza az alkalmazás eszköztárát.
     * 
     * @return <code>JToolBar</code>
     */
    private JToolBar createToolBar() {
	JToolBar toolBar = new JToolBar();
	toolBar.setFloatable(false);
	toolBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	openButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("open.png")));
	openButton.setToolTipText("Szöveges fájl(ok) megnyitása");

	resetButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("population.png")));
	resetButton.setToolTipText("Új populáció generálása");
	resetButton.setEnabled(false);

	// okButton = new JButton(new ImageIcon(
	// ResourceLoader.loadImageFromResource("ok.png")));
	// okButton.setToolTipText("A fájlkiválasztás befejezése");
	// okButton.setEnabled(false);

	startButton = new JButton(new ImageIcon(
		ResourceUtils.loadImageFromResource("start.png")));
	startButton.setToolTipText("Indítás/folytatás");
	startButton.setEnabled(false);

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

	openButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int returnInt = fileChooser.showOpenDialog(rootPane);
		if (returnInt == JFileChooser.APPROVE_OPTION) {
		    addFiles(fileChooser.getSelectedFiles());
		}
	    }
	});
	// okButton.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// okButton.setEnabled(false);
	// openButton.setEnabled(false);
	// }
	// });
	resetButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		openButton.setEnabled(false);
		resetButton.setEnabled(false);

		startButton.setEnabled(true);
		fullStopButton.setEnabled(true);

		contentPanel.getFileNamesList().setEnabled(false);
	    }
	});
	startButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		openButton.setEnabled(false);
		resetButton.setEnabled(false);

		startButton.setEnabled(false);
		pauseButton.setEnabled(true);
		stopButton.setEnabled(true);
	    }
	});
	pauseButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		startButton.setEnabled(true);
		pauseButton.setEnabled(false);
	    }
	});
	stopButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		stopButton.setEnabled(false);
		pauseButton.setEnabled(false);
		startButton.setEnabled(true);
	    }
	});
	fullStopButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		fullStopButton.setEnabled(false);

		startButton.setEnabled(false);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);

		openButton.setEnabled(true);
		resetButton.setEnabled(true);

		contentPanel.getFileNamesList().setEnabled(true);
	    }
	});

	toolBar.add(openButton);
	toolBar.add(resetButton);
	toolBar.add(startButton);
	toolBar.add(pauseButton);
	toolBar.add(stopButton);
	toolBar.add(fullStopButton);

	return toolBar;
    }

    public void removeFilesAtIndices(int[] indices) {
	Engine.removeFilesAtIndices(indices);
	if (Engine.fileList().isEmpty()) {
	    resetButton.setEnabled(false);
	}
    }
}
