import java.util.*;
import java.util.stream.Stream;

public class Main {
    static Scanner reader = new Scanner(System.in);

    public static Integer getInt(String message) {
        System.out.print(message);
        try {
            return Integer.parseInt(reader.nextLine());
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Integer> getIntList() {
        ArrayList<Integer> list = new ArrayList<>();

        while (true) {
            Integer number = getInt("Enter a number or hit enter: ");
            if (number == null) {
                return list;
            } else {
                list.add(number);
            }
        }
    }

    public static int greatest(ArrayList<Integer> list) {
        int greatest = list.get(0);

        for (int i : list) {
            if (i > greatest) {
                greatest = i;
            }
        }

        return greatest;
    }

    public static int sum(ArrayList<Integer> list) {
        int total = 0;

        for (int i : list) {
            total += i;
        }

        return total;
    }

    public static double average(ArrayList<Integer> list) {
        return sum(list) / (double) list.size();
    }

    public static double median(ArrayList<Integer> list) {
        Collections.sort(list);

        if (list.size() % 2 == 0) {
            return (list.get(list.size() / 2 - 1) + list.get(list.size() / 2)) / 2.0;
        } else {
            return list.get(list.size() / 2);
        }
    }

    public static double variance(ArrayList<Integer> list) {
        double average = average(list);
        double unweightedVariance = list.stream().mapToDouble(elem -> Math.pow(elem - average, 2)).sum();

        return unweightedVariance / (list.size() - 1);
    }

    public static void main(String[] args) {
        ArrayList data = new ArrayList<>(List.of(10, 12, 7, 8));
        System.out.println(variance(data));
    }
}