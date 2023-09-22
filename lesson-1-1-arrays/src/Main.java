import java.util.Scanner;

public class Main {
    static Scanner reader = new Scanner(System.in);
    public static void main(String[] args) {
        exercise5();
    }


    public static int getInt(String message) {
        System.out.print(message);
        return Integer.parseInt(reader.nextLine());
    }

    public static void exercise2() {
        int[] array = {13, 60, 50, 46, 56, 83, 22, 71};

        int[] reversed = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            int reverseElement = array[array.length - 1 - i];
            System.out.println(reverseElement);
            reversed[i] = reverseElement;
        }

        for (int elem : reversed) {
            System.out.println(elem);
        }
    }

    private static double getDouble(String message) {
        System.out.print(message);
        return Double.parseDouble(reader.nextLine());
    }
    public static double[] getDoubleArray(int size) {
        double[] array = new double[size];

        for (int i = 0; i < size; i++) {
            double n = getDouble("Please enter a double: ");
            array[i] = n;
        }

        return array;
    }

    private static double sum(double[] array) {
        double total = 0;
        for (double i : array) {
            total += i;
        }
        return total;
    }

    public static void exercise3() {
        int n = getInt("Enter an integer: ");
        if (n <= 0) {
            System.out.printf("Please enter a number greater than 0.%n");
            return;
        }
        double[] array = getDoubleArray(n);

        double total = sum(array);
        double average = total / array.length;

        System.out.printf("The sum is %.2f and the average is %.2f.%n", total, average);

        System.out.printf("%s%n", prettifyDoubleArray(array));

        int minIndex = minimumIndex(array);
        double minimum = array[minIndex];

        System.out.printf("The minimum element is %.2f at index %d.%n", minimum, minIndex);
    }

    private static int minimumIndex(double[] array) {
        double minimumValue = array[0];
        int minimumIndex = 0;

        for (int i = 1; i < array.length; i++) {
            double element = array[i];

            if (element < minimumValue) {
                minimumValue = element;
                minimumIndex = i;
            }
        }

        return minimumIndex;
    }

    private static String prettifyDoubleArray(double[] array) {
        String result = "";
        for (int i = 0; i < array.length - 1; i++) {
            result += String.format("%.2f, ", array[i]);
        }
        result += String.format("%.2f", array[array.length - 1]);
        return result;
    }


    private static void exercise5() {
        String[] names = {"Alan", "Ada", "Grace", "Linus"};
        String[] numbers = {"181256345", "181159830", "181245891", "189875304"};

        printClassList(names, numbers);

        String studentName = getString("Please enter a name: ");
        performClassQuery(names, numbers, studentName);
    }

    private static String getString(String message) {
        System.out.print(message);
        return reader.nextLine();
    }

    private static void performClassQuery(String[] names, String[] numbers, String studentName) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(studentName)) {
                System.out.printf("%s's student number is %s.%n", names[i], numbers[i]);
                return;
            }
        }

        System.out.printf("Student %s does not exist.%n", studentName);
    }

    private static void printClassList(String[] names, String[] numbers) {
        System.out.println("Class:");
        System.out.println("\tCS101");
        System.out.println("Students:");

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String number = numbers[i];
            System.out.printf("\t%s, %s%n", name, number);
        }
    }
}