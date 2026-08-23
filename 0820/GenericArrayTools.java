public class GenericArrayTools {

    public static <T> int countMatches(T[] data, T target) {
        if (data == null) {
            System.out.println("[Warning] countMatches: Input array is null.");
            return 0;
        }
        int count = 0;
        for (T item : data) {
            if (target == null) {
                if (item == null) {
                    count++;
                }
            } else {
                if (target.equals(item)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static <T> T last(T[] data) {
        if (data == null || data.length == 0) {
            System.out.println("[Warning] last: Input array is null or empty.");
            return null;
        }
        return data[data.length - 1];
    }

    public static <T> void swap(T[] data, int first, int second) {
        if (data == null) {
            System.out.println("[Warning] swap: Input array is null.");
            return;
        }
        if (first < 0 || first >= data.length || second < 0 || second >= data.length) {
            System.out.println("[Warning] swap: Invalid indices (first: " + first + ", second: " + second + ") for array length " + data.length);
            return;
        }
        T temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題二：Generic 陣列工具 ===");

        // 1. 測試 countMatches
        String[] words = {"apple", null, "banana", "apple", null, "cherry"};
        System.out.println("測試 countMatches：");
        System.out.println("尋找 'apple' 的次數：" + countMatches(words, "apple")); // 應為 2
        System.out.println("尋找 null 的次數：" + countMatches(words, null));       // 應為 2
        System.out.println("尋找 'durian' 的次數：" + countMatches(words, "durian")); // 應為 0
        System.out.println("傳入 null 陣列的次數：" + countMatches(null, "apple")); // 應為 0

        // 2. 測試 last
        Integer[] emptyArray = {};
        Integer[] numbers = {10, 20, 30, 42};
        System.out.println("\n測試 last：");
        System.out.println("非空陣列的最後一個元素：" + last(numbers));    // 應為 42
        System.out.println("空陣列的最後一個元素：" + last(emptyArray));   // 應為 null
        System.out.println("null 陣列的最後一個元素：" + last(null));       // 應為 null

        // 3. 測試 swap
        String[] colors = {"Red", "Green", "Blue"};
        System.out.println("\n測試 swap：");
        System.out.print("原始陣列：");
        printArray(colors);

        System.out.println("交換索引 0 與 2：");
        swap(colors, 0, 2);
        System.out.print("交換後：");
        printArray(colors); // 應為 Blue, Green, Red

        System.out.println("交換越界索引 (0 與 5)：");
        swap(colors, 0, 5); // 應印出警告
        System.out.print("越界交換後陣列 (應維持原樣)：");
        printArray(colors);

        System.out.println("對 null 陣列進行交換：");
        swap(null, 0, 1); // 應印出警告
    }

    private static <T> void printArray(T[] array) {
        if (array == null) {
            System.out.println("null");
            return;
        }
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + (i == array.length - 1 ? "" : ", "));
        }
        System.out.println();
    }
}
