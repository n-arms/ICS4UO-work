public class Student extends Person {
    private String number;

    public Student(String name, char gender, String number) {
        super(name, gender);
        this.number = number;
    }
}