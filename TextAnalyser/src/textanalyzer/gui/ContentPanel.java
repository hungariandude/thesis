package textanalyzer.gui;

import textanalyzer.logic.Engine;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * A tartalmat megjelenítõ panel.
 * 
 * @author Istvánfi Zsolt
 */
public class ContentPanel extends JPanel {

    private static final long serialVersionUID = -4149327819915066560L;

    private DefaultListModel<String> fileNamesModel = new DefaultListModel<>();
    private final GAPanel characterPanel = new GAPanel();

    public ContentPanel() {
	super();

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

	fileNamesPanel.add(new JLabel("Kiválasztott fájlok:"),
		BorderLayout.NORTH);

	final JList<String> fileNamesList = new JList<>(fileNamesModel);
	fileNamesList.setEnabled(false);
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

	JScrollPane scrollPane = new JScrollPane(fileNamesList);
	fileNamesPanel.add(scrollPane, BorderLayout.CENTER);

	fileNamesPanel.setMinimumSize(new Dimension(200, fileNamesPanel
		.getMinimumSize().height));

	return fileNamesPanel;
    }

    private JPanel createTextPanel() {
	JPanel textPanel = new JPanel();
	textPanel.setLayout(new BorderLayout(0, 5));

	textPanel.add(new JLabel("A genetikus algoritmus állapota:"),
		BorderLayout.NORTH);

	characterPanel.setMinimumSize(new Dimension(200, characterPanel
		.getMinimumSize().height));
	JScrollPane scrollPane = new JScrollPane(characterPanel);
	textPanel.add(scrollPane, BorderLayout.CENTER);

	return textPanel;
    }

    public void setFileNames(String[] fileNames) {
	fileNamesModel.clear();
	for (String fileName : fileNames) {
	    fileNamesModel.addElement(fileName);
	}
    }

}
