package payroll;

public abstract class Employee {
    protected String employeeNumber;
    protected String lastName;
    protected String firstName;
    protected String jobTitle;

    public Employee(String employeeNumber, String lastName, String firstName, String jobTitle) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.jobTitle = jobTitle;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

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
     *
     * @return
     */
    public abstract double getSickDays();
    public abstract void resetSickDays();
    public abstract void printPayStub();
}
