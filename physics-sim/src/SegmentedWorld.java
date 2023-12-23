import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

public class SegmentedWorld {
    private final double size;
    private final int gridsPerSide = 10;
    private final ArrayList<Particle>[][] grid;
    public SegmentedWorld(double size) {
        this.size = size;
        grid = new ArrayList[gridsPerSide][gridsPerSide];

        for (var row : grid) {
            for (int i = 0; i < row.length; i++) {
                row[i] = new ArrayList<>();
            }
        }
    }
    public void addParticle(int gridRow, int gridCol, Particle particle) {
        if (inBounds(gridRow) && inBounds(gridCol)) {
            grid[gridRow][gridCol].add(particle);
        } else {
            System.out.printf("Trying to add particle at row %s and col %s%n", gridRow, gridCol);
        }
    }
    public boolean inBounds(int coord) {
        return 0 <= coord && coord < gridsPerSide;
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
        double gridSize = size / gridsPerSide;
        double x = col * gridSize + Math.random() * gridSize;
        double y = row * gridSize + Math.random() * gridSize;

        return new Vector2(x, y);
    }

    record Collision(int first, int second, Vector2 point) {}

    private Optional<Collision> testRandomCollision(int rowNum, int colNum) {
        var pool = grid[rowNum][colNum];
        Vector2 point = randomPoint(rowNum, colNum);

        Function<Vector2, Vector2> trans = position -> position.scale(Loop.pubCanv.getCanvasSize() / size).pairwiseMul(new Vector2(1, -1)).add(new Vector2(0, Loop.pubCanv.getCanvasSize()));

        var toDraw = trans.apply(point);

        for (int first = 0; first < pool.size(); first++) {
            for (int second = 0; second < first; second++) {
                Particle a = pool.get(first);
                Particle b = pool.get(second);

                //System.out.printf("Checking two particles with distances %s and %s%n", a.distance(point), b.distance(point));

                if (a.distance(point) <= 0 && b.distance(point) <= 0) {
                    System.out.println("collision");
                    a.collisionForce(point);
                    b.collisionForce(point);
                    Loop.pubCanv.drawCircle((int) toDraw.getX(), (int) toDraw.getY(), 10, Color.RED);
                    return Optional.of(new Collision(first, second, point));
                }
            }
        }

        return Optional.empty();
    }
    public void update() {
        drawGrid();

        for (int rowNum = 0; rowNum < grid.length; rowNum++) {
            var row = grid[rowNum];
            for (int colNum = 0; colNum < row.length; colNum++) {
                var pool = row[colNum];

                if (pool.isEmpty()) {
                    continue;
                }

                Loop.fillGrid(rowNum, colNum, Color.GREEN);

                if (pool.size() == 1) {
                    continue;
                }


                System.out.println("testing for big boom");

                for (int i = 0; i < 1000; i++) {
                    testRandomCollision(rowNum, colNum);
                }
            }
        }

    }
    private void drawGrid() {
        for (int row = 0; row <= grid.length; row++) {
            for (int col = 0; col <= grid[0].length; col++) {
                Loop.highlightGrid(row, col, Color.YELLOW);
            }
        }
    }
}
