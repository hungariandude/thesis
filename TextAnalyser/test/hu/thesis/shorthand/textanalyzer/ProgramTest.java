package hu.thesis.shorthand.textanalyzer;

import hu.thesis.shorthand.textanalyzer.logic.Parameters;
import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;

public class ProgramTest {

    public static void main(String[] args) {
	String[] files = { "arany_ember.txt", "egri_csillagok.txt",
		"ember_tragediaja.txt", "koszivu_ember_fiai.txt",
		"szent_peter_esernyoje.txt", "legy_jo_mindhalalig.txt" };
	String[] urls = new String[files.length];
	for (int i = 0; i < files.length; ++i) {
	    urls[i] = ResourceUtils.getResourceAsURL(files[i]).getPath();
	}
	Program.main(urls);
	Parameters.debugMode = true;
    }

}
