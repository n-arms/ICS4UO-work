import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;

public class Student {
    private String name;
    private String number;
    private ArrayList<Integer> marks;
    public Student(String name, String number, ArrayList<Integer> marks) {
        this.name = name;
        this.number = number;
        this.marks = marks;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void addMark(int mark) {
        marks.add(mark);
    }
    public void editMark(int assignment, int newMark) {
        marks.set(assignment, newMark);
    }
    public ArrayList<Integer> getMarks() {
        return marks;
    }
    public void setMarks(ArrayList<Integer> marks) {
        this.marks = marks;
    }
    public void deleteAssignment(int assignment) {
        marks.remove(assignment);
    }
    public double average() {
        OptionalDouble average = marks.stream().mapToInt(Integer::intValue).filter(x -> x != -1).average();

        return average.orElseGet(() -> -1);
    }
    public void printMarks() {
        System.out.printf("%s%n", this);
        for (int i = 0; i < marks.size(); i++) {
            int mark = marks.get(i);
            if (mark != -1) {
                System.out.printf("Mark %d: %d%%%n", i, mark);
            }
        }
    }
    public int getMark(int assignment) {
        return marks.get(assignment);
    }
}