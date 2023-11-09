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
            if (s.getName().equals(nameOrNumber) || s.getNumber().equals(nameOrNumber)) {
                return s;
            }
        }
        return null;
    }
    public void addStudent(Student s) {
        Student existing = getStudent(s.getNumber());
        if (existing == null) {
            existing = getStudent(s.getName());
        }
        if (existing != null) {
            students.remove(existing);
        }
        students.add(s);
    }
    public void removeStudent(Student toRemove) {
        students.remove(toRemove);
    }
    public int getAssignments() {
        return assignments;
    }
    public void printStudents() {
        for (Student s : students) {
            System.out.printf("%s%n", s);
        }
    }
    public ArrayList<Student> getStudents() {
        return students;
    }

    public void printAssignment(int assignment) {
        for (Student s : students) {
            System.out.printf("%s: %d%%%n", s, s.getMark(assignment));
        }
    }
    public void setMark(int student, int assignment, int mark) {
        students.get(student).editMark(assignment, mark);
    }
    public void addAssignment(ArrayList<Integer> marks) {
        for (int i = 0; i < students.size(); i++) {
            students.get(i).addMark(marks.get(i));
        }
    }
    public void deleteAssignment(int assignment) {
        for (Student s : students) {
            s.deleteAssignment(assignment);
        }
    }
    public double average() {
    }
}
