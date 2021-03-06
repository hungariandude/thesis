package hu.thesis.shorthand.textanalyzer.gui.components;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Fejlett funkciókkal rendelkező <code>JFileChooser</code>.
 * 
 * @author Istvánfi Zsolt
 */
public class AdvancedFileChooser extends JFileChooser {

    private static final long serialVersionUID = 1451184755366721003L;

    public AdvancedFileChooser() {
	super(new File("").getAbsolutePath());
    }

    @Override
    public void approveSelection() {
	if (getDialogType() == SAVE_DIALOG) {
	    File file = getSelectedFile();
	    String name = file.getName();
	    FileFilter ff = getFileFilter();
	    if (ff instanceof FileNameExtensionFilter) {
		FileNameExtensionFilter fnef = (FileNameExtensionFilter) ff;
		String[] extensions = fnef.getExtensions();
		if (extensions.length == 1) {
		    String extension = '.' + extensions[0];
		    if (!name.contains(extension)
			    || name.lastIndexOf(extension) != name.length()
				    - extension.length()) {
			file = new File(file.getPath() + extension);
			setSelectedFile(file);
		    }
		}
	    }
	    if (file.exists()) {
		int result = JOptionPane.showConfirmDialog(this, file.getPath()
			+ "\n\nA fájl már létezik. Felülírja?", "Létező fájl",
			JOptionPane.YES_NO_CANCEL_OPTION);
		switch (result) {
		case JOptionPane.YES_OPTION:
		    super.approveSelection();
		default:
		    return;
		}
	    }
	} else if (getDialogType() == OPEN_DIALOG) {
	    File file = getSelectedFile();
	    if (!file.exists()) {
		JOptionPane.showMessageDialog(this, file.getPath()
			+ "\n\nA fájl nem létezik!", "Fájl nem található",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	super.approveSelection();
    }
}
