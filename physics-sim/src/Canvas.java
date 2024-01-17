import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;
public class Canvas extends JPanel {
    private static final Color BACKGROUND = Color.BLACK;
    private final int size = 600;
    private final BufferedImage texture = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<Shape> oldShapes = new ArrayList<Shape>();

    public Canvas() {
        ImageIcon icon = new ImageIcon(texture);
        add(new JLabel(icon));
    }

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

    public void drawCircle(int x, int y, int radius, Color color) {
        Circle circle = new Circle(new Point(x, y), radius, color);
        shapes.add(circle);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color) {
        Rectangle rect = new Rectangle(new Point(x, y), width, height, color);
        shapes.add(rect);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color, Color stroke) {
        Rectangle rect = new Rectangle(new Point(x, y), width, height, color, stroke);
        shapes.add(rect);
    }

    public int getCanvasSize() {
        return size;
    }
}
