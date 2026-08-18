import java.util.Arrays;

final class InventorySnapshot {
    private final String warehouseId;
    private final int[] quantities;

    InventorySnapshot(String warehouseId, int[] quantities) {
        this.warehouseId = warehouseId == null || warehouseId.trim().isEmpty() ? "Unknown" : warehouseId.trim();
        this.quantities = quantities == null ? new int[0] : Arrays.copyOf(quantities, quantities.length);
    }

    String getWarehouseId() {
        return warehouseId;
    }

    int[] getQuantities() {
        return Arrays.copyOf(quantities, quantities.length);
    }

    int totalQuantity() {
        int total = 0;
        for (int q : quantities) {
            total += q;
        }
        return total;
    }

    int outOfStockCount() {
        int count = 0;
        for (int q : quantities) {
            if (q == 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "InventorySnapshot{warehouseId='" + warehouseId + "', quantities=" + Arrays.toString(quantities) + "}";
    }
}

public class InventorySnapshotPractice {
    public static void main(String[] args) {
        int[] original = {5, 0, 3, 0};
        InventorySnapshot snapshot = new InventorySnapshot("W101", original);

        System.out.println("Snapshot created: " + snapshot);
        System.out.println("Total quantity: " + snapshot.totalQuantity());
        System.out.println("Out of stock count: " + snapshot.outOfStockCount());

        // Defensive copy check 1: modify original array
        original[0] = 99;
        System.out.println("\nAfter modifying original array to [99, 0, 3, 0]:");
        System.out.println("Snapshot quantities: " + Arrays.toString(snapshot.getQuantities()));
        System.out.println("Total quantity: " + snapshot.totalQuantity() + " (should still be 8)");

        // Defensive copy check 2: modify returned array from getter
        int[] getterResult = snapshot.getQuantities();
        getterResult[2] = 88;
        System.out.println("\nAfter modifying returned quantities array from getter:");
        System.out.println("Snapshot quantities: " + Arrays.toString(snapshot.getQuantities()));
        System.out.println("Total quantity: " + snapshot.totalQuantity() + " (should still be 8)");

        // Null array check
        InventorySnapshot nullSnapshot = new InventorySnapshot("WEmpty", null);
        System.out.println("\nSnapshot created with null array: " + nullSnapshot);
        System.out.println("Null snapshot total quantity: " + nullSnapshot.totalQuantity() + " (should be 0)");
    }
}
