import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;
public class Canvas extends JPanel {
    private final int size = 600;
    private final BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    private final ArrayList<Shape> shapes = new ArrayList<>();
    private final ArrayList<Shape> oldShapes = new ArrayList<Shape>();

    public Canvas() {
        ImageIcon icon = new ImageIcon(bi);
        add(new JLabel(icon));
    }

    public void update() {
        for (Shape s : oldShapes) {
            s.undraw(bi);
        }
        oldShapes.clear();
        for (Shape s : shapes) {
            s.draw(bi);
            oldShapes.add(s);
        }
        shapes.clear();
    }

    public void drawCircle(int x, int y, int radius) {
        Circle circle = new Circle(new Point(x, y), radius, Color.BLUE);
        shapes.add(circle);
    }

    public void drawCircle(int x, int y, int radius, Color color) {
        Circle circle = new Circle(new Point(x, y), radius, color);
        shapes.add(circle);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void startLoop() {
        update();
        this.repaint();
        try {
            Thread.sleep(20);
        } catch (Exception e) {}
        EventQueue.invokeLater(this::startLoop);
    }

    public int getCanvasSize() {
        return size;
    }
}
