import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Texture {
    private BufferedImage image;
    private int width;
    private int height;

    public Texture(String path) {
        loadFromFile(path);
    }

    public void loadFromFile(String path) {
        try {
            image = ImageIO.read(getClass().getResource(path));
            width = image.getWidth();
            height = image.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}