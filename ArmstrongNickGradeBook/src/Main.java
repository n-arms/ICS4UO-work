import java.util.ArrayList;
import java.util.List;
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
        return getValue("Enter an integer: ", "Invalid input.", Integer::parseInt);
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
    public static int getMark() {
        return getIntInRange("Enter a mark or -1 for a no mark: ", -1, 100);
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
        course.addStudent(new Student(name, number, marks));
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
        course.printStudents();
        Student toRemove = getStudent();

        course.removeStudent(toRemove);

        return Main::topMenu;
    }
    private static Menu editStudentMarkMenu() {
        course.printStudents();
        Student toEdit = getStudent();
        toEdit.printMarks();

        System.out.println("1) Edit all marks");
        System.out.println("2) Edit some marks");
        System.out.println("0) Back");

        int action = getIntInRange("Enter an action: ", 0, 2);

        switch (action) {
            case 1:
                return () -> editStudentAllMarksMenu(toEdit);
            case 2:
                return () -> editStudentSomeMarksMenu(toEdit);
            case 0:
                return Main::topMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    private static Menu editStudentAllMarksMenu(Student toEdit) {
        System.out.println("Old Marks:");
        toEdit.printMarks();

        for (int i = 0; i < toEdit.getMarks().size(); i++) {
            int newMark = getMark();
            toEdit.editMark(i, newMark);
        }

        return Main::topMenu;
    }
    private static Menu editStudentSomeMarksMenu(Student toEdit) {
        System.out.println("Old Marks:");
        toEdit.printMarks();

        int action;
        do {
            System.out.println("1) Edit a mark");
            System.out.println("0) Back");
            action = getIntInRange("", 0, 1);

            if (action == 1) {
                int assignment = getIntInRange("Which mark # do you wish to change: ", 0, toEdit.getMarks().size() - 1);
                int mark = getMark();
                toEdit.editMark(assignment, mark);
            }
        } while  (action != 0);

        System.out.println("New Marks:");
        toEdit.printMarks();

        return Main::topMenu;
    }
    private static Menu assignmentMenu() {
        System.out.println("1) Edit marks for an assignment");
        System.out.println("2) Add an assignment");
        System.out.println("3) Delete an assignment");
        System.out.println("0) Exit");

        int action = getIntInRange("Enter an action: ", 0, 3);

        switch (action) {
            case 1:
                return editAssignmentMarkMenu();
            case 2:
                return addAssignmentMenu();
            case 3:
                return deleteAssignmentMenu();
            case 0:
                return Main::topMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    private static Menu editAssignmentMarkMenu() {
        course.printAssignments();

        int assignment = getIntInRange("Enter the assignment to edit: ", 0, course.getAssignments() - 1);

        System.out.println("1) Edit all marks for an assignment");
        System.out.println("2) Edit some marks for an assignment");
        System.out.println("0) Exit");

        int action = getIntInRange("Enter an action: ", 0, 2);

        switch (action) {
            case 1:
                return () -> editAssignmentAllMarksMenu(assignment);
            case 2:
                return () -> editAssignmentSomeMarksMenu(assignment);
            case 0:
                return Main::topMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    private static Menu editAssignmentAllMarksMenu(int assignment) {
        course.printAssignment(assignment);

        for (Student s : course.getStudents()) {
            System.out.printf("The old mark for %s was %d%%.%n", s, s.getMark(assignment));
            int newMark = getMark();
            s.editMark(assignment, newMark);
        }

        return Main::topMenu;
    }
    private static Menu editAssignmentSomeMarksMenu(int assignment) {
        course.printAssignment(assignment);

        int action;
        do {
            System.out.println("1) Edit a mark");
            System.out.println("0) Exit");

            action = getIntInRange("Enter an action: ", 0, 1);

            if (action == 1) {
                int student = getIntInRange("Enter the mark # to edit", 0, course.getStudents().size() - 1);
                int newMark = getMark();
                course.setMark(student, assignment, newMark);
            }
        } while (action != 0);

        course.printAssignment(assignment);

        return Main::topMenu;
    }
    private static Menu addAssignmentMenu() {
        course.printStudents();
        ArrayList<Integer> marks = new ArrayList<>();

        for (int i = 0; i < course.getStudents().size(); i++) {
            System.out.printf("Student %d. ", i);
            int mark = getMark();
            marks.add(mark);
        }

        course.addAssignment(marks);

        course.printAssignment(course.getAssignments() - 1);

        return Main::topMenu;
    }
    private static Menu deleteAssignmentMenu() {
        course.printStudents();

        int assignment = getIntInRange("Enter the assignment you wish to delete or -1 to exit: ", -1, course.getAssignments() - 1);

        if (assignment != -1) {
            course.deleteAssignment(assignment);
        }

        return Main::topMenu;
    }
    private static Menu printMenu() {
        System.out.println("1) Calculate course average");
        System.out.println("2) Calculate assignment averages");
        System.out.println("3) Calculate student averages");
        System.out.println("4) Print student list");
        System.out.println("5) Print assignment marks");
        System.out.println("6) Print student marks");
        System.out.println("0) Back");

        int action = getIntInRange("Enter an action: ", 0, 6);

        switch (action) {
            case 1:
                return Main::courseAverageMenu;
            case 2:
                return Main::assignmentAverageMenu;
            case 3:
                return Main::studentAverageMenu;
            case 4:
                return Main::studentListMenu;
            case 5:
                return Main::assignmentMarkMenu;
            case 6:
                return Main::studentMarkMenu;
            case 0:
                return Main::topMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 6] should get past input validation");
        }
    }
    public static Menu courseAverageMenu() {
        System.out.println(course);
        System.out.printf("The course average is %f%%.%n", course.average());

        return Main::topMenu;
    }
    public static Menu assignmentAverageMenu() {
        System.out.println("1) Print some assignment averages");
        System.out.println("2) Print all assignment averages");
        System.out.println("0) Exit");

        int action = getIntInRange("Enter an action:", 0, 2);

        switch (action) {
            case 1:
                return Main::printSomeAssignmentAveragesMenu;
            case 2:
                return Main::printAllAssignmentAveragesMenu;
            case 0:
                return Main::topMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    public static Menu printSomeAssignmentAveragesMenu() {
        int action;
        do {
            System.out.println("1) Print an assignment average");
            System.out.println("0) Exit");

            action = getIntInRange("Enter an action: ", 0, 1);

            if (action == 1) {
                int assignment = getIntInRange("Enter the assignment to print: ", 0, course.getAssignments() - 1);
                System.out.printf("The average for assignment %d is %f%%%n", assignment, course.assignmentAverage(assignment));
            }
        } while (action != 0);

        return Main::topMenu;
    }
    public static Menu printAllAssignmentAveragesMenu() {
        for (int i = 0; i < course.getAssignments(); i++) {
            System.out.printf("The average for assignment %d is %f%%%n", i, course.assignmentAverage(i));
        }

        return Main::topMenu;
    }
    public static Menu studentAverageMenu() {
        System.out.println("1) Print some student averages");
        System.out.println("2) Print all student averages");
        System.out.println("0) Exit");

        int action = getIntInRange("Enter an action: ", 0, 2);

        switch (action) {
            case 1:
                return Main::printSomeStudentAveragesMenu;
            case 2:
                return Main::printAllStudentAveragesMenu;
            case 0:
                return Main::topMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    public static Menu printSomeStudentAveragesMenu() {
        int action;
        do {
            System.out.println("1) Print a student average");
            System.out.println("0) Exit");

            action = getIntInRange("Enter an action: ", 0, 1);

            if (action == 1) {
                Student toPrint = getStudent();
                System.out.printf("The average of student %s is %f%%%n", toPrint, toPrint.average());
            }
        } while (action != 0);

        return Main::topMenu;
    }
    public static Menu printAllStudentAveragesMenu() {
        for (Student s : course.getStudents()) {
            System.out.printf("The average of student %s is %f%%%n", s, s.average());
        }

        return Main::topMenu;
    }
    public static Menu studentListMenu() {
        System.out.println("1) Print student names and averages");
        System.out.println("2) Print student names, numbers, and averages");
        System.out.println("0) Exit");

        int action = getIntInRange("Enter an action: ", 0, 2);
        boolean printStudentNumbers;

        switch (action) {
            case 1:
                printStudentNumbers = false;
                break;
            case 2:
                printStudentNumbers = true;
                break;
            case 0:
                return Main::topMenu;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }

        course.printStudentList(printStudentNumbers);

        return Main::topMenu;
    }
    public static Menu assignmentMarkMenu() {
        course.printAssignments();

        int assignment = getIntInRange("Enter an assignment #: ", 0, course.getAssignments() - 1);

        for (Student s : course.getStudents()) {
            int mark = s.getMark(assignment);
            if (mark != -1) {
                System.out.printf("Student %s got a %d%%%n", s.getName(), mark);
            }
        }

        return Main::topMenu;
    }
    public static Menu studentMarkMenu() {
        course.printStudents();

        Student toPrint = getStudent();
        ArrayList<Integer> marks = toPrint.getMarks();

        for (int i = 0; i < marks.size(); i++) {
            int mark = marks.get(i);
            if (mark != -1) {
                System.out.printf("Assignment %d. %d%%%n", i, mark);
            }
        }

        return Main::topMenu;
    }
    public static void main(String[] args) {
        course.addStudent(new Student("Alan T", "110101011", List.of(83, 71, 76, 91, 85)));
        course.addStudent(new Student("Donald K", "314159265", List.of(84, 90, 88, 99, 80)));
        course.addStudent(new Student("Albert E", "299792458", List.of(93, 65, 95, 40, 19)));
        course.addStudent(new Student("Marie C", "002661600", List.of(76, 52, 96, 92, 66)));
        course.addStudent(new Student("Ada L", "018151210", List.of(91, 98, 89, 99, 99)));

        Menu current = Main::topMenu;

        while (current != null) {
            current = current.next();
            waitForInput();
        }
    }
}