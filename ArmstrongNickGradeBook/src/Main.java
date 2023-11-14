import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Main contains a user interface that lets the user interact with a course.
 */
public class Main {
    public static Scanner reader = new Scanner(System.in);
    public static Course course;

    /**
     * Get a value from the user.
     *
     * @param prompt the message to prompt the user with
     * @param error the message to print if the user inputs invalid input
     * @param predicate a function to call to convert the user input to the desired value
     * @return a valid value inputted by the user
     * @param <T> the type of value that will be gotten from the user
     */
    public static <T> T getValue(String prompt, String error, Function<String, T> predicate) {
        while (true) {
            // repeatedly print the prompt and get user input
            System.out.print(prompt);
            String line = reader.nextLine();
            try {
                // try to return the given function applied to the user input
                return predicate.apply(line);
            } catch (Exception e) {
                // if the given function throws, print out the error message and try again
                System.out.println(error);
            }
        }
    }

    /**
     * Get an integer in the inclusive range [low, high] from the user.
     *
     * @param prompt the message to prompt the user with
     * @param low the inclusive low bound
     * @param high the inclusive high bound
     * @return the integer
     */
    public static int getIntInRange(String prompt, int low, int high) {
        return getValue(prompt, "Invalid input.", line -> {
            // parse the user input as an integer
            int value = Integer.parseInt(line);
            // if it isn't inside the inclusive bounds [low, high], throw an exception
            if (value >= low && value <= high) {
                return value;
            } else {
                throw new IllegalArgumentException("Integer input out of bounds");
            }
        });
    }

    /**
     * Get a number representing an action from the user.
     *
     * @param low the inclusive lowest allowable action
     * @param high the inclusive highest allowable action
     * @return the action
     */
    public static int getAction(int low, int high) {
        return getIntInRange("Enter an action: ", low, high);
    }

    /**
     * Get a string from the user.
     *
     * @param prompt the message to prompt the user with
     * @return the string
     */
    public static String getString(String prompt) {
        return getValue(prompt, null, string -> string);
    }

    /**
     * Get a student from the user by inputting their name or number.
     *
     * @param prompt the message to prompt the user with
     * @return the student
     */
    public static Student getStudent(String prompt) {
        return getValue(prompt, "Enter a valid student name or number.", nameOrNumber -> {
            // look up the given student name or number
            Student student = course.getStudent(nameOrNumber);
            if (student == null) {
                // if they don't exist, throw an error
                throw new IllegalArgumentException("Did not enter a valid student name or number");
            } else {
                return student;
            }
        });
    }

    /**
     * Get a student from the user by inputting their name or number.
     *
     * @return the student
     */
    public static Student getStudent() {
        return getStudent("Enter the student's name or number: ");
    }

    /**
     * Get a mark from the user
     * @param prompt the message to prompt the user with
     * @return the mark
     */
    public static int getMark(String prompt) {
        return getIntInRange(prompt, Student.NO_MARK, Student.MAX_MARK);
    }

