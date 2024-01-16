import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
import java.util.function.Function;

public abstract class Particle {
    // m
    protected Vector2 position;
    // m/s
    protected Vector2 velocity = new Vector2(0, 0);
    protected Vector2 acceleration = new Vector2(0, 0);
    protected Color color = Color.WHITE;
    protected int identifier;
    protected double mass;
    protected double elasticity;

    public Particle(Vector2 position, int identifier, double mass, double elasticity) {
        this.position = position;
        this.identifier = identifier;
        this.mass = mass;
        this.elasticity = elasticity;
    }

    // dt is in seconds
    public void update(double dt) {
        for (int i = 0; i < 1000; i++) {
            velocity.addEquals(acceleration.scale(dt / 1000));
            position.addEquals(velocity.scale(dt / 1000));
        }

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
    public void repulsionForce(Vector2 point) {
        if (distance(point) >= 0) {
            return;
        }
        Vector2 displacement = position.sub(point);
        double length = Math.min(3, 0.002 / Math.pow(displacement.magnitude(), 2));
        Vector2 normal = displacement.normalize();
        applyForce(normal.scale(length));
    }
    public void collisionForce(Vector2 pointOfCollision) {
        if (distance(pointOfCollision) > 0) {
            return;
        }

        var normal = position.add(pointOfCollision.scale(-1)).normalize();
        Vector2 reflection = velocity.sub(normal.scale(2).scale(normal.dot(velocity))).scale(elasticity);
        velocity = reflection;

        moveFromPoint(pointOfCollision);
    }
    public void moveFromPoint(Vector2 point) {
        if (velocity.magnitude() > 0.00001) {
            while (distance(point) < 0) {
                position.addEquals(velocity.scale(0.001));
            }
        }
    }
    public abstract void collide(SegmentedWorld world);
    public abstract void render(Canvas c, Function<Vector2, Vector2> vectorTransform, Function<Double, Double> scalarTransform);
    public abstract double distance(Vector2 point);

    @Override
    public String toString() {
        return "Particle{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }

    public void writeToCSV(Writer writer) throws IOException {
        writer.write(String.format("%f,%f,%f,%f,%d,%f,%f", position.getX(), position.getY(), velocity.getX(), velocity.getY(), identifier, mass, elasticity));
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getIdentifier() {
        return identifier;
    }
}
