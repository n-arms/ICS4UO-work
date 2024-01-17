import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

public abstract class Particle {
    // m
    protected Vector2D position;
    // m/s
    protected Vector2D velocity = new Vector2D(0, 0);
    protected Vector2D acceleration = new Vector2D(0, 0);
    protected Color color = Color.WHITE;
    protected int identifier;
    protected double mass;
    protected double elasticity;

    public Particle(Vector2D position, int identifier, double mass, double elasticity) {
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

        acceleration = new Vector2D(0, 0);
    }
    public Vector2D getPosition() {
        return position;
    }
    public Vector2D getVelocity() {
        return velocity;
    }
    public void setPosition(Vector2D position) {
        this.position = position;
    }
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }
    public double getMass() {
        return mass;
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
    public void applyForce(Vector2D force) {
        acceleration.addEquals(force.scale(1 / mass));
    }
    public void collisionForce(Vector2D pointOfCollision, double size) {
        if (distance(pointOfCollision) > 0) {
            System.out.println("Tried to resolve collisionless collision");
            return;
        }

        var normal = normal(pointOfCollision);
        var direction = pointOfCollision.sub(position).dot(velocity) > 0 ? 1 : -1;
        Vector2D reflection = velocity.sub(normal.scale(2).scale(normal.dot(velocity))).scale(elasticity).scale(direction);
        velocity = reflection;

        moveFromPoint(pointOfCollision, size);
    }
    public void moveFromPoint(Vector2D point, double size) {
        Vector2D immediateVelocity = normal(point).scale(-1);
        boolean goingRight = immediateVelocity.getX() > 0;
        boolean goingUp = immediateVelocity.getY() > 0;
        if (immediateVelocity.magnitude() > 0.00001) {
            while (distance(point) <= 0) {
                Vector2D newPosition = position.add(immediateVelocity.scale(0.001));
                if (newPosition.getX() < 0 && !goingRight) {
                    throw new RuntimeException("Clipped left");
                } else if (newPosition.getX() > size && goingRight) {
                    throw new RuntimeException("Clipped right");
                } else if (newPosition.getY() < 0 && !goingUp) {
                    throw new RuntimeException("Clipped down");
                } else if (newPosition.getY() > size && goingUp) {
                    throw new RuntimeException("Clipped up");
                } else {
                    position = newPosition;
                }
            }
        } else {
            throw new RuntimeException("Immediate velocity was too small to resolve");
        }
    }
    public abstract void collide(CollisionManager collisions);
    public abstract void render(Canvas canvas, Function<Vector2D, Vector2D> vectorToCanvas, Function<Double, Double> scalarToCanvas);
    public abstract double distance(Vector2D point);
    public abstract Vector2D normal(Vector2D point);
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
}
