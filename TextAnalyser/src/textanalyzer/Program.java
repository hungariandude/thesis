package textanalyzer;

import textanalyzer.gui.MainFrame;

import java.io.File;
import java.util.ArrayList;

/**
 * A program belépési pontja.
 * 
 * @author Istvánfi Zsolt
 */
public class Program {

    public static void main(String[] args) {
	MainFrame mainFrame = new MainFrame();

	// ha vannak megadva parancssori argumentumok, akkor azokat fájlelérési
	// utakként próbálja értelmezni az alkalmazás
	if (args.length > 0) {
	    ArrayList<File> files = new ArrayList<>();
	    for (String path : args) {
		File file = new File(path);
		if (file.exists() && !file.isDirectory()) {
		    files.add(file);
		} else {
		    System.err
			    .println("Nem található fájl ezen az elérési úton: "
				    + path);
		}
	    }
	    mainFrame.getControlToolBar().addFiles(
		    files.toArray(new File[files.size()]));
	}

	mainFrame.setVisible(true);
    }

}
