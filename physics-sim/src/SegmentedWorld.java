import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class SegmentedWorld {
    private final double size;
    private final int gridsPerSide;
    record GridPosition(int row, int col) {}
    private final HashMap<GridPosition, ArrayList<Particle>> grid;
    public SegmentedWorld(double size, int gridsPerSide) {
        this.size = size;
        this.gridsPerSide = gridsPerSide;
        grid = new HashMap<>();
    }

    public int getGridsPerSide() {
        return gridsPerSide;
    }

    public double getGridSize() {
        return size / gridsPerSide;
    }

    public void addParticle(int gridRow, int gridCol, Particle particle) {
        var position = new GridPosition(gridRow, gridCol);
        var pool = grid.computeIfAbsent(position, k -> new ArrayList<>());
        pool.add(particle);
//
//        if (inBounds(gridRow) && inBounds(gridCol)) {
//            grid[gridRow][gridCol].add(particle);
//        } else {
//            if (gridRow < 0) {
//                particle.collisionForce(new Vector2(particle.getPosition().getX(), 0));
//            } else if (gridRow >= gridsPerSide) {
//                particle.collisionForce(new Vector2(particle.getPosition().getX(), fromGrid(gridsPerSide)));
//            }
//
//            if (gridCol < 0) {
//                particle.collisionForce(new Vector2(0, particle.getPosition().getY()));
//            } else if (gridCol >= gridsPerSide) {
//                particle.collisionForce(new Vector2(fromGrid(gridsPerSide), particle.getPosition().getY()));
//            }
//            System.out.printf("Trying to add particle at row %s and col %s%n", gridRow, gridCol);
//        }

        Loop.fillGrid(gridRow, gridCol, Color.PINK);
    }
    public boolean inBounds(int coord) {
        return 0 <= coord && coord < gridsPerSide;
    }
    public void clear() {
        grid.clear();
    }

    public int toGridFloor(double coord) {
        return (int) Math.floor(coord / getGridSize());
    }

    public int toGridCeil(double coord) {
        return (int) Math.ceil(coord / getGridSize());
    }

    public double fromGrid(int coord) {
        return coord * getGridSize();
    }

    public Vector2 randomPoint(int row, int col) {
        double gridSize = getGridSize();
        double x = col * gridSize + Math.random() * gridSize;
        double y = row * gridSize + Math.random() * gridSize;

        return new Vector2(x, y);
    }

    private void testRandomCollision(Particle a, Particle b, int rowNum, int colNum) {
        ArrayList<Vector2> collisions = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Vector2 point = randomPoint(rowNum, colNum);

            //var toDraw = trans.apply(point);

            //System.out.printf("Checking two particles with distances %s and %s%n", a.distance(point), b.distance(point));

            if (a.distance(point) <= 0 && b.distance(point) <= 0) {
                System.out.println("collision");
                collisions.add(point);
                //Loop.pubCanv.drawCircle((int) toDraw.getX(), (int) toDraw.getY(), 10, Color.RED);
            }
        }

        Optional<Vector2> best = collisions.stream().min((o1, o2) -> {
            double distance1 = a.distance(o1) + b.distance(o1);
            double distance2 = a.distance(o2) + b.distance(o2);

            return Double.compare(distance1, distance2);
        });
        best.ifPresent(point -> {
            a.collisionForce(point);
            b.collisionForce(point);
        });
    }
    public void update() {
        drawGrid();

        for (var position : grid.keySet()) {
            var pool = grid.get(position);

            if (pool.isEmpty()) {
                continue;
            }

            for (Particle p : pool) {
                if (position.row < 0) {
                    p.collisionForce(new Vector2(p.getPosition().getX(), 0));
                } else if (position.row >= gridsPerSide) {
                    p.collisionForce(new Vector2(p.getPosition().getX(), fromGrid(gridsPerSide)));
                }

                if (position.col < 0) {
                    p.collisionForce(new Vector2(0, p.getPosition().getY()));
                } else if (position.col >= gridsPerSide) {
                    p.collisionForce(new Vector2(fromGrid(gridsPerSide), p.getPosition().getY()));
                }
            }

            if (pool.size() == 1) {
                continue;
            }

            for (int first = 0; first < pool.size(); first++) {
                for (int second = 0; second < first; second++) {
                    testRandomCollision(pool.get(first), pool.get(second), position.row, position.col);
                }
            }
        }
//
//        for (int rowNum = 0; rowNum < grid.length; rowNum++) {
//            var row = grid[rowNum];
//            for (int colNum = 0; colNum < row.length; colNum++) {
//                var pool = row[colNum];
//
//                if (pool.isEmpty()) {
//                    continue;
//                }
//
//                //Loop.fillGrid(rowNum, colNum, Color.GREEN);
//
//                if (pool.size() == 1) {
//                    continue;
//                }
//
//
//                System.out.println("testing for big boom");
//
//                for (int first = 0; first < pool.size(); first++) {
//                    for (int second = 0; second < first; second++) {
//                        testRandomCollision(pool.get(first), pool.get(second), rowNum, colNum);
//                    }
//                }
//            }
//        }

    }
    private void drawGrid() {
//        for (int row = 0; row <= grid.length; row++) {
//            for (int col = 0; col <= grid[0].length; col++) {
//                Loop.highlightGrid(row, col, Color.YELLOW);
//            }
//        }
    }
}
