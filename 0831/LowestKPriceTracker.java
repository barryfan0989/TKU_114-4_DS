import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LowestKPriceTracker {

    /**
     * 使用固定大小為 K 的 Max Heap 保留最低的 K 個價格。
     * Max Heap 的頂端為目前候選清單中的最大值。
     * 若新元素小於堆頂，則替換堆頂，確保堆內維持最小的 K 個元素。
     *
     * @param prices 價格清單
     * @param k      保留的最低價格數量
     * @return 依價格遞增排序的結果清單
     */
    public static List<Integer> findLowestKPrices(List<Integer> prices, int k) {
        if (prices == null || k <= 0) {
            return List.of();
        }

        // 使用 Max Heap (自然倒序)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

        for (Integer price : prices) {
            // 忽略 null 與負數
            if (price == null || price < 0) {
                continue;
            }

            if (maxHeap.size() < k) {
                maxHeap.offer(price);
            } else if (!maxHeap.isEmpty() && price < maxHeap.peek()) {
                maxHeap.poll(); // 移除目前 K 個候選中較大的價格
                maxHeap.offer(price);
            }
        }

        // 取出 Max Heap 中的所有元素並由小到大排序
        List<Integer> result = new ArrayList<>(maxHeap);
        Collections.sort(result); // 遞增排列
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題四：Lowest-K Price Tracker ===");

        // 測試案例 1: 包含負數與 null 的價格清單
        List<Integer> prices1 = List.of(350, 120, -50, 99, 450, 200, 150, -10, 80, 500);
        int k1 = 4;
        List<Integer> res1 = findLowestKPrices(prices1, k1);
        System.out.println("測試 1 原始價格: " + prices1);
        System.out.printf("最低 %d 個價格 (忽略負數/null, 遞增排序): %s%n", k1, res1);

        // 測試案例 2: K 大於有效資料筆數
        List<Integer> prices2 = List.of(100, 50, 20);
        int k2 = 5;
        List<Integer> res2 = findLowestKPrices(prices2, k2);
        System.out.printf("\n測試 2 (K > 資料長度): K=%d -> %s%n", k2, res2);

        // 測試案例 3: 包含重複值
        List<Integer> prices3 = List.of(50, 20, 50, 10, 20, 5);
        int k3 = 3;
        List<Integer> res3 = findLowestKPrices(prices3, k3);
        System.out.printf("\n測試 3 (含重複值): %s, K=%d -> %s%n", prices3, k3, res3);

        // 測試案例 4: 邊界條件 (K <= 0, null 清單, 全為負數)
        System.out.println("\n測試 4 邊界條件:");
        System.out.println("K=0: " + findLowestKPrices(prices1, 0));
        System.out.println("K=-2: " + findLowestKPrices(prices1, -2));
        System.out.println("Null 清單: " + findLowestKPrices(null, 3));
        System.out.println("全負數/null: " + findLowestKPrices(List.of(-1, -20, -5), 2));
    }
}
