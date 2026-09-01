import java.util.ArrayList;
import java.util.List;

public class CollisionBucketReport {
    private final int bucketCount;
    private final List<List<Integer>> buckets;

    public CollisionBucketReport(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("Bucket count must be positive");
        }
        this.bucketCount = bucketCount;
        this.buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            this.buckets.add(new ArrayList<>());
        }
    }

    private int getIndex(int key) {
        return Math.floorMod(Integer.hashCode(key), bucketCount);
    }

    public void insert(int key) {
        int idx = getIndex(key);
        buckets.get(idx).add(key);
    }

    public void insertAll(List<Integer> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }
        for (Integer key : keys) {
            if (key != null) {
                insert(key);
            }
        }
    }

    public void generateReport() {
        System.out.println("---------------- Bucket 雜湊分佈報告 ----------------");
        System.out.printf("Bucket 總數: %d%n", bucketCount);

        int totalKeys = 0;
        int totalCollisions = 0;
        int maxChainLength = 0;
        int nonEmptyBuckets = 0;

        for (int i = 0; i < bucketCount; i++) {
            List<Integer> chain = buckets.get(i);
            int chainSize = chain.size();
            totalKeys += chainSize;

            if (chainSize > 0) {
                nonEmptyBuckets++;
                if (chainSize > 1) {
                    totalCollisions += (chainSize - 1);
                }
            }

            if (chainSize > maxChainLength) {
                maxChainLength = chainSize;
            }

            System.out.printf("Bucket [%2d]: 長度=%2d, Keys=%s%n", i, chainSize, chain);
        }

        System.out.println("---------------- 統計摘要 ----------------");
        System.out.printf("資料總筆數: %d%n", totalKeys);
        System.out.printf("非空 Bucket 數: %d / %d%n", nonEmptyBuckets, bucketCount);
        System.out.printf("總 Collision 次數: %d%n", totalCollisions);
        System.out.printf("最長 Chain 長度: %d%n", maxChainLength);
        double avgChain = nonEmptyBuckets == 0 ? 0.0 : (double) totalKeys / nonEmptyBuckets;
        System.out.printf("非空 Bucket 平均 Chain 長度: %.2f%n", avgChain);
        System.out.println("-----------------------------------------\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題六：Collision Bucket Report ===");

        // 測試 1: 包含正數、負數與重複值的鍵值清單
        System.out.println(">>> 測試 1: 正常資料 (含負數與重複值, Bucket 數量 = 5)");
        CollisionBucketReport report1 = new CollisionBucketReport(5);
        List<Integer> testKeys1 = List.of(12, 7, 22, -3, 7, 17, -8, 0, 25, -15);
        System.out.println("輸入 Keys: " + testKeys1);
        report1.insertAll(testKeys1);
        report1.generateReport();

        // 測試 2: 空輸入
        System.out.println(">>> 測試 2: 空資料輸入 (Bucket 數量 = 4)");
        CollisionBucketReport report2 = new CollisionBucketReport(4);
        report2.insertAll(List.of());
        report2.generateReport();

        // 測試 3: 極端碰撞 (所有 key 映射到相同 bucket)
        System.out.println(">>> 測試 3: 所有 Key 映射至同一個 Bucket (Bucket 數量 = 7)");
        CollisionBucketReport report3 = new CollisionBucketReport(7);
        List<Integer> testKeys3 = List.of(7, 14, 21, 28, -7, 0, 35);
        System.out.println("輸入 Keys (皆為 7 的倍數): " + testKeys3);
        report3.insertAll(testKeys3);
        report3.generateReport();
    }
}
