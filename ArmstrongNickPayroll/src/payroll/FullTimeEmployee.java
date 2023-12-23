package payroll;

/**
 * A full time employee with a yearly salary and a maximum number of sick days.
 */
public class FullTimeEmployee extends Employee {
    private static final double YEARLY_SICK_DAYS = 20;
    private static final int MONTHS = 12;
    private double yearlySalary;
    private double sickDaysLeft;

    /**
     * Construct a new full time employee.
     * @param employeeNumber the employee's number
     * @param lastName the employee's last name
     * @param firstName the employee's first name
     * @param jobTitle the employee's job title
     * @param yearlySalary the employee's yearly salary
     * @param sickDaysLeft the number of sick days left
     */
    public FullTimeEmployee(String employeeNumber, String lastName, String firstName, String jobTitle, double yearlySalary, double sickDaysLeft) {
        super(employeeNumber, lastName, firstName, jobTitle);

        this.yearlySalary = yearlySalary;
        this.sickDaysLeft = sickDaysLeft;
    }

    /**
     * Get the employee's yearly salary.
     * @return the employee's yearly salary
     */
    public double getYearlySalary() {
        return yearlySalary;
    }

    /**
     * Return the amount to pay the given employee for the current month's work.
     * @return the amount of money
     */
    @Override
    public double pay() {
        return yearlySalary / MONTHS;
    }

    /**
     * Use up a given number of sick days.
     * @param amount the number of sick days to use
     */
    @Override
    public void useSickDay(double amount) {
        sickDaysLeft -= amount;
        System.out.printf("New sick days left: %.1f%n", sickDaysLeft);
    }

    /**
     * Get the number of sick days the employee has remaining.
     * @return the number of sick days
     */
    @Override
    public double getSickDays() {
        return sickDaysLeft;
    }

    /**
     * Reset the number of sick days.
     */
    @Override
    public void resetSickDays() {
        sickDaysLeft = YEARLY_SICK_DAYS;
    }

    /**
     * Print the employee's pay stub.
     */
    @Override
    public void printPayStub() {
        System.out.println();
        System.out.println("--------------- PAY STUB ---------------");
        System.out.printf("Employee: %s%n", this);
        System.out.printf("Yearly salary: $%.2f%n", yearlySalary);
        System.out.printf("Current Month pay: $%.2f%n", pay());
        System.out.printf("Sick days left: %.1f%n", getSickDays());
        System.out.println("----------------------------------------");
        System.out.println();
    }

    @Override
    public String toString() {
        return super.toString() + ", full-time";
    }
}
