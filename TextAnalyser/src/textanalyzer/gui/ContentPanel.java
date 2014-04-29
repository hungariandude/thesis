package textanalyzer.gui;

import textanalyzer.logic.Engine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

/**
 * A tartalmat megjelen�t� panel.
 * 
 * @author Istv�nfi Zsolt
 */
public class ContentPanel extends JPanel {

    private static final long serialVersionUID = -4149327819915066560L;

    private final MainFrame mainFrame;

    private final GAPanel gaPanel = new GAPanel();
    private DefaultListModel<String> fileNamesModel = new DefaultListModel<>();
    private final JList<String> fileNamesList = new JList<>(fileNamesModel);

    public ContentPanel(MainFrame mainFrame) {
	this.mainFrame = mainFrame;

	setLayout(new BorderLayout());

	JPanel leftPanel = createFileNamesPanel();
	JPanel rightPanel = createTextPanel();

	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		leftPanel, rightPanel);

	add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createFileNamesPanel() {
	JPanel fileNamesPanel = new JPanel();
	fileNamesPanel.setLayout(new BorderLayout(0, 5));

	fileNamesPanel
		.add(new JLabel("Megnyitott f�jlok:"), BorderLayout.NORTH);

	fileNamesList
		.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	fileNamesList.addMouseMotionListener(new MouseMotionAdapter() {
	    @Override
	    public void mouseMoved(MouseEvent e) {
		int index = fileNamesList.locationToIndex(e.getPoint());
		if (index > -1) {
		    fileNamesList.setToolTipText(Engine.fileList().get(index)
			    .getPath());
		}
	    }
	});
	fileNamesList.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
		    int[] indices = fileNamesList.getSelectedIndices();
		    if (indices.length > 0) {
			for (int index : indices) {
			    fileNamesModel.remove(index);
			}
			mainFrame.removeFilesAtIndices(indices);
		    }
		}
	    }
	});

	JScrollPane scrollPane = new JScrollPane(fileNamesList);
	fileNamesPanel.add(scrollPane, BorderLayout.CENTER);

	fileNamesPanel.setMinimumSize(new Dimension(200, fileNamesPanel
		.getMinimumSize().height));

	return fileNamesPanel;
    }

    private JPanel createTextPanel() {
	JPanel textPanel = new JPanel();
	textPanel.setLayout(new BorderLayout(0, 5));

	textPanel.add(new JLabel("A genetikus algoritmus �llapota:"),
		BorderLayout.NORTH);

	gaPanel.setMinimumSize(new Dimension(200,
		gaPanel.getMinimumSize().height));
	JScrollPane scrollPane = new JScrollPane(gaPanel);
	textPanel.add(scrollPane, BorderLayout.CENTER);

	return textPanel;
    }

    public JList<String> getFileNamesList() {
	return fileNamesList;
    }

    public void setFileNames(String[] fileNames) {
	fileNamesModel.clear();
	for (String fileName : fileNames) {
	    fileNamesModel.addElement(fileName);
	}
    }

}
