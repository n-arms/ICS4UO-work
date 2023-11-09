import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;

@FunctionalInterface
interface Menu {
    Menu next();
}

public class Main {
    public static Scanner reader = new Scanner(System.in);
    public static Course course;
    public static <T> T getValue(String prompt, String error, Function<String, T> predicate) {
        while (true) {
            System.out.print(prompt);
            String line = reader.nextLine();
            try {
                return predicate.apply(line);
            } catch (Exception e) {
                System.out.println(error);
            }
        }
    }
    public static int getInt() {
        return getValue("Enter an integer: ", "Invalid input.", line -> Integer.parseInt(line));
    }
    public static int getIntInRange(String prompt, int low, int high) {
        return getValue(prompt, "Invalid input.", line -> {
            int value = Integer.parseInt(line);
            if (value >= low && value <= high) {
                return value;
            } else {
                throw new IllegalArgumentException("Integer input out of bounds");
            }
        });
    }
    public static String getString(String prompt) {
        return getValue(prompt, null, string -> string);
    }
    public static Student getStudent() {
        return getValue("Enter a student's name or number: ", "Enter a valid student name or number.", nameOrNumber -> {
            Student student = course.getStudent(nameOrNumber);
            if (student == null) {
                throw new IllegalArgumentException("Did not enter a valid student name or number");
            } else {
                return student;
            }
        });
    }
    public static void waitForInput() {
        getValue("Press enter to continue.", null, x -> 0);
    }
    public static Menu topMenu() {
        System.out.println("1) Edit students");
        System.out.println("2) Edit assignments");
        System.out.println("3) Print information");
        System.out.println("0) Quit");

        int action = getIntInRange("Enter an action: ", 0, 3);
        switch (action) {
            case 0:
                return Main::quitMenu;
            case 1:
                return Main::studentMenu;
            case 2:
                return Main::assignmentMenu;
            case 3:
                return Main::printMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 3] should get past input validation");
        }
    }
    public static Menu quitMenu() {
        System.out.println("Thank you for using GradeBook!");
        return null;
    }
    public static Menu studentMenu() {
        System.out.println("1) Add a student");
        System.out.println("2) Edit a student's information");
        System.out.println("3) Delete a student");
        System.out.println("4) Edit a student's marks");
        System.out.println("0) Back");

        int action = getIntInRange("Enter an action: ", 0, 4);
        switch (action) {
            case 0:
                return Main::topMenu;
            case 1:
                return Main::addStudentMenu;
            case 2:
                return Main::editStudentMenu;
            case 3:
                return Main::deleteStudentMenu;
            case 4:
                return Main::editStudentMarkMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 3] should get past input validation");
        }
    }
    public static Menu addStudentMenu() {
        String name = getString("Enter the student's name: ");
        String number = getString("Enter the student's number: ");

        ArrayList<Integer> marks = new ArrayList<>();
        for (int i = 0; i < course.getAssignments(); i++) {
            String prompt = String.format("Enter the mark for assignment %d, or -1 if not completed: ", i);
            int mark = getIntInRange(prompt,-1, 100);
            marks.add(mark);
        }
        course.addOrEditStudent(new Student(name, number, marks));
        return Main::topMenu;
    }
    public static Menu editStudentMenu() {
        course.printStudents();
        Student toEdit = getStudent();

        String name = getString("Enter the student's new name: ");
        String number = getString("Enter the student's new number: ");

        toEdit.setName(name);
        toEdit.setNumber(number);

        return Main::topMenu;
    }
    private static Menu deleteStudentMenu() {
        return null;
    }
    private static Menu editStudentMarkMenu() {
        return null;
    }
    private static Menu assignmentMenu() {
        return null;
    }
    private static Menu printMenu() {
        return null;
    }
    public static void main(String[] args) {
        Student[] students = {
            new Student()
        };
        course = new Course("Introduction to Computer Science", "CSC100", new ArrayList<Student>(Arrays.asList(students)));
        Menu current = Main::topMenu;

        while (current != null) {
            current = current.next();
            waitForInput();
        }
    }
}