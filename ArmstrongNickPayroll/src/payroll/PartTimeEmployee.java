package payroll;

/**
 * A part time employee with assigned hours, an hourly wage, and a number of sick days taken.
 */
public class PartTimeEmployee extends Employee {
    private static final double HOURS_PER_DAY = 7;
    private double numHoursAssigned;
    private double hourlyWage;
    private double sickDaysTaken;

    /**
     * Construct a new full time employee.
     * @param employeeNumber the employee's number
     * @param lastName the employee's last name
     * @param firstName the employee's first name
     * @param jobTitle the employee's job title
     * @param numHoursAssigned the number of hours assigned
     * @param hourlyWage the employee's hourly wage
     * @param sickDaysTaken the number of sick days taken
     */
    public PartTimeEmployee(String employeeNumber, String lastName, String firstName, String jobTitle, double numHoursAssigned, double hourlyWage, double sickDaysTaken) {
        super(employeeNumber, lastName, firstName, jobTitle);
        this.numHoursAssigned = numHoursAssigned;
        this.hourlyWage = hourlyWage;
        this.sickDaysTaken = sickDaysTaken;
    }

    /**
     * Get the number of hours assigned to the employee.
     * @return the number of hours
     */
    public double getNumHoursAssigned() {
        return numHoursAssigned;
    }

    /**
     * Get the employee's hourly wage.
     * @return the hourly wage
     */
    public double getHourlyWage() {
        return hourlyWage;
    }

    /**
     * Return the amount to pay the given employee for the current month's work.
     * @return the amount of money
     */
    @Override
    public double pay() {
        return hourlyWage * (numHoursAssigned - sickDaysTaken * HOURS_PER_DAY);
    }

    /**
     * Use up a given number of sick days.
     * @param amount the number of sick days to use
     */
    @Override
    public void useSickDay(double amount) {
        sickDaysTaken += amount;
        System.out.printf("New sick days taken: %.1f%n", sickDaysTaken);
    }

    /**
     * Get the number of sick days the employee has remaining.
     * @return the number of sick days
     */
    @Override
    public double getSickDays() {
        return sickDaysTaken;
    }

    /**
     * Reset the number of sick days.
     */
    @Override
    public void resetSickDays() {
        sickDaysTaken = 0;
    }

    /**
     * Print the employee's pay stub.
     */
    @Override
    public void printPayStub() {
        System.out.println();
        System.out.println("--------------- PAY STUB ---------------");
        System.out.printf("Employee: %s%n", this);
        System.out.printf("Hourly wage: $%.2f%n", hourlyWage);
        System.out.printf("Number of hours assigned: %.1f%n", numHoursAssigned);
        System.out.printf("Sick days taken: %.1f%n", sickDaysTaken);
        System.out.printf("Current Month pay: $%.2f%n", pay());
        System.out.println("----------------------------------------");
        System.out.println();
    }

    @Override
    public String toString() {
        return super.toString() + ", part-time";
    }
}
