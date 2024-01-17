import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;
public class Canvas extends JPanel {
    private static final Color BACKGROUND = Color.BLACK;
    private final int size;
    private final BufferedImage texture;
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<Shape> oldShapes = new ArrayList<Shape>();

    /**
     * Construct a new square canvas with the given size in pixels.
     * @param size
     */
    public Canvas(int size) {
        this.size = size;
        texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        ImageIcon icon = new ImageIcon(texture);
        add(new JLabel(icon));
    }

    /**
     * Resolve all new draw calls to the canvas, and erase all the old images drawn to it.
     */
    public void update() {
        for (Shape s : oldShapes) {
            s.undraw(texture, BACKGROUND);
        }
        oldShapes.clear();
        for (Shape s : shapes) {
            s.draw(texture);
            oldShapes.add(s);
        }
        shapes.clear();
    }

    /**
     * Draw a circle to the canvas.
     * @param x the x position of the circle's centre in on-canvas units
     * @param y the y position of the circle's centre in on-canvas units
     * @param radius the radius of the circle in on-canvas units
     * @param color the color of the circle
     */
    public void drawCircle(int x, int y, int radius, Color color) {
        Circle circle = new Circle(new Point(x, y), radius, color);
        shapes.add(circle);
    }

    /**
     * Draw a rectangle to the canvas.
     * @param x the x position of the bottom left of the rectangle in on-canvas units
     * @param y the y position of the bottom left of the rectangle in on-canvas units
     * @param width the width of the rectangle in on-canvas units
     * @param height the height of the rectangle in on-canvas units
     * @param color the fill and stroke color of the particle
     */
    public void drawRectangle(int x, int y, int width, int height, Color color) {
        Rectangle rect = new Rectangle(new Point(x, y), width, height, color);
        shapes.add(rect);
    }

    /**
     * Draw a rectangle to the canvas.
     * @param x the x position of the bottom left of the rectangle in on-canvas units
     * @param y the y position of the bottom left of the rectangle in on-canvas units
     * @param width the width of the rectangle in on-canvas units
     * @param height the height of the rectangle in on-canvas units
     * @param color the fill color of the particle
     * @param stroke the stroke color of the particle
     */
    public void drawRectangle(int x, int y, int width, int height, Color color, Color stroke) {
        Rectangle rect = new Rectangle(new Point(x, y), width, height, color, stroke);
        shapes.add(rect);
    }

    /**
     * Get the canvas size.
     * @return the canvas size
     */
    public int getCanvasSize() {
        return size;
    }
}
