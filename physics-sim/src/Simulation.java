import java.awt.*;
import java.util.ArrayList;
import java.util.function.Function;

public class Simulation {
    private final ArrayList<Particle> particles;
    private final double size;
    public final SegmentedWorld world;
    private static Function<Vector2, Vector2> toCanvasVectorTransform;
    private static Function<Double, Double> toCanvasScalarTransform;

    public Simulation(ArrayList<Particle> particles, double size) {
        this.particles = particles;
        this.size = size;
        this.world = new SegmentedWorld(size, 20);
    }

    public static Vector2 toCanvas(Vector2 world) {
        return toCanvasVectorTransform.apply(world);
    }

    public static double toCanvas(double world) {
        return toCanvasScalarTransform.apply(world);
    }

    public void update(double dt, Canvas c) {
        toCanvasScalarTransform = scalar -> scalar * c.getCanvasSize() / size;
        toCanvasVectorTransform = vector -> vector.scale(c.getCanvasSize() / size).pairwiseMul(new Vector2(1, -1)).add(new Vector2(0, c.getCanvasSize()));
        drawGrid(c);
        //gravity();
        collisions();
        //edgeCollisions();
        for (Particle p : particles) {
            p.update(dt);
            p.render(c, toCanvasVectorTransform);
        }
    }

    private void drawGrid(Canvas c) {
        int gridSize = (int) toCanvas(size / world.getGridsPerSide());
        for (int row = 0; row <= world.getGridsPerSide(); row++) {
            for (int col = 0; col <= world.getGridsPerSide(); col++) {
                double worldX = world.fromGrid(col);
                double worldY = world.fromGrid(row);
                Vector2 canvas = toCanvas(new Vector2(worldX, worldY));
                c.drawRectangle((int) canvas.getX(), (int) canvas.getY(), gridSize, gridSize, Color.BLACK, Color.YELLOW);
            }
        }
    }

    private void gravity() {
        for (Particle p : particles) {
            Vector2 gravity = new Vector2(0, -9.8 * p.mass);
            p.applyForce(gravity);
        }
    }

    private boolean inBounds(double coord) {
        return 0 <= coord && coord < size;
    }

    private void collisions() {
        world.clear();
        for (Particle p : particles) {
            p.collide(world);
        }
        world.update();
    }

    private void edgeCollisions() {
        for (Particle p : particles) {
            double x = p.getPosition().getX();
            double y = p.getPosition().getY();
            if (!inBounds(x)) {
                p.setVelocity(p.getVelocity().scaleX(-1));
            }
            if (!inBounds(y)) {
                p.setVelocity(p.getVelocity().scaleY(-1));
            }
        }
    }
}
