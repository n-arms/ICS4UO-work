import java.awt.*;
import java.awt.image.BufferedImage;

public class Circle extends Shape {
    private final Point centre;
    private final int radius;
    private final Color color;

    /**
     * Construct a circle at a given position and with a given radius and color.
     * @param centre the centre of the circle (in on-canvas units)
     * @param radius the radius of the circle (in on-canvas units)
     * @param color the color of the circle
     */
    public Circle(Point centre, int radius, Color color) {
        this.centre = centre;
        this.radius = radius;
        this.color = color;
    }

    /**
     * Draw the circle to a texture.
     * @param texture the texture to draw to
     */
    @Override
    void draw(BufferedImage texture) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                double distance = Math.sqrt(x*x + y*y);
                int totalX = centre.x + x;
                int totalY = centre.y + y;
                if (distance <= radius) {
                    setPixel(texture, totalX, totalY, color);
                }
            }
        }
    }

    /**
     * Erase the circle from a texture by filling it with the background color.
     * @param texture the texture to erase from
     * @param background the background color
     */
    @Override
    void undraw(BufferedImage texture, Color background) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                double distance = Math.sqrt(x*x + y*y);
                int totalX = centre.x + x;
                int totalY = centre.y + y;
                if (distance <= radius) {
                    setPixel(texture, totalX, totalY, background);
                }
            }
        }
    }
}
