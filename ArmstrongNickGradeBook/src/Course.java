import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Course contains all the logic for interacting with the students and assignments in a course.
 */
public class Course {
    private String name;
    private String code;
    private ArrayList<Student> students;
    private int assignments;
    static private final int MARK_WIDTH = 6;
    static private final int NAME_WIDTH = 10;
    static private final int NUMBER_WIDTH = 11;
    static private final int ASSIGNMENT_WIDTH = 12;
    static private final int AVERAGE_WIDTH = 10;
    /**
     * Construct a new course with the given name, code, and students.
     *
     * @param name the name of the course
     * @param code the course code
     * @param students the list of students
     */
    public Course(String name, String code, ArrayList<Student> students) {
        this.name = name;
        this.code = code;
        this.students = students;

        // set the number of assignments if there are any students to reference
        if (students.isEmpty()) {
            assignments = 0;
        } else {
            assignments = students.get(0).assignments();
        }
    }
    /**
     * Construct a new course with the given name and code, and no students.
     *
     * @param name the name of the course
     * @param code the course code
     */
    public Course(String name, String code) {
        this(name, code, new ArrayList<>());
    }
    /**
     * Find a student with the given name or number.
     *
     * @param nameOrNumber the name or number of the student
     * @return the student
     */
    public Student getStudent(String nameOrNumber) {
        for (Student s : students) {
            // check if the current student matches the given name or number
            if (s.getName().equals(nameOrNumber) || s.getNumber().equals(nameOrNumber)) {
                return s;
            }
        }
        return null;
    }
    /**
     * Add a student to the course.
     *
     * @param s the student to add
     */
    public void addStudent(Student s) {
        // if there aren't any students yet, set the number of assignments in the course
        // to be the number of assignments the student has done
        if (students.isEmpty()) {
            assignments = s.assignments();
        }

        students.add(s);
    }
    /**
     * Remove a student from the course.
     *
     * @param toRemove the student to remove
     */
    public void removeStudent(Student toRemove) {
        students.remove(toRemove);
    }
    /**
     * Get the number of assignments in the course.
     *
     * @return the number of assignments
     */
    public int getAssignments() {
        return assignments;
    }
    /**
     * Get the list of students in the course.
     *
     * @return the list of students
     */
    public ArrayList<Student> getStudents() {
        return students;
    }
    /**
     * Add an assignment to the course.
     *
     * @param marks each student's mark
     */
    public void addAssignment(ArrayList<Integer> marks) {
        assignments++;
        for (int i = 0; i < students.size(); i++) {
            // add the ith mark to the ith student
            students.get(i).addMark(marks.get(i));
        }
    }
    /**
     * Delete an assignment.
     *
     * @param assignment the assignment to remove
     */
    public void deleteAssignment(int assignment) {
        assignments--;
        for (Student s : students) {
            s.deleteAssignment(assignment);
        }
    }
    /**
     * Calculate the course average.
     *
     * @return the average
     */
    public double average() {
        // take the average of each student, then take the average of the those averages
        return Student.averageMark(students.stream().map(Student::average));
    }
    /**
     * Calculate the average mark of an assignment.
     *
     * @return the average
     */
    public double assignmentAverage(int assignment) {
        // find each student's mark in the given assignment, then find the average of those marks
        return Student.averageMark(students.stream().map(student -> (double) student.getMark(assignment)));
    }
    /**
     * Make a table containing the names and marks of each student.
     *
     * @return the table
     */
    public TableBuilder studentMarksTable() {
        // start with a class list of just names
        TableBuilder table = studentListTable(false, false);

        // add marks for each assignment
        for (int i = 0; i < assignments; i++) {
            // store in a final variable to bypass restrictions on mutating lambda captures
            final int assignment = i;
            // take all the marks for the given assignment and format them nicely with a % at the end
            Stream<String> marks = students.stream().map(student -> String.format("%d%%", student.getMark(assignment)));
            // add the nicely formatted assignment marks to the table
            table.addColumn(String.valueOf(i), MARK_WIDTH, marks);
        }
        return table;
    }
    /**
     * Make a table listing the students, optionally including student numbers and averages.
     *
     * @param withNumber whether the table has student numbers in it
     * @param withAverage whether the table has student averages in it
     * @return the table
     */
    public TableBuilder studentListTable(boolean withNumber, boolean withAverage) {
        TableBuilder table = new TableBuilder();
        // add a column with all student names
        table.addColumn("Name", NAME_WIDTH, students.stream().map(Student::getName));
        if (withNumber) {
            // if requested, add a column with all student numbers
            table.addColumn("Number", NUMBER_WIDTH, students.stream().map(Student::getNumber));
        }
        if (withAverage) {
            // if requested, find the average of each student, and format nicely with a %
            Stream<String> averages = students.stream().map(Student::average).map(average -> String.format("%.0f%%", average));
            // add a column with all student averages
            table.addColumn("Average", AVERAGE_WIDTH, averages);
        }
        return table;
    }
    /**
     * Make a table listing the students and their marks on a specific assignment.
     *
     * @param assignment the assignment
     * @return the table
     */
    public TableBuilder assignmentMarksTable(int assignment) {
        TableBuilder table = studentListTable(false, false);
        // find the marks on the given assignment, and format nicely with a %
        Stream<String> marks = students.stream().mapToInt(student -> student.getMark(assignment)).mapToObj(mark -> String.format("%d%%", mark));
        // add a column with all the marks on the assignment
        table.addColumn("Mark", MARK_WIDTH, marks);

        return table;
    }
    /**
     * Make a table listing the average mark on all assignments.
     *
     * @return the table
     */
    public TableBuilder assignmentAverageTable() {
        TableBuilder table = new TableBuilder();
        // add a column with the assignment numbers from 0 to the maximum assignment number
        table.addColumn("Assignment", ASSIGNMENT_WIDTH, IntStream.range(0, assignments).mapToObj(String::valueOf));
        // find the average mark in each assignment by iterating over all the assignment numbers,
        // finding the average mark for that assignment, and formatting it nicely with a %
        Stream<String> averages = IntStream.range(0, assignments).mapToDouble(this::assignmentAverage).mapToObj(avg -> String.format("%.0f%%", avg));
        // add a column with the average marks
        table.addColumn("Average", AVERAGE_WIDTH, averages);

        return table;
    }
    @Override
    public String toString() {
        return name + " " + code;
    }
}