class Equipment {
    private String id;
    private String name;
    private int availableCount;

    Equipment(String id, String name, int availableCount) {
        this.id = (id == null || id.trim().isEmpty()) ? "Unknown" : id.trim();
        this.name = (name == null || name.trim().isEmpty()) ? "Unknown" : name.trim();
        this.availableCount = Math.max(0, availableCount);
    }

    boolean borrowOne() {
        if (availableCount > 0) {
            availableCount--;
            return true;
        }
        return false;
    }

    void returnItems(int quantity) {
        if (quantity > 0) {
            availableCount += quantity;
        }
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getAvailableCount() {
        return availableCount;
    }

    @Override
    public String toString() {
        return "Equipment{id='" + id + "', name='" + name + "', availableCount=" + availableCount + "}";
    }
}

public class EquipmentInventory {
    public static void main(String[] args) {
        Equipment e1 = new Equipment("E001", "Projector", 2);
        Equipment e2 = new Equipment("   ", null, -5);

        System.out.println("Initial State:");
        System.out.println(e1);
        System.out.println(e2);

        System.out.println("\nTesting e1 borrowing:");
        System.out.println("Borrow 1: " + e1.borrowOne() + " (Remaining: " + e1.getAvailableCount() + ")");
        System.out.println("Borrow 2: " + e1.borrowOne() + " (Remaining: " + e1.getAvailableCount() + ")");
        System.out.println("Borrow 3: " + e1.borrowOne() + " (Remaining: " + e1.getAvailableCount() + ")");

        System.out.println("\nTesting e1 returning:");
        e1.returnItems(3);
        System.out.println("After returning 3: " + e1);
        e1.returnItems(-2);
        System.out.println("After returning -2 (invalid): " + e1);
    }
}
