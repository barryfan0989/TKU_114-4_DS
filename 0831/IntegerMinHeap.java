import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class IntegerMinHeap {
    private final List<Integer> data = new ArrayList<>();

    public void add(int value) {
        data.add(value);
        bubbleUp(data.size() - 1);
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return data.get(0);
    }

    public int removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        int minVal = data.get(0);
        int lastVal = data.remove(data.size() - 1);
        if (!data.isEmpty()) {
            data.set(0, lastVal);
            bubbleDown(0);
        }
        return minVal;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public List<Integer> snapshot() {
        return List.copyOf(data);
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data.get(index) >= data.get(parent)) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        int size = data.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && data.get(left) < data.get(smallest)) {
                smallest = left;
            }
            if (right < size && data.get(right) < data.get(smallest)) {
                smallest = right;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        int temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題二：Min Heap 完整操作 ===");
        IntegerMinHeap minHeap = new IntegerMinHeap();

        // 測試 1: 空 Heap 邊界條件檢查
        System.out.println("1. 檢查空 Heap 狀態: isEmpty=" + minHeap.isEmpty() + ", size=" + minHeap.size());
        try {
            minHeap.peek();
            System.out.println("FAIL: 空 Heap peek 未拋出例外");
        } catch (NoSuchElementException e) {
            System.out.println("PASS: 空 Heap peek 正確拋出 NoSuchElementException");
        }

        try {
            minHeap.removeMin();
            System.out.println("FAIL: 空 Heap removeMin 未拋出例外");
        } catch (NoSuchElementException e) {
            System.out.println("PASS: 空 Heap removeMin 正確拋出 NoSuchElementException");
        }

        // 測試 2: 加入包含負數與重複值的資料
        int[] input = {45, 12, -5, 30, 12, 8, 100, -5, 20};
        System.out.println("\n2. 加入測試資料: [45, 12, -5, 30, 12, 8, 100, -5, 20]");
        for (int val : input) {
            minHeap.add(val);
        }

        System.out.println("加入後 Heap 快照: " + minHeap.snapshot());
        System.out.println("目前最小值 peek: " + minHeap.peek());
        System.out.println("Heap 大小 size: " + minHeap.size());

        // 測試 3: 依序取出全部元素並驗證非遞減順序
        System.out.println("\n3. 依序移除最小值 (removeMin):");
        List<Integer> extracted = new ArrayList<>();
        boolean isNonDecreasing = true;
        Integer prev = null;

        while (!minHeap.isEmpty()) {
            int current = minHeap.removeMin();
            extracted.add(current);
            if (prev != null && current < prev) {
                isNonDecreasing = false;
            }
            prev = current;
        }

        System.out.println("取出順序: " + extracted);
        System.out.println("是否符合非遞減順序 (Sorted Validation): " + (isNonDecreasing ? "通過 (PASS)" : "失敗 (FAIL)"));
        System.out.println("最終 Heap 是否為空: " + minHeap.isEmpty());
    }
}
