/**
 * The Dual class represents a dual number of the form a + b𝜺, where 𝜺 has the property
 * that 𝜺 ≠ 0 and 𝜺2 = 0. Dual numbers are useful for automatic differentiation,
 * because f(a + b𝜺) = f(a) + bf’(a)𝜺. Each dual number keeps track of its real
 * component and its dual component. The dual class also tracks the largest difference
 * between two numbers that it considers still equal. Dual numbers support fairly
 * intuitive operations, including addition, subtraction, multiplication, division,
 * and reciprocation. A dual number can be raised to a power, or used as the exponent
 * of a number. The real or dual parts of a dual number can be extracted. Dual numbers
 * can be converted into a String and compared for equality by checking if the fields
 * are approximately equal.
 */
public class Dual {
    private double real;
    private double epsilon;
    private static final double MAX_RELATIVE_DIFFERENCE = 1E-7;

    /**
     * Construct a new Dual with an epsilon of 0.
     *
     * @param real the real component of the Dual
     */
    public Dual(double real) {
        this(real, 0);
    }
    /**
     * Construct a new Dual with the given real and epsilon values.
     *
     * @param real the real component of the Dual
     * @param epsilon the epsilon component of the Dual
     */
    public Dual(double real, double epsilon) {
        this.real = real;
        this.epsilon = epsilon;
    }

    /**
     * Construct a new Dual to be used as a variable assigned the given real value. The epsilon value is set to 1.
     *
     * @param real the real component of the Dual
     * @return the new Dual number
     */
    public static Dual variable(double real) {
        return new Dual(real, 1);
    }

    /**
     * Add two Duals together.
     *
     * @param other the value to be added
     * @return the sum of the two Duals
     */
    public Dual add(Dual other) {
        return new Dual(
                this.real + other.real,
                this.epsilon + other.epsilon
        );
    }
    /**
     * Subtract two Duals.
     *
     * @param other the value to be subtracted
     * @return the difference of the two Duals
     */
    public Dual sub(Dual other) {
        return this.add(other.mul(new Dual(-1)));
    }
    /**
     * Multiply two Duals.
     *
     * @param other the value to be multiplied
     * @return the product of the two Duals
     */
    public Dual mul(Dual other) {
        // (a + b𝜺)(c + d𝜺) = ac + (ad + bc)𝜺 + bd𝜺^2 = ac + (ad + bc)𝜺
        return new Dual(
                this.real * other.real,
                this.real * other.epsilon + this.epsilon * other.real
        );
    }
    /**
     * Divide two Duals.
     *
     * @param other the value to divide by
     * @return the quotient of the two Duals
     */
    public Dual div(Dual other) {
        return this.mul(other.reciprocal());
    }

    /**
     * Calculate the reciprocal of a Dual.
     *
     * @return the reciprocal
     */
    public Dual reciprocal() {
        // (a + b𝜺)(c + d𝜺) = 1 => ac + (ad + bc)𝜺 = 1 => ac = 1, ad = bc => c = 1/a, d = b/c^2
        return new Dual(
                1/real,
                -epsilon/Math.pow(real, 2)
        );
    }

    /**
     * Raise a Dual to a power.
     *
     * @param power the power to raise the number to
     * @return the result of the exponentiation
     */
    public Dual pow(double power) {
        // (a + b𝜺)^n = a^n + nba^(n-1) * 𝜺
        return new Dual(
                Math.pow(real, power),
                power * Math.pow(real, power - 1) * epsilon
        );
    }

    /**
     * Raise a base to the power of a Dual.
     *
     * @param base the base of the exponent
     * @return the result of the exponentiation
     */
    public Dual exp(double base) {
        // n^(a + b𝜺) = n^a + n^a * bln(a)𝜺
        return new Dual(
                Math.pow(base, real),
                Math.pow(base, real) * Math.log(real) * epsilon
        );
    }

    /**
     * Get the real component of a Dual.
     *
     * @return the real component
     */
    public double getValue() {
        return real;
    }

    /**
     * Get the epsilon component, or derivative, of a Dual.
     *
     * @return the epsilon component
     */
    public double getDerivative() {
        return epsilon;
    }

    /**
     * Convert a Dual to a string.
     *
     * @return a nicely formatted string representation of the Dual
     */
    @Override
    public String toString() {
        String result = "";
        // check if the result should contain a real component (if real ~= 0)
        boolean hasReal = !doubleEquals(real, 0);
        // add the real component as necessary
        if (hasReal) {
            result += String.format("%.2f", real);
        }
        // check if the result should contain an epsilon component (if epsilon ~= 0)
        if (!doubleEquals(epsilon, 0)) {
            // only add the + character if we printed a real component
            if (hasReal) {
                result += " + ";
            }
            // add the epsilon component, including the epsilon character
            result += String.format("%.2f\uD835\uDF3A", epsilon);
        }
        return result;
    }

    /**
     * Check if two doubles are close enough to be considered equal.
     *
     * @param a the first number to be compared
     * @param b the second number to be compared
     * @return whether the two numbers are equal
     */
    private static boolean doubleEquals(double a, double b) {
        // compare two floating point numbers for approximate equality using the algorithm described in
        // https://randomascii.wordpress.com/2012/02/25/comparing-floating-point-numbers-2012-edition/
        double diff = Math.abs(a - b);
        double largest = Math.max(Math.abs(a), Math.abs(b));
        return diff <= largest * MAX_RELATIVE_DIFFERENCE;
    }

    /**
     * Check if two Duals are equal.
     *
     * @param other the number to be compared against
     * @return whether the two numbers are equal
     */
    public boolean equals(Dual other) {
        return doubleEquals(this.real, other.real) && doubleEquals(this.epsilon, other.epsilon);
    }
}