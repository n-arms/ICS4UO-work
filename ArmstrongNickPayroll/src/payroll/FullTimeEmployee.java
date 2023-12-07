package payroll;

public class FullTimeEmployee extends Employee {
    private static final double YEARLY_SICK_DAYS = 17;
    private static final int MONTHS = 12;
    private double yearlySalary;
    private double sickDaysLeft;

    public FullTimeEmployee(String employeeNumber, String lastName, String firstName, String jobTitle, double yearlySalary, double sickDaysLeft) {
        super(employeeNumber, lastName, firstName, jobTitle);

        this.yearlySalary = yearlySalary;
        this.sickDaysLeft = sickDaysLeft;
    }

    @Override
    public double pay() {
        return yearlySalary / MONTHS;
    }

    @Override
    public void useSickDay(double amount) {
        sickDaysLeft -= amount;
    }

    @Override
    public double getSickDays() {
        return YEARLY_SICK_DAYS - sickDaysLeft;
    }

    @Override
    public void resetSickDays() {
        sickDaysLeft = YEARLY_SICK_DAYS;
    }

    @Override
    public void printPayStub() {
        // no
    }
}
