package textanalyzer.gui.ga;

import textanalyzer.logic.algorithm.Chromosome;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A genetikus algoritmus statisztikáit megjelenítő panel.
 * 
 * @author Istvánfi Zsolt
 */
public class StatPanel extends JPanel {
    private static final long serialVersionUID = 7756544306960018744L;

    private ChromosomeRow maxRow;
    private final JPanel maxPanel;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public StatPanel() {
	setLayout(new BorderLayout());

	JLabel label = new JLabel("A megtalált legjobb fitneszértékű elem:");
	label.setBorder(new EmptyBorder(3, 3, 3, 3));
	// label.setAlignmentX(JComponent.LEFT_ALIGNMENT);

	Font font = label.getFont();
	Map attributes = font.getAttributes();
	attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	label.setFont(font.deriveFont(attributes));

	maxPanel = new JPanel();
	maxPanel.setLayout(new BoxLayout(maxPanel, BoxLayout.Y_AXIS));

	JPanel labelWrapper = new JPanel(new BorderLayout());
	labelWrapper.add(label);
	maxPanel.add(labelWrapper);

	add(maxPanel, BorderLayout.NORTH);
    }

    public void checkMaxRow(Chromosome chrom) {
	if (maxRow.getChromosome().getFitnessScore() < chrom.getFitnessScore()) {
	    maxRow.setChromosome(chrom);
	}
    }

    public void reset() {

    }

    public void setMaxRow(ChromosomeRow maxRow) {
	if (this.maxRow != null) {
	    maxPanel.remove(this.maxRow);
	}
	this.maxRow = maxRow;
	maxPanel.add(this.maxRow);
    }
}
