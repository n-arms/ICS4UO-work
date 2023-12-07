package payroll;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Payroll {
    private ArrayList<Employee> staffList;
    public Payroll() {
        staffList = new ArrayList<>();
    }
    public boolean loadStaffList(String filename) {
        try {
            Scanner reader = new Scanner(new File(filename));
            reader.useDelimiter(",");

            while (reader.hasNext()) {
                String id = reader.next();
                String lastName = reader.next();
                String firstName = reader.next();
                String jobTitle = reader.next();
                String status = reader.next();

                switch (status) {
                    case "full-time": {
                        double salary = reader.nextDouble();
                        double sickDaysLeft = reader.nextDouble();
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
            return false;
        }
    }

    public boolean saveStaffList(String filename) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));

            for (Employee employee : staffList) {
                out.write(employee.toString());
                out.newLine();
            }

            out.flush();
            out.close();

            return true;
        } catch (IOException iox) {
            return false;
        }
    }

    public void listAllEmployees() {
        for (Employee employee : staffList) {
            System.out.println(employee);
        }
    }

    public Employee getEmployee(String id) {
        for (Employee employee : staffList) {
            if (employee.getEmployeeNumber().equals(id)) {
                return employee;
            }
        }
        return null;
    }

    public void printEmployeePayStub(String id) {
        Employee toPrint = getEmployee(id);

        System.out.println("-------------------------PAY STUB------------------------");
        System.out.println(toPrint);
        System.out.println("---------------------------------------------------------");
    }

    public void printAllPayStubs() {
        for (Employee employee : staffList) {
            printEmployeePayStub(employee.getEmployeeNumber());
        }
    }

    public void enterSickDay(String id, double amount) {
        Employee toEdit = getEmployee(id);
        if (toEdit != null) {
            toEdit.useSickDay(amount);
        }
    }

    public void yearlySickDayReset() {
        for (Employee employee : staffList) {
            if (employee instanceof FullTimeEmployee) {
                employee.resetSickDays();
            }
        }
    }

    public void monthlySickDayReset() {
        for (Employee employee : staffList) {
            if (employee instanceof PartTimeEmployee) {
                employee.resetSickDays();
            }
        }
    }
}
