import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.List;
import java.util.Timer;
import javax.swing.*;
public class Canvas extends JPanel {
    private final int size = 3000;
    private final BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

    public Canvas() {
        ImageIcon icon = new ImageIcon(bi);
        add(new JLabel(icon));
    }

    public void update() {
        new Circle(new Point(100, 100), 100).draw(bi);
//
//        for (int y = 0; y < size; y += 5) {
//            for (int x = 0; x < size; x++) {
//                Color color = (Math.random() > 0.5) ? Color.RED : Color.GREEN;
//                int colorValue = color.getRGB();
//                bi.setRGB(x, y, colorValue);
//                bi.setRGB(x, y + 1, colorValue);
//                bi.setRGB(x, y + 2, colorValue);
//                bi.setRGB(x, y + 3, colorValue);
//                bi.setRGB(x, y + 4, colorValue);
//            }
//        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void startLoop(JFrame frame) {
        update();
        this.repaint();
        try {
            Thread.sleep(20);
        } catch (Exception e) {}
        EventQueue.invokeLater(() -> startLoop(frame));
    }
}
