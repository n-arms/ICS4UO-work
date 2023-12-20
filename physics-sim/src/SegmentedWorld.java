import java.util.ArrayList;

public class SegmentedWorld {
    private final double size;
    private final int gridsPerSide = 10;
    private final ArrayList<Particle>[][] grid;
    public SegmentedWorld(double size) {
        this.size = size;
        grid = new ArrayList[gridsPerSide][gridsPerSide];
    }
    public void addParticle(int gridRow, int gridCol, Particle particle) {
        grid[gridRow][gridCol].add(particle);
    }
    public void clear() {
        for (var row : grid) {
            for (var pool : row) {
                pool.clear();
            }
        }
    }

    public int toGrid(double coord) {
        return (int) (coord / size * gridsPerSide);
    }

    public Vector2 randomPoint(int row, int col) {
        double x = col * size + Math.random() * size;
        double y = row * size + Math.random() * size;

        return new Vector2(x, y);
    }

    public void update() {
        for (int rowNum = 0; rowNum < grid.length; rowNum++) {
            var row = grid[rowNum];
            for (int colNum = 0; colNum < row.length; colNum++) {
                var pool = row[colNum];

                for (int i = 0; i < 10; i++) {
                    Vector2 point = randomPoint(rowNum, colNum);
                    for (int first = 0; first < pool.size(); first++) {
                        for (int second = 0; second < first; second++) {
                            Particle a = pool.get(first);
                            Particle b = pool.get(second);

                            if (a.distance(point) <= 0 && b.distance(point) <= 0) {
                                a.collisionForce(point);
                                b.collisionForce(point);
                            }
                        }
                    }
                }
            }
        }
    }
}
