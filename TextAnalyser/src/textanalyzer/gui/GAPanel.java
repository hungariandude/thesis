package textanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

/**
 * A genetikus algoritmus állapotát megjelenítő panel.
 * 
 * @author Istvánfi Zsolt
 */
public class GAPanel extends JPanel {

    class GenerationNumberLabel extends JLabel {
	private static final long serialVersionUID = 8789472591281770364L;

	private static final String placeholder = "Generáció: %d.";

	GenerationNumberLabel() {
	    super(String.format(placeholder, 0));

	    setFont(new Font("Arial", Font.BOLD, 20));
	    setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void setGenerationNumber(int count) {
	    setText(String.format(placeholder, count));
	}
    }

    private static final long serialVersionUID = 7283428682414885355L;

    private final GenerationNumberLabel generationLabel = new GenerationNumberLabel();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final JPanel populationPanel;

    public GAPanel() {
	setLayout(new BorderLayout());
	setBorder(new BevelBorder(BevelBorder.LOWERED));

	generationLabel.setEnabled(false);
	add(generationLabel, BorderLayout.NORTH);

	populationPanel = createPopulationPanel();
	JScrollPane scrollPane = new JScrollPane(populationPanel);
	tabbedPane.addTab("Populáció", scrollPane);
	tabbedPane.setFocusable(false);

	add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPopulationPanel() {
	JPanel populationPanel = new JPanel();
	return populationPanel;
    }

    public GenerationNumberLabel getGenerationLabel() {
	return generationLabel;
    }

    public void reset() {
	generationLabel.setGenerationNumber(0);
    }

}
