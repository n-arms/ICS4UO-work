public class Main {
    public static void main(String[] args) {
        Person p = new Person("Alan", 42);
        Person q = new Person("Ada", 39);
        Person r = new Person("Albert", 99);
        System.out.println(p);
        p.getOlder();
        System.out.println(p);

        People people = new People();
        people.addPerson(p);
        people.addPerson(q);
        people.addPerson(r);
        people.addPerson("Alice", 46);
        System.out.println(people);
        people.happyNewYear();
        people.printPeople();
        people.removePerson(new Person("Alice", 47));
        people.printPeople();
    }
}