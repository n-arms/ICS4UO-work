import java.awt.*;
import java.awt.image.BufferedImage;

public class Circle extends Shape {
    Point centre;
    int radius;

    public Circle(Point centre, int radius) {
        this.centre = centre;
        this.radius = radius;
    }

    @Override
    void draw(BufferedImage bi) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                double distance = Math.sqrt(x*x + y*y);
                int totalX = centre.x + x;
                int totalY = centre.y + y;
                if (distance <= radius) {
                    setRGB(bi, totalX, totalY, Color.BLUE.getRGB());
                }
            }
        }
    }

    @Override
    void undraw(BufferedImage bi) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                double distance = Math.sqrt(x*x + y*y);
                int totalX = centre.x + x;
                int totalY = centre.y + y;
                if (distance <= radius) {
                    setRGB(bi, totalX, totalY, Color.BLACK.getRGB());
                }
            }
        }
    }
}
