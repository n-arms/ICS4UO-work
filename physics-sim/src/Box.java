import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

public class Box extends Particle {
    private final double radius;

    public Box(Vector2 position, Vector2 velocity, double mass, int identifier, double elasticity) {
        super(position, identifier, mass, elasticity);
        this.velocity = velocity;
        radius = Math.sqrt(mass) / 2;
    }

    @Override
    public void collide(SegmentedWorld world) {
        int left = world.toGridFloor(position.getX() - radius);
        int right = world.toGridCeil(position.getX() + radius);
        int top = world.toGridCeil(position.getY() + radius);
        int bottom = world.toGridFloor(position.getY() - radius);

        for (int col = left; col <= right; col++) {
            for (int row = bottom; row <= top; row++) {
                world.addParticle(row, col, this);
            }
        }
    }

    @Override
    public void render(Canvas c, Function<Vector2, Vector2> vectorTransform, Function<Double, Double> scalarTransform) {
        Vector2 canvasPos = vectorTransform.apply(position);
        int canvasRadius = scalarTransform.apply(radius).intValue();
        c.drawRectangle((int) canvasPos.getX() - canvasRadius, (int) canvasPos.getY() - canvasRadius, 2 * canvasRadius, 2 * canvasRadius, color);
    }

    @Override
    public double distance(Vector2 point) {
        Vector2 relativePoint = point.sub(position).abs();
        Vector2 radiusVector = new Vector2(radius, radius);

        return relativePoint.sub(radiusVector).max(new Vector2()).magnitude();
    }

    @Override
    public Vector2 normal(Vector2 point) {
        Vector2 relativePoint = point.sub(position);
        if (Math.abs(relativePoint.getX()) > Math.abs(relativePoint.getY())) {
            if (relativePoint.getX() > 0) {
                return new Vector2(1, 0);
            } else {
                return new Vector2(-1, 0);
            }
        } else {
            if (relativePoint.getY() > 0) {
                return new Vector2(0, 1);
            } else {
                return new Vector2(0, -1);
            }
        }
    }

    @Override
    public void writeToCSV(Writer writer) throws IOException {
        super.writeToCSV(writer);
        writer.write(",box");
    }
}
