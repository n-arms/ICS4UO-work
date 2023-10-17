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
        reduce();
    }

    public Fraction(Fraction old) {
        numerator = old.numerator;
        denominator = old.denominator;
    }

    public Fraction(double value) {
        double tempNum = value;
        int tempDen = 1;

        while ((int) value != ((int) (value * tempDen))) {
            value *= 10;
            tempDen *= 10;
        }

        numerator = (int) tempNum;
        denominator = tempDen;
        reduce();
    }

    public Fraction(int value) {
        numerator = value;
        denominator = 1;
    }

    public Fraction() {
        this(0);
    }

    @Override
    public String toString() {
        if (this.equals(new Fraction())) {
            return "0";
        } else if (denominator == 1) {
            return Integer.toString(numerator);
        } else {
            return numerator + "/" + denominator;
        }
    }

    public double toDecimal() {
        return (double) numerator / denominator;
    }

    public boolean greaterThan(Fraction other) {
        return this.toDecimal() > other.toDecimal();
    }

    public boolean equals(Fraction other) {
        return this.numerator * other.denominator == this.denominator * other.numerator;
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

    public static Fraction product(Fraction f1, Fraction f2) {
        return f1.multiply(f2);
    }

    public void timesEquals(Fraction other) {
        Fraction product = this.multiply(other);
        numerator = product.numerator;
        denominator = product.denominator;
    }

    public void plusEquals(Fraction other) {
        Fraction sum = this.add(other);
        numerator = sum.numerator;
        denominator = sum.denominator;
    }

    public void integerMultiply(int k) {
        this.timesEquals(new Fraction(k));
    }

    public Fraction reciprocal() {
        return new Fraction(denominator, numerator);
    }

    public Fraction divide(Fraction other) {
        return this.multiply(other.reciprocal());
    }

    public Fraction subtract(Fraction other) {
        Fraction localOther = new Fraction(other);
        localOther.integerMultiply(-1);
        return this.add(localOther);
    }

    public static Fraction sum(Fraction f1, Fraction f2) {
        return f1.add(f2);
    }

    public static Fraction difference(Fraction f1, Fraction f2) {
        return f1.subtract(f2);
    }

    public static Fraction quotient(Fraction f1, Fraction f2) {
        return f1.divide(f2);
    }
}