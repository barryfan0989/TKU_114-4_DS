class Device {
    private final String model;

    Device(String model) {
        this.model = (model == null || model.trim().isEmpty()) ? "Unknown Model" : model.trim();
    }

    public String getModel() {
        return model;
    }

    public void runDiagnostic() {
        System.out.println("Basic Diagnostic: Device " + model + " is functioning.");
    }
}

class Laptop extends Device {
    Laptop(String model) {
        super(model);
    }

    @Override
    public void runDiagnostic() {
        System.out.println("Laptop Diagnostic [" + getModel() + "]: CPU and Battery health checks passed.");
    }
}

class Printer extends Device {
    Printer(String model) {
        super(model);
    }

    @Override
    public void runDiagnostic() {
        System.out.println("Printer Diagnostic [" + getModel() + "]: Ink levels and paper trays checks passed.");
    }

    public void cleanPrintHead() {
        System.out.println(">>> Printer Action: Cleaning print heads on printer: " + getModel());
    }
}

class Router extends Device {
    Router(String model) {
        super(model);
    }

    @Override
    public void runDiagnostic() {
        System.out.println("Router Diagnostic [" + getModel() + "]: Signal quality and routing table check passed.");
    }
}

public class DeviceInspectionSystem {
    public static void main(String[] args) {
        Device[] devices = {
            new Laptop("ThinkPad T14"),
            new Printer("HP LaserJet Pro"),
            new Router("ASUS AX86U"),
            new Printer("Epson L3250")
        };

        System.out.println("=== Device Inspection System ===");

        for (Device device : devices) {
            System.out.println("\nChecking device: " + device.getModel());
            
            // Polymorphic call
            device.runDiagnostic();

            // Safe casting using pattern matching for instanceof (Java 17+)
            if (device instanceof Printer printer) {
                printer.cleanPrintHead();
            }
        }
    }
}
