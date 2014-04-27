package textanalyzer.gui;

import textanalyzer.util.ResourceLoader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

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

    private ContentPanel mainPanel;
    private StatusBar statusBar;
    private JFileChooser fileChooser;

    public MainFrame() throws HeadlessException {
	super("Szövegelemzõ");

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLayout(new BorderLayout());

	Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	final int windowWidth = (int) (screenDim.width * 0.75f);
	final int windowHeight = (int) (screenDim.height * 0.75f);
	setSize(windowWidth, windowHeight);
	setMinimumSize(new Dimension(400, 300));

	// int posX = screenDim.width / 2 - windowWidth / 2;
	// int posY = screenDim.height / 2 - windowHeight / 2;
	// setLocation(posX, posY);
	setLocationRelativeTo(null);

	setJMenuBar(createMenuBar());

	add(createToolBar(), BorderLayout.NORTH);
	mainPanel = new ContentPanel();
	add(mainPanel, BorderLayout.CENTER);
	statusBar = new StatusBar(20);
	statusBar.setText("Kész.");
	add(statusBar, BorderLayout.SOUTH);
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

	fileChooser = new JFileChooser();
	fileChooser.setMultiSelectionEnabled(true);

	JButton openButton = new JButton(new ImageIcon(
		ResourceLoader.loadImageFromResource("open_icon.png")));
	openButton.setToolTipText("Szöveges fájl(ok) megnyitása");
	openButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int returnInt = fileChooser.showOpenDialog(rootPane);
		if (returnInt == JFileChooser.APPROVE_OPTION) {
		    mainPanel.addFiles(fileChooser.getSelectedFiles());
		}
	    }
	});

	toolBar.add(openButton);

	return toolBar;
    }
}
