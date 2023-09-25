import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static Scanner reader = new Scanner(System.in);

    static int getInt(String message) {
        System.out.print(message);
        return Integer.parseInt(reader.nextLine());
    }

    public static void main(String[] args) {
        exercise3();
    }

    public static void exercise1() {
        int rows = getInt("Enter the length of the array: ");
        int[][] array = new int[rows][];

        for (int i = 0; i < rows; i++) {
            System.out.printf("Array %d:%n", i);
            int cols = getInt(String.format("Enter the length of array %d: ", i));
            int[] row = new int[cols];

            System.out.printf("Enter the %d integer(s):%n", cols);

            for (int j = 0; j < cols; j++) {
                int num = getInt("");
                row[j] = num;
            }

            array[i] = row;
        }

        System.out.println("The array:");
        System.out.println(Arrays.deepToString(array));
    }

    public static void exercise2() {
        int[][] data = {
                {3, 2, 5},
                {1, 4, 4, 8, 13},
                {9, 1, 0, 2},
                {0, 2, 6, 4, -1, -8}
        };
        for (int i = 0; i < data.length; i++) {
            System.out.printf("Row %d sum is %d.%n", i, arraySum(data[i]));
        }
        printArray(data);
    }

    public static int arraySum(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum;
    }

    public static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                System.out.print("[ ");
            }
            System.out.print("[");
            int[] row = array[i];
            for (int j = 0; j < row.length; j++) {
                if (j != 0) {
                    System.out.print(", ");
                }
                System.out.print(row[j]);
            }
            System.out.print("]");
            if (i == array.length - 1) {
                System.out.println(" ]");
            } else {
                System.out.println(",");
            }
        }
    }

    public static void exercise3() {
        int[][] data = {
                {3, 1, 4},
                {1, 5, 9},
        };

        printArray(data);
        printArray(rotate(data));
    };

    public static void swapRow(int[][] array, int a, int b) {
        int[] temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    public static void swapCol(int[][] array, int a, int b) {
        for (int[] row : array) {
            int temp = row[a];
            row[a] = row[b];
            row[b] = temp;
        }
    }

    public static void multiplyRow(int[][] array, int rowIndex, int factor) {
        int[] row = array[rowIndex];
        for (int i = 0; i < row.length; i++) {
            row[i] *= factor;
        }
    }

    public static int[][] rotate(int[][] array) {
        if (array.length == 0) {
            return new int[0][];
        }
        int rows = array.length;
        int cols = array[0].length;
        int[][] rotated = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][i] = array[rows - i - 1][j];
            }
        }

        return rotated;
    }
}