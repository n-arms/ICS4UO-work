import java.util.function.Function;

public abstract class Particle {
    // m
    protected Vector2 position;
    // m/s
    protected Vector2 velocity = new Vector2(0, 0);
    protected Vector2 acceleration = new Vector2(0, 0);
    protected double mass;
    // dt is in seconds
    public void update(double dt) {
        System.out.printf("%s moving at %s%n", position, velocity);
        position.addEquals(velocity.scale(dt));
        velocity.addEquals(acceleration.scale(dt));
        acceleration = new Vector2(0, 0);
    }
    public void applyForce(Vector2 force) {
        acceleration.addEquals(force.scale(1 / mass));
    }
    public abstract void collide(SegmentedWorld world);
    public abstract void render(Canvas c, Function<Vector2, Vector2> canvasTransform);
}
