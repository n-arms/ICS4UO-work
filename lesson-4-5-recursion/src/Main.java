public class Main {
    public static double power(double x, int n) {
        if (n == 0) {
            return 1;
        } else if (n < 0) {
            return 1 / power(x, -n);
        } else {
            return x * power(x, n - 1);
        }
    }
    public static int fib(int n) {
        if (n <= 2) {
            return 1;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }
    public static int gcdA(int m, int n) {
        if (m == n) {
            return m;
        } else if (m > n) {
            return gcdA(n, m - n);
        } else {
            return gcdA(n, m);
        }
    }
    public static int gcdB(int m, int n) {
        if (n == 0) {
            return m;
        } else {
            return gcdB(n, m % n);
        }
    }
    public static int square(int n) {
        if (n == 1) {
            return 1;
        } else {
            return square(n - 1) + 2 * n - 1;
        }
    }
    public static boolean prime(int n) {
        return prime(n, n - 1);
    }
    public static boolean prime(int n, int m) {
        if (m == 1) {
            return true;
        } if (n % m == 0) {
            return false;
        } else {
            return prime(n, m - 1);
        }
    }
    public static void main(String[] args) {
        // 1
        // a) there is no base case
        // b) you will never hit the recursion case if you start with a negative number, and you will never hit the base case if you start with a positive number

        System.out.printf("3.5^4 = %s%n", power(3.5, 4));
        System.out.printf("The 7th fibonacci number is %s%n", fib(7));
        System.out.printf("gcdA of 2023 and 867 is %s%n", gcdA(2023, 867));
        System.out.printf("gcdB of 2023 and 867 is %s%n", gcdB(2023, 867));
        System.out.printf("prime(17113) is %s%n", prime(109 * 157));
    }
}
