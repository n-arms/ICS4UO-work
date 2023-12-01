public class Main {
    public static void main(String[] args) {
        Person alan = new Person("Alan", 'm');
        alan.greet();

        Student ada = new Student("Ada", 'X', "123456");
    }
}

class Arm {
    ArmIO.Inputs inputs = new ArmIO.Inputs();
    ArmIO io;
    public Arm(ArmIO io) {
        this.io = io;
    }
}

abstract class ArmIO {
    static class Inputs {
        public double position;
    }

    public abstract void setSpeed(double speed);

    public abstract void updateInputs(Inputs inputs);
}

class ArmPhysicsIO extends ArmIO {
    // radians from downwards vertical
    private double position = 0;
    // radians per second
    private double velocity = 0;
    // radians per second squared
    private double acceleration = 0;
    private double voltage = 0;

    private double kS;
    private double kG;
    private double kV;
    private double kA;

    private static final double INTERVAL = 0.02;
    private static final int TICKS_PER_UPDATE = 5;

    @Override
    public void setSpeed(double speed) {
        voltage = speed * 12;
    }

    // should be called at 50 Hz
    @Override
    public void updateInputs(Inputs inputs) {
        for (int i = 0; i < TICKS_PER_UPDATE; i++) {
            tick(INTERVAL / TICKS_PER_UPDATE);
        }
        inputs.position = position;
    }

    // V = Ks + Kv v + Ka a + Kg cos d
    private void tick(double dt) {
        position += velocity * dt;
        velocity += acceleration * dt;
        acceleration = (voltage - kS - kV * velocity - kG * Math.cos(position)) / kA;
    }
}