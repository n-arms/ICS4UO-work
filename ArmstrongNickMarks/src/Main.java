import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.println("MARKS PROGRAM");

        // get the number of students and tests from the user
        int numStudents = getInt(reader, "Enter the number of students: ");
        int numTests = getInt(reader, "Enter the number of tests: ");

        System.out.println();

        // get the marks of each student from the user
        double[][] marks = getMarks(reader, numStudents, numTests);

        // calculate and print each student's average and marks
        double[] stuAvgs = computeAverages(marks);
        System.out.println();
        printGrades(marks, stuAvgs);

        // calculate and print the class average
        double classAverage = average(stuAvgs);
        System.out.printf("Class average: %.1f%n%n", classAverage);

        // find and print the student with the highest average
        int bestStudentIndex = maxIndex(stuAvgs);
        double bestAverage = stuAvgs[bestStudentIndex];
        System.out.printf("Highest average: Student %d, average %.1f%n%n", bestStudentIndex, bestAverage);

        // print all the students who beat the class average
        printBestStudents(stuAvgs, classAverage);
    }

    // get an integer from the user using the given message
    public static int getInt(Scanner reader, String message) {
        System.out.print(message);
        return Integer.parseInt(reader.nextLine());
    }

    // get a list of marks for the given number of students and tests from the user
    public static double[][] getMarks(Scanner reader, int numStudents, int numTests) {
        // initialize an array for marks
        double[][] marks = new double[numStudents][numTests];

        // loop over all the students
        for (int student = 0; student < numStudents; student++) {
            System.out.printf("Enter all the marks for Student %d:%n", student);

            // loop over all the tests
            for (int test = 0; test < numTests; test++) {
                // get the student's mark and store it in the array
                System.out.printf("mark %d: ", test);
                double mark = Double.parseDouble(reader.nextLine());
                marks[student][test] = mark;
            }
        }

        return marks;
    }

    // calculate the average of a non-empty array of numbers
    private static double average(double[] numbers) {
        // find the sum of the numbers
        double total = 0;
        for (double number : numbers) {
            total += number;
        }
        // return the average
        return total / numbers.length;
    }

    // calculate the average of each student
    private static double[] computeAverages(double[][] marks) {
        // initialize an array for the averages
        double[] stuAvgs = new double[marks.length];

        // loop over each student
        for (int student = 0; student < marks.length; student++) {
            // find the student's average and store it in the array
            double average = average(marks[student]);
            stuAvgs[student] = average;
        }

        return stuAvgs;
    }

    // pretty print the grades and averages from the given parallel arrays of marks and averages
    private static void printGrades(double[][] marks, double[] stuAvgs) {
        System.out.println("The test marks:");

        // loop over all the students
        for (int student = 0; student < marks.length; student++) {
            System.out.printf("Student %d%n", student);
            System.out.print("marks: ");

            // find the number of tests that the student wrote
            int numTests = marks[student].length;
            // loop over each mark they received
            for (int test = 0; test < numTests; test++) {
                double mark = marks[student][test];

                // check if the mark is the last one to be printed
                if (test == numTests - 1) {
                    // if it is, print a newline
                    System.out.printf("%.1f%n", mark);
                } else {
                    // if it isn't, print a mark
                    System.out.printf("%.1f, ", mark);
                }
            }

            // print the average of the current student
            double average = stuAvgs[student];
            System.out.printf("average: %.1f%n%n", average);
        }
    }

    // find the index of the maximum element in a non-empty array
    private static int maxIndex(double[] numbers) {
        // assume the maximum element is at index 0
        int maxIndex = 0;

        // loop over all the indexes of the array
        for (int i = 0; i < numbers.length; i++) {
            // check if the value at that index is greater than the previous largest index, updating as necessary
            if (numbers[i] > numbers[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    // print the students who scored an average higher than the class average
    private static void printBestStudents(double[] stuAvgs, double classAverage) {
        System.out.println("Students above class average:");

        // loop over all the students
        for (int student = 0; student < stuAvgs.length; student++) {
            // check if a student is above the class average, then print them out
            if (stuAvgs[student] >= classAverage) {
                System.out.printf("Student %d%n", student);
            }
        }
    }
}