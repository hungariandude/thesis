package textanalyzer.gui;

import textanalyzer.logic.Settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * A beállítások előugró ablaka.
 * 
 * @author Istvánfi Zsolt
 */
public class SettingsDialog {

    private JPanel panel = new JPanel();

    private JFormattedTextField populationSizeTF;
    private JFormattedTextField sleepTimeTF;

    public SettingsDialog() {
	panel.setPreferredSize(new Dimension(350, 50));

	// hack, hogy a TextField-ek kurzorpozíciója ne mindig a mező elejére
	// ugorjon
	MouseAdapter mouseAdapter = new MouseAdapter() {
	    @Override
	    public void mousePressed(final MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
		    public void run() {
			JTextField tf = (JTextField) e.getSource();
			int offset = tf.viewToModel(e.getPoint());
			tf.setCaretPosition(offset);
		    }
		});
	    }
	};

	// Populáció mérete
	populationSizeTF = new JFormattedTextField(
		NumberFormat.getIntegerInstance());
	populationSizeTF.setColumns(8);
	populationSizeTF.setValue(Settings.populationSize);
	populationSizeTF.addMouseListener(mouseAdapter);
	JPanel populationSizePanel = new JPanel(new BorderLayout());
	populationSizePanel.add(populationSizeTF, BorderLayout.EAST);
	JLabel populationLabel = new JLabel("Populáció mérete:");
	populationSizePanel.add(populationLabel, BorderLayout.WEST);

	// Szüneteltetési idő
	sleepTimeTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
	sleepTimeTF.setColumns(8);
	sleepTimeTF.setValue(Settings.sleepTime);
	sleepTimeTF.addMouseListener(mouseAdapter);
	JPanel sleepTimePanel = new JPanel(new BorderLayout());
	sleepTimePanel.add(sleepTimeTF, BorderLayout.EAST);
	JLabel sleepTimeLabel = new JLabel(
		"Szüneteltetési idő a lépések között (ms):");
	sleepTimePanel.add(sleepTimeLabel, BorderLayout.WEST);

	panel.setLayout(new BorderLayout());
	panel.add(populationSizePanel, BorderLayout.NORTH);
	panel.add(sleepTimePanel, BorderLayout.SOUTH);
    }

    public void show() {
	int result = JOptionPane.showConfirmDialog(null, panel, "Beállítások",
		JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	if (result == JOptionPane.OK_OPTION) {
	    int populationSize = ((Number) populationSizeTF.getValue())
		    .intValue();
	    long sleepTime = ((Number) sleepTimeTF.getValue()).longValue();
	    if (populationSize < 0) {
		populationSize = 0;
	    }
	    if (sleepTime < 0) {
		sleepTime = 0;
	    }

	    Settings.populationSize = populationSize;
	    Settings.sleepTime = sleepTime;
	}
    }
}
