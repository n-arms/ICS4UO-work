import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Function;

public class Loop {
    private final Canvas canvas = new Canvas();
    private final int targetMillisPerFrame = 20;
    private long lastUpdate;
    private final Simulation sim;

    public static Canvas pubCanv;
    private static Simulation pubSim;
    public Loop() {
        lastUpdate = System.currentTimeMillis();
        var particles = new ArrayList<Particle>();
        particles.add(new Disk(new Vector2(3, 7), new Vector2(5, 0), 1));
        particles.add(new Disk(new Vector2(7, 7), new Vector2(-5, 0), 1, Color.GREEN));
        //particles.add(new Disk(new Vector2(7, 7), new Vector2(-5, 0), 1, Color.GREEN));
        sim = new Simulation(particles, 11);
        pubCanv = canvas;
        pubSim = sim;
    }
    private void update() {
        sim.update(targetMillisPerFrame / 1000.0, canvas);
    }

    public static void highlightGrid(int rowNum, int colNum, Color color) {
//        int rectSize = (int) Math.abs(trans.apply(new Vector2(1, 0)).sub(trans.apply(new Vector2(0, 0))).getX());
//
//        var point = new Vector2(colNum, rowNum);
//        var toDraw = trans.apply(point);
//        Loop.pubCanv.drawRectangle((int) toDraw.getX(), (int) toDraw.getY(), rectSize, rectSize, Color.BLACK, color);

    }
    public static void fillGrid(int rowNum, int colNum, Color color) {
        double gridSize = Simulation.toCanvas(pubSim.world.getGridSize());

        var point = new Vector2(pubSim.world.fromGrid(colNum), pubSim.world.fromGrid(rowNum));
        var toDraw = Simulation.toCanvas(point);
        Loop.pubCanv.drawRectangle((int) toDraw.getX(), (int) toDraw.getY(), (int) gridSize, (int) gridSize, color);
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
