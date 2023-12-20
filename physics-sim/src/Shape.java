import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Shape {
    abstract void draw(BufferedImage bi);
    abstract void undraw(BufferedImage bi);
    protected static void setRGB(BufferedImage bi, int x, int y, int rgb) {
        boolean inXBounds = x >= 0 && x < bi.getWidth();
        boolean inYBounds = y >= 0 && y < bi.getHeight();

        if (inXBounds && inYBounds) {
            bi.setRGB(x, y, rgb);
        }
    }
}
