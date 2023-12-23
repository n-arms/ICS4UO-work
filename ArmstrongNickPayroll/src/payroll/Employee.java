package payroll;

/**
 * A generic employee that records their number, name, and title.
 */
public abstract class Employee {
    protected String employeeNumber;
    protected String lastName;
    protected String firstName;
    protected String jobTitle;

    /**
     * Construct a new employee.
     * @param employeeNumber the employee's number
     * @param lastName the employee's last name
     * @param firstName the employee's first name
     * @param jobTitle the employee's job title
     */
    public Employee(String employeeNumber, String lastName, String firstName, String jobTitle) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.jobTitle = jobTitle;
    }

    /**
     * Get the employee's number.
     * @return the employee number
     */
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Get the employee's last name.
     * @return the employee's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the employee's first name.
     * @return the employee's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the employee's job title.
     * @return the employee's job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public String toString() {
        return employeeNumber + ", " + firstName + " " + lastName + ", " + jobTitle;
    }

    /**
     * Return the amount to pay the given employee for the current month's work.
     * @return the amount of money
     */
    public abstract double pay();

    /**
     * Use up a given number of sick days.
     * @param amount the number of sick days to use
     */
    public abstract void useSickDay(double amount);

    /**
     * Get the number of sick days the employee has.
     * @return the number of sick days
     */
    public abstract double getSickDays();

    /**
     * Reset the number of sick days.
     */
    public abstract void resetSickDays();

    /**
     * Print the employee's pay stub.
     */
    public abstract void printPayStub();
}
