package textanalyzer.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * A resource-ok betöltését segítõ singleton osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class ResourceLoader {

    private static final String DEFAULT_RESOURCES_LOCATION = "textanalyzer/resources/";

    private static ResourceLoader instance;

    private static String resourcesLocation;

    public static ResourceLoader getInstance() {
	if (instance == null) {
	    instance = new ResourceLoader();
	}
	resourcesLocation = DEFAULT_RESOURCES_LOCATION;

	return instance;
    }

    public static ResourceLoader getInstance(String resourcesRootLocation) {
	if (instance == null) {
	    instance = new ResourceLoader();
	}
	resourcesLocation = resourcesRootLocation;

	return instance;
    }

    private ResourceLoader() {
	// singleton
    }

    public BufferedImage loadImageFromResource(String imageName) {
	BufferedImage image = null;
	try {
	    image = ImageIO.read(ClassLoader
		    .getSystemResourceAsStream(resourcesLocation + imageName));
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return image;
    }

    public String loadStringFromResource(String textFileName, String encoding) {
	String str = null;
	try (InputStream is = ClassLoader
		.getSystemResourceAsStream(resourcesLocation + textFileName)) {
	    try (Scanner sc = new Scanner(is, encoding)) {
		sc.useDelimiter("\\A");
		str = sc.hasNext() ? sc.next() : "";
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return str;
    }
}
