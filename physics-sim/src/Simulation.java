import java.util.ArrayList;

public class Simulation {
    private final ArrayList<Particle> particles;
    private final double size;

    public Simulation(ArrayList<Particle> particles, double size) {
        this.particles = particles;
        this.size = size;
    }

    public void update(double dt, Canvas c) {
        gravity();
        for (Particle p : particles) {
            p.update(dt);
            p.render(c, position -> position.scale(c.getCanvasSize() / size));
        }
    }

    private void gravity() {
        for (Particle p : particles) {
            Vector2 gravity = new Vector2(0, 9.8 * p.mass);
            p.applyForce(gravity);
        }
    }
}
