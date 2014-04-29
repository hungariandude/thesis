package textanalyzer;

import textanalyzer.gui.MainFrame;

import java.io.File;
import java.util.ArrayList;

/**
 * A program bel�p�si pontja.
 * 
 * @author Istv�nfi Zsolt
 */
public class Program {

    public static void main(String[] args) {
	MainFrame mainFrame = new MainFrame();

	// ha vannak megadva parancssori argumentumok, akkor azokat f�jlel�r�si
	// utakk�nt pr�b�lja �rtelmezni az alkalmaz�s
	if (args.length > 0) {
	    ArrayList<File> files = new ArrayList<>();
	    for (String path : args) {
		File file = new File(path);
		if (file.exists() && !file.isDirectory()) {
		    files.add(file);
		} else {
		    System.err
			    .println("Nem tal�lhat� f�jl ezen az el�r�si �ton: "
				    + path);
		}
	    }
	    mainFrame.addFiles(files.toArray(new File[files.size()]));
	}

	mainFrame.setVisible(true);
    }

}
