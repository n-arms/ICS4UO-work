import java.util.ArrayList;

/**
 * Manages a list of employees (of type Employee). Employee objects are
 * stored in an ArrayList. Employees can be added, removed, and given raises.
 * Sick days can be used. Employees are referenced by their String
 * employee ID or name.
 * @author Student
 */
public class Employees {

    private ArrayList<Employee> employees;

    /**
     * Constructs an Employees instance and initializes the list.
     */
    public Employees() {
        employees = new ArrayList<>();
    }

    /**
     * Adds an employee to the list.
     * 
     * @param e an instance of class Employee
     */
    public void addEmployee(Employee e) {
        employees.add(e);
    }

    /**
     * Gets an Employee from the list by matching the employee
     * name or ID given.
     * 
     * @param nameOrID the employee name or ID
     * @return the Employee or null if not in the list
     */
    public Employee getEmployee(String nameOrID) {
        for (Employee employee : employees) {
            if (employee.getName().equals(nameOrID) || (employee.getId() != null && employee.getId().equals(nameOrID))) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Removes the first occurrence of an employee from the list, if they exist.
     * Prints a message with results.
     * 
     * @param nameOrID String that is the employee name or ID
     */
    public void removeEmployee(String nameOrID) {
        Employee employee = getEmployee(nameOrID);

        if (employee == null) {
            System.out.printf("%s is not an employee.%n", nameOrID);
        } else {
            employees.remove(employee);
            System.out.printf("Employee %s has been removed.%n", nameOrID);
        }
    }


    /**
     * Reduces by 1 the number of sick days of the given employee.
     * Prints a success message or an error message if the employee does not
     * exist or does not have enough sick days.
     * 
     * @param nameOrID the employee name or ID
     */
    public void useSickDay(String nameOrID) {
        Employee employee = getEmployee(nameOrID);

        if (employee == null) {
            System.out.printf("%s is not an employee.", nameOrID);
            return;
        }

        int currentSickDays = employee.getSickDays();
        if (currentSickDays > 0) {
            employee.setSickDays(currentSickDays - 1);
            System.out.printf("%s used a sick day.%n", nameOrID);
        } else {
            System.out.printf("%s does not have enough sick days.%n", nameOrID);
        }
    }


    /**
     * Gives a raise to the employee. Prints the new salary or an error
     * message if they do not exist.
     * 
     * @param nameOrID the name or ID of the employee
     * @param amount dollar amount to increase the salary
     */
    public void giveRaise(String nameOrID, int amount) {
        Employee employee = getEmployee(nameOrID);

        if (employee == null) {
            System.out.printf("%s is not an employee.", nameOrID);
            return;
        }

        int newSalary = employee.getSalary() + amount;
        employee.setSalary(newSalary);
        System.out.printf("%s new salary: $%s%n", nameOrID, newSalary);
    }


    /**
     * Returns the String representation of Employee, including all field values.
     * 
     * @return all employee information formatted nicely
     */
    @Override
    public String toString() {
        return employees.toString();
    }

}

