import java.awt.*;
import java.awt.image.BufferedImage;

public class Rectangle extends Shape {
    private final Point min;
    private final Point max;
    private final Color color;
    private final Color stroke;

    /**
     * Construct a rectangle given its smallest corner, dimensions, and color.
     * @param start the corner of the rectangle with the smallest dimensions in on-canvas units
     * @param width the width of the rectangle in on-canvas units
     * @param height the height of the rectangle in on-canvas units
     * @param color the fill and stroke color of the rectangle
     */
    public Rectangle(Point start, int width, int height, Color color) {
        this.min = start;
        this.max = new Point(start.x + width, start.y + height);
        this.color = color;
        this.stroke = color;
    }

    /**
     * Construct a rectangle given its smallest corner, dimensions, fill, and stroke colors.
     * @param start the corner of the rectangle with the smallest dimensions in on-canvas units
     * @param width the width of the rectangle in on-canvas units
     * @param height the height of the rectangle in on-canvas units
     * @param color the stroke color of the rectangle
     * @param stroke the stroke color of the rectangle
     */
    public Rectangle(Point start, int width, int height, Color color, Color stroke) {
        this.min = start;
        this.max = new Point(start.x + width, start.y + height);
        this.color = color;
        this.stroke = stroke;
    }

    /**
     * Draw the rectangle to a texture.
     * @param texture the texture to draw to
     */
    @Override
    void draw(BufferedImage texture) {
        for (int x = min.x; x <= max.x; x++) {
            for (int y = min.y; y <= max.y; y++) {
                if (x == min.x || x == max.x || y == min.y || y == max.y) {
                    setPixel(texture, x, y, stroke);
                } else {
                    setPixel(texture, x, y, color);
                }
            }
        }
    }

    /**
     * Erase the rectangle from a texture by filling it with the background color.
     * @param texture the texture to erase from
     * @param background the background color
     */
    @Override
    void undraw(BufferedImage texture, Color background) {
        for (int x = min.x; x <= max.x; x++) {
            for (int y = min.y; y <= max.y; y++) {
                setPixel(texture, x, y, background);
            }
        }
    }
}
