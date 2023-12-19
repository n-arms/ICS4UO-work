import java.util.function.Function;

public class Disk extends Particle {
    public Disk(Vector2 position, double mass) {
        this.position = position;
        this.mass = mass;
    }

    @Override
    public void collide(SegmentedWorld world) {

    }

    @Override
    public void render(Canvas c, Function<Vector2, Vector2> canvasTransform) {
        Vector2 canvasPos = canvasTransform.apply(position);
        c.drawCircle((int) canvasPos.getX(), (int) canvasPos.getY(), 50);
    }
}
