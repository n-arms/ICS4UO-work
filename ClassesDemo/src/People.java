import java.util.ArrayList;

public class People {
    private ArrayList<Person> people;

    public People() {
        people = new ArrayList<>();
    }
    public void addPerson(Person p) {
        people.add(p);
    }
    public void addPerson(String name, int age) {
        people.add(new Person(name, age));
    }
    @Override
    public String toString() {
        return people.toString();
    }
    public void happyNewYear() {
        people.forEach(Person::getOlder);
    }
    public void printPeople() {
        System.out.println("People:");
        people.forEach(person -> System.out.printf("  %s%n", person));
    }
    public void removePerson(Person p) {
        people.removeIf(p::equals);
    }
}
