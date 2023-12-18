import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Canvas c = new Canvas();
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Physics Sim");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(c);
            frame.setLocationByPlatform(true);
            frame.pack();
            frame.setVisible(true);

            c.startLoop(frame);
        });
    }
}
