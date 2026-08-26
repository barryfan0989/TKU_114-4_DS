import java.util.List;

public class Q03_EmployeePayroll {
    public static abstract class Employee {
        private final String id;
        private final String name;

        protected Employee(String id, String name) {
            if (id == null || name == null || id.trim().isEmpty() || name.trim().isEmpty()) {
                throw new IllegalArgumentException("ID and name cannot be null or blank");
            }
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public abstract int monthlyPay();

        public String summary() {
            return id + "|" + name + "|" + monthlyPay();
        }
    }

    public static class SalariedEmployee extends Employee {
        private final int salary;

        public SalariedEmployee(String id, String name, int salary) {
            super(id, name);
            this.salary = Math.max(0, salary);
        }

        @Override
        public int monthlyPay() {
            return salary;
        }
    }

    public static class HourlyEmployee extends Employee {
        private final int hours;
        private final int hourlyRate;

        public HourlyEmployee(String id, String name, int hours, int hourlyRate) {
            super(id, name);
            this.hours = Math.max(0, hours);
            this.hourlyRate = Math.max(0, hourlyRate);
        }

        @Override
        public int monthlyPay() {
            if (hours <= 160) {
                return hours * hourlyRate;
            } else {
                int basePay = 160 * hourlyRate;
                double overtimeHours = hours - 160;
                double overtimePay = overtimeHours * hourlyRate * 1.5;
                return (int) (basePay + overtimePay);
            }
        }
    }

    public static int totalPayroll(List<Employee> employees) {
        if (employees == null) {
            return 0;
        }
        int total = 0;
        for (Employee emp : employees) {
            if (emp != null) {
                total += emp.monthlyPay();
            }
        }
        return total;
    }
}
