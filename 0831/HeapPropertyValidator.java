import java.util.List;

public class HeapPropertyValidator {

    /**
     * 檢查陣列表示之 Complete Binary Tree 是否符合 Min Heap Invariant。
     * 規則：
     * 1. null 回傳 false。
     * 2. 空清單或單一元素回傳 true。
     * 3. 對所有 parent i，left child (2i+1) 及 right child (2i+2) 若存在，parent 值皆需 <= child 值。
     */
    public static boolean isMinHeap(List<Integer> heap) {
        if (heap == null) {
            return false;
        }
        int n = heap.size();
        if (n <= 1) {
            return true;
        }

        for (int i = 0; i < n / 2; i++) {
            Integer parent = heap.get(i);
            if (parent == null) return false;

            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n) {
                Integer leftChild = heap.get(left);
                if (leftChild == null || parent > leftChild) {
                    return false;
                }
            }

            if (right < n) {
                Integer rightChild = heap.get(right);
                if (rightChild == null || parent > rightChild) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 檢查陣列表示之 Complete Binary Tree 是否符合 Max Heap Invariant。
     * 規則：
     * 1. null 回傳 false。
     * 2. 空清單或單一元素回傳 true。
     * 3. 對所有 parent i，left child (2i+1) 及 right child (2i+2) 若存在，parent 值皆需 >= child 值。
     */
    public static boolean isMaxHeap(List<Integer> heap) {
        if (heap == null) {
            return false;
        }
        int n = heap.size();
        if (n <= 1) {
            return true;
        }

        for (int i = 0; i < n / 2; i++) {
            Integer parent = heap.get(i);
            if (parent == null) return false;

            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n) {
                Integer leftChild = heap.get(left);
                if (leftChild == null || parent < leftChild) {
                    return false;
                }
            }

            if (right < n) {
                Integer rightChild = heap.get(right);
                if (rightChild == null || parent < rightChild) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題五：Heap Property Validator ===");

        // 測試 Min Heap
        List<Integer> validMin1 = List.of(10, 20, 30, 40, 50, 60);
        List<Integer> validMin2 = List.of(5, 5, 5, 10, 10);
        List<Integer> invalidMin1 = List.of(10, 5, 20);
        List<Integer> invalidMin2 = List.of(10, 20, 15, 30, 40, 12); // 12 < 15

        System.out.println("--- Min Heap 驗證 ---");
        System.out.println(validMin1 + " isMinHeap: " + isMinHeap(validMin1) + " (預期: true)");
        System.out.println(validMin2 + " isMinHeap: " + isMinHeap(validMin2) + " (預期: true)");
        System.out.println(invalidMin1 + " isMinHeap: " + isMinHeap(invalidMin1) + " (預期: false)");
        System.out.println(invalidMin2 + " isMinHeap: " + isMinHeap(invalidMin2) + " (預期: false)");

        // 測試 Max Heap
        List<Integer> validMax1 = List.of(90, 80, 70, 40, 50, 60);
        List<Integer> validMax2 = List.of(100, 100, 90);
        List<Integer> invalidMax1 = List.of(50, 80, 30);
        List<Integer> invalidMax2 = List.of(50, 40, 30, 20, 45); // 45 > 40

        System.out.println("\n--- Max Heap 驗證 ---");
        System.out.println(validMax1 + " isMaxHeap: " + isMaxHeap(validMax1) + " (預期: true)");
        System.out.println(validMax2 + " isMaxHeap: " + isMaxHeap(validMax2) + " (預期: true)");
        System.out.println(invalidMax1 + " isMaxHeap: " + isMaxHeap(invalidMax1) + " (預期: false)");
        System.out.println(invalidMax2 + " isMaxHeap: " + isMaxHeap(invalidMax2) + " (預期: false)");

        // 測試邊界條件
        System.out.println("\n--- 邊界條件驗證 ---");
        System.out.println("null isMinHeap: " + isMinHeap(null) + " (預期: false)");
        System.out.println("null isMaxHeap: " + isMaxHeap(null) + " (預期: false)");
        System.out.println("empty [] isMinHeap: " + isMinHeap(List.of()) + " (預期: true)");
        System.out.println("empty [] isMaxHeap: " + isMaxHeap(List.of()) + " (預期: true)");
        System.out.println("single [42] isMinHeap: " + isMinHeap(List.of(42)) + " (預期: true)");
        System.out.println("single [42] isMaxHeap: " + isMaxHeap(List.of(42)) + " (預期: true)");
    }
}
