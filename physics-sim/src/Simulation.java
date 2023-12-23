import java.util.ArrayList;

public class Simulation {
    private final ArrayList<Particle> particles;
    private final double size;
    private final SegmentedWorld world;

    public Simulation(ArrayList<Particle> particles, double size) {
        this.particles = particles;
        this.size = size;
        this.world = new SegmentedWorld(size);
    }

    public void update(double dt, Canvas c) {
        gravity();
        collisions();
        edgeCollisions();
        for (Particle p : particles) {
            p.update(dt);
            p.render(c, position -> position.scale(c.getCanvasSize() / size).pairwiseMul(new Vector2(1, -1)).add(new Vector2(0, c.getCanvasSize())));
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
