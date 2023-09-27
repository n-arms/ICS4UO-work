import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    static Scanner reader = new Scanner(System.in);

    public static Integer getInt(String message) {
        System.out.print(message);
        try {
            return Integer.parseInt(reader.nextLine());
        } catch (Exception e) {
            return None;
        }
    }

    public ArrayList<Integer> getIntList() {
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

    public int greatest(ArrayList<Integer> list) {
        int greatest = list.get(0);

        for (int i : list) {
            if (i > greatest) {
                greatest = i;
            }
        }

        return greatest;
    }

    public int sum(ArrayList<Integer> list) {
        int total = 0;

        for (int i : list) {
            total += i;
        }

        return total;
    }

    public double average(ArrayList<Integer> list) {
        return sum(list) / (double) list.size();
    }

    public double median(ArrayList<Integer> list) {
        Collections.sort(list);

        if (list.size() % 2 == 0) {
            return (list.get(list.size() / 2 - 1) + list.get(list.size() / 2)) / 2.0;
        } else {
            return list.get(list.size() / 2);
        }
    }

    public double variance(ArrayList<Integer> list) {
        double average = average(list);
        double variance = list.stream().map(elem -> Math.pow(elem - average, 2));
    }

    public static void main(String[] args) {
        Integer[] ints = {1, 2, 3};
        Number[] nums = ints;
        nums[0] = 0.0;
    }
}