public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void greet() {
        System.out.println("hello");
    }

    @Override
    public String toString() {
        return "Name: " + name +", Age: " + age;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() == this.getClass()) {
            Person p = (Person) o;

            return p.name.equals(this.name) && p.age == this.age;
        } else {
            return false;
        }
    }
}
