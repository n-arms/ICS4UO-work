import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
class Graph {
    private record Point(int x, int y) {}
    private final ArrayList<Point> data = new ArrayList<>();
    public void add(int x, int y) {
        data.add(new Point(x, y));
    }
    public ArrayList<ArrayList<Point>> buckets(int width) {
        int minX = 0;
        int maxX = 0;
        for (Point point : data) {
            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
        }

        int xPerChar = (int) Math.ceil((maxX - minX) / (double) width);

        ArrayList<ArrayList<Point>> buckets = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            buckets.add(new ArrayList<>());
        }

        for (Point p : data) {
            buckets.get((p.x - minX) / xPerChar).add(p);
        }

        return buckets;
    }
    public void printGraph(int width, int height) {
        int minY = 0;
        int maxY = 0;
        for (Point point : data) {
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
        }

        int yPerChar = (int) Math.ceil((maxY - minY) / (double) height);

        int[] values = new int[width];
        Arrays.fill(values, Integer.MAX_VALUE);

        var buckets = buckets(width);

        for (int i = 0; i < width; i++) {
            values[i] = (int) buckets.get(i).stream().mapToInt(Point::y).average().orElse(Integer.MAX_VALUE);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (values[j] <= (height - i)) {
                    System.out.print("\033[0m ");
                } else {
                    System.out.print("\033[44m ");
                }
            }
            System.out.println("\033[0m");
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
    public static void bubbleSort(int[] array) {
        int swap;
        int sorted = array.length;

        do {
            swap = 0;

            for (int i = 1; i < sorted; i++) {
                int a = array[i - 1];
                int b = array[i];
                if (a > b) {
                    swap(array, i - 1, i);
                    swap += 1;
                }
            }

            sorted -= 1;

        } while (swap != 0);
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

        Graph graph = new Graph();
        for (int i = 10; i < 10000; i += 10) {
            long start = System.currentTimeMillis();
            int[] array = generateArray(i);
            bubbleSort(array);
            graph.add(i, (int) (System.currentTimeMillis() - start));
        }

        graph.printGraph(30, 10);
    }
}
