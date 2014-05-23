package hu.thesis.shorthand.textanalyzer.gui;

import hu.thesis.shorthand.textanalyzer.gui.components.StatusBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Az alkalmazás főablaka, ami a program indításakor azonnal megjelenik.
 * 
 * @author Istvánfi Zsolt
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = -9021902498585692548L;

    private final ContentPanel contentPanel;

    private final StatusBar statusBar;
    private final ControlToolBar controlToolBar;

    public MainFrame() throws HeadlessException {
	super("Szövegelemző");

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

	setJMenuBar(createMenuBar());

	controlToolBar = new ControlToolBar(this);
	add(controlToolBar, BorderLayout.NORTH);

	contentPanel = new ContentPanel(this);
	add(contentPanel, BorderLayout.CENTER);

	statusBar = new StatusBar(22);
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
	aboutMenu.add(new JMenuItem(new AbstractAction("Használat") {
	    private static final long serialVersionUID = -3219128113154876190L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		String body = "A program használata:\n\n"
			+ "A program szöveges fájlokból dolgozik. A fájlok az eszköztáron\n"
			+ "található, első gomb segítségével nyithatóak meg.\n"
			+ "Miután a fájlok megnyitásra kerültek, a második gombbal\n"
			+ "inicializálhatjuk a genetikus algoritmust. Az algoritmus\n"
			+ "inicializálása után a vezérlőgombokkal indíthatjuk/folytathatjuk,\n"
			+ "szüneteltethetjük, leállíthatjuk az algoritmus futását.\n"
			+ "A fehér X-et tartalmazó piros gomb az algoritmust véglegesen leállítja,\n"
			+ "ami után már nem folytathatjuk, új algoritmust kell inicializálnunk.\n"
			+ "Amíg az algoritmus nem fut, az elkészült ábécék exportálhatók az\n"
			+ "\"Exportálás\" gombbal.\n\n"
			+ "További információért kérem tekintse át a felhasználói dokumentációt!";
		JOptionPane.showMessageDialog(MainFrame.this, body,
			"Használat", JOptionPane.INFORMATION_MESSAGE);
	    }
	}));
	aboutMenu.add(new JMenuItem(new AbstractAction("Névjegy") {
	    private static final long serialVersionUID = -3219128113154876190L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		String body = "Készítette: Istvánfi Zsolt\nE-mail: iszraai@gmail.com\n\nELTE, 2014";
		JOptionPane.showMessageDialog(MainFrame.this, body, "Névjegy",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}));

	return menuBar;
    }

    public ContentPanel getContentPanel() {
	return contentPanel;
    }

    public ControlToolBar getControlToolBar() {
	return controlToolBar;
    }

    public StatusBar getStatusBar() {
	return statusBar;
    }

}
