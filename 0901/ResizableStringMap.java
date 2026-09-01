import java.util.ArrayList;
import java.util.List;

public class ResizableStringMap {
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int INITIAL_BUCKET_COUNT = 5;

    public record Entry(String key, String value) {
        @Override
        public String toString() {
            return key + "=\"" + value + "\"";
        }
    }

    private List<List<Entry>> buckets;
    private int size;

    public ResizableStringMap() {
        this(INITIAL_BUCKET_COUNT);
    }

    public ResizableStringMap(int initialBucketCount) {
        if (initialBucketCount <= 0) {
            throw new IllegalArgumentException("Initial bucket count must be positive");
        }
        this.buckets = new ArrayList<>(initialBucketCount);
        for (int i = 0; i < initialBucketCount; i++) {
            this.buckets.add(new ArrayList<>());
        }
        this.size = 0;
    }

    private int getIndex(String key, int bucketCapacity) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return Math.floorMod(key.hashCode(), bucketCapacity);
    }

    public void put(String key, String value) {
        int idx = getIndex(key, buckets.size());
        List<Entry> chain = buckets.get(idx);

        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key().equals(key)) {
                // Key 已存在，更新 Value，size 不變
                chain.set(i, new Entry(key, value));
                return;
            }
        }

        // 新 Key 加入
        chain.add(new Entry(key, value));
        size++;

        // 檢查是否超過負載因子門檻值，若超過則進行 Rehash 擴展
        if (loadFactor() > LOAD_FACTOR_THRESHOLD) {
            rehash(buckets.size() * 2 + 1);
        }
    }

    public String get(String key) {
        int idx = getIndex(key, buckets.size());
        for (Entry entry : buckets.get(idx)) {
            if (entry.key().equals(key)) {
                return entry.value();
            }
        }
        return null;
    }

    public boolean remove(String key) {
        int idx = getIndex(key, buckets.size());
        List<Entry> chain = buckets.get(idx);
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key().equals(key)) {
                chain.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public int bucketCount() {
        return buckets.size();
    }

    public double loadFactor() {
        return (double) size / buckets.size();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void rehash(int newBucketCount) {
        int oldCap = buckets.size();
        System.out.printf("  [觸發 Rehash] 負載因子 = %.2f > %.2f，Bucket 數從 %d 擴展為 %d%n",
                loadFactor(), LOAD_FACTOR_THRESHOLD, oldCap, newBucketCount);

        List<List<Entry>> newBuckets = new ArrayList<>(newBucketCount);
        for (int i = 0; i < newBucketCount; i++) {
            newBuckets.add(new ArrayList<>());
        }

        // 重新計算每個既有 entry 在新 bucket 陣列中的 index
        for (List<Entry> chain : buckets) {
            for (Entry entry : chain) {
                int newIdx = getIndex(entry.key(), newBucketCount);
                newBuckets.get(newIdx).add(entry);
            }
        }

        this.buckets = newBuckets;
    }

    public void printBuckets() {
        System.out.printf("--- 雜湊表狀態 (Size: %d, Buckets: %d, Load: %.2f) ---%n",
                size, buckets.size(), loadFactor());
        for (int i = 0; i < buckets.size(); i++) {
            System.out.printf("Bucket [%2d]: %s%n", i, buckets.get(i));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題一：可擴充 Hash Table (ResizableStringMap) ===\n");
        ResizableStringMap map = new ResizableStringMap(3);
        map.printBuckets();

        // 插入資料並觸發多次 Rehash (容量擴展 3 -> 7 -> 15...)
        System.out.println("1. 逐步新增鍵值對:");
        String[][] data = {
            {"ID_101", "Alice"},
            {"ID_102", "Bob"},
            {"ID_103", "Charlie"},
            {"ID_104", "David"},
            {"ID_105", "Emma"},
            {"ID_106", "Frank"},
            {"ID_107", "Grace"}
        };

        for (String[] pair : data) {
            System.out.printf("Put: %s -> %s%n", pair[0], pair[1]);
            map.put(pair[0], pair[1]);
        }

        System.out.println("\n2. 查看擴展後的雜湊桶分佈:");
        map.printBuckets();

        // 測試重複更新
        System.out.println("3. 測試更新既有 Key (ID_103):");
        System.out.println("更新前 get(ID_103): " + map.get("ID_103"));
        map.put("ID_103", "Charlie_Updated");
        System.out.println("更新後 get(ID_103): " + map.get("ID_103"));
        System.out.println("更新後 size: " + map.size() + " (預期 size 不變為 7)");

        // 測試刪除
        System.out.println("\n4. 測試刪除 (ID_102 與不存在的 ID_999):");
        System.out.println("remove(ID_102): " + map.remove("ID_102"));
        System.out.println("get(ID_102) (應為 null): " + map.get("ID_102"));
        System.out.println("remove(ID_999): " + map.remove("ID_999"));
        System.out.println("刪除後 size: " + map.size() + " (預期 size 為 6)");

        map.printBuckets();
    }
}
