import java.awt.*;
import java.util.function.Function;

public class Disk extends Particle {
    private final double radius;
    private final Color color;
    public Disk(Vector2 position, double mass, Color color) {
        this.position = position;
        this.mass = mass;
        radius = Math.sqrt(mass);
        this.color = color;
    }

    public Disk(Vector2 position, Vector2 velocity, double mass, Color color) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        radius = Math.sqrt(mass);
        this.color = color;

    }

    public Disk(Vector2 position, Vector2 velocity, double mass) {
        this.position = position;
        this.velocity = velocity;
        this.mass = mass;
        radius = Math.sqrt(mass);
        this.color = Color.BLUE;

    }

    @Override
    public void collide(SegmentedWorld world) {
        int left = world.toGrid(position.getX() - radius);
        int right = world.toGrid(position.getX() + radius);
        int top = world.toGrid(position.getY() + radius);
        int bottom = world.toGrid(position.getY() - radius);

        for (int col = left; col <= right; col++) {
            for (int row = bottom; row <= top; row++) {
                Function<Vector2, Vector2> trans = position -> position.scale(Loop.pubCanv.getCanvasSize() / 11.0).pairwiseMul(new Vector2(1, -1)).add(new Vector2(0, Loop.pubCanv.getCanvasSize()));
                Vector2 toDraw = trans.apply(new Vector2(col, row));
                Loop.pubCanv.drawCircle((int) toDraw.getX(), (int) toDraw.getY(), 20, Color.PINK);
                world.addParticle(row, col, this);
            }
        }
    }

    @Override
    public void render(Canvas c, Function<Vector2, Vector2> canvasTransform) {
        Vector2 canvasPos = canvasTransform.apply(position);
        int canvasRadius = (int) canvasTransform.apply(new Vector2(radius, 0)).getX();
        c.drawCircle((int) canvasPos.getX(), (int) canvasPos.getY(), canvasRadius, color);
    }

    @Override
    public double distance(Vector2 point) {
        return point.add(position.scale(-1)).magnitude() - radius;
    }
}
