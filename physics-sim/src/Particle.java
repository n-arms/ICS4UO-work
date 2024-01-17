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

    /**
     * Construct a new particle with the given position, identifier, mass, and elasticity.
     * Set velocity, acceleration and color to their default values of 0, 0, and white.
     * @param position the position of the particle
     * @param identifier the particle's identifier
     * @param mass the mass of the particle
     * @param elasticity the elasticity of the particle
     */
    public Particle(Vector2D position, int identifier, double mass, double elasticity) {
        this.position = position;
        this.identifier = identifier;
        this.mass = mass;
        this.elasticity = elasticity;
    }

    /**
     * Update the particle as if time has passed.
     * @param dt the elapsed time, in seconds
     */
    public void update(double dt) {
        for (int i = 0; i < 1000; i++) {
            velocity.addEquals(acceleration.scale(dt / 1000));
            position.addEquals(velocity.scale(dt / 1000));
        }

        acceleration = new Vector2D(0, 0);
    }

    /**
     * Get the position of the particle.
     * @return the position
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Get the velocity of the particle.
     * @return the velocity
     */
    public Vector2D getVelocity() {
        return velocity;
    }

    /**
     * Set the position of the particle.
     * @param position the new position
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    /**
     * Set the velocity of the particle.
     * @param velocity the new velocity
     */
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Get the mass of the particle.
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * Set the mass of the particle.
     * @param mass the new mass
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Set the color of the particle.
     * @param color the new color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the identifier of the particle
     * @return the identifier
     */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * Apply a force to the particle for a single frame.
     * @param force the force to apply
     */
    public void applyForce(Vector2D force) {
        acceleration.addEquals(force.scale(1 / mass));
    }

    /**
     * Alter the velocity of the particle as if a collision has occurred.
     * @param pointOfCollision a point inside the particle where the collision occurred
     * @param size the size of the world in in-world units
     */
    public void collisionForce(Vector2D pointOfCollision, double size) {
        if (distance(pointOfCollision) > 0) {
            System.out.println("Tried to resolve collisionless collision");
            return;
        }

        var normal = normal(pointOfCollision);
        var direction = pointOfCollision.sub(position).dot(velocity) > 0 ? 1 : -1;
        velocity = velocity.sub(normal.scale(2).scale(normal.dot(velocity))).scale(elasticity).scale(direction);

        moveFromPoint(pointOfCollision, size);
    }

    /**
     * Move the particle along its normal so that it no longer intersects with a point.
     * @param point the point to move away from
     * @param size the size of the world in in-world units
     */
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

    /**
     * Tells the collision manager what regions of the simulation the particle is in.
     * This allows the collision manager to only check for collisions between particles in the same region.
     * @param collisions the collision manager
     */
    public abstract void collide(CollisionManager collisions);

    /**
     * Render the particle to a canvas.
     * @param canvas the canvas to render to
     * @param vectorToCanvas a function that transforms in-world vector quantities to on-canvas vector quantities
     * @param scalarToCanvas a function that transforms in-world scalar quantities to on-canvas scalar quantities
     */
    public abstract void render(Canvas canvas, Function<Vector2D, Vector2D> vectorToCanvas, Function<Double, Double> scalarToCanvas);

    /**
     * Compute the signed distance field from a point to the particle.
     * Return a non-positive number if the point is inside the particle, or the distance to the closest edge in in-world units if the point is outside the particle.
     * @param point the point to measure distance from
     * @return the signed distance field
     */
    public abstract double distance(Vector2D point);

    /**
     * Compute the normal vector to the particle at a point.
     * The normal vector at a given point inside the particle is the fastest direction to move out of the particle.
     * @param point the point to calculate the normal from
     * @return the normal vector
     */
    public abstract Vector2D normal(Vector2D point);

    /**
     * Convert the particle to a human-readable string.
     * @return the string
     */
    @Override
    public String toString() {
        return "Particle{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }

    /**
     * Record the particle to a CSV save file.
     * @param writer the output to write the file contents to
     * @throws IOException if writing fails
     */
    public void writeToCSV(Writer writer) throws IOException {
        writer.write(String.format("%f,%f,%f,%f,%d,%f,%f", position.getX(), position.getY(), velocity.getX(), velocity.getY(), identifier, mass, elasticity));
    }
}
