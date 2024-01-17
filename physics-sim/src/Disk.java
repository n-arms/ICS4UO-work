import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

public class Disk extends Particle {
    private final double radius;

    public Disk(Vector2D position, Vector2D velocity, double mass, int identifier, double elasticity) {
        super(position, identifier, mass, elasticity);
        this.velocity = velocity;
        radius = Math.sqrt(mass / Math.PI);
    }

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

    @Override
    public void render(Canvas c, Function<Vector2D, Vector2D> vectorTransform, Function<Double, Double> scalarTransform) {
        Vector2D canvasPos = vectorTransform.apply(position);
        int canvasRadius = scalarTransform.apply(radius).intValue();
        c.drawCircle((int) canvasPos.getX(), (int) canvasPos.getY(), canvasRadius, color);
    }

    @Override
    public double distance(Vector2D point) {
        return point.add(position.scale(-1)).magnitude() - radius;
    }

    @Override
    public Vector2D normal(Vector2D point) {
        return point.sub(position).normalize();
    }

    @Override
    public void writeToCSV(Writer writer) throws IOException {
        super.writeToCSV(writer);
        writer.write(",disk");
    }
}
