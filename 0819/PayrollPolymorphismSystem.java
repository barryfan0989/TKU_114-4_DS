abstract class Employee {
    private final String id;
    private final String name;

    Employee(String id, String name) {
        this.id = (id == null || id.trim().isEmpty()) ? "Unknown ID" : id.trim();
        this.name = (name == null || name.trim().isEmpty()) ? "Unknown Name" : name.trim();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    abstract int calculatePay();
}

class SalariedEmployee extends Employee {
    private final int monthlySalary;

    SalariedEmployee(String id, String name, int monthlySalary) {
        super(id, name);
        this.monthlySalary = Math.max(0, monthlySalary);
    }

    @Override
    int calculatePay() {
        return monthlySalary;
    }
}

class HourlyEmployee extends Employee {
    private final int hourlyRate;
    private final int hours;

    HourlyEmployee(String id, String name, int hourlyRate, int hours) {
        super(id, name);
        this.hourlyRate = Math.max(0, hourlyRate);
        this.hours = Math.max(0, hours);
    }

    @Override
    int calculatePay() {
        return hourlyRate * hours;
    }
}

class CommissionEmployee extends Employee {
    private final int basePay;
    private final int salesAmount;
    private final double commissionRate;

    CommissionEmployee(String id, String name, int basePay, int salesAmount, double commissionRate) {
        super(id, name);
        this.basePay = Math.max(0, basePay);
        this.salesAmount = Math.max(0, salesAmount);
        this.commissionRate = Math.max(0.0, Math.min(1.0, commissionRate));
    }

    @Override
    int calculatePay() {
        return basePay + (int) (salesAmount * commissionRate);
    }
}

public class PayrollPolymorphismSystem {
    public static void main(String[] args) {
        Employee[] payroll = {
            new SalariedEmployee("E001", "Amy", 60000),
            new HourlyEmployee("E002", "Ben", 200, 160),
            new CommissionEmployee("E003", "Charlie", 30000, 150000, 0.15),
            new SalariedEmployee("E004", "David", 45000)
        };

        System.out.println("=== Payroll Polymorphism System ===");
        int totalPayroll = 0;
        Employee highestPaid = null;

        for (Employee emp : payroll) {
            int pay = emp.calculatePay();
            totalPayroll += pay;
            
            System.out.printf("Employee ID: %s | Name: %-8s | Salary: %6d 元%n", emp.getId(), emp.getName(), pay);

            if (highestPaid == null || pay > highestPaid.calculatePay()) {
                highestPaid = emp;
            }
        }

        System.out.println("\n-------------------------------------");
        System.out.println("Total Payroll Expense: " + totalPayroll + " 元");
        if (highestPaid != null) {
            System.out.printf("Highest Paid Employee: %s (%s) with %d 元%n", 
                highestPaid.getName(), highestPaid.getId(), highestPaid.calculatePay());
        }
    }
}
