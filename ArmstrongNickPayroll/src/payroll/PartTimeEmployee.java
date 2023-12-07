package payroll;

public class PartTimeEmployee extends Employee {
    private static final double HOURS_PER_DAY = 7;
    private double numHoursAssigned;
    private double hourlyWage;
    private double sickDaysTaken;

    public PartTimeEmployee(String employeeNumber, String lastName, String firstName, String jobTitle, double numHoursAssigned, double hourlyWage, double sickDaysTaken) {
        super(employeeNumber, lastName, firstName, jobTitle);
        this.numHoursAssigned = numHoursAssigned;
        this.hourlyWage = hourlyWage;
        this.sickDaysTaken = sickDaysTaken;
    }

    @Override
    public double pay() {
        return hourlyWage * (numHoursAssigned - sickDaysTaken * HOURS_PER_DAY);
    }

    @Override
    public void useSickDay(double amount) {
        sickDaysTaken += amount;
    }

    @Override
    public double getSickDays() {
        return sickDaysTaken;
    }

    @Override
    public void resetSickDays() {
        sickDaysTaken = 0;
    }

    @Override
    public void printPayStub() {
        // no
    }
}
