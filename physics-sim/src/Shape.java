import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Shape {
    /**
     * Draw the shape to a texture.
     * @param texture the texture to draw to
     */
    abstract void draw(BufferedImage texture);

    /**
     * Erase the shape from a texture by filling it with the background color.
     * @param texture the texture to erase from
     * @param background the background color
     */
    abstract void undraw(BufferedImage texture, Color background);

    /**
     * Sets the color of a pixel on a given texture, ignoring errors if an out-of-bounds pixel is colored.
     * @param texture the texture to draw to
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param color the color of the pixel
     */
    protected static void setPixel(BufferedImage texture, int x, int y, Color color) {
        boolean inXBounds = x >= 0 && x < texture.getWidth();
        boolean inYBounds = y >= 0 && y < texture.getHeight();

        if (inXBounds && inYBounds) {
            texture.setRGB(x, y, color.getRGB());
        }
    }
}
