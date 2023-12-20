import java.util.function.Function;

public class Disk extends Particle {
    private final double radius;
    public Disk(Vector2 position, double mass) {
        this.position = position;
        this.mass = mass;
        radius = Math.sqrt(mass);
    }

    @Override
    public void collide(SegmentedWorld world) {
        int left = world.toGrid(position.getX() - radius);
        int right = world.toGrid(position.getX() + radius);
        int top = world.toGrid(position.getY() + radius);
        int bottom = world.toGrid(position.getY() - radius);

        for (int col = left; col <= right; col++) {
            for (int row = bottom; row <= top; row++) {
                world.addParticle(row, col, this);
            }
        }
    }

    @Override
    public void render(Canvas c, Function<Vector2, Vector2> canvasTransform) {
        Vector2 canvasPos = canvasTransform.apply(position);
        System.out.printf("Real position %s was transformed to canvas position %s%n", position, canvasPos);
        c.drawCircle((int) canvasPos.getX(), (int) canvasPos.getY(), 50);
    }

    @Override
    public double distance(Vector2 point) {
        return point.add(position.scale(-1)).magnitude() - radius;
    }
}
