package hu.thesis.shorthand.textanalyzer;

import hu.thesis.shorthand.textanalyzer.Program;
import hu.thesis.shorthand.textanalyzer.logic.Parameters;
import hu.thesis.shorthand.textanalyzer.util.ResourceUtils;

import java.net.URL;

public class ProgramTest {

    public static void main(String[] args) {
	URL textFileURL = ResourceUtils.getResourceAsURL("alkotmany.txt");
	Program.main(new String[] { textFileURL.getPath() });
	Parameters.debugMode = true;
    }

}
