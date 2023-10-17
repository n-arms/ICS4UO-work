public class Dual {
    private double real;
    private double epsilon;
    private static final MAX_RELATIVE_FLOATING_DIFF = Double.FLT
    public Dual() {
        this(0, 0);
    }
    public Dual(double real) {
        this(real, 0);
    }
    public Dual(double real, double epsilon) {
        this.real = real;
        this.epsilon = epsilon;
    }
    public static Dual variable(double real) {
        return new Dual(real, 1);
    }
    public Dual add(Dual other) {
        return new Dual(
                this.real + other.real,
                this.epsilon + other.epsilon
        );
    }
    public Dual sub(Dual other) {
        return this.add(other.mul(new Dual(-1)));
    }
    public Dual mul(Dual other) {
        return new Dual(
                this.real * other.real,
                this.real * other.epsilon + this.epsilon * other.real
        );
    }
    public Dual div(Dual other) {
        return this.mul(other.reciprocal());
    }
    public Dual reciprocal() {
        return new Dual(
                1/real,
                -epsilon/Math.pow(real, 2)
        );
    }
    public Dual pow(double power) {
        return new Dual(
                Math.pow(real, power),
                power * Math.pow(epsilon, power - 1)
        );
    }
    public Dual exp(double base) {
        return new Dual(
                Math.pow(base, real),
                Math.pow(base, real) * Math.log(real) * epsilon
        );
    }
    public double getValue() {
        return real;
    }
    public double getDerivative() {
        return epsilon;
    }
    public String toString() {
        return String.format("%.2f + %.2f\uD835\uDF3A", real, epsilon);
    }
    // https://randomascii.wordpress.com/2012/02/25/comparing-floating-point-numbers-2012-edition/#:~:text=The%20idea%20of%20a%20relative,fabs(f1%2Df2).
    private static boolean doubleEquals(double a, double b) {
        double diff = Math.abs(a - b);
        float largest = Math.max(Math.abs(a), Math.abs(b));
        return diff <= largest * MAX_RELATIVE_FLOATING_DIFF;
    }
    public boolean equals(Dual other) {

    }
}
