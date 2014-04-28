package textanalyzer.gui;

import textanalyzer.logic.Engine;
import textanalyzer.util.ResourceLoader;

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
 * Az alkalmaz�s f�ablaka, ami a program ind�t�sakor azonnal megjelenik.
 * 
 * @author Istv�nfi Zsolt
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = -9021902498585692548L;

    private final ContentPanel contentPanel;
    private final StatusBar statusBar;
    private final JFileChooser fileChooser;

    public MainFrame() throws HeadlessException {
	super("Sz�vegelemz�");

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
	contentPanel = new ContentPanel();
	add(contentPanel, BorderLayout.CENTER);
	statusBar = new StatusBar(20);
	statusBar.setText("K�sz.");
	add(statusBar, BorderLayout.SOUTH);
    }

    public void addFiles(File[] files) {
	Engine.addFiles(files);
	contentPanel.setFileNames(Engine.getFileNames());
    }

    /**
     * L�trehozza az ablak men�sor�t.
     * 
     * @return <code>JMenuBar</code>
     */
    private JMenuBar createMenuBar() {
	JMenuBar menuBar = new JMenuBar();

	JMenu fileMenu = new JMenu("F�jl");
	menuBar.add(fileMenu);
	fileMenu.add(new JMenuItem(new AbstractAction("Be�ll�t�sok") {
	    private static final long serialVersionUID = -2355160966436405427L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		new SettingsDialog().show();
	    }
	}));
	fileMenu.add(new JMenuItem(new AbstractAction("Kil�p�s") {
	    private static final long serialVersionUID = -2355160966436405427L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		MainFrame.this.dispatchEvent(new WindowEvent(MainFrame.this,
			WindowEvent.WINDOW_CLOSING));
	    }
	}));

	JMenu aboutMenu = new JMenu("S�g�");
	menuBar.add(aboutMenu);
	aboutMenu.add(new JMenuItem(new AbstractAction("N�vjegy") {
	    private static final long serialVersionUID = -3219128113154876190L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		String body = "K�sz�tette: Istv�nfi Zsolt\n\nELTE, 2014";
		JOptionPane.showMessageDialog(MainFrame.this, body, "N�vjegy",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}));

	return menuBar;
    }

    /**
     * L�trehozza az alkalmaz�s eszk�zt�r�t.
     * 
     * @return <code>JToolBar</code>
     */
    private JToolBar createToolBar() {
	JToolBar toolBar = new JToolBar();
	toolBar.setFloatable(false);
	toolBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

	JButton openButton = new JButton(new ImageIcon(
		ResourceLoader.loadImageFromResource("open_icon.png")));
	openButton.setToolTipText("Sz�veges f�jl(ok) megnyit�sa");
	openButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int returnInt = fileChooser.showOpenDialog(rootPane);
		if (returnInt == JFileChooser.APPROVE_OPTION) {
		    addFiles(fileChooser.getSelectedFiles());
		}
	    }
	});

	toolBar.add(openButton);

	return toolBar;
    }
}
