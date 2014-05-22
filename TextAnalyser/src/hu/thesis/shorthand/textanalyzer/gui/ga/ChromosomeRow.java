package hu.thesis.shorthand.textanalyzer.gui.ga;

import hu.thesis.shorthand.textanalyzer.gui.components.AdvancedFileChooser;
import hu.thesis.shorthand.textanalyzer.logic.Engine;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Chromosome;
import hu.thesis.shorthand.textanalyzer.logic.algorithm.Gene;
import hu.thesis.shorthand.textanalyzer.util.CharCollator;
import hu.thesis.shorthand.textanalyzer.util.CharacterUtils;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Egy kromoszómát megjelenítő sor (panel).
 * 
 * @author Istvánfi Zsolt
 */
public class ChromosomeRow extends JPanel implements ActionListener {

    private class CharacterLabel extends JTextField {
	private static final long serialVersionUID = 5165720073338826413L;

	CharacterLabel(char ch) {
	    setEditable(false);
	    setBackground(null);
	    setHorizontalAlignment(JTextField.CENTER);

	    setBorder(DEFAULT_BORDER);
	    setFont(GAPanel.HEADER_FONT);
	    setText(CharacterUtils.escapeChar(ch));
	    setToolTipText(String.format("\\u%04x", (int) ch));
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
    private static final AdvancedFileChooser fileChooser;
    static {
	fileChooser = new AdvancedFileChooser();
	fileChooser.setMultiSelectionEnabled(false);
	fileChooser.setAcceptAllFileFilterUsed(false);
	FileNameExtensionFilter filter = new FileNameExtensionFilter(
		"Java Serialized Objects [ser]", "ser");
	fileChooser.setFileFilter(filter);
    }

    private Chromosome chromosome;

    private final JLabel fitnessLabel, titleLabel;
    private final HashMap<Character, GeneCanvas> canvasMap = new HashMap<>();
    private final JButton exportButton;

    static {
	DOUBLE_FORMAT = new DecimalFormat("#.0000");
	DOUBLE_FORMAT.setRoundingMode(RoundingMode.HALF_UP);

	// Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	// Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	// DEFAULT_BORDER = BorderFactory.createCompoundBorder(raisedbevel,
	// loweredbevel)
	DEFAULT_BORDER = new EmptyBorder(3, 3, 3, 3);
    }

    public ChromosomeRow(Chromosome chrom, int rowNum) {
	this(chrom, "Rang: " + rowNum + '.');
    }

    public ChromosomeRow(Chromosome chrom, String title) {
	super();
	this.chromosome = chrom;
	setLayout(new BorderLayout());
	setBorder(DEFAULT_BORDER);

	titleLabel = new JLabel(title);
	fitnessLabel = new JLabel();
	updateFitness();

	JPanel northPanel = new JPanel();
	northPanel.setLayout(new BorderLayout());

	JPanel textPanel = new JPanel(new BorderLayout());
	textPanel.add(titleLabel, BorderLayout.NORTH);
	textPanel.add(fitnessLabel, BorderLayout.SOUTH);
	northPanel.add(textPanel, BorderLayout.WEST);

	JPanel buttonPanel = new JPanel(new BorderLayout());
	exportButton = new JButton("Exportálás");
	exportButton.addActionListener(this);
	exportButton.setMargin(new Insets(0, 0, 0, 0));
	JPanel buttonWrapper = new JPanel();
	buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.Y_AXIS));
	buttonWrapper.add(exportButton);
	buttonWrapper.setBorder(new EmptyBorder(5, 10, 0, 0));
	buttonPanel.add(buttonWrapper, BorderLayout.WEST);
	northPanel.add(buttonPanel, BorderLayout.CENTER);

	add(northPanel, BorderLayout.NORTH);
	add(createGenesPanel(), BorderLayout.CENTER);

	setMaximumSize(getPreferredSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	int returnInt = fileChooser.showSaveDialog(null);
	if (returnInt == JFileChooser.APPROVE_OPTION) {
	    final File file = fileChooser.getSelectedFile();

	    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
		@Override
		protected Void doInBackground() throws Exception {
		    Engine.exportChromosome(chromosome, file);
		    return null;
		}

		@Override
		protected void done() {
		    try {
			get();
			JOptionPane.showMessageDialog(null,
				"Az exportálás sikeresen befejeződött!",
				"Sikeres művelet",
				JOptionPane.INFORMATION_MESSAGE);
		    } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
				"Az exportálás nem sikerült!\n\nA hiba oka: "
					+ e.getMessage(), "Hiba",
				JOptionPane.ERROR_MESSAGE);
		    }
		}
	    };
	    worker.execute();
	}
    }

    private JPanel createGenesPanel() {
	JPanel genesPanel = new JPanel();
	genesPanel.setLayout(new BoxLayout(genesPanel, BoxLayout.X_AXIS));

	TreeMap<Character, Gene> treeMap = new TreeMap<>(huCollator);
	treeMap.putAll(chromosome.getGeneMap());

	for (Entry<Character, Gene> entry : treeMap.entrySet()) {
	    JPanel genePanel = new JPanel();
	    genePanel.setLayout(new BoxLayout(genePanel, BoxLayout.Y_AXIS));

	    CharacterLabel charLabel = new CharacterLabel(entry.getKey());
	    GeneCanvas geneCanvas = new GeneCanvas(entry.getValue());
	    canvasMap.put(entry.getKey(), geneCanvas);

	    charLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	    genePanel.add(charLabel);
	    genePanel.add(geneCanvas);

	    genesPanel.add(genePanel);
	}

	return genesPanel;
    }

    public void disableExport() {
	this.exportButton.setEnabled(false);
    }

    public void enableExport() {
	this.exportButton.setEnabled(true);
    }

    public Chromosome getChromosome() {
	return chromosome;
    }

    public void setChromosome(Chromosome chromosome) {
	this.chromosome = chromosome;
	updateFitness();

	for (Entry<Character, Gene> entry : this.chromosome.getGeneMap()
		.entrySet()) {
	    GeneCanvas canvas = canvasMap.get(entry.getKey());
	    if (canvas != null) {
		canvas.setGene(entry.getValue());
	    }
	}
    }

    public void setTitle(String title) {
	titleLabel.setText(title);
    }

    private void updateFitness() {
	fitnessLabel.setText("Fitnesz: "
		+ DOUBLE_FORMAT.format(this.chromosome.getFitnessScore()));
    }
}
