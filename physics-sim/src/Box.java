import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

public class Box extends Particle {
    private final double radius;

    /**
     * Construct a new box with the given position, velocity, mass, identifier, elasticity, and radius.
     * @param position the position of the box
     * @param velocity the velocity of the box
     * @param mass the mass of the box
     * @param identifier the box's identifier
     * @param elasticity the elasticity of the box
     */
    public Box(Vector2D position, Vector2D velocity, double mass, int identifier, double elasticity) {
        super(position, identifier, mass, elasticity);
        this.velocity = velocity;
        radius = Math.sqrt(mass) / 2;
    }

    /**
     * Tells the collision manager what regions of the simulation the box is in.
     * This allows the collision manager to only check for collisions between particles in the same region.
     * @param collisions the collision manager
     */
    @Override
    public void collide(CollisionManager collisions) {
        int left = collisions.toGridFloor(position.getX() - radius);
        int right = collisions.toGridCeil(position.getX() + radius);
        int top = collisions.toGridCeil(position.getY() + radius);
        int bottom = collisions.toGridFloor(position.getY() - radius);

        for (int col = left; col <= right; col++) {
            for (int row = bottom; row <= top; row++) {
                collisions.addParticle(row, col, this);
            }
        }
    }

    /**
     * Render the box to a canvas.
     * @param canvas the canvas to render to
     * @param vectorToCanvas a function that transforms in-world vector quantities to on-canvas vector quantities
     * @param scalarToCanvas a function that transforms in-world scalar quantities to on-canvas scalar quantities
     */
    @Override
    public void render(Canvas canvas, Function<Vector2D, Vector2D> vectorToCanvas, Function<Double, Double> scalarToCanvas) {
        Vector2D canvasPos = vectorToCanvas.apply(position);
        int canvasRadius = scalarToCanvas.apply(radius).intValue();
        canvas.drawRectangle((int) canvasPos.getX() - canvasRadius, (int) canvasPos.getY() - canvasRadius, 2 * canvasRadius, 2 * canvasRadius, color);
    }

    /**
     * Compute the signed distance field from a point to the box.
     * Return a non-positive number if the point is inside the box, or the distance to the closest edge in in-world units if the point is outside the box.
     * @param point the point to measure distance from
     * @return the signed distance field
     */
    @Override
    public double distance(Vector2D point) {
        Vector2D relativePoint = point.sub(position).abs();
        Vector2D radiusVector = new Vector2D(radius, radius);

        return relativePoint.sub(radiusVector).max(new Vector2D()).magnitude();
    }

    /**
     * Compute the normal vector to the box at a point.
     * The normal vector at a given point inside the box is the fastest direction to move out of the box.
     * @param point the point to calculate the normal from
     * @return the normal vector
     */
    @Override
    public Vector2D normal(Vector2D point) {
        Vector2D relativePoint = point.sub(position);
        if (Math.abs(relativePoint.getX()) > Math.abs(relativePoint.getY())) {
            if (relativePoint.getX() > 0) {
                return new Vector2D(1, 0);
            } else {
                return new Vector2D(-1, 0);
            }
        } else {
            if (relativePoint.getY() > 0) {
                return new Vector2D(0, 1);
            } else {
                return new Vector2D(0, -1);
            }
        }
    }

    /**
     * Record the box to a CSV save file.
     * @param writer the output to write the file contents to
     * @throws IOException if writing fails
     */
    @Override
    public void writeToCSV(Writer writer) throws IOException {
        super.writeToCSV(writer);
        writer.write(",box");
    }
}
