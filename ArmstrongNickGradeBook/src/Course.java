import java.util.ArrayList;

public class Course {
    private String name;
    private String code;
    private ArrayList<Student> students;
    private int assignments = 0;
    public Course(String name, String code, ArrayList<Student> students) {
        this.name = name;
        this.code = code;
        this.students = students;
    }
    public Course(String name, String code) {
        this(name, code, new ArrayList<>());
    }
    public Student getStudent(String nameOrNumber) {
        for (Student s : students) {
            if (s.getName() == nameOrNumber || s.getName() == nameOrNumber) {
                return s;
            }
        }
        return null;
    }
    public void addOrEditStudent(Student updated) {
        Student existing = getStudent(updated.getNumber());
        if (existing == null) {
            existing = getStudent(updated.getName());
        }
        if (existing != null) {
            students.remove(existing);
        }
        students.add(updated);
    }
    public void removeStudent(String nameOrNumber) {
        Student s = getStudent(nameOrNumber);
        students.remove(s);
    }
    public int getAssignments() {
        return assignments;
    }
    public void printStudents() {
        for (Student s : students) {
            System.out.printf("%s%n", s);
        }
    }
}
