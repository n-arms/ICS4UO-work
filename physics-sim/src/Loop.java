import javax.swing.*;
import java.awt.*;

public class Loop {
    private final Canvas canvas;
    private final int TARGET_MILLIS_PER_FRAME = 20;
    private final int MILLIS_PER_SECOND = 1000;
    private long lastUpdate;
    private final Simulation simulation;

    /**
     * Construct a new loop around the given simulation.
     * @param simulation the simulation to run
     */
    public Loop(Simulation simulation, int canvasSize) {
        lastUpdate = System.currentTimeMillis();
        canvas = new Canvas(canvasSize);
        this.simulation = simulation;
    }

    /**
     * Update the loop's simulation as if the target number of milliseconds per frame has elapsed.
     */
    private void update() {
        simulation.update(TARGET_MILLIS_PER_FRAME / (double) MILLIS_PER_SECOND, canvas);
    }

    /**
     * Update the simulation, render a single frame of the simulation,
     * pause an appropriate amount of time to ensure smooth animation, then asynchronously repeat.
     */
    private void loop() {
        long now = System.currentTimeMillis();
        long dt = now - lastUpdate;

        try {
            Thread.sleep(TARGET_MILLIS_PER_FRAME - dt);
        } catch (Exception e) {}

        lastUpdate = System.currentTimeMillis();
        update();

        canvas.update();
        canvas.repaint();
        EventQueue.invokeLater(this::loop);
    }

    /**
     * Starts running the loop, launching the GUI and periodically updating and rendering the simulation.
     * Start runs simulation logic asynchronously, so it should always return promptly.
     */
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
