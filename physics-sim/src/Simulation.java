import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Function;

public class Simulation {
    private final ArrayList<Particle> particles;
    private final double size;
    public final CollisionManager collisions;
    private boolean showGrid = false;
    private int unusedIdentifier = 0;

    /**
     * Construct a new simulation.
     * @param particles the list of particles to simulate
     * @param size the size (in in-world units) of the simulation
     */
    public Simulation(ArrayList<Particle> particles, double size) {
        this.particles = particles;
        this.size = size;
        this.collisions = new CollisionManager(size, 20);
    }

    /**
     * Load a simulation from a file.
     * @param file the file to read from
     * @return the loaded simulation
     * @throws IllegalArgumentException if the file is malformed
     */
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
                case "disk" -> particles.add(new Disk(new Vector2D(xPos, yPos), new Vector2D(xVel, yVel), mass, identifier, elasticity));
                case "box" -> particles.add(new Box(new Vector2D(xPos, yPos), new Vector2D(xVel, yVel), mass, identifier, elasticity));
                default -> {
                    throw new IllegalArgumentException("Poorly formatted simulation file");
                }
            }
        }

        return new Simulation(particles, size);
    }

    /**
     * Update the simulation as if a certain number of seconds have passed, and render it to a canvas.
     * @param dt the time, in seconds, that has passed
     * @param canvas the canvas to render to
     */
    public void update(double dt, Canvas canvas) {
        Function<Double, Double> scalarToCanvas = scalar -> scalar * canvas.getCanvasSize() / size;
        Function<Vector2D, Vector2D> vectorToCanvas = vector -> vector.scale(canvas.getCanvasSize() / size).pairwiseMul(new Vector2D(1, -1)).add(new Vector2D(0, canvas.getCanvasSize()));
        if (showGrid) {
            drawGrid(canvas, scalarToCanvas, vectorToCanvas);
        }
        gravity();
        collisions();
        for (Particle p : particles) {
            p.update(dt);
            p.render(canvas, vectorToCanvas, scalarToCanvas);
        }
    }

    /**
     * Draw a grid on the canvas where each grid line corresponds to an integer value of in-world space.
     * @param canvas the canvas to render to
     * @param toCanvasScalar a function to transform in-world scalar quantities to on-canvas scalar quantities
     * @param toCanvasVector a function to transform in-world vector quantities to on-canvas vector quantities
     */
    private void drawGrid(Canvas canvas, Function<Double, Double> toCanvasScalar, Function<Vector2D, Vector2D> toCanvasVector) {
        int gridSize = toCanvasScalar.apply(1.0).intValue();
        for (int row = (int) minCoord() + 1; row <= maxCoord(); row++) {
            for (int col = (int) minCoord(); col < maxCoord(); col++) {
                Vector2D canvasPos = toCanvasVector.apply(new Vector2D(col, row));
                canvas.drawRectangle((int) canvasPos.getX(), (int) canvasPos.getY(), gridSize, gridSize, Color.BLACK, Color.YELLOW);
            }
        }
    }

    /**
     * Apply the force of gravity to all the particles in the simulation.
     */
    private void gravity() {
        for (Particle p : particles) {
            Vector2D gravity = new Vector2D(0, -9.8 * p.mass);
            p.applyForce(gravity);
        }
    }

    /**
     * Resolve particle-particle and particle-wall collisions.
     */
    private void collisions() {
        collisions.clear();
        for (Particle p : particles) {
            p.collide(collisions);
        }
        collisions.update();
    }

    /**
     * Add a particle to the simulation.
     * @param particle the particle to add
     */
    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    /**
     * Set whether the visual grid is enabled or disabled.
     * @param shows if the grid is shown
     */
    public void showGrid(boolean shows) {
        showGrid = shows;
    }

    /**
     * Get the minimum in-world coordinate.
     * @return the minimum coordinate
     */
    public double minCoord() {
        return 0;
    }

    /**
     * Get the maximum in-world coordinate.
     * @return the maximum coordinate
     */
    public double maxCoord() {
        return size;
    }

    /**
     * Find a particle with the given ID.
     * @param id the ID to search for
     * @return the matching particle
     * @throws IllegalArgumentException if there is no matching particle
     */
    public Particle getParticleById(int id) throws IllegalArgumentException {
        for (Particle p : particles) {
            if (p.getIdentifier() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown particle id");
    }


    /**
     /**
     * Find the closest particle to a given position.
     * @param position the position to search around
     * @return the closest particle
     * @throws IndexOutOfBoundsException if there are no particles
     */
    public Particle getParticleByPosition(Vector2D position) throws IndexOutOfBoundsException {
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

    /**
     * Remove the given particle from the simulation.
     * @param particle the particle to remove
     */
    public void removeParticle(Particle particle) {
        particles.remove(particle);
    }

    /**
     * Color the particles from white to blue in order of increasing ID.
     */
    public void colorById() {
        colorBy(new Comparator<Particle>() {
            @Override
            public int compare(Particle o1, Particle o2) {
                return Integer.compare(o1.getIdentifier(), o2.getIdentifier());
            }
        }, Color.WHITE, Color.BLUE);
    }

    /**
     * Color the particles from blue to orange in order of increasing kinetic energy.
     */
    public void colorByKineticEnergy() {
        colorBy(new Comparator<Particle>() {
            @Override
            public int compare(Particle o1, Particle o2) {
                double v1 = o1.getVelocity().magnitude();
                double v2 = o2.getVelocity().magnitude();
                return Double.compare(v1*v1*o1.getMass(), v2*v2*o2.getMass());
            }
        }, Color.BLUE, Color.ORANGE);
    }

    /**
     * Color the particles from one color to another by sorting them according to a comparator.
     * @param comparator the comparator to order the particles by
     * @param low the color for the lowest ordered particle
     * @param high the color for the highest ordered particle
     */
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

    /**
     * Get a new as-of-yet unused identifier for a new particle.
     * @return the identifier
     */
    public int getNewIdentifier() {
        int id = unusedIdentifier;
        unusedIdentifier++;
        return id;
    }

    /**
     * Check if the simulation has any particles.
     * @return if there are any particles in the simulation
     */
    public boolean hasParticles() {
        return !particles.isEmpty();
    }

    /**
     * Record the simulation to a CSV save file.
     * @param writer the output to write the file contents to
     * @throws IOException if writing fails
     */
    public void writeToCSV(Writer writer) throws IOException {
        writer.write(String.format("%f\n", size));
        for (Particle p : particles) {
            p.writeToCSV(writer);
            writer.write('\n');
        }
    }
}
