import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class BfsLayerReport {

    /**
     * 使用 BFS 計算從起點出發至圖中所有頂點的最少邊數距離。
     * 若頂點不可達，距離標記為 -1。
     *
     * @param graph 鄰接清單表示之圖形
     * @param start 起點頂點
     * @return 頂點到最少邊數距離的對照表
     */
    public static Map<String, Integer> computeDistances(Map<String, List<String>> graph, String start) {
        if (graph == null || start == null || !graph.containsKey(start)) {
            return Collections.emptyMap();
        }

        Map<String, Integer> distances = new LinkedHashMap<>();
        for (String v : graph.keySet()) {
            distances.put(v, -1); // 預設皆為不可達 (-1)
        }

        Queue<String> queue = new ArrayDeque<>();
        queue.offer(start);
        distances.put(start, 0); // 起點距離為 0

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDist = distances.get(current);

            List<String> neighbors = graph.getOrDefault(current, List.of());
            for (String next : neighbors) {
                if (graph.containsKey(next) && distances.get(next) == -1) {
                    distances.put(next, currentDist + 1);
                    queue.offer(next);
                }
            }
        }

        return distances;
    }

    public static void printLayerReport(Map<String, List<String>> graph, String start) {
        System.out.printf("==================== BFS 分層距離報告 (起點: %s) ====================%n", start);
        Map<String, Integer> distances = computeDistances(graph, start);

        if (distances.isEmpty()) {
            System.out.println("【錯誤】起點不存在於圖中或圖形為空。\n");
            return;
        }

        // 依距離 (Layer) 分組
        Map<Integer, List<String>> layers = new TreeMap<>();
        List<String> unreachable = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : distances.entrySet()) {
            int d = entry.getValue();
            if (d == -1) {
                unreachable.add(entry.getKey());
            } else {
                layers.computeIfAbsent(d, k -> new ArrayList<>()).add(entry.getKey());
            }
        }

        System.out.printf("%-10s | %-12s | %s%n", "層級 (Layer)", "最少 Edge 數", "包含節點 (Vertices)");
        System.out.println("------------------------------------------------------------------");
        for (Map.Entry<Integer, List<String>> entry : layers.entrySet()) {
            int layer = entry.getKey();
            System.out.printf("Layer %-4d | %-12d | %s%n", layer, layer, entry.getValue());
        }

        if (!unreachable.isEmpty()) {
            System.out.println("------------------------------------------------------------------");
            System.out.println("不可達節點 (Unreachable, 距離=-1): " + unreachable);
        }
        System.out.println("==================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題一：BFS Layer Report (分層距離報告) ===\n");

        // 建立測試圖形 (包含環、多層與孤立節點)
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("A", "D", "E"));
        graph.put("C", List.of("A", "F"));
        graph.put("D", List.of("B", "G"));
        graph.put("E", List.of("B", "F", "G"));
        graph.put("F", List.of("C", "E"));
        graph.put("G", List.of("D", "E"));
        graph.put("H", List.of("I")); // 獨立連通塊
        graph.put("I", List.of("H"));
        graph.put("Isolated", List.of()); // 孤立節點

        // 測試 1: 正常多層起點 A
        printLayerReport(graph, "A");

        // 測試 2: 獨立連通塊起點 H
        printLayerReport(graph, "H");

        // 測試 3: 孤立節點
        printLayerReport(graph, "Isolated");

        // 測試 4: 邊界條件 (不存在的起點與空圖)
        printLayerReport(graph, "UnknownNode");
        printLayerReport(Map.of(), "A");
    }
}
