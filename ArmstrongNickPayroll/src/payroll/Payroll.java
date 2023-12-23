package payroll;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Payroll provides the logic for interacting with a list of employees, dealing with pay stubs and sick days.
 */
public class Payroll {
    private ArrayList<Employee> staffList;

    /**
     * Construct a new Payroll with no employees.
     */
    public Payroll() {
        staffList = new ArrayList<>();
    }

    /**
     * Populate a Payroll with employee data saved in a csv file.
     * @param filename the name of the file
     * @return whether the employee data was correctly loaded
     */
    public boolean loadStaffList(String filename) {
        // run all the code in a try-catch in case the file doesn't exist
        try {
            Scanner reader = new Scanner(new File(filename));

            // [,\n] means calls to .next will return next tokens seperated by either a comma or a newline
            reader.useDelimiter("[,\n]");

            // repeat for all the lines in the file
            while (reader.hasNext()) {
                // read the fields common to all employees
                String id = reader.next();
                String lastName = reader.next();
                String firstName = reader.next();
                String jobTitle = reader.next();
                String status = reader.next();

                // read employee-specific fields and add the resulting employees to the master list
                switch (status) {
                    case "full-time": {
                        double salary = Double.parseDouble(reader.next());
                        double sickDaysLeft = Double.parseDouble(reader.next());
                        staffList.add(new FullTimeEmployee(id, lastName, firstName, jobTitle, salary, sickDaysLeft));
                        break;
                    }
                    case "part-time": {
                        double hoursAssigned = reader.nextDouble();
                        double hourlyWage = reader.nextDouble();
                        double sickDaysTaken = reader.nextDouble();
                        staffList.add(new PartTimeEmployee(id, lastName, firstName, jobTitle, hoursAssigned, hourlyWage, sickDaysTaken));
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Incorrectly formatted document");
                    }
                }
                reader.nextLine();
            }

            reader.close();

            return true;
        } catch (IOException iox) {
            // print an appropriate error message if the file can't be read
            System.out.println("Problem reading file.");
            return false;
        }
    }

    /**
     * Save the employee data from the Payroll to a csv file
     * @param filename the name of the file
     * @return whether the employee data was correctly loaded
     */
    public boolean saveStaffList(String filename) {
        // run all the code in a try-catch block in case there are io problems
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));

            // loop over all the employees
            for (Employee employee : staffList) {
                // write out the generic employee information to the csv file
                String genericDescription = String.format("%s,%s,%s,%s,", employee.getEmployeeNumber(), employee.getLastName(), employee.getFirstName(), employee.getJobTitle());
                out.write(genericDescription);
                // write out employee-specific data
                if (employee instanceof FullTimeEmployee) {
                    FullTimeEmployee fullTimeEmployee = (FullTimeEmployee) employee;
                    double yearlySalary = fullTimeEmployee.getYearlySalary();
                    out.write(String.format("full-time,%.1f", yearlySalary));
                } else if (employee instanceof PartTimeEmployee) {
                    PartTimeEmployee partTimeEmployee = (PartTimeEmployee) employee;
                    double hoursAssigned = partTimeEmployee.getNumHoursAssigned();
                    double hourlyWage = partTimeEmployee.getHourlyWage();
                    out.write(String.format("part-time,%.1f,%.1f", hoursAssigned, hourlyWage));
                }
                // write out the sick days
                out.write(String.format(",%.1f", employee.getSickDays()));
                out.newLine();
            }

            out.flush();
            out.close();

            return true;
        } catch (IOException iox) {
            // print an appropriate error message if the file can't be written to
            System.out.println("Problem writing file.");
            return false;
        }
    }

    /**
     * Print a brief list of all employees, including generic employee information and their contract status.
     */
    public void listAllEmployees() {
        System.out.println("All Employees:");
        for (Employee employee : staffList) {
            System.out.printf("Employee: %s%n", employee);
        }
        System.out.println();
    }

    /**
     * Find an employee with the given id, or null if there is no such employee.
     * @param id the employee id to look for
     * @return the target employee
     */
    public Employee getEmployee(String id) {
        for (Employee employee : staffList) {
            if (employee.getEmployeeNumber().equals(id)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Print the pay stub for the employee with the given id, or an error message if there is no such employee.
     * @param id the employee id
     */
    public void printEmployeePayStub(String id) {
        Employee toPrint = getEmployee(id);

        if (toPrint == null) {
            System.out.printf("Employee %s not found!%n", id);
        } else {
            toPrint.printPayStub();
        }
        System.out.println();
    }

    /**
     * Print the pay stubs for all employees.
     */
    public void printAllPayStubs() {
        System.out.println("All Employee Pay Stubs:");
        for (Employee employee : staffList) {
            employee.printPayStub();
        }
        System.out.println();
    }

    /**
     * Use some sick days for a given employee, or print an error message if there is no such employee.
     * @param id the employee id
     * @param amount the number of sick days to use
     */
    public void enterSickDay(String id, double amount) {
        Employee toEdit = getEmployee(id);
        if (toEdit != null) {
            toEdit.useSickDay(amount);
        } else {
            System.out.printf("Employee %s not found!%n", id);
        }
        System.out.println();
    }

    /**
     * Reset employee sick days as if a year has gone by.
     */
    public void yearlySickDayReset() {
        for (Employee employee : staffList) {
            if (employee instanceof FullTimeEmployee) {
                employee.resetSickDays();
            }
        }
    }

    /**
     * Reset employee sick days as if a month has gone by.
     */
    public void monthlySickDayReset() {
        for (Employee employee : staffList) {
            if (employee instanceof PartTimeEmployee) {
                employee.resetSickDays();
            }
        }
    }
}
