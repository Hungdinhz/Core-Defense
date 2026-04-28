package td;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class AssetManager {
    private static AssetManager instance;
    private Map<String, BufferedImage> imageCache;
    private final String ASSET_DIR = "assets/";

    private AssetManager() {
        imageCache = new HashMap<>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public BufferedImage getImage(String name) {
        if (imageCache.containsKey(name)) {
            return imageCache.get(name);
        }
        
        try {
            BufferedImage img = ImageIO.read(new File(ASSET_DIR + name + ".png"));
            imageCache.put(name, img);
            return img;
        } catch (IOException e) {
            System.err.println("Could not load image: " + name);
            return null;
        }
    }
}
