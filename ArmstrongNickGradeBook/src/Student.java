import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalDouble;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Student provides all the logic for interacting with the name, number and marks of a specific student.
 */
public class Student {
    private String name;
    private String number;
    private ArrayList<Integer> marks;
    public static final int NO_MARK = -1;
    public static final int MAX_MARK = 100;

    static private final int MARK_WIDTH = 6;
    static private final int ASSIGNMENT_WIDTH = 12;
    /**
     * Construct a new student.
     *
     * @param name the name of the student
     * @param number the student's number
     * @param marks the student's marks
     */
    public Student(String name, String number, Collection<? extends Integer> marks) {
        this.name = name;
        this.number = number;
        this.marks = new ArrayList<>(marks);
    }
    /**
     * Get the name of the student.
     *
     * @return the student's name
     */
    public String getName() {
        return name;
    }
    /**
     * Set the name of a student.
     *
     * @param name the student's new name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get the number of the student.
     *
     * @return the student's number
     */
    public String getNumber() {
        return number;
    }
    /**
     * Set the number of the student.
     *
     * @param number the student's new name
     */
    public void setNumber(String number) {
        this.number = number;
    }
    /**
     * Add a mark to the student's mark list.
     *
     * @param mark the new mark
     */
    public void addMark(int mark) {
        marks.add(mark);
    }
    /**
     * Change one of the student's marks.
     *
     * @param assignment the assignment number to edit
     * @param newMark the new mark
     */
    public void setMark(int assignment, int newMark) {
        marks.set(assignment, newMark);
    }
    /**
     * Get the student's marks.
     *
     * @return a list containing the student's marks
     */
    public ArrayList<Integer> getMarks() {
        return marks;
    }
    /**
     * Delete the mark of a given assignment.
     *
     * @param assignment the assignment number to delete
     */
    public void deleteAssignment(int assignment) {
        marks.remove(assignment);
    }
    /**
     * Calculate the student's average.
     *
     * @return the average
     */
    public double average() {
        // convert the marks into a stream containing doubles instead of integers, then take their average
        return averageMark(marks.stream().map(Double::valueOf));
    }

    /**
     * Calculate the average of a stream of marks.
     *
     * @param marks the marks to average
     * @return the average
     */
    public static double averageMark(Stream<Double> marks) {
        // find the average of a stream of marks by filtering out no marks, then taking the average
        OptionalDouble average = marks.mapToDouble(Double::doubleValue).filter(x -> x != Student.NO_MARK).average();

        // yield a no mark if there were no mark :)
        return average.orElse(Student.NO_MARK);
    }
    /**
     * Print the student's marks.
     */
    public void printMarks() {
        TableBuilder table = new TableBuilder();
        // add a column with assignment numbers from 0 to the largest assignment number
        table.addColumn("Assignment", ASSIGNMENT_WIDTH, IntStream.range(0, assignments()).mapToObj(String::valueOf));
        // add a column with the mark on each assignment
        table.addColumn("Mark", MARK_WIDTH, marks.stream().map(mark -> String.format("%d%%", mark)));
        // print out the table
        System.out.println(table.build());
    }
    /**
     * Get the mark of a specific assignment.
     *
     * @param assignment the assignment number
     * @return the mark
     */
    public int getMark(int assignment) {
        return marks.get(assignment);
    }
    @Override
    public String toString() {
        return name + " " + number;
    }
    /**
     * Calculate the number of assignments.
     *
     * @return the number of assignments
     */
    public int assignments() {
        return marks.size();
    }
}