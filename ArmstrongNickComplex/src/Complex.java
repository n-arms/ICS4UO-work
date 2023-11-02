public class Complex {
    private double re;
    private double im;
    private static final String NUMBER_FORMAT = "%.2f";

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex() {
        this(0, 0);
    }

    @Override
    public String toString() {
        // check if one of the components is zero and print accordingly, using the appropriate format string
        if (this.im == 0) {
            return String.format(NUMBER_FORMAT, this.re);
        } else if (this.re == 0) {
            return String.format(NUMBER_FORMAT + "i", this.im);
        } else {
            return String.format(NUMBER_FORMAT + " + " + NUMBER_FORMAT + "i", this.re, this.im);
        }
    }

    public boolean equals(Complex other) {
        return this.re == other.re && this.im == other.im;
    }

    public double getRe() {
        return re;
    }

    public double abs() {
        double squaredMagnitude = re * re + im * im;
        return Math.sqrt(squaredMagnitude);
    }

    public void scale(double x) {
        re *= x;
        im *= x;
    }

    public Complex plus(Complex other) {
        return new Complex(
                this.re + other.re,
                this.im + other.im
        );
    }

    public Complex conjugate() {
        return new Complex(
                re,
                -im
        );
    }
}
