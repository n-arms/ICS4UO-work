public class Main {
    public static void main(String[] args) {
        Fraction f = new Fraction(1, 2);
        System.out.println(f);
        Fraction g = new Fraction(-2, 3);
        System.out.println(g);
        g.setNumerator(0);

        System.out.println(f +" = " + f.toDecimal());
        System.out.println(g);
        System.out.println(f.greaterThan(g));
    }
}