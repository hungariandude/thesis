package textanalyzer.gui;

import textanalyzer.logic.algorithm.Chromosome;

import java.awt.BorderLayout;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Egy kromoszómát megjelenítő sor (panel).
 * 
 * @author Istvánfi Zsolt
 */
public class ChromosomeRow extends JPanel {

    private static final long serialVersionUID = 4804365096380267792L;

    private static DecimalFormat df = new DecimalFormat("#.####");
    static {
	df.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * A paraméterként megkapott kromoszóma alapján létrehoz egy új példányt.
     */
    public ChromosomeRow(Chromosome chrom) {
	super();
	setLayout(new BorderLayout());

	JLabel nameLabel = new JLabel("Kromoszóma " + chrom.getUniqueID());
	JLabel fitnessLabel = new JLabel("Fitnesz: "
		+ df.format(chrom.getFitnessScore()));

	JPanel northPanel = new JPanel();
	northPanel.setLayout(new BorderLayout());
	northPanel.add(nameLabel, BorderLayout.NORTH);
	northPanel.add(fitnessLabel, BorderLayout.SOUTH);

	add(northPanel, BorderLayout.NORTH);
    }
}
