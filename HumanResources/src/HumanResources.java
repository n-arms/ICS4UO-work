
/**
 * Class that tests the Employee and Employees classes.
 * @author mrcho
 */
public class HumanResources {
    public static void main(String[] args) {
        System.out.println("--Creating two Employee instances...");
        Employee emp1 = new Employee("Alan T", "12AX5", 80000, 1);
        Employee emp2 = new Employee("Donald K", "12AX6");
        System.out.println(emp1);
        System.out.println(emp2);
        System.out.println("");

        System.out.println("--Set Donald K's salary to 90000 and give 11 sick days...");
        emp2.setSalary(90000);
        emp2.setSickDays(11);
        System.out.println(emp2);
        System.out.println("");

        System.out.println("--Creating an Employees instance, and adding ");
        System.out.println("--Alan and Donald and two new employees...");
        Employees staff = new Employees();
        staff.addEmployee(emp1);
        staff.addEmployee(emp2);
        staff.addEmployee(new Employee("Grace H", "12AX7", 85000, 8));
        staff.addEmployee(new Employee("Shafi G", "12AX8", 99000, 12));
        System.out.println("--Here is the staff list...");
        System.out.println(staff);
        System.out.println("");

        System.out.println("--Remove Donald...");
        staff.removeEmployee("Donald K");
        System.out.println("--Try to remove 12AX0...");
        staff.removeEmployee("12AX0");
        System.out.println("");

        System.out.println("--Use two sick days for Alan...");
        staff.useSickDay("Alan T");
        staff.useSickDay("Alan T");
        System.out.println("--Alan's sick days left: " + staff.getEmployee("Alan T").getSickDays());
        System.out.println("--Try to use a sick day for Monty P...");
        staff.useSickDay("Monty P");
        System.out.println("");


        System.out.println("--Give Shafi a raise of $10000...");
        staff.giveRaise("12AX8", 10000);
        System.out.println("--Try to give Linus a raise of $1...");
        staff.giveRaise("Linus T", 1);
        System.out.println("");

        System.out.println("--The final staff list...");
        System.out.println(staff);
    }
}