    /**
     * Wait for the user to engage with the program before continuing.
     */
    public static void waitForInput() {
        getValue("Press enter to continue.", null, x -> 0);
    }
    public static void topMenu() {
        int action;
        
        do {
            System.out.println("1) Edit students");
            System.out.println("2) Edit assignments");
            System.out.println("3) Print information");
            System.out.println("0) Quit");

            action = getAction(0, 3);
            
            switch (action) {
                case 0:
                    quitMenu();
                    break;
                case 1:
                    studentMenu();
                    break;
                case 2:
                    assignmentMenu();
                    break;
                case 3:
                    printMenu();
                    break;
                default:
                    throw new AssertionError("Only numbers in the range [0, 3] should get past input validation");
            }
        } while (action != 0);
    }
    public static void quitMenu() {
        System.out.println("Thank you for using GradeBook!");
    }
    public static void studentMenu() {
        System.out.println(course.studentListTable(true, false).build());

        System.out.println("1) Add a student");
        System.out.println("2) Edit a student's information");
        System.out.println("3) Delete a student");
        System.out.println("4) Edit a student's marks");
        System.out.println("0) Back");

        int action = getAction(0, 4);
        switch (action) {
            case 0:
                break;
            case 1:
                addStudentMenu();
                break;
            case 2:
                editStudentMenu();
                break;
            case 3:
                deleteStudentMenu();
                break;
            case 4:
                editStudentMarkMenu();
                break;
            default:
                throw new AssertionError("Only numbers in the range [0, 3] should get past input validation");
        }
    }
    public static void addStudentMenu() {
        String name = getString("Enter the student's name: ");
        String number = getString("Enter the student's number: ");

        ArrayList<Integer> marks = new ArrayList<>();
        for (int i = 0; i < course.getAssignments(); i++) {
            String prompt = String.format("Enter the mark for assignment %d, or -1 if not completed: ", i);
            int mark = getIntInRange(prompt,-1, 100);
            marks.add(mark);
        }
        Student toAdd = new Student(name, number, marks);
        course.addStudent(toAdd);
        System.out.printf("Added student %s.%n", toAdd);
        waitForInput();
    }
    public static void editStudentMenu() {
        Student toEdit = getStudent();

        String name = getString("Enter the student's new name: ");
        String number = getString("Enter the student's new number: ");

        toEdit.setName(name);
        toEdit.setNumber(number);

        System.out.printf("Updated student to %s.%n", toEdit);
        waitForInput();
    }
    private static void deleteStudentMenu() {
        Student toRemove = getStudent();

        course.removeStudent(toRemove);

        System.out.printf("Removed student %s.%n", toRemove);
        waitForInput();
    }
    private static void editStudentMarkMenu() {
        Student toEdit = getStudent();
        toEdit.printMarks();

        System.out.println("1) Edit all marks");
        System.out.println("2) Edit some marks");
        System.out.println("0) Back");

        int action = getAction(0, 2);

        switch (action) {
            case 1:
                editStudentAllMarksMenu(toEdit);
                break;
            case 2:
                editStudentSomeMarksMenu(toEdit);
                break;
            case 0:
                break;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    private static void editStudentAllMarksMenu(Student toEdit) {
        for (int i = 0; i < toEdit.getMarks().size(); i++) {
            int newMark = getMark(String.format("Enter the mark for assignment %d or -1 for a no mark: ", i));
            toEdit.setMark(i, newMark);
        }
        System.out.printf("Updated %s's marks.%n", toEdit.getName());
        waitForInput();
    }
    private static void editStudentSomeMarksMenu(Student toEdit) {
        int action;
        do {
            System.out.println("1) Edit a mark");
            System.out.println("0) Back");
            action = getIntInRange("", 0, 1);

            if (action == 1) {
                int assignment = getIntInRange("Enter the mark # to change: ", 0, toEdit.getMarks().size() - 1);
                int mark = getMark("Enter the mark or -1 for a no mark: ");
                toEdit.setMark(assignment, mark);
            }
        } while  (action != 0);

        System.out.printf("Updated %s's marks.%n", toEdit.getName());
        System.out.println("New Marks:");
        toEdit.printMarks();
        waitForInput();
    }
    private static void assignmentMenu() {
        System.out.println("1) Edit marks for an assignment");
        System.out.println("2) Add an assignment");
        System.out.println("3) Delete an assignment");
        System.out.println("0) Back");

        int action = getAction(0, 3);

        switch (action) {
            case 1:
                editAssignmentMarkMenu();
                break;
            case 2:
                addAssignmentMenu();
                break;
            case 3:
                deleteAssignmentMenu();
                break;
            case 0:
                break;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    private static void editAssignmentMarkMenu() {
        System.out.println(course.studentMarksTable().build());

        int assignment = getIntInRange("Enter the assignment to edit: ", 0, course.getAssignments() - 1);

        System.out.println("1) Edit all marks for an assignment");
        System.out.println("2) Edit some marks for an assignment");
        System.out.println("0) Back");

        int action = getAction(0, 2);

        switch (action) {
            case 1:
                editAssignmentAllMarksMenu(assignment);
                break;
            case 2:
                editAssignmentSomeMarksMenu(assignment);
                break;
            case 0:
                break;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    private static void editAssignmentAllMarksMenu(int assignment) {
        for (Student s : course.getStudents()) {
            int newMark = getMark(String.format("Enter the mark for %s or -1 for a no mark: ", s.getName()));
            s.setMark(assignment, newMark);
        }
        System.out.printf("Updated marks for assignment %d.%n", assignment);
        waitForInput();
    }
    private static void editAssignmentSomeMarksMenu(int assignment) {
        int action;
        do {
            System.out.println("1) Edit a mark");
            System.out.println("0) Back");

            action = getAction(0, 1);

            if (action == 1) {
                Student student = getStudent("Enter the student name or number to edit: ");
                int newMark = getMark("Enter the new mark or -1 for a no mark: ");
                student.setMark(assignment, newMark);
            }
        } while (action != 0);

        System.out.printf("Updated marks for assignment %d.%n", assignment);
        System.out.println(course.studentMarksTable().build());
        waitForInput();
    }
    private static void addAssignmentMenu() {
        System.out.println(course.studentListTable(true, false).build());
        ArrayList<Integer> marks = new ArrayList<>();

        for (Student s : course.getStudents()) {
            int mark = getMark(String.format("Enter the mark for %s or -1 for a no mark: ", s.getName()));
            marks.add(mark);
        }

        course.addAssignment(marks);

        System.out.println("Added assignment.");
        System.out.println(course.studentMarksTable().build());
        waitForInput();
    }
    private static void deleteAssignmentMenu() {
        System.out.println(course.studentMarksTable().build());

        int assignment = getIntInRange("Enter the assignment you wish to delete or -1 to exit: ", -1, course.getAssignments() - 1);

        if (assignment != -1) {
            course.deleteAssignment(assignment);
        }

        System.out.printf("Deleted assignment %d.%n", assignment);
        System.out.println(course.studentMarksTable().build());
        waitForInput();
    }
    private static void printMenu() {
        System.out.println("1) Calculate course average");
        System.out.println("2) Calculate assignment averages");
        System.out.println("3) Calculate student averages");
        System.out.println("4) Print student list");
        System.out.println("5) Print assignment marks");
        System.out.println("6) Print student marks");
        System.out.println("0) Back");

        int action = getAction(0, 6);

        switch (action) {
            case 1:
                courseAverageMenu();
                break;
            case 2:
                assignmentAverageMenu();
                break;
            case 3:
                studentAverageMenu();
                break;
            case 4:
                studentListMenu();
                break;
            case 5:
                assignmentMarkMenu();
                break;
            case 6:
                studentMarkMenu();
                break;
            case 0:
                break;
            default:
                throw new AssertionError("Only numbers in the range [0, 6] should get past input validation");
        }
    }
    public static void courseAverageMenu() {
        System.out.printf("The course average for %s is %f%%.%n", course, course.average());
        waitForInput();
    }
    public static void assignmentAverageMenu() {
        System.out.println("1) Print some assignment averages");
        System.out.println("2) Print all assignment averages");
        System.out.println("0) Back");

        int action = getAction(0, 2);

        switch (action) {
            case 1:
                printSomeAssignmentAveragesMenu();
                break;
            case 2:
                printAllAssignmentAveragesMenu();
                break;
            case 0:
                break;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    public static void printSomeAssignmentAveragesMenu() {
        System.out.println(course.studentMarksTable().build());

        int action;
        do {
            System.out.println("1) Print an assignment average");
            System.out.println("0) Back");

            action = getAction(0, 1);

            if (action == 1) {
                int assignment = getIntInRange("Enter the assignment to print: ", 0, course.getAssignments() - 1);
                System.out.printf("The average for assignment %d is %f%%.%n", assignment, course.assignmentAverage(assignment));
            }
        } while (action != 0);

        waitForInput();
    }
    public static void printAllAssignmentAveragesMenu() {
        System.out.println(course.assignmentAverageTable().build());

        waitForInput();
    }
    public static void studentAverageMenu() {
        System.out.println("1) Print some student averages");
        System.out.println("2) Print all student averages");
        System.out.println("0) Back");

        int action = getAction(0, 2);

        switch (action) {
            case 1:
                printSomeStudentAveragesMenu();
                break;
            case 2:
                printAllStudentAveragesMenu();
                break;
            case 0:
                break;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }
    }
    public static void printSomeStudentAveragesMenu() {
        System.out.println(course.studentListTable(false, false).build());
        int action;
        do {
            System.out.println("1) Print a student average");
            System.out.println("0) Back");

            action = getAction(0, 1);

            if (action == 1) {
                Student toPrint = getStudent();
                System.out.printf("The average of student %s is %f%%.%n", toPrint, toPrint.average());
            }
        } while (action != 0);

        waitForInput();
    }
    public static void printAllStudentAveragesMenu() {
        System.out.println(course.studentListTable(false, true).build());

        waitForInput();
    }
    public static void studentListMenu() {
        System.out.println("1) Print student names and averages");
        System.out.println("2) Print student names, numbers, and averages");
        System.out.println("0) Back");

        int action = getAction(0, 2);
        boolean printStudentNumbers;

        switch (action) {
            case 1:
                printStudentNumbers = false;
                break;
            case 2:
                printStudentNumbers = true;
                break;
            case 0:
                return;
            default:
                throw new AssertionError("Only numbers in the range [0, 2] should get past input validation");
        }

        System.out.println(course.studentListTable(printStudentNumbers, true).build());
        waitForInput();
    }
    public static void assignmentMarkMenu() {
        int assignment = getIntInRange("Enter an assignment #: ", 0, course.getAssignments() - 1);

        System.out.println(course.assignmentMarksTable(assignment).build());
        waitForInput();
    }
    public static void studentMarkMenu() {
        System.out.println(course.studentListTable(true, true).build());

        Student toPrint = getStudent();

        toPrint.printMarks();
        waitForInput();
    }
    public static void main(String[] args) {
        course = new Course("Introduction to Computer Science", "ICS101");
        course.addStudent(new Student("Alan T", "110101011", List.of(83, 71, 76, 91, 85)));
        course.addStudent(new Student("Donald K", "314159265", List.of(84, 90, 88, 99, 80)));
        course.addStudent(new Student("Albert E", "299792458", List.of(93, 65, 95, 40, 19)));
        course.addStudent(new Student("Marie C", "002661600", List.of(76, 52, 96, 92, 66)));
        course.addStudent(new Student("Ada L", "018151210", List.of(91, 98, 89, 99, 99)));

        topMenu();
    }
}