package textanalyzer.gui;

import textanalyzer.logic.algorithm.Population;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

/**
 * A genetikus algoritmus állapotát megjelenítő panel.
 * 
 * @author Istvánfi Zsolt
 */
public class GAPanel extends JPanel {

    /**
     * A generáció számát megjelenítő címke.
     */
    class GenerationNumberLabel extends JLabel {
	private static final long serialVersionUID = 8789472591281770364L;

	private static final String placeholder = "Generáció: %d.";

	GenerationNumberLabel() {
	    super(String.format(placeholder, 0));

	    setFont(HEADER_FONT);
	    setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void setGenerationNumber(long count) {
	    setText(String.format(placeholder, count));
	}
    }

    private static final long serialVersionUID = 7283428682414885355L;
    public final static Font HEADER_FONT = new Font("Arial", Font.BOLD, 20);

    private final GenerationNumberLabel generationLabel = new GenerationNumberLabel();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final ArrayList<ChromosomeRow> rows = new ArrayList<>();
    private final JPanel populationPanel = new JPanel();

    public GAPanel() {
	setLayout(new BorderLayout());
	setBorder(new BevelBorder(BevelBorder.LOWERED));

	add(generationLabel, BorderLayout.NORTH);

	populationPanel.setLayout(new BoxLayout(populationPanel,
		BoxLayout.Y_AXIS));
	JScrollPane scrollPane = new JScrollPane(populationPanel);
	tabbedPane.addTab("Populáció", scrollPane);
	tabbedPane.setFocusable(false);

	add(tabbedPane, BorderLayout.CENTER);

	setEnabled(false);
    }

    public void reset() {
	generationLabel.setGenerationNumber(0);
	populationPanel.removeAll();
	rows.clear();
    }

    @Override
    public void setEnabled(boolean enabled) {
	super.setEnabled(enabled);

	for (Component c : getComponents()) {
	    c.setEnabled(enabled);
	}
    }

    /**
     * Megjeleníti a paraméterként kapott populációt a felületen.
     */
    public void setPopulation(Population population) {
	generationLabel.setGenerationNumber(population.getGenerationNumber());

	if (rows.isEmpty()) {
	    for (int i = 0; i < population.size(); ++i) {
		ChromosomeRow row = new ChromosomeRow(population.get(i), i + 1);
		rows.add(row);
		populationPanel.add(row);
		if (i != population.size() - 1) {
		    populationPanel.add(new JSeparator());
		}
	    }
	} else {
	    for (int i = 0; i < population.size(); ++i) {
		rows.get(i).updateChromosome(population.get(i));
	    }
	}
    }

}
