import java.awt.*;
import java.awt.image.BufferedImage;

public class Rectangle extends Shape {
    Point min;
    Point max;
    Color color;
    Color stroke;

    public Rectangle(Point min, Point max, Color color) {
        this.min = min;
        this.max = max;
        this.color = color;
        this.stroke = color;
    }

    public Rectangle(Point start, int width, int height, Color color) {
        this.min = start;
        this.max = new Point(start.x + width, start.y + height);
        this.color = color;
        this.stroke = color;
    }

    public Rectangle(Point start, int width, int height, Color color, Color stroke) {
        this.min = start;
        this.max = new Point(start.x + width, start.y + height);
        this.color = color;
        this.stroke = stroke;
    }

    @Override
    void draw(BufferedImage bi) {
        for (int x = min.x; x <= max.x; x++) {
            for (int y = min.y; y <= max.y; y++) {
                if (x == min.x || x == max.x || y == min.y || y == max.y) {
                    setRGB(bi, x, y, stroke.getRGB());
                } else {
                    setRGB(bi, x, y, color.getRGB());
                }
            }
        }
    }

    @Override
    void undraw(BufferedImage bi) {
        for (int x = min.x; x <= max.x; x++) {
            for (int y = min.y; y <= max.y; y++) {
                setRGB(bi, x, y, Color.BLACK.getRGB());
            }
        }
    }
}
