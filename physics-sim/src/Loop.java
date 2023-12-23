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
    public Loop() {
        lastUpdate = System.currentTimeMillis();
        var particles = new ArrayList<Particle>();
        particles.add(new Disk(new Vector2(3, 7), new Vector2(5, 0), 1));
        particles.add(new Disk(new Vector2(7, 7), new Vector2(-5, 0), 1, Color.GREEN));
        sim = new Simulation(particles, 11);
        pubCanv = canvas;
    }
    private void update() {
        sim.update(targetMillisPerFrame / 1000.0, canvas);
    }

    public static void highlightGrid(int rowNum, int colNum, Color color) {
        Function<Vector2, Vector2> trans = position -> position.scale(Loop.pubCanv.getCanvasSize() / 11.0).pairwiseMul(new Vector2(1, -1)).add(new Vector2(0, Loop.pubCanv.getCanvasSize()));
        int rectSize = (int) Math.abs(trans.apply(new Vector2(1, 0)).sub(trans.apply(new Vector2(0, 0))).getX());

        var point = new Vector2(colNum, rowNum);
        var toDraw = trans.apply(point);
        Loop.pubCanv.drawRectangle((int) toDraw.getX(), (int) toDraw.getY(), rectSize, rectSize, Color.BLACK, color);

    }
    public static void fillGrid(int rowNum, int colNum, Color color) {
        Function<Vector2, Vector2> trans = position -> position.scale(Loop.pubCanv.getCanvasSize() / 11.0).pairwiseMul(new Vector2(1, -1)).add(new Vector2(0, Loop.pubCanv.getCanvasSize()));
        int rectSize = (int) Math.abs(trans.apply(new Vector2(1, 0)).sub(trans.apply(new Vector2(0, 0))).getX());

        var point = new Vector2(colNum, rowNum);
        var toDraw = trans.apply(point);
        Loop.pubCanv.drawRectangle((int) toDraw.getX(), (int) toDraw.getY(), rectSize, rectSize, color);

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
