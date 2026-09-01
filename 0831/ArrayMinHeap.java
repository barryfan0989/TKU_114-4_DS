import java.util.Arrays;
import java.util.NoSuchElementException;

public class ArrayMinHeap {
    private static final int DEFAULT_INITIAL_CAPACITY = 4;
    private int[] elements;
    private int size;

    public ArrayMinHeap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMinHeap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0");
        }
        this.elements = new int[initialCapacity];
        this.size = 0;
    }

    public void add(int value) {
        if (size == elements.length) {
            resize(elements.length * 2);
        }
        elements[size] = value;
        bubbleUp(size);
        size++;
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return elements[0];
    }

    public int removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        int min = elements[0];
        elements[0] = elements[size - 1];
        size--;
        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return elements.length;
    }

    public int[] snapshot() {
        return Arrays.copyOf(elements, size);
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (elements[index] >= elements[parent]) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && elements[left] < elements[smallest]) {
                smallest = left;
            }
            if (right < size && elements[right] < elements[smallest]) {
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
        int temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    private void resize(int newCapacity) {
        int oldCap = elements.length;
        elements = Arrays.copyOf(elements, newCapacity);
        System.out.printf("  [陣列自動擴容] 容量從 %d 擴充為 %d (目前元素數: %d)%n",
                oldCap, newCapacity, size);
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業三：可調整容量 ArrayMinHeap ===\n");

        // 初始容量設定為 4，測試自動倍增擴容
        ArrayMinHeap heap = new ArrayMinHeap(4);
        System.out.println("初始 Heap 建立完畢，容量: " + heap.capacity() + ", 大小: " + heap.size());

        // 測試 25 筆資料連續加入 (包含負數與重複數值)
        int[] testData = {
            64, 25, 12, 22, 11, 90, 45, 78, 33, 56,
            89, 4, 2, 77, 88, 1, 99, 10, 50, 60,
            -10, 30, 25, -5, 100
        };

        System.out.println("\n--- 開始加入 " + testData.length + " 筆測試資料 ---");
        for (int num : testData) {
            heap.add(num);
        }

        System.out.println("\n資料加入完成！");
        System.out.println("當前元素數量: " + heap.size());
        System.out.println("當前陣列總容量: " + heap.capacity());
        System.out.println("當前 Heap 陣列快照: " + Arrays.toString(heap.snapshot()));
        System.out.println("當前最小值 (peek): " + heap.peek());

        // 測試連續移除並驗證排序
        System.out.println("\n--- 依序執行 removeMin() 取出並驗證排序 ---");
        int prev = Integer.MIN_VALUE;
        boolean sorted = true;
        int count = 0;

        while (!heap.isEmpty()) {
            int current = heap.removeMin();
            System.out.printf("%d ", current);
            if (current < prev) {
                sorted = false;
            }
            prev = current;
            count++;
        }
        System.out.println();

        System.out.println("\n驗證取出筆數: " + count + " / " + testData.length);
        System.out.println("取出順序是否完全符合由小到大排序: " + (sorted ? "通過 (PASS)" : "失敗 (FAIL)"));

        // 測試空 Heap 拋出例外
        System.out.println("\n--- 測試空 Heap 邊界例外 ---");
        try {
            heap.peek();
        } catch (NoSuchElementException e) {
            System.out.println("PASS: 空 Heap peek 正確拋出 NoSuchElementException");
        }
    }
}
