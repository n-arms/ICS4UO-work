import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

public class Menu {
    private static Scanner reader = new Scanner(System.in);
    private static<T> T getValid(String message, String error, Function<String, T> verifier) {
        while (true) {
            System.out.print(message);
            String line = reader.nextLine();
            try {
                return verifier.apply(line);
            } catch (Exception e) {
                System.out.println(error);
            }
        }
    }
    private static int getOption(int low, int high) {
        return getOption("Enter an option: ", low, high);
    }

    private static int getOption(String message, int low, int high) {
        return getValid(message, String.format("Please enter an integer in the range %d to %d", low, high), line -> {
            int option = Integer.parseInt(line);
            if (option >= low && option <= high) {
                return option;
            } else {
                throw new IllegalArgumentException("Expected an in bounds option");
            }
        });
    }

    private static String getPath() {
        System.out.printf("Enter the path: ");
        return reader.nextLine();
    }

    private static double getCoord(String axis, double min, double max) {
        return getValid(String.format("Enter the %s coordinate: ", axis), String.format("Please enter a decimal number in the range %f to %f", min, max), line -> {
            double coord = Double.parseDouble(line);
            if (coord >= min && coord <= max) {
                return coord;
            } else {
                throw new IllegalArgumentException("Expected an in bounds coordinate");
            }
        });
    }

    private static Vector2D getPosition() {
        double min = simulation.minCoord();
        double max = simulation.maxCoord();

        double x = getCoord("x", min, max);
        double y = getCoord("y", min, max);

        return new Vector2D(x, y);
    }

    private static Vector2D getVelocity() {
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        double x = getCoord("x", min, max);
        double y = getCoord("y", min, max);

        return new Vector2D(x, y);
    }

    private static double getMass() {
        return getValid("Enter a mass: ", "Please enter a number greater than 0.", line -> {
            double mass = Double.parseDouble(line);
            if (mass > 0) {
                return mass;
            } else {
                throw new IllegalArgumentException("Expected a mass greater than 0");
            }
        });
    }

    private static double getElasticity() {
        return getValid("Enter an elasticity: ", "Please enter a number greater than 0 and less than 1.", line -> {
            double elasticity = Double.parseDouble(line);
            if (elasticity >= 0 && elasticity <= 1) {
                return elasticity;
            } else {
                throw new IllegalArgumentException("Expected an elasticity greater than 0 and less than 1");
            }
        });
    }

    private static Particle getParticleById() {
        return getValid("Enter the particle ID: ", "Please enter a valid particle ID.", line -> {
            return simulation.getParticleById(Integer.parseInt(line));
        });
    }

    private static Simulation simulation = null;
    private static Loop loop = null;
    public static void topLevel() {
        if (simulation == null) {
            loadSimulation();
            loop = new Loop(simulation, 600);
            EventQueue.invokeLater(loop::start);
        }
        boolean isFinished = false;
        while (!isFinished) {
            System.out.println("1) Add particle");
            System.out.println("2) Edit or delete particle");
            System.out.println("3) Sort particles");
            System.out.println("4) Load simulation from file");
            System.out.println("5) Write simulation to file");
            System.out.println("0) Exit");

            switch (getOption(0, 5)) {
                case 0 -> isFinished = true;
                case 1 -> addParticle();
                case 2 -> editRemoveParticle();
                case 3 -> sortParticles();
                case 4 -> loadSimulation();
                case 5 -> writeSimulation();
            }
        }
    }

    private static void sortParticles() {
        System.out.println("How do you want to sort the particles?");
        System.out.println("1) Sort in order of increasing ID");
        System.out.println("2) Sort in order of increasing kinetic energy");
        System.out.println("0) Back");

        switch (getOption(0, 2)) {
            case 0 -> {}
            case 1 -> simulation.colorById();
            case 2 -> simulation.colorByKineticEnergy();
        }
    }

    public static void loadSimulation() {
        while (true) {
            System.out.println("Enter the path to a simulation from ./src/, or hit enter to exit without loading a file.");
            String pathSuffix = getPath();

            if (pathSuffix.isBlank()) {
                if (simulation == null) {
                    simulation = new Simulation(new ArrayList<>(), 11);
                }
                return;
            }
            String path = "./src/" + pathSuffix;
            try {
                File f = new File(path);
                Scanner file = new Scanner(f);
                simulation = Simulation.loadFromFile(file);
                return;
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
                System.out.printf("%s is invalid, please enter a valid path\n", path);
            }
        }
    }

    public static void writeSimulation() {
        System.out.println("Enter the path to the simulation file from ./src/, or hit enter to exit without loading a file.");
        String pathSuffix = getPath();

        String path = "./src/" + pathSuffix;
        try {
            File file = new File(path);
            file.createNewFile();
            BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
            simulation.writeToCSV(buffer);
            buffer.close();
        } catch (IOException e) {
            System.out.println("Error while trying to write file.");
        }
    }

    public static void addParticle() {
        simulation.showGrid(true);
        double min = simulation.minCoord();
        double max = simulation.maxCoord();
        System.out.printf("Enter all coordinates from the bottom left, starting at %f and going to %f\n", min, max);

        Vector2D position = getPosition();
        double mass = getMass();
        double elasticity = getElasticity();

        System.out.println("Particle types:");
        System.out.println("0) Disk");
        System.out.println("1) Box");

        Particle particle;
        switch (getOption("Enter the particle type: ", 0, 1)) {
            case 0 -> particle = new Disk(position, new Vector2D(), mass, simulation.getNewIdentifier(), elasticity);
            case 1 -> particle = new Box(position, new Vector2D(), mass, simulation.getNewIdentifier(), elasticity);
            default -> throw new IllegalArgumentException("The user should only have been able to enter valid options");
        }

        simulation.addParticle(particle);
        simulation.showGrid(false);
    }

    private static Particle getParticle() {
        System.out.println("How do you want to search for a particle.");
        System.out.println("0) Search by ID");
        System.out.println("1) Find the closest to a position");

        return switch (getOption(0, 1)) {
            case 0 -> getParticleById();
            case 1 -> simulation.getParticleByPosition(getPosition());
            default -> throw new IllegalArgumentException("The user should only have been able to enter valid options");
        };
    }

    public static void editRemoveParticle() {
        if (!simulation.hasParticles()) {
            System.out.println("Cannot edit or remove particles from an empty simulation.");
        }
        Particle particle = getParticle();

        System.out.println("1) Edit particle");
        System.out.println("2) Remove particle");
        System.out.println("0) Back");

        switch (getOption("Enter an option: ", 0, 2)) {
            case 1 -> editParticle(particle);
            case 2 -> simulation.removeParticle(particle);
            case 0 -> {}
        }
    }

    public static void editParticle(Particle particle) {
        System.out.printf("Editing particle %s\n", particle);

        System.out.println("1) Edit position");
        System.out.println("2) Edit velocity");
        System.out.println("3) Edit mass");
        System.out.println("0) Back");

        switch (getOption("Enter an option: ", 0, 3)) {
            case 1 -> {
                Vector2D position = getPosition();
                particle.setPosition(position);
            }
            case 2 -> {
                Vector2D velocity = getVelocity();
                particle.setVelocity(velocity);
            }
            case 3 -> {
                double mass = getMass();
                particle.setMass(mass);
            }
            case 0 -> {}
        }
    }
}
