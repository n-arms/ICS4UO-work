
import java.util.*;

public class CollisionManager {
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
    private record GridPosition(int row, int col) {}

    private final double size;
    private final int gridsPerSide;
    private final HashMap<GridPosition, ArrayList<Particle>> grid;

    /**
     * Construct a new collision manager with the given size and number of grids.
     * @param size the size of the collision manager in in-world units
     * @param gridsPerSide the number of grid sections on one side of the collision manager's square grid
     */
    public CollisionManager(double size, int gridsPerSide) {
        this.size = size;
        this.gridsPerSide = gridsPerSide;
        grid = new HashMap<>();
    }

    /**
     * Get the size in in-world units of each grid square.
     * @return the size of a grid square
     */
    private double getGridSize() {
        return size / gridsPerSide;
    }

    /**
     * Register a particle as being in a certain grid square. Particles can be added to multiple grid squares, and must be re-added after each call to update.
     * The collision manager will check for collisions between particles that are in the same grid square and will never check for collisions between particles not in the same grid square.
     * False positives (claiming a particle is in a grid square when it isn't) will result in minor degradations of performance,
     * while false negatives (not claiming a particle is in a grid square when it is) will result in soundness issues.
     * For this reason, err on the side of adding a particle to too many grid squares rather than too few.
     * @param gridRow the row of the grid
     * @param gridCol the column of the grid
     * @param particle the particle to add
     */
    public void addParticle(int gridRow, int gridCol, Particle particle) {
        var position = new GridPosition(gridRow, gridCol);
        var pool = grid.computeIfAbsent(position, k -> new ArrayList<>());
        pool.add(particle);
    }

    /**
     * Clear the collision manager, getting rid of all the registered particles.
     */
    public void clear() {
        grid.clear();
    }

    /**
     * Convert an in-world coordinate to a grid number, rounding down.
     * @param coord the in-world coordinate
     * @return the floored grid number
     */
    public int toGridFloor(double coord) {
        return (int) Math.floor(coord / getGridSize());
    }

    /**
     * Convert an in-world coordinate to a grid number, rounding up.
     * @param coord the in-world coordinate
     * @return the ceiled grid number
     */
    public int toGridCeil(double coord) {
        return (int) Math.ceil(coord / getGridSize());
    }

    /**
     * Convert a grid number to an in-world coordinate.
     * @param coord the grid number
     * @return the in-world coordinate
     */
    private double fromGrid(int coord) {
        return coord * getGridSize();
    }

    /**
     * Generate a random point measured in in-world units inside a grid square.
     * @param row the row of the grid square
     * @param col the column of the grid square
     * @return the random point
     */
    private Vector2D randomPoint(int row, int col) {
        double gridSize = getGridSize();
        double x = col * gridSize + Math.random() * gridSize;
        double y = row * gridSize + Math.random() * gridSize;

        return new Vector2D(x, y);
    }

    /**
     * Add a key, value pair to a "multimap": a map that maps to a set. A multimap can contain several values for the same key.
     * @param map the multimap
     * @param key the key
     * @param element the value
     * @param <K> the type of key
     * @param <V> the type of value
     */
    private static<K, V> void addToMultimap(Map<K, HashSet<V>> map, K key, V element) {
        var list = map.get(key);

        if (list == null) {
            map.put(key, new HashSet<>(List.of(element)));
        } else {
            list.add(element);
        }
    }

    /**
     * Find the "best" point in the given set of grid squares that intersects with both particles.
     * A point is good if it minimizes the sum of the signed distance fields of the two particle and the two signed distance fields have similar values at that point.
     * @param a the first particle
     * @param b the second particle
     * @param grids the set of grid squares to check for collisions in
     * @return the best point, or Optional.empty() if no such point exists
     */
    private Optional<Vector2D> findBestCollisionPointIn(Particle a, Particle b, HashSet<GridPosition> grids) {
        ArrayList<Vector2D> collisions = new ArrayList<>();
        for (GridPosition grid : grids) {
            for (int i = 0; i < 100; i++) {
                Vector2D point = randomPoint(grid.row, grid.col);

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

    /**
     * Check if two particles collide in the given grid square.
     * @param a the first particle
     * @param b the second particle
     * @param gridRow the row of the grid square to check
     * @param gridCol the column of the grid square to check
     * @return whether the two particles collide
     */
    private boolean collidesIn(Particle a, Particle b, int gridRow, int gridCol) {
        HashSet<GridPosition> gridSquare = new HashSet<>(List.of(new GridPosition(gridRow, gridCol)));

        return findBestCollisionPointIn(a, b, gridSquare).isPresent();
    }

    /**
     * Update the collision manager, resolving all particle-particle and particle-world collisions.
     * All the particles in the simulation should be added to the collision manager each frame before this method is called.
     */
    public void update() {
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
                    if (collidesIn(pool.get(first), pool.get(second), position.row, position.col)) {
                        addToMultimap(collisions, new Collision(pool.get(first), pool.get(second)), position);
                    }
                }
            }
        }

        for (Particle p : edgeCollisions.keySet()) {
            for (Edge e : edgeCollisions.get(p)) {
                Vector2D collision = switch (e) {
                    case LEFT -> new Vector2D(0, p.getPosition().getY());
                    case RIGHT -> new Vector2D(fromGrid(gridsPerSide), p.getPosition().getY());
                    case TOP -> new Vector2D(p.getPosition().getX(), fromGrid(gridsPerSide));
                    case BOTTOM -> new Vector2D(p.getPosition().getX(), 0);
                };
                p.collisionForce(collision, size);
            }
        }

        for (Collision c : collisions.keySet()) {
            var best = findBestCollisionPointIn(c.a, c.b, collisions.get(c));
            best.ifPresent(point -> {
                System.out.printf("Starting resolution between %s and %s\n", c.a, c.b);
                c.a.collisionForce(point, size);
                c.b.collisionForce(point, size);

                for (int i = 0; i < 100; i++) {
                    System.out.printf("Resolving collision between %s and %s\n", c.a, c.b);
                    var collision = findBestCollisionPointIn(c.a, c.b, collisions.get(c));
                    System.out.printf("Found collision point %s\n", collision);
                    collision.ifPresent(innerPoint -> {
                        c.a.moveFromPoint(innerPoint, size);
                        c.b.moveFromPoint(innerPoint, size);
                    });
                    if (collision.isEmpty()) {
                        return;
                    }
                }
                throw new RuntimeException();
            });
        }
    }
}
