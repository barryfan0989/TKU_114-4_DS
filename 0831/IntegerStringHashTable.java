import java.util.ArrayList;
import java.util.List;

public class IntegerStringHashTable {

    public record Entry(int key, String value) {
        @Override
        public String toString() {
            return key + "=\"" + value + "\"";
        }
    }

    private final int bucketCount;
    private final List<List<Entry>> buckets;
    private int size;

    public IntegerStringHashTable() {
        this(7);
    }

    public IntegerStringHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("Bucket count must be positive");
        }
        this.bucketCount = bucketCount;
        this.buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            this.buckets.add(new ArrayList<>());
        }
        this.size = 0;
    }

    private int getIndex(int key) {
        return Math.floorMod(Integer.hashCode(key), bucketCount);
    }

    public void put(int key, String value) {
        int idx = getIndex(key);
        List<Entry> chain = buckets.get(idx);

        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key() == key) {
                // Key 已存在，更新值且 size 不增加
                chain.set(i, new Entry(key, value));
                return;
            }
        }

        // 新 Key，加入至 chain 並累加 size
        chain.add(new Entry(key, value));
        size++;
    }

    public String get(int key) {
        int idx = getIndex(key);
        List<Entry> chain = buckets.get(idx);
        for (Entry entry : chain) {
            if (entry.key() == key) {
                return entry.value();
            }
        }
        return null;
    }

    public boolean containsKey(int key) {
        return get(key) != null;
    }

    public String remove(int key) {
        int idx = getIndex(key);
        List<Entry> chain = buckets.get(idx);
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key() == key) {
                String removedValue = chain.remove(i).value();
                size--;
                return removedValue;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void bucketReport() {
        System.out.println("================= HashTable Bucket 分佈報告 =================");
        System.out.printf("總鍵值對 (size): %d | Bucket 容量: %d | 負載因子: %.2f%n",
                size, bucketCount, (double) size / bucketCount);

        int maxChain = 0;
        int collisions = 0;

        for (int i = 0; i < bucketCount; i++) {
            List<Entry> chain = buckets.get(i);
            int len = chain.size();
            if (len > maxChain) maxChain = len;
            if (len > 1) collisions += (len - 1);

            System.out.printf("Bucket [%2d] (長度: %d): %s%n", i, len, chain);
        }

        System.out.printf("統計: 總 Collision 次數 = %d, 最長 Chain = %d%n", collisions, maxChain);
        System.out.println("============================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業五：IntegerStringHashTable (自製 Separate Chaining 雜湊表) ===\n");
        IntegerStringHashTable table = new IntegerStringHashTable(5);

        // 測試 1: 插入一般與負數鍵值
        System.out.println("--- 1. 插入資料 (含負數 Key 與可能碰撞的 Key) ---");
        table.put(10, "Alpha");
        table.put(15, "Beta");    // 10 與 15 在 mod 5 均落在 index 0 (Collision)
        table.put(-5, "NegativeFive"); // -5 mod 5 = 0 (Collision with 10, 15)
        table.put(7, "Gamma");    // 7 mod 5 = 2
        table.put(22, "Delta");   // 22 mod 5 = 2 (Collision with 7)
        table.put(-3, "Epsilon"); // -3 mod 5 = 2 (Collision with 7, 22)
        table.put(4, "Zeta");     // 4 mod 5 = 4

        table.bucketReport();
        System.out.println("目前 size: " + table.size() + " (預期: 7)");

        // 測試 2: 重複 Key 更新值 (驗證 size 不增加)
        System.out.println("--- 2. 測試重複 Key 覆蓋更新 ---");
        System.out.println("更新前 get(7): " + table.get(7));
        table.put(7, "Gamma_Updated");
        System.out.println("更新後 get(7): " + table.get(7));
        System.out.println("更新後 size: " + table.size() + " (預期 size 仍為 7)");

        // 測試 3: 查詢與 containsKey
        System.out.println("\n--- 3. 查詢操作 (get & containsKey) ---");
        System.out.println("containsKey(15): " + table.containsKey(15) + " -> get: " + table.get(15));
        System.out.println("containsKey(-3): " + table.containsKey(-3) + " -> get: " + table.get(-3));
        System.out.println("containsKey(999) (不存在): " + table.containsKey(999) + " -> get: " + table.get(999));

        // 測試 4: 刪除操作 (remove)
        System.out.println("\n--- 4. 刪除操作 (remove) ---");
        System.out.println("刪除 key=15: " + table.remove(15));
        System.out.println("刪除後 containsKey(15): " + table.containsKey(15));
        System.out.println("刪除不存在的 key=100: " + table.remove(100));
        System.out.println("刪除後 size: " + table.size() + " (預期: 6)");

        table.bucketReport();
    }
}
