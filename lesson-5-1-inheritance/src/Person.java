public class Person {
    private String name;
    private char gender;

    public Person(String name, char gender) {
        this.name = name;
        this.gender = gender;
    }

    public void greet() {
        System.out.println("hello");
    }

    @Override
    public String toString() {
        return name;
    }
}
