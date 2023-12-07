public class Student extends Person {
    private String number;

    public Student(String name, int age, String number) {
        super(name, age);
        this.number = number;
    }

    @Override
    public String toString() {
        return super.toString() + ", ID: " + number;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            if (o.getClass() == this.getClass()) {
                Student s = (Student) o;
                return s.number.equals(this.number);
            }
        }
        return false;
    }
}