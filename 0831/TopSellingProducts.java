import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TopSellingProducts {

    public record Product(String id, int sales) {
        public Product {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("Product ID cannot be null or blank");
            }
            if (sales < 0) {
                throw new IllegalArgumentException("Sales cannot be negative");
            }
        }

        @Override
        public String toString() {
            return String.format("[商品: %s | 銷量: %d]", id, sales);
        }
    }

    /**
     * 取得銷量前 K 名熱門商品。
     * 1. 先使用 HashMap 彙總相同商品的總銷量。
     * 2. 使用大小為 K 的 Min Heap 保留銷量最高者。
     *    Heap 頂端為「目前 Top-K 中最弱的元素」（銷量較低者；同銷量則字典序較大者）。
     * 3. 最終結果排序為：銷量由高到低（降序），銷量相同則 ID 字典序由小到大（升序）。
     */
    public static List<Product> getTopKSellingProducts(List<Product> rawTransactions, int k) {
        if (rawTransactions == null || k <= 0) {
            return List.of();
        }

        // 步驟 1: 彙總合併相同 ID 的銷量
        Map<String, Integer> salesMap = new HashMap<>();
        for (Product item : rawTransactions) {
            if (item != null) {
                salesMap.merge(item.id(), item.sales(), Integer::sum);
            }
        }

        if (salesMap.isEmpty()) {
            return List.of();
        }

        // 定義 Min-Heap 的比較器 (在 Top-K 中，最先被淘汰的排在堆頂)：
        // 銷量較小者優先被淘汰；若銷量相同，ID 字典序較大者優先被淘汰 (reversed)
        Comparator<Product> minHeapComparator = Comparator
                .comparingInt(Product::sales)
                .thenComparing(Product::id, Comparator.reverseOrder());

        PriorityQueue<Product> minHeap = new PriorityQueue<>(minHeapComparator);

        for (Map.Entry<String, Integer> entry : salesMap.entrySet()) {
            Product candidate = new Product(entry.getKey(), entry.getValue());
            if (minHeap.size() < k) {
                minHeap.offer(candidate);
            } else if (minHeapComparator.compare(candidate, minHeap.peek()) > 0) {
                minHeap.poll(); // 淘汰較弱者
                minHeap.offer(candidate);
            }
        }

        // 步驟 2: 取出所有 Top-K 候選，並進行最終展示排序
        // 最終展示排序：銷量由大到小 (降序)，同銷量則 ID 字典序由小到大 (升序)
        Comparator<Product> displayComparator = Comparator
                .comparingInt(Product::sales).reversed()
                .thenComparing(Product::id);

        List<Product> result = new ArrayList<>(minHeap);
        result.sort(displayComparator);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業四：Top-K 熱門商品 (TopSellingProducts) ===\n");

        // 測試資料：包含重複商品 ID 的交易明細
        List<Product> transactions = List.of(
            new Product("PROD-APPLE", 30),
            new Product("PROD-BANANA", 50),
            new Product("PROD-ORANGE", 20),
            new Product("PROD-APPLE", 45),   // APPLE 累計: 75
            new Product("PROD-CHERRY", 90),  // CHERRY 累計: 90
            new Product("PROD-BANANA", 25),  // BANANA 累計: 75 (與 APPLE 同銷量，測試 tie-breaker)
            new Product("PROD-DATE", 15),
            new Product("PROD-EGGPLANT", 75), // EGGPLANT 累計: 75 (第三個同銷量商品)
            new Product("PROD-FIG", 10),
            new Product("PROD-ORANGE", 40)   // ORANGE 累計: 60
        );

        System.out.println("原始交易明細筆數: " + transactions.size());

        // 測試 1: 取 Top 3
        int k1 = 3;
        List<Product> top3 = getTopKSellingProducts(transactions, k1);
        System.out.printf("\n--- Top %d 熱門商品排行 ---%n", k1);
        for (int i = 0; i < top3.size(); i++) {
            System.out.printf("第 %d 名: %s%n", i + 1, top3.get(i));
        }

        // 測試 2: 取 Top 5
        int k2 = 5;
        List<Product> top5 = getTopKSellingProducts(transactions, k2);
        System.out.printf("\n--- Top %d 熱門商品排行 (觀察同為 75 銷量的 APPLE, BANANA, EGGPLANT 字典序) ---%n", k2);
        for (int i = 0; i < top5.size(); i++) {
            System.out.printf("第 %d 名: %s%n", i + 1, top5.get(i));
        }

        // 測試 3: 邊界條件 (K <= 0, K 超過總商品數)
        System.out.println("\n--- 邊界條件測試 ---");
        System.out.println("K=0: " + getTopKSellingProducts(transactions, 0));
        System.out.println("K=20 (超過總商品數): " + getTopKSellingProducts(transactions, 20));
        System.out.println("null 輸入: " + getTopKSellingProducts(null, 3));
    }
}
