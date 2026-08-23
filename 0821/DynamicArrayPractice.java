import java.util.Arrays;

class DynamicArray<T> {
    private Object[] data;
    private int size;

    public DynamicArray(int initialCapacity) {
        int cap = Math.max(1, initialCapacity);
        this.data = new Object[cap];
        this.size = 0;
    }

    public void add(T value) {
        ensureCapacity();
        data[size] = value;
        size++;
    }

    public void add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        // 將 index 後方的元素往後移一格
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    public T set(int index, T value) {
        checkIndex(index);
        T oldValue = (T) data[index];
        data[index] = value;
        return oldValue;
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removedValue = (T) data[index];
        // 將 index 後方的元素往前移一格
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = null; // 釋放物件引用，防止記憶體洩漏
        return removedValue;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            int newCapacity = data.length * 2;
            data = Arrays.copyOf(data, newCapacity);
            System.out.println("[Resize] 內部陣列擴容，新容量 = " + newCapacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }
}

public class DynamicArrayPractice {
    public static void main(String[] args) {
        System.out.println("=== 課堂實作題五：Dynamic array 插入與刪除 ===");

        // 1. 測試 Integer 動態陣列
        System.out.println("--- 測試 DynamicArray<Integer> (初始容量 = 2) ---");
        DynamicArray<Integer> numbers = new DynamicArray<>(2);
        numbers.add(10);
        numbers.add(20);
        System.out.println("加入兩筆：" + numbers + " | size=" + numbers.size() + ", capacity=" + numbers.capacity());

        // 觸發擴容
        numbers.add(30);
        System.out.println("加入第三筆後：" + numbers + " | size=" + numbers.size() + ", capacity=" + numbers.capacity()); // cap 4

        // 測試指定位置插入
        System.out.println("\n在索引 1 插入 99：");
        numbers.add(1, 99);
        System.out.println("結果：" + numbers + " | size=" + numbers.size()); // [10, 99, 20, 30]

        // 測試 get 與 set
        System.out.println("獲取索引 2 的值：" + numbers.get(2)); // 20
        System.out.println("修改索引 2 的值為 88 (舊值為 " + numbers.set(2, 88) + ")：");
        System.out.println("結果：" + numbers); // [10, 99, 88, 30]

        // 測試刪除
        System.out.println("\n刪除索引 1 元素 (值為 " + numbers.remove(1) + ")：");
        System.out.println("結果：" + numbers + " | size=" + numbers.size() + ", capacity=" + numbers.capacity()); // [10, 88, 30]

        // 2. 測試異常邊界條件 (try-catch)
        System.out.println("\n--- 測試邊界條件與異常處理 ---");

        // (a) 測試索引 -1
        try {
            System.out.println("嘗試獲取 index = -1：");
            numbers.get(-1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   捕獲預期例外: " + e.getMessage());
        }

        // (b) 測試索引大於等於 size (這裡 size 是 3)
        try {
            System.out.println("嘗試獲取 index = 3 (等於 size)：");
            numbers.get(3);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   捕獲預期例外: " + e.getMessage());
        }

        // (c) 嘗試在不合法的 index 插入 (如 index = 5，大於 size)
        try {
            System.out.println("嘗試在 index = 5 插入 500：");
            numbers.add(5, 500);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   捕獲預期例外: " + e.getMessage());
        }

        // (d) 空結構刪除測試
        DynamicArray<String> emptyList = new DynamicArray<>(3);
        try {
            System.out.println("對空 DynamicArray 進行刪除 index = 0：");
            emptyList.remove(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("   捕獲預期例外: " + e.getMessage());
        }
    }
}
