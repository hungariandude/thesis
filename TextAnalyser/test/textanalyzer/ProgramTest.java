package textanalyzer;

import textanalyzer.logic.Parameters;
import textanalyzer.util.ResourceUtils;

import java.net.URL;

public class ProgramTest {

    public static void main(String[] args) {
	URL textFileURL = ResourceUtils.getResourceAsURL("alkotmany.txt");
	Program.main(new String[] { textFileURL.getPath() });
	Parameters.debugMode = true;
    }

}
