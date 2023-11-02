public class Main {
    public static void main(String[] args) {
        Complex reAndIm = new Complex(2.7, -1.1);
        System.out.printf("Complex numbers can have a real and imaginary component: %s%n", reAndIm);
        Complex zero = new Complex();
        System.out.printf("It is very easy to make the complex number %s!%n", zero);
        Complex justRe = new Complex(2.7, 0);
        Complex justIm = new Complex(0, 1.1);
        System.out.printf("You can have complex numbers with no imaginary (%s) or no real (%s) component!%n", justRe, justIm);

        System.out.println("");

        Complex alsoZero = new Complex(0, 0);
        System.out.printf("You can check if complex numbers are equal!%n");
        if (zero.equals(alsoZero)) {
            System.out.printf("%s is equal to %s%n", zero, alsoZero);
        } else {
            System.out.printf("%s is not equal to %s%n", zero, alsoZero);
        }
        if (zero.equals(reAndIm)) {
            System.out.printf("%s is equal to %s%n", zero, reAndIm);
        } else {
            System.out.printf("%s is not equal to %s%n", zero, reAndIm);
        }

        System.out.println("");

        double a = reAndIm.getRe();
        System.out.printf("You can access the real part of a complex number: %.2f is the real part of %s%n", a, reAndIm);

        System.out.println("");

        System.out.printf("Complex numbers support several mathematical operations%n");
        Complex easyMagnitude = new Complex(-3, 4);
        double magnitude = easyMagnitude.abs();
        System.out.printf("Absolute value: |%s| = %.2f%n", easyMagnitude, magnitude);

        Complex sum = easyMagnitude.plus(reAndIm);
        System.out.printf("Addition: (%s) + (%s) = %s%n", easyMagnitude, reAndIm, sum);

        Complex conjugate = easyMagnitude.conjugate();
        System.out.printf("Conjugate: (%s)* = %s%n", easyMagnitude, conjugate);

        double factor = 3;
        System.out.printf("Complex numbers can also be scaled by a factor: %.2f(%s) = ", factor, easyMagnitude);
        easyMagnitude.scale(factor);
        System.out.printf("%s%n", easyMagnitude);
    }
}