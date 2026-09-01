import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IterativeDfsTrace {

    /**
     * 顯式 Stack 迭代式 DFS，並追蹤每次 push 與 pop 之 Stack 與 Visited 狀態。
     */
    public static List<String> dfsWithTrace(Map<String, List<String>> graph, String start) {
        List<String> traversalOrder = new ArrayList<>();
        if (graph == null || start == null || !graph.containsKey(start)) {
            System.out.println("【無效起點或空圖】無法執行 DFS 追蹤。");
            return traversalOrder;
        }

        ArrayDeque<String> stack = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();

        System.out.printf("==================== 開始迭代式 DFS 追蹤 (起點: %s) ====================%n", start);
        stack.push(start);
        printTrace("PUSH (起點)", start, stack, visited, traversalOrder);

        int step = 1;
        while (!stack.isEmpty()) {
            String current = stack.pop();
            printTrace("POP", current, stack, visited, traversalOrder);

            if (!visited.add(current)) {
                System.out.printf("  -> 節點 [%s] 已存在於 visited 集合，跳過處理。%n", current);
                continue;
            }

            traversalOrder.add(current);
            System.out.printf("  => 【訪問節點 #%d】: %s%n", step++, current);

            List<String> neighbors = graph.getOrDefault(current, List.of());
            // 倒序 push，使相鄰節點由左至右依序 pop
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String next = neighbors.get(i);
                if (graph.containsKey(next) && !visited.contains(next)) {
                    stack.push(next);
                    printTrace("PUSH (鄰居)", next, stack, visited, traversalOrder);
                }
            }
        }

        System.out.println("=========================================================================");
        System.out.println("最終 DFS 走訪順序: " + traversalOrder + "\n");
        return traversalOrder;
    }

    private static void printTrace(String action, String node, ArrayDeque<String> stack,
                                   Set<String> visited, List<String> order) {
        System.out.printf("[%-12s: %-6s] Stack (Top->Bottom): %-20s | Visited: %-20s | 走訪路徑: %s%n",
                action, node, stack, visited, order);
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題二：Iterative DFS Trace (顯式 Stack 走訪歷程) ===\n");

        // 建立測試圖 (包含分歧、環路與回溯)
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D", "E"));
        graph.put("C", List.of("F"));
        graph.put("D", List.of("A")); // 環路回到 A
        graph.put("E", List.of("F")); // 匯聚到 F
        graph.put("F", List.of());

        // 測試 1: 正常圖形從 A 走訪
        dfsWithTrace(graph, "A");

        // 測試 2: 從中間節點 C 走訪
        dfsWithTrace(graph, "C");

        // 測試 3: 邊界測試 (不存在節點與空圖)
        dfsWithTrace(graph, "NonExistent");
        dfsWithTrace(Map.of(), "A");
    }
}
