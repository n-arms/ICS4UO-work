import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Function;

public class Disk extends Particle {
    private final double radius;

    public Disk(Vector2 position, Vector2 velocity, double mass, int identifier, double elasticity) {
        super(position, identifier, mass, elasticity);
        this.velocity = velocity;
        radius = Math.sqrt(mass / Math.PI);
    }

    @Override
    public void collide(SegmentedWorld world) {
        int left = world.toGridFloor(position.getX() - radius);
        int right = world.toGridCeil(position.getX() + radius);
        int top = world.toGridCeil(position.getY() + radius);
        int bottom = world.toGridFloor(position.getY() - radius);

        for (int col = left; col <= right; col++) {
            for (int row = bottom; row <= top; row++) {
//                Vector2 toDraw = trans.apply(new Vector2(col, row));
//                Loop.pubCanv.drawCircle((int) toDraw.getX(), (int) toDraw.getY(), 20, Color.PINK);
                world.addParticle(row, col, this);
            }
        }
    }

    @Override
    public void render(Canvas c, Function<Vector2, Vector2> vectorTransform, Function<Double, Double> scalarTransform) {
        Vector2 canvasPos = vectorTransform.apply(position);
        int canvasRadius = scalarTransform.apply(radius).intValue();
        c.drawCircle((int) canvasPos.getX(), (int) canvasPos.getY(), canvasRadius, color);
    }

    @Override
    public double distance(Vector2 point) {
        return point.add(position.scale(-1)).magnitude() - radius;
    }

    @Override
    public Vector2 normal(Vector2 point) {
        return point.sub(position).normalize();
    }

    @Override
    public void writeToCSV(Writer writer) throws IOException {
        super.writeToCSV(writer);
        writer.write(",disk");
    }
}
