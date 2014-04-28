package textanalyzer.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * A resource-ok betöltését segítõ statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class ResourceLoader {

    private static final String RESOURCES_LOCATION = "textanalyzer/resources/";

    public static BufferedImage loadImageFromResource(String imageName) {
	BufferedImage image = null;
	try {
	    image = ImageIO.read(ClassLoader
		    .getSystemResourceAsStream(RESOURCES_LOCATION + imageName));
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return image;
    }

    public static String loadStringFromResource(String textFileName,
	    String encoding) {
	String str = null;
	try (InputStream is = ClassLoader
		.getSystemResourceAsStream(RESOURCES_LOCATION + textFileName)) {
	    try (Scanner sc = new Scanner(is, encoding)) {
		sc.useDelimiter("\\A");
		str = sc.hasNext() ? sc.next() : "";
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return str;
    }

    private ResourceLoader() {
	// singleton
    }
}
