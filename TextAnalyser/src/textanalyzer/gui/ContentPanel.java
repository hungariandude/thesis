package textanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A tartalmat megjelenítõ panel.
 * 
 * @author Istvánfi Zsolt
 */
public class ContentPanel extends JPanel {

    private static final long serialVersionUID = -4149327819915066560L;

    private TreeSet<File> fileSet = new TreeSet<>();
    private DefaultListModel<String> fileNamesModel = new DefaultListModel<>();
    private int selectedFileIndex = -1;
    private JTextArea textArea;

    public ContentPanel() {
	super();

	setLayout(new BorderLayout());

	JPanel leftPanel = createFileNamesPanel();
	JPanel rightPanel = createTextPanel();

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		leftPanel, rightPanel);

	add(splitPane, BorderLayout.CENTER);
    }

    public void addFiles(File[] files) {
	fileSet.addAll(Arrays.asList(files));
	fileNamesModel.clear();
	for (File file : fileSet) {
	    fileNamesModel.addElement(file.getName());
	}
    }

    private JPanel createFileNamesPanel() {
	JPanel fileNamesPanel = new JPanel();
	fileNamesPanel.setLayout(new BorderLayout(0, 5));

	fileNamesPanel.add(new JLabel("Kiválasztott fájlok:"),
		BorderLayout.NORTH);

	final JList<String> fileNamesList = new JList<>(fileNamesModel);
	// fileNamesList.setFixedCellWidth(200);
	fileNamesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	fileNamesList.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		int newIndex = fileNamesList.getSelectedIndex();
		if (newIndex != selectedFileIndex) {
		    selectedFileIndex = newIndex;
		    loadFile();
		}
	    }
	});

	JScrollPane scrollPane = new JScrollPane(fileNamesList);
	fileNamesPanel.add(scrollPane, BorderLayout.CENTER);

	return fileNamesPanel;
    }

    private JPanel createTextPanel() {
	JPanel textPanel = new JPanel();
	textPanel.setLayout(new BorderLayout(0, 5));

	// textPanel.add(new JLabel("A fájl tartalma:"), BorderLayout.NORTH);

	textArea = new JTextArea();
	textArea.setEditable(false);
	textArea.setLineWrap(true);
	textArea.setBackground(SystemColor.control);
	textArea.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseEntered(MouseEvent e) {
		textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
	    }
	});

	JScrollPane scrollPane = new JScrollPane(textArea);
	textPanel.add(scrollPane, BorderLayout.CENTER);

	return textPanel;
    }

    private void loadFile() {
	// File selectedFile = fileSet.
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		textArea.setText(TOOL_TIP_TEXT_KEY);
	    }
	});
    }

}
