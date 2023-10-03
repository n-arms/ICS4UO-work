public class Fraction {
    private int numerator;
    private int denominator;

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction(Fraction old) {
        numerator = old.numerator;
        denominator = old.denominator;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public double toDecimal() {
        return (double) numerator / denominator;
    }

    public boolean greaterThan(Fraction other) {
        return this.toDecimal() > other.toDecimal();
    }

    // fix later
    @Override
    public boolean equals(Fraction other) {
        return this.toDecimal() == other.toDecimal();
    }

    public Fraction add(Fraction other) {
        return new Fraction(
                this.numerator * other.denominator + other.numerator * this.denominator,
                this.denominator * other.denominator
        );
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(
                this.numerator * other.numerator,
                this.denominator * other.denominator
        );
    }

    public void reduce() {
        int smallest = Math.min(Math.abs(numerator), Math.abs(denominator));

        for (int i = smallest; smallest > 0; smallest--) {
            if (numerator % i == 0 && denominator % i == 0) {
                numerator /= i;
                denominator /= i;
            }
        }
    }
}