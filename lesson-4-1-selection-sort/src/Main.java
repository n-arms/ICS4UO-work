import java.util.Arrays;
import java.util.Timer;

public class Main {
    public static int smallest(int[] array) {
        int smallest = array[0];
        for (int element : array) {
            smallest = Math.min(smallest, element);
        }
        return smallest;
    }
    public static int indexOfSmallest(int[] array) {
        int smallestIndex = 0;
        int smallest = array[0];
        for (int i = 0; i < array.length; i++) {
            int element = array[i];

            if (element < smallest) {
                smallest = element;
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }
    public static int indexOfSmallestStartingFrom(int[] array, int startIndex) {
        int smallestIndex = startIndex;
        int smallest = array[startIndex];
        for (int i = startIndex; i < array.length; i++) {
            int element = array[i];

            if (element < smallest) {
                smallest = element;
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    public static void nicelyFormatLongArray(StringBuilder builder, int[] array, int unsorted, int swapping, int width) {
        int indexesPerChar = Math.max(1, array.length / width);
        builder.append("\u001b[44m");
        for (int i = 0; i < unsorted / indexesPerChar; i++) {
            builder.append(' ');
        }
        builder.append("\u001b[0m");
        builder.append("\u001b[43m");
        for (int i = unsorted / indexesPerChar; i < array.length / indexesPerChar; i++) {
//            if (i * indexesPerChar < swapping && swapping < (i + 1) * indexesPerChar) {
//                builder.append("\u001b[42m \u001b[0m");
//            } else {
                builder.append(' ');
            //}
        }
        builder.append("\u001b[0m");
    }
    private final static int SCREEN_WIDTH = 120;
    public static void selectionSort(int[] array) {
        StringBuilder builder = new StringBuilder(SCREEN_WIDTH);
        for (int i = 0; i < array.length; i++) {
            int swapping = indexOfSmallestStartingFrom(array, i);
            nicelyFormatLongArray(builder, array, i, swapping, SCREEN_WIDTH);
            System.out.printf("\033[2J\033[H%s", builder.toString());
            builder.setLength(0);
            swap(array, i, swapping);
        }
    }
    private final static int RAND_MAX = 1024;
    public static int[] generateArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * RAND_MAX);
        }
        return array;
    }
    public static void main(String[] args) {
        int[] array = generateArray(120_000);
        selectionSort(array);
        //System.out.printf("%s%n", Arrays.toString(array));
    }
}
