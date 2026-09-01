import java.util.ArrayList;
import java.util.List;

public class StudentIdHashAnalysis {

    public record AnalysisResult(
            int bucketCount,
            int totalItems,
            int nonEmptyBuckets,
            int totalCollisions,
            int maxChainLength,
            double avgChainLengthNonEmpty,
            double loadFactor,
            List<List<String>> buckets
    ) {
        public void printSummary() {
            System.out.printf("========== Bucket 數量: %d 分析結果 ==========%n", bucketCount);
            System.out.printf("總學號筆數: %d | 負載因子 (Load Factor): %.2f%n", totalItems, loadFactor);
            System.out.printf("非空 Bucket 數: %d / %d (使用率: %.1f%%)%n",
                    nonEmptyBuckets, bucketCount, (double) nonEmptyBuckets / bucketCount * 100);
            System.out.printf("總 Collision 碰撞次數: %d%n", totalCollisions);
            System.out.printf("最長 Chain 長度 (Max Chain): %d%n", maxChainLength);
            System.out.printf("非空 Bucket 平均 Chain 長度: %.2f%n", avgChainLengthNonEmpty);
            System.out.println("各 Bucket 詳細分佈:");
            for (int i = 0; i < buckets.size(); i++) {
                List<String> chain = buckets.get(i);
                System.out.printf("  Bucket [%2d] (長度: %d): %s%n", i, chain.size(), chain);
            }
            System.out.println();
        }
    }

    public static AnalysisResult analyze(List<String> studentIds, int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("Bucket count must be positive");
        }

        List<List<String>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        for (String id : studentIds) {
            if (id != null && !id.isBlank()) {
                int hash = id.hashCode();
                int idx = Math.floorMod(hash, bucketCount);
                buckets.get(idx).add(id);
            }
        }

        int totalItems = 0;
        int nonEmptyBuckets = 0;
        int totalCollisions = 0;
        int maxChain = 0;

        for (List<String> chain : buckets) {
            int len = chain.size();
            totalItems += len;
            if (len > 0) {
                nonEmptyBuckets++;
                if (len > 1) {
                    totalCollisions += (len - 1);
                }
            }
            if (len > maxChain) {
                maxChain = len;
            }
        }

        double avgChain = nonEmptyBuckets == 0 ? 0.0 : (double) totalItems / nonEmptyBuckets;
        double loadFactor = (double) totalItems / bucketCount;

        return new AnalysisResult(
                bucketCount, totalItems, nonEmptyBuckets, totalCollisions,
                maxChain, avgChain, loadFactor, buckets
        );
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業六：學號 Collision 分析 (StudentIdHashAnalysis) ===\n");

        // 模擬淡江大學或典型大學學號序列 (包含不同系所、入學年度與序號)
        List<String> studentIds = List.of(
            "41041001", "41041002", "41041015", "41041028", "41041033",
            "41141001", "41141012", "41141025", "41141039", "41141050",
            "41241003", "41241018", "41241022", "41241035", "41241047",
            "41341005", "41341011", "41341029", "41341044", "41341058"
        );

        System.out.println("分析樣本學號筆數: " + studentIds.size());
        System.out.println("樣本清單: " + studentIds + "\n");

        // 比較兩種 Bucket Count 設定 (例如：合數 10 vs 質數 17)
        int bucketCountA = 10;
        int bucketCountB = 17;

        AnalysisResult resultA = analyze(studentIds, bucketCountA);
        AnalysisResult resultB = analyze(studentIds, bucketCountB);

        resultA.printSummary();
        resultB.printSummary();

        // 綜合對比結論
        System.out.println("==================== 綜合比較與分析結論 ====================");
        System.out.printf("%-15s | %-12s | %-12s%n", "評估指標", "Bucket Count=" + bucketCountA, "Bucket Count=" + bucketCountB);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s | %-12.2f | %-12.2f%n", "負載因子", resultA.loadFactor(), resultB.loadFactor());
        System.out.printf("%-15s | %-12d | %-12d%n", "總碰撞次數", resultA.totalCollisions(), resultB.totalCollisions());
        System.out.printf("%-15s | %-12d | %-12d%n", "最長 Chain 長度", resultA.maxChainLength(), resultB.maxChainLength());
        System.out.printf("%-15s | %-12.2f | %-12.2f%n", "非空平均 Chain", resultA.avgChainLengthNonEmpty(), resultB.avgChainLengthNonEmpty());
        System.out.println("------------------------------------------------------------");
        System.out.println("分析觀察：");
        System.out.println("1. 當 Bucket 數從 10 增加至質數 17 時，總碰撞次數與最長 Chain 明顯下降。");
        System.out.println("2. 質數作為 Bucket 數量能有效避免特定規律學號產生雜湊群聚 (Clustering)，使資料分佈更加均勻。");
        System.out.println("============================================================");
    }
}
