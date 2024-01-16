import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Function;

public class Simulation {
    private final ArrayList<Particle> particles;
    private final double size;
    public final SegmentedWorld world;
    private static Function<Vector2, Vector2> toCanvasVectorTransform;
    private static Function<Double, Double> toCanvasScalarTransform;
    private boolean showGrid = false;
    private int unusedIdentifier = 0;

    public Simulation(ArrayList<Particle> particles, double size) {
        this.particles = particles;
        this.size = size;
        this.world = new SegmentedWorld(size, 20);
    }

    public static Vector2 toCanvas(Vector2 world) {
        return toCanvasVectorTransform.apply(world);
    }

    public static double toCanvas(double world) {
        return toCanvasScalarTransform.apply(world);
    }

    public static Simulation loadFromFile(Scanner file) throws IllegalArgumentException {
        file.useDelimiter("[,\n ]");
        double size = Double.parseDouble(file.next());
        file.nextLine();

        ArrayList<Particle> particles = new ArrayList<>();

        while (file.hasNext()) {
            double xPos = Double.parseDouble(file.next());
            double yPos = Double.parseDouble(file.next());
            double xVel = Double.parseDouble(file.next());
            double yVel = Double.parseDouble(file.next());
            int identifier = Integer.parseInt(file.next());
            double mass = Double.parseDouble(file.next());
            double elasticity = Double.parseDouble(file.next());
            String kind = file.next();
            file.nextLine();

            switch (kind) {
                case "disk" -> particles.add(new Disk(new Vector2(xPos, yPos), new Vector2(xVel, yVel), mass, identifier, elasticity));
                case "box" -> particles.add(new Box(new Vector2(xPos, yPos), new Vector2(xVel, yVel), mass, identifier, elasticity));
                default -> {
                    throw new IllegalArgumentException("Poorly formatted simulation file");
                }
            }
        }

        return new Simulation(particles, size);
    }

    public void update(double dt, Canvas c) {
        toCanvasScalarTransform = scalar -> scalar * c.getCanvasSize() / size;
        toCanvasVectorTransform = vector -> vector.scale(c.getCanvasSize() / size).pairwiseMul(new Vector2(1, -1)).add(new Vector2(0, c.getCanvasSize()));
        if (showGrid) drawGrid(c);
        gravity();
        long start = System.currentTimeMillis();
        collisions();
        long elapsed = System.currentTimeMillis() - start;
        if (elapsed > 30) {
            //throw new RuntimeException();
        }
        //edgeCollisions();
        for (Particle p : particles) {
            p.update(dt);
            p.render(c, toCanvasVectorTransform, toCanvasScalarTransform);
        }
    }

    private void drawGrid(Canvas c) {
        int gridSize = (int) toCanvas(size / world.getGridsPerSide());
        for (int row = 0; row <= world.getGridsPerSide(); row++) {
            for (int col = 0; col <= world.getGridsPerSide(); col++) {
                double worldX = world.fromGrid(col);
                double worldY = world.fromGrid(row);
                Vector2 canvas = toCanvas(new Vector2(worldX, worldY));
                c.drawRectangle((int) canvas.getX(), (int) canvas.getY(), gridSize, gridSize, Color.BLACK, Color.YELLOW);
            }
        }
    }

    private void gravity() {
        for (Particle p : particles) {
            Vector2 gravity = new Vector2(0, -9.8 * p.mass);
            p.applyForce(gravity);
        }
    }

    private boolean inBounds(double coord) {
        return 0 <= coord && coord < size;
    }

    private void collisions() {
        world.clear();
        for (Particle p : particles) {
            p.collide(world);
        }
        world.update();
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public void showGrid(boolean shows) {
        showGrid = shows;
    }

    public double minGrid() {
        return 0;
    }

    public double maxGrid() {
        return world.getGridsPerSide();
    }

    public double minCoord() {
        return 0;
    }
    public double maxCoord() {
        return size;
    }

    public Particle getParticleById(int id) {
        for (Particle p : particles) {
            if (p.getIdentifier() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown particle id");
    }

    public Particle getParticleByPosition(Vector2 position) {
        Particle best = particles.get(0);
        double distance = best.getPosition().sub(position).magnitude();
        for (Particle p : particles) {
            double newDistance = p.getPosition().sub(position).magnitude();
            if (distance >= newDistance) {
                distance = newDistance;
                best = p;
            }
        }
        return best;
    }

    public void removeParticle(Particle particle) {
        particles.remove(particle);
    }

    public void colorById() {
        colorBy(new Comparator<Particle>() {
            @Override
            public int compare(Particle o1, Particle o2) {
                return Integer.compare(o1.getIdentifier(), o2.getIdentifier());
            }
        }, Color.WHITE, Color.BLUE);
    }

    public void colorByKineticEnergy() {
        colorBy(new Comparator<Particle>() {
            @Override
            public int compare(Particle o1, Particle o2) {
                return Double.compare(o1.getVelocity().magnitude(), o2.getVelocity().magnitude());
            }
        }, Color.BLUE, Color.ORANGE);
    }

    private void colorBy(Comparator<Particle> comparator, Color low, Color high) {
        ArrayList<Particle> sorted = (ArrayList<Particle>) particles.clone();
        sorted.sort(comparator);
        int deltaScale = Math.max(1, sorted.size() - 1);
        int deltaRed = (high.getRed() - low.getRed()) / deltaScale;
        int deltaGreen = (high.getGreen() - low.getGreen()) / deltaScale;
        int deltaBlue = (high.getBlue() - low.getBlue()) / deltaScale;

        int currentRed = low.getRed();
        int currentGreen = low.getGreen();
        int currentBlue = low.getBlue();

        for (Particle p : sorted) {
            p.setColor(new Color(currentRed, currentGreen, currentBlue));
            currentRed += deltaRed;
            currentGreen += deltaGreen;
            currentBlue += deltaBlue;
        }
    }

    public int getNewIdentifier() {
        int id = unusedIdentifier;
        unusedIdentifier++;
        return id;
    }

    public boolean hasParticles() {
        return !particles.isEmpty();
    }

    public void writeToCSV(Writer writer) throws IOException {
        writer.write(String.format("%f\n", size));
        for (Particle p : particles) {
            p.writeToCSV(writer);
            writer.write('\n');
        }
    }
}
