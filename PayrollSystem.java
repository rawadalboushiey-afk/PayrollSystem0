
package payrollsystem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

interface Taxable {
    double TAX_RATE = 0.15;
    double calculateTax();
}

interface BonusCalculable {
    double calculateBonus(double performanceScore);
}

abstract class Employee implements Comparable<Employee>, Taxable, BonusCalculable {
    private String id;
    private String name;
    protected double baseSalary;
    private LocalDate joinDate;

    public Employee(String id, String name, double baseSalary, LocalDate joinDate) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
        this.joinDate = joinDate;
    }

    public abstract double calculateNetSalary();

    @Override
    public int compareTo(Employee other) {
        return Double.compare(other.calculateNetSalary(), this.calculateNetSalary());
    }

    @Override
    public double calculateTax() {
        return calculateNetSalary() * TAX_RATE;
    }

    @Override
    public double calculateBonus(double performanceScore) {
        return 0.0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Base Salary: " + baseSalary + 
               ", Join Date: " + joinDate + ", Net Salary: " + calculateNetSalary();
    }
}

class FullTimeEmployee extends Employee {
    private double allowance;

    public FullTimeEmployee(String id, String name, double baseSalary, LocalDate joinDate, double allowance) {
        super(id, name, baseSalary, joinDate);
        this.allowance = allowance;
    }

    @Override
    public double calculateNetSalary() {
        return baseSalary + allowance;
    }

    @Override
    public double calculateBonus(double performanceScore) {
        if (performanceScore >= 1.0 && performanceScore <= 5.0) {
            return baseSalary * 0.10;
        }
        return 0.0;
    }
}

class ContractorEmployee extends Employee {
    private int hourlyRate;
    private int hoursWorked;

    public ContractorEmployee(String id, String name, LocalDate joinDate, int hourlyRate, int hoursWorked) {
        super(id, name, 0.0, joinDate);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateNetSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public double calculateBonus(double performanceScore) {
        if (performanceScore > 4.0) {
            return 500.0;
        }
        return 0.0;
    }
}

public class PayrollSystem {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();

        employees.add(new FullTimeEmployee("E001", "Alice Smith", 5000.0, LocalDate.of(2020, 5, 12), 1200.0));
        employees.add(new FullTimeEmployee("E002", "Bob Johnson", 4500.0, LocalDate.of(2021, 8, 20), 1000.0));
        employees.add(new ContractorEmployee("E003", "Charlie Brown", LocalDate.of(2022, 1, 15), 50, 160));
        employees.add(new ContractorEmployee("E004", "Diana Prince", LocalDate.of(2019, 11, 5), 60, 180));

        System.out.println("--- Employees Sorted by Net Salary (Descending) ---");
        Collections.sort(employees);
        for (Employee emp : employees) {
            System.out.println(emp);
        }

        System.out.println("\n--- Employees Sorted by Join Date (Ascending) ---");
        employees.sort(Comparator.comparing(Employee::getJoinDate));
        for (Employee emp : employees) {
            System.out.println("ID: " + emp.getId() + ", Name: " + emp.getName() + ", Join Date: " + emp.getJoinDate());
        }

        System.out.println("\n--- Total Financial Cost (Net Salaries, Taxes, and Bonuses) ---");
        double totalNetSalaries = 0;
        double totalTaxes = 0;
        double totalBonuses = 0;

        for (Employee emp : employees) {
            totalNetSalaries += emp.calculateNetSalary();
            totalTaxes += emp.calculateTax();
            totalBonuses += emp.calculateBonus(4.5);
        }

        System.out.println("Total Net Salaries: " + totalNetSalaries);
        System.out.println("Total Taxes: " + totalTaxes);
        System.out.println("Total Bonuses: " + totalBonuses);
    }
}