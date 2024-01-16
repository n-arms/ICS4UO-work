
import java.util.*;
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

    public boolean testRandomCollisionIteration(Particle a, Particle b, int rowNum, int colNum) {
        for (int iter = 0; iter < 100; iter++) {
            var best = bestCollisionPoint(a, b, rowNum, colNum);

            if (best.isPresent()) {
                Vector2 point = best.get();

                a.moveFromPoint(point, size);
                b.moveFromPoint(point, size);
            } else {
                return false;
            }
        }
        return true;
    }

    private Optional<Vector2> bestCollisionPoint(Particle a, Particle b, int rowNum, int colNum) {
        ArrayList<Vector2> collisions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Vector2 point = randomPoint(rowNum, colNum);

            if (a.distance(point) <= 0 && b.distance(point) <= 0) {
                collisions.add(point);
            }
        }

        return collisions.stream().min((o1, o2) -> {
            double distance1 = a.distance(o1) + b.distance(o1) + 0.01 * Math.abs(a.distance(o1) - b.distance(o1));
            double distance2 = a.distance(o2) + b.distance(o2) + 0.01 * Math.abs(a.distance(o1) - b.distance(o1));

            return Double.compare(distance1, distance2);
        });
    }

    private record Collision(Particle a, Particle b) {
        @Override
        public int hashCode() {
            // commutative hashing
            return a.hashCode() + b.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Collision collision) {
                boolean sameOrder = this.a.equals(collision.a) && this.b.equals(collision.b);
                boolean swapped = this.b.equals(collision.a) && this.a.equals(collision.b);
                return sameOrder || swapped;
            } else {
                return false;
            }
        }
    }

    private enum Edge {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private static<K, V> void addToMultimap(Map<K, HashSet<V>> map, K key, V element) {
        var list = map.get(key);

        if (list == null) {
            map.put(key, new HashSet<>(List.of(element)));
        } else {
            list.add(element);
        }
    }

    private Optional<Vector2> findBestCollisionPointIn(Particle a, Particle b, HashSet<GridPosition> grids) {
        ArrayList<Vector2> collisions = new ArrayList<>();
        for (GridPosition grid : grids) {
            for (int i = 0; i < 100; i++) {
                Vector2 point = randomPoint(grid.row, grid.col);

                if (a.distance(point) <= 0 && b.distance(point) <= 0) {
                    collisions.add(point);
                }
            }
        }

        return collisions.stream().min((o1, o2) -> {
            double distance1 = a.distance(o1) + b.distance(o1) + 0.01 * Math.abs(a.distance(o1) - b.distance(o1));
            double distance2 = a.distance(o2) + b.distance(o2) + 0.01 * Math.abs(a.distance(o1) - b.distance(o1));

            return Double.compare(distance1, distance2);
        });
    }

    private boolean startCollision(Particle a, Particle b, HashSet<GridPosition> grids) {
        var best = findBestCollisionPointIn(a, b, grids);

        best.ifPresent((point) -> {
            a.collisionForce(point, size);
            b.collisionForce(point, size);
        });
        return best.isPresent();
    }

    public void update() {
        //drawGrid();

        HashMap<Collision, HashSet<GridPosition>> collisions = new HashMap<>();
        HashMap<Particle, HashSet<Edge>> edgeCollisions = new HashMap<>();

        for (var position : grid.keySet()) {
            var pool = grid.get(position);

            if (pool.isEmpty()) {
                continue;
            }

            for (Particle p : pool) {
                if (position.row < 0) {
                    addToMultimap(edgeCollisions, p, Edge.BOTTOM);
                } else if (position.row >= gridsPerSide) {
                    addToMultimap(edgeCollisions, p, Edge.TOP);
                }

                if (position.col < 0) {
                    addToMultimap(edgeCollisions, p, Edge.LEFT);
                } else if (position.col >= gridsPerSide) {
                    addToMultimap(edgeCollisions, p, Edge.RIGHT);
                }
            }

            if (pool.size() == 1) {
                continue;
            }

            for (int first = 0; first < pool.size(); first++) {
                for (int second = 0; second < first; second++) {
                    if (bestCollisionPoint(pool.get(first), pool.get(second), position.row, position.col).isPresent()) {
                        addToMultimap(collisions, new Collision(pool.get(first), pool.get(second)), position);
                    }
                }
            }
        }

        for (Particle p : edgeCollisions.keySet()) {
            for (Edge e : edgeCollisions.get(p)) {
                Vector2 collision = switch (e) {
                    case LEFT -> new Vector2(0, p.getPosition().getY());
                    case RIGHT -> new Vector2(fromGrid(gridsPerSide), p.getPosition().getY());
                    case TOP -> new Vector2(p.getPosition().getX(), fromGrid(gridsPerSide));
                    case BOTTOM -> new Vector2(p.getPosition().getX(), 0);
                };
                p.collisionForce(collision, size);
            }
        }

        for (Collision c : collisions.keySet()) {
            var best = findBestCollisionPointIn(c.a, c.b, collisions.get(c));
            best.ifPresent(point -> {
//                c.a.repulsionForce(point);
//                c.b.repulsionForce(point);
                System.out.printf("Starting resolution between %s and %s\n", c.a, c.b);
                c.a.collisionForce(point, size);
                c.b.collisionForce(point, size);

                for (int i = 0; i < 3; i++) {
                    System.out.printf("Resolving collision between %s and %s\n", c.a, c.b);
                    var collision = findBestCollisionPointIn(c.a, c.b, collisions.get(c));
                    System.out.printf("Found collision point %s\n", collision);
                    collision.ifPresent(innerPoint -> {
                        c.a.moveFromPoint(innerPoint, size);
                        c.b.moveFromPoint(innerPoint, size);
                    });
                    if (!collision.isPresent()) {
                        return;
                    }
                }
                throw new RuntimeException();
            });
        }
/*
        long findingTime = 0;
        long movingTime = 0;
        long start;

        for (Collision c : collisions.keySet()) {
            start = System.currentTimeMillis();
            var best = findBestCollisionPointIn(c.a, c.b, collisions.get(c));
            findingTime += System.currentTimeMillis() - start;

            start = System.currentTimeMillis();
            best.ifPresent((point) -> {
                c.a.collisionForce(point);
                c.b.collisionForce(point);
            });
            movingTime += System.currentTimeMillis() - start;
            boolean colliding = best.isPresent();
            int i = 0;
            while (colliding) {
                start = System.currentTimeMillis();

                var collision = findBestCollisionPointIn(c.a, c.b, collisions.get(c));
                findingTime += System.currentTimeMillis() - start;

                colliding = collision.isPresent();

                start = System.currentTimeMillis();
                if (colliding) {
                    c.a.moveFromPoint(collision.get());
                    c.b.moveFromPoint(collision.get());
                }
                movingTime += System.currentTimeMillis() - start;
                if (i ++ > 100) {
                    break;
                }
            }
        }


        if (edgeCollisions.isEmpty() && collisions.isEmpty()) {
            return;
        } else {
            System.out.println(collisions);
            System.out.println(edgeCollisions);
            System.out.printf("%d millis finding, %d millis moving\n", findingTime, movingTime);
        }

        */
    }
    private void drawGrid() {
//        for (int row = 0; row <= grid.length; row++) {
//            for (int col = 0; col <= grid[0].length; col++) {
//                Loop.highlightGrid(row, col, Color.YELLOW);
//            }
//        }
    }
}
