abstract class EmployeeBase {
    private final String id;
    private final String name;

    EmployeeBase(String id, String name) {
        this.id = (id == null) ? "Unknown" : id;
        this.name = (name == null) ? "Unknown" : name;
        System.out.println("[Constructor Chain] EmployeeBase constructor running for id: " + this.id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    abstract int calculatePay();
}

class FullTimeEmployee extends EmployeeBase {
    private final int salary;

    FullTimeEmployee(String id, String name, int salary) {
        super(id, name);
        this.salary = Math.max(0, salary);
        System.out.println("[Constructor Chain] FullTimeEmployee constructor running. Salary: " + this.salary);
    }

    @Override
    int calculatePay() {
        return salary;
    }
}

class PartTimeEmployee extends EmployeeBase {
    private final int hourlyRate;
    private final int hours;

    PartTimeEmployee(String id, String name, int hourlyRate, int hours) {
        super(id, name);
        this.hourlyRate = Math.max(0, hourlyRate);
        this.hours = Math.max(0, hours);
        System.out.println("[Constructor Chain] PartTimeEmployee constructor running. Rate: " + this.hourlyRate + ", Hours: " + this.hours);
    }

    @Override
    int calculatePay() {
        return hourlyRate * hours;
    }
}

public class EmployeeConstructorChain {
    public static void main(String[] args) {
        System.out.println("=== Constructor Chain Demo ===");
        
        System.out.println("\n1. Creating FullTimeEmployee:");
        FullTimeEmployee ft = new FullTimeEmployee("F001", "Amy", 50000);
        System.out.println("Pay: " + ft.calculatePay());

        System.out.println("\n2. Creating PartTimeEmployee:");
        PartTimeEmployee pt = new PartTimeEmployee("P001", "Ben", 150, 80);
        System.out.println("Pay: " + pt.calculatePay());

        System.out.println("\n3. Testing negative bounds check:");
        PartTimeEmployee invalidPt = new PartTimeEmployee("P002", "ErrorCase", -10, -5);
        System.out.println("Pay: " + invalidPt.calculatePay());

        System.out.println("\nExplanation of constructor execution order:");
        System.out.println("When instantiating a subclass (e.g. FullTimeEmployee):");
        System.out.println("  First, the superclass constructor (EmployeeBase) is invoked via super().");
        System.out.println("  Only after the superclass fields are initialized, the subclass constructor logic executes.");
    }
}
