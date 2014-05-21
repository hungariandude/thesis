package hu.thesis.shorthand.textanalyzer.gui.ga;

import hu.thesis.shorthand.textanalyzer.logic.algorithm.Chromosome;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Population;

import java.awt.BorderLayout;
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
    private final StatPanel statPanel = new StatPanel();

    public GAPanel() {
	setLayout(new BorderLayout());
	setBorder(new BevelBorder(BevelBorder.LOWERED));

	add(generationLabel, BorderLayout.NORTH);

	populationPanel.setLayout(new BoxLayout(populationPanel,
		BoxLayout.Y_AXIS));
	JScrollPane scrollPane = new JScrollPane(populationPanel);
	tabbedPane.addTab("Populáció", scrollPane);
	scrollPane = new JScrollPane(statPanel);
	tabbedPane.addTab("Rekord", scrollPane);
	tabbedPane.setEnabledAt(1, false);

	add(tabbedPane, BorderLayout.CENTER);
	setEnabled(false);
    }

    public void disableExport() {
	for (ChromosomeRow row : rows) {
	    row.disableExport();
	}
    }

    public void enableExport() {
	for (ChromosomeRow row : rows) {
	    row.enableExport();
	}
    }

    public void reset() {
	generationLabel.setGenerationNumber(0);
	populationPanel.removeAll();
	rows.clear();
    }

    @Override
    public void setEnabled(boolean enabled) {
	super.setEnabled(enabled);

	if (enabled) {
	    this.tabbedPane.setEnabledAt(1, true);
	}

	this.generationLabel.setEnabled(enabled);
	// for (Component c : getComponents()) {
	// c.setEnabled(enabled);
	// }
    }

    /**
     * Megjeleníti a paraméterként kapott populációt a felületen.
     */
    public void setPopulation(Population population) {
	generationLabel.setGenerationNumber(population.getGenerationNumber());

	if (rows.isEmpty()) {
	    if (!population.isEmpty()) {
		ChromosomeRow maxRow = new ChromosomeRow(population.first(),
			"Generáció: " + population.getGenerationNumber());
		statPanel.setMaxRow(maxRow);
	    }
	    int i = 0;
	    int sizeMinus1 = population.size() - 1;
	    for (Chromosome chrom : population) {
		ChromosomeRow row = new ChromosomeRow(chrom, i + 1);
		rows.add(row);
		populationPanel.add(row);

		if (i != sizeMinus1) {
		    populationPanel.add(new JSeparator());
		}
		++i;
	    }
	} else {
	    if (!population.isEmpty()) {
		Chromosome chrom = population.first();
		statPanel.checkMaxRow(chrom, population.getGenerationNumber());
	    }
	    int i = 0;
	    for (Chromosome chrom : population) {
		rows.get(i++).setChromosome(chrom);
	    }
	}
    }

}
