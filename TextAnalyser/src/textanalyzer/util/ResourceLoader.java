package textanalyzer.util;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * A resource-ok betöltését segítõ statikus osztály.
 * 
 * @author Istvánfi Zsolt
 */
public class ResourceLoader {

    private static final String resourcesLocation = "textanalyzer/resources/";

    public static BufferedImage loadImageFromResource(String imageName) {
	BufferedImage image = null;
	try {
	    image = ImageIO.read(ClassLoader
		    .getSystemResource(resourcesLocation + imageName));
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return image;
    }

    private ResourceLoader() {
	// statikus osztály
    }
}
