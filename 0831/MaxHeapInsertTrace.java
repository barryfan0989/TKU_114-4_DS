import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MaxHeapInsertTrace {
    private final List<Integer> heap = new ArrayList<>();

    public void add(int value) {
        heap.add(value);
        int index = heap.size() - 1;
        System.out.printf("Insert: %d -> initial array: %s%n", value, heap);

        // Bubble-up for Max Heap: if child > parent, swap
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index) <= heap.get(parent)) {
                break; // Invariant satisfied
            }
            swap(index, parent);
            System.out.printf("  Swap index %d and %d -> %s%n", index, parent, heap);
            index = parent;
        }
    }

    public int peekMax() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap.get(0);
    }

    public List<Integer> snapshot() {
        return List.copyOf(heap);
    }

    public int size() {
        return heap.size();
    }

    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題一：Max Heap Insert Trace ===");
        MaxHeapInsertTrace maxHeap = new MaxHeapInsertTrace();
        int[] testValues = {25, 40, 10, 50, 30, 50};

        System.out.println("逐步加入測試資料: [25, 40, 10, 50, 30, 50]");
        for (int val : testValues) {
            maxHeap.add(val);
        }

        System.out.println("\n最終 Max Heap 陣列內容: " + maxHeap.snapshot());
        int root = maxHeap.peekMax();
        System.out.println("目前 Root (最大值): " + root);
        System.out.println("Root 驗證是否為 50: " + (root == 50 ? "通過 (PASS)" : "失敗 (FAIL)"));
    }
}
