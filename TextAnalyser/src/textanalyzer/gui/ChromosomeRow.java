package textanalyzer.gui;

import textanalyzer.logic.algorithm.Chromosome;
import textanalyzer.logic.algorithm.Gene;
import textanalyzer.util.CharCollator;
import textanalyzer.util.CharacterUtils;

import java.awt.BorderLayout;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Egy kromoszómát megjelenítő sor (panel).
 * 
 * @author Istvánfi Zsolt
 */
public class ChromosomeRow extends JPanel {

    private class CharacterLabel extends JTextField {
	private static final long serialVersionUID = 5165720073338826413L;

	CharacterLabel(char ch) {
	    setEditable(false);
	    setBackground(null);
	    setHorizontalAlignment(JTextField.CENTER);

	    setBorder(DEFAULT_BORDER);
	    setFont(GAPanel.HEADER_FONT);
	    setText(CharacterUtils.escapeChar(ch));
	}
    }

    /**
     * Magyar betűk szerinti rendezéshez.
     */
    private final static CharCollator huCollator = new CharCollator(new Locale(
	    "hu", "HU"));

    private static final long serialVersionUID = 4804365096380267792L;
    private static final DecimalFormat DOUBLE_FORMAT;

    private static final Border DEFAULT_BORDER;

    private Chromosome chrom;

    static {
	DOUBLE_FORMAT = new DecimalFormat("#.0000");
	DOUBLE_FORMAT.setRoundingMode(RoundingMode.HALF_UP);

	// Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	// Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	// DEFAULT_BORDER = BorderFactory.createCompoundBorder(raisedbevel,
	// loweredbevel)
	DEFAULT_BORDER = new EmptyBorder(3, 3, 3, 3);
    }

    /**
     * A paraméterként megkapott kromoszóma alapján létrehoz egy új példányt.
     */
    public ChromosomeRow(Chromosome chrom, int rowNum) {
	super();
	this.chrom = chrom;
	setLayout(new BorderLayout());
	setBorder(DEFAULT_BORDER);

	JLabel rankLabel = new JLabel("Rang: " + rowNum + '.');
	JLabel fitnessLabel = new JLabel("Fitnesz: "
		+ DOUBLE_FORMAT.format(this.chrom.getFitnessScore()));

	JPanel northPanel = new JPanel();
	northPanel.setLayout(new BorderLayout());
	northPanel.add(rankLabel, BorderLayout.NORTH);
	northPanel.add(fitnessLabel, BorderLayout.SOUTH);

	add(northPanel, BorderLayout.NORTH);
	add(createGenesPanel(), BorderLayout.CENTER);
    }

    private JPanel createGenesPanel() {
	JPanel genesPanel = new JPanel();
	genesPanel.setLayout(new BoxLayout(genesPanel, BoxLayout.X_AXIS));

	TreeMap<Character, Gene> treeMap = new TreeMap<>(huCollator);
	treeMap.putAll(chrom.geneMap());

	for (Entry<Character, Gene> entry : treeMap.entrySet()) {
	    JPanel genePanel = new JPanel();
	    genePanel.setLayout(new BoxLayout(genePanel, BoxLayout.Y_AXIS));

	    CharacterLabel charLabel = new CharacterLabel(entry.getKey());
	    GeneCanvas geneCanvas = new GeneCanvas(entry.getValue());

	    charLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	    genePanel.add(charLabel);
	    genePanel.add(geneCanvas);

	    genesPanel.add(genePanel);
	}

	return genesPanel;
    }
}
