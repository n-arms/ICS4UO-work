import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
class Graph {
    private record Point(int x, int y) {}
    private final ArrayList<Point> data = new ArrayList<>();
    public void add(int x, int y) {
        data.add(new Point(x, y));
    }
    public void printGraph(int width, int height) {
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        for (Point point : data) {
            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
        }
        
        int xPerChar = (maxX - minX) / width;
        int yPerChar = (maxY - minY) / height;
        
        int[] values = new int[width + 1];
        for (Point point : data) {
            values[point.x / xPerChar] = point.y / yPerChar;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (values[j] >= (height - i)) {
                    System.out.print("\033[0m ");
                } else {
                    System.out.println("\033[34m ");
                }
            }
        }
    }
}
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
        for (int i = 0; i < array.length; i += indexesPerChar) {
            builder.append(' ');

            if (i >= unsorted && i < (unsorted + indexesPerChar)) {
                builder.append("\u001b[0m");
                builder.append("\u001b[43m");
            }
        }
        builder.append("\u001b[0m");
    }
    private final static int SCREEN_WIDTH = 120;
    public static void selectionSort(int[] array) {
        StringBuilder builder = new StringBuilder(SCREEN_WIDTH);
        for (int i = 0; i < array.length; i++) {
            int swapping = indexOfSmallestStartingFrom(array, i);
            nicelyFormatLongArray(builder, array, i, swapping, SCREEN_WIDTH);
            System.out.printf("\033[1A%s%n", builder.toString());
            builder.setLength(0);
            swap(array, i, swapping);
        }
    }
    public static void quickSort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }
    public static void quickSort(int[] array, int start, int end) {
        int size = end - start;
        if (size < 16) {
            insertionSort(array, start, end);
        }
        if (start >= end) return;

        int secondSectionStart = partition(array, start, end, start);

        quickSort(array, start, secondSectionStart - 2);
        quickSort(array, secondSectionStart, end);
    }
    public static int partition(int[] array, int start, int end, int pivot) {
        int leftBoundary = start;
        int rightBoundary = end;

        int i = start;
        int pivotValue = array[pivot];

        while (i <= rightBoundary) {
            int value = array[i];
            if (value > pivotValue) {
                swap(array, i, rightBoundary);
                rightBoundary -= 1;
            } else {
                swap(array, i, leftBoundary);
                leftBoundary += 1;
                i += 1;
            }
        }
        swap(array, leftBoundary - 1, pivot);
        return leftBoundary;
    }
    public static void insertionSort(int[] array) {
        insertionSort(array, 0, array.length - 1);
    }
    public static void insertionSort(int[] array, int start, int end) {
        if (start >= end) return;
        for (int i = start + 1; i <= end; i++) {
            int x = array[i];
            int j;
            for (j = i - 1; j >= start && array[j] > x; j--) {
                array[j + 1] = array[j];
            }
            array[j + 1] = x;
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
        /*
        int[] array = generateArray(120_000);
        selectionSort(array);
        */
        int[] array = generateArray(20);
        quickSort(array);
        System.out.println(Arrays.toString(array));

        Graph graph = new Graph();
        for (int i = 0; i < 100; i++) {
            graph.add(i, i);
        }
        graph.printGraph(10, 10);
    }
}
