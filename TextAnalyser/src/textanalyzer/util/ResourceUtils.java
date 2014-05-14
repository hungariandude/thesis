package textanalyzer.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * A resource-okkal kapcsolatos műveletek elvégézését segítő statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public final class ResourceUtils {

    private static final String RESOURCES_LOCATION = "textanalyzer/resources/";

    public static URL getResourceAsURL(String resourceName) {
	return ClassLoader.getSystemResource(RESOURCES_LOCATION + resourceName);
    }

    public static BufferedImage loadImageFromResource(String imageName) {
	BufferedImage image = null;
	try {
	    image = ImageIO.read(ClassLoader
		    .getSystemResourceAsStream(RESOURCES_LOCATION + imageName));
	} catch (IOException e) {
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

    private ResourceUtils() {
	// singleton
    }
}
