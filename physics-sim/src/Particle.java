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
        position.addEquals(velocity.scale(dt));
        velocity.addEquals(acceleration.scale(dt));
        acceleration = new Vector2(0, 0);
    }
    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
    public void applyForce(Vector2 force) {
        acceleration.addEquals(force.scale(1 / mass));
    }
    public void collisionForce(Vector2 pointOfCollision) {
        System.out.println("Colliding.");
        System.out.printf("Currently at point %s.%n", position);
        System.out.printf("Current at velocity %s.%n", velocity);
        System.out.printf("The collision occurred at point %s.%n", pointOfCollision);

        var normal = position.add(pointOfCollision.scale(-1)).normalize();
        Vector2 reflection = velocity.sub(normal.scale(2).scale(normal.dot(velocity)));
        velocity = reflection;

        System.out.printf("The normal was calculated to be %s.%n", normal);
        System.out.printf("The velocity was updated to be %s.%n", velocity);


        for (int i = 0; i < 10; i++) {
            position.addEquals(velocity.scale(0.01));
        }

        //throw new RuntimeException();

    }
    public abstract void collide(SegmentedWorld world);
    public abstract void render(Canvas c, Function<Vector2, Vector2> canvasTransform);
    public abstract double distance(Vector2 point);
}
