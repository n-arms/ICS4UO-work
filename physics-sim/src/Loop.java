import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Loop {
    private final Canvas canvas = new Canvas();
    private final int targetMillisPerFrame = 20;
    private long lastUpdate;
    private final Simulation sim;
    public Loop() {
        lastUpdate = System.currentTimeMillis();
        var particles = new ArrayList<Particle>();
        particles.add(new Disk(new Vector2(3, 7), new Vector2(5, 0), 10));
        particles.add(new Disk(new Vector2(7, 7), new Vector2(-5, 0), 10, Color.GREEN));
        sim = new Simulation(particles, 11);
    }
    private void update() {
        sim.update(targetMillisPerFrame / 1000.0, canvas);
    }
    private void loop() {
        long now = System.currentTimeMillis();
        long dt = now - lastUpdate;

        try {
            Thread.sleep(targetMillisPerFrame - dt);
        } catch (Exception e) {}

        lastUpdate = System.currentTimeMillis();
        update();

        canvas.update();
        canvas.repaint();
        EventQueue.invokeLater(this::loop);
    }
    public void start() {
        JFrame frame = new JFrame("Physics Sim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);

        EventQueue.invokeLater(this::loop);
    }
}
