public class Duals {
    public static Dual f(Dual x) {
        return new Dual(3).mul(x).add(new Dual(1));
    }
    public static void main(String[] args) {
        System.out.println("Dual Numbers Testing");

        Dual constant = new Dual(7);
        System.out.printf("Made a dual number with a value of 7 and a derivative of 0: %s%n", constant);
        System.out.println("This kind of number would be used as a constant, like the 7 in the expression 7x.\n");

        Dual variable = Dual.variable(6);
        System.out.printf("Made a dual number with a value of 6 and a derivative of 1: %s%n", variable);
        System.out.println("This kind of number would be used as a variable with the value 6, like evaluating 7x at x=6.\n");

        Dual intermediate = new Dual(8, 5);
        System.out.printf("Made a dual number with a value of 8 and a derivative of 5: %s%n", intermediate);
        System.out.println("This kind of number would be used for an intermediate computation.\n");

        System.out.println("Dual numbers support operations like addition, subtraction, multiplication, division, and reciprocal");
        Dual sum = constant.add(variable);
        Dual difference = intermediate.sub(constant);
        Dual product = variable.mul(intermediate);
        Dual quotient = variable.div(constant);
        Dual reciprocal = intermediate.reciprocal();
        System.out.printf("(%s) + (%s) = %s%n", constant, variable, sum);
        System.out.printf("(%s) - (%s) = %s%n", intermediate, constant, difference);
        System.out.printf("(%s) * (%s) = %s%n", variable, intermediate, product);
        System.out.printf("(%s) / (%s) = %s%n", variable, constant, quotient);
        System.out.printf("1 / (%s) = %s%n", intermediate, reciprocal);

        System.out.println();

        System.out.println("Dual numbers can also be used in exponents.");
        Dual asBase = variable.pow(2);
        Dual asExponent = intermediate.exp(2);
        System.out.printf("(%s)^2 = %s%n", variable, asBase);
        System.out.printf("2^(%s) = %s%n", intermediate, asExponent);

        System.out.println();

        System.out.println("f(x) = 3x + 1");
        System.out.println("We expect f(5) = 16 and f'(5) = 3");
        Dual x = Dual.variable(5);
        Dual result = f(x);
        System.out.printf("f(%s) = %s%n", x, result);
        System.out.printf("Therefore f(%.2f) has a value of %.2f and a derivative of %.2f.%n", x.getValue(), result.getValue(), result.getDerivative());

        Dual expected = new Dual(16, 3);

        if (result.equals(expected)) {
            System.out.printf("f(%s) == %s, so the expected and actual answers line up!%n", x, expected);
        } else {
            System.out.printf("f(%s) == %s, so the expected answer and the actual answer don't line up :(%n", x, expected);
        }
    }
}