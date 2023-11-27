import java.util.ArrayList;
import java.util.HashMap;

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
    private static boolean prime(int n, int m) {
        if (m == 1) {
            return true;
        } if (n % m == 0) {
            return false;
        } else {
            return prime(n, m - 1);
        }
    }
    public static String reverse(String s) {
        if (s.length() <= 1) {
            return s;
        } else {
            return reverse(s.substring(0, s.length()-1)) + s.charAt(0);
        }
    }
    public static void printTriangle(int n) {
        if (n != 0) {
            printTriangle(n - 1);
            for (int i = 0; i < n; i++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
    public static void printPattern(int n) {
        if (n == 1) {
            System.out.println("*");
        } else if (n > 1) {
            for (int i = 0; i < n; i++) {
                System.out.print("*");
            }
            System.out.println();
            printPattern(n - 1);
            for (int i = 0; i < n; i++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
    public static int numDigits(int n) {
        if (n == 0) {
            return 0;
        } else {
            return 1 + numDigits(n / 10);
        }
    }
    public static int digitSum(int n) {
        if (n == 0) {
            return 0;
        } else {
            return n % 10 + digitSum(n / 10);
        }
    }
    public static void hailstone(long n) {
        if (n == 1) {
            System.out.print("1\n");
            return;
        } else {
            System.out.printf("%d, ", n);
        }

        if (n % 2 == 0) {
            hailstone(n / 2);
        } else {
            hailstone(3 * n + 1);
        }
    }
    public record AckermannCall(int m, int n) {}
    public static HashMap<AckermannCall, Integer> calls = new HashMap<>();
    public static ArrayList<AckermannCall> cacheHits = new ArrayList<>();
    public static int counter = 0;
    /*
    public static int ackermann(int m, int n) {
        var call = new AckermannCall(m, n);
        var result = calls.get(call);
        if (result != null) {
            cacheHits.add(call);
            return result;
        }
        counter += 1;

        int answer;

        while (true) {
            if (m == 0) {
                answer = n + 1;
                break;
            } else if (n == 0) {
                m = m - 1;
                n = 1;
            } else {
                m = m - 1;
                n = ackermann(m, n - 1);
            }
        }
        //calls.put(call, answer);
        return answer;
    }
    */
    public static int ackermannTrivial(int m, int n) {
        if (m == 0) {
           return n + 1;
        } else if (n == 0) {
            return ackermannTrivial(m - 1, 1);
        } else {
            return ackermannTrivial(m - 1, ackermannTrivial(m, n - 1));
        }
    }
    static int CACHE_SIZE = 1000;
    static int[][] callsArray = new int[CACHE_SIZE][CACHE_SIZE];
    public static int ackermannUnrolledCached(int m, int n) {
        if (m < CACHE_SIZE && n < CACHE_SIZE && callsArray[m][n] != -1) {
            return callsArray[m][n];
        }
        while (true) {
            if (m == 0) {
                return n + 1;
            } else if (n == 0) {
                m -= 1;
                n = 1;
            } else {
                int result = ackermannUnrolledCached(m, n - 1);
                if (m < CACHE_SIZE && n < CACHE_SIZE) callsArray[m][n - 1] = result;
                n = result;
                m -= 1;
            }
        }
    }
    public static int ackermannUnrolled(int m, int n) {
        while (true) {
            if (m == 0) {
                return n + 1;
            } else if (n == 0) {
                m -= 1;
                n = 1;
            } else {
                n = ackermannUnrolled(m, n - 1);
                m -= 1;
            }
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < CACHE_SIZE; i++) {
            for (int j = 0; j < CACHE_SIZE; j++) {
                callsArray[i][j] = -1;
            }
        }
        // 1
        // a) there is no base case
        // b) you will never hit the recursion case if you start with a negative number, and you will never hit the base case if you start with a positive number
//
//        System.out.printf("3.5^4 = %s%n", power(3.5, 4));
//        System.out.printf("The 7th fibonacci number is %s%n", fib(7));
//        System.out.printf("gcdA of 2023 and 867 is %s%n", gcdA(2023, 867));
//        System.out.printf("gcdB of 2023 and 867 is %s%n", gcdB(2023, 867));
//        System.out.printf("prime(17113) is %s%n", prime(109 * 157));
//        System.out.printf("reverse(\"hello\") -> %s%n", reverse("hello"));
//        printTriangle(5);
//        printPattern(4);
//        System.out.printf("numDigits(314) -> %d%n", numDigits(314));
//        System.out.printf("digitSum(314) -> %d%n", digitSum(314));
//        hailstone(5);
        //System.out.printf("%s%n", ackermannTrivial(4, 1));
        long start = System.currentTimeMillis();
        int result = ackermannUnrolled(3, 7);
        System.out.printf("unrolled took %s%n", System.currentTimeMillis() - start);

        long start2 = System.currentTimeMillis();
        int resultCached = ackermannUnrolledCached(3, 7);
        System.out.printf("unrolled cached took %s%n", System.currentTimeMillis() - start2);

        long start3 = System.currentTimeMillis();
        int resultTrivial = ackermannUnrolledCached(3, 7);
        System.out.printf("trivial took %s%n", System.currentTimeMillis() - start3);

        System.out.printf("%s%n", result);
        System.out.printf("%s%n", resultCached);
        System.out.printf("%s%n", resultTrivial);


        //System.out.printf("%d took %d calls with a cache of %d elements and %d cache hits%n", ackermann(4, 1), counter, calls.size(), cacheHits.size());
//        for (int i = 0; i <= 3; i++) {
//            for (var call : cacheHits) {
//                if (call.m == i) {
//                    System.out.printf("%s%n", call);
//                }
//            }
//        }
    }
}
