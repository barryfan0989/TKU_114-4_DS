import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DirectedReachability {
    private final Map<String, Set<String>> adjacency = new LinkedHashMap<>();

    public record Query(String from, String to) {
        @Override
        public String toString() {
            return from + " -> " + to;
        }
    }

    public boolean addVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) return false;
        String name = vertex.trim();
        return adjacency.putIfAbsent(name, new LinkedHashSet<>()) == null;
    }

    public boolean addEdge(String from, String to) {
        if (from == null || to == null) return false;
        String src = from.trim();
        String dst = to.trim();

        addVertex(src);
        addVertex(dst);
        return adjacency.get(src).add(dst);
    }

    /**
     * 判斷有向圖中是否存在 from 到 to 的有效路徑 (具備提早終止機制)。
     */
    public boolean isReachable(String from, String to) {
        if (from == null || to == null) return false;
        String src = from.trim();
        String dst = to.trim();

        if (!adjacency.containsKey(src) || !adjacency.containsKey(dst)) {
            return false;
        }

        // 起點與終點相同且節點存在
        if (src.equals(dst)) {
            return true;
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        queue.offer(src);
        visited.add(src);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            for (String next : adjacency.getOrDefault(current, Set.of())) {
                if (next.equals(dst)) {
                    return true; // 提早終止 (Early Exit)
                }
                if (visited.add(next)) {
                    queue.offer(next);
                }
            }
        }

        return false;
    }

    public void runBatchQueries(List<Query> queries) {
        System.out.println("==================== 有向圖可達性 (Directed Reachability) 批次查詢 ====================");
        System.out.printf("%-18s | %-12s | %s%n", "查詢路徑", "是否可達", "備註說明");
        System.out.println("-------------------------------------------------------------------------------------");

        for (Query q : queries) {
            boolean reachable = isReachable(q.from(), q.to());
            String note;
            if (!adjacency.containsKey(q.from()) || !adjacency.containsKey(q.to())) {
                note = "節點不存在";
            } else if (q.from().equals(q.to())) {
                note = "起點即終點 (自環可達)";
            } else if (reachable) {
                note = "存在單向連通路徑";
            } else {
                note = "無路徑或單向逆行不可達";
            }

            System.out.printf("%-18s | %-12s | %s%n", q, reachable ? "true (可達)" : "false (不可達)", note);
        }
        System.out.println("=====================================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題三：Directed Reachability (有向圖可達性判斷) ===\n");
        DirectedReachability graph = new DirectedReachability();

        // 建立有向拓撲結構:
        // A -> B -> C -> D
        // A -> E -> F
        // D -> B (形成 B-C-D 循環環路)
        // G -> H (獨立子圖)
        // Isolated (孤立節點)
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "B"); // Cycle
        graph.addEdge("A", "E");
        graph.addEdge("E", "F");
        graph.addEdge("G", "H");
        graph.addVertex("Isolated");

        List<Query> testQueries = List.of(
            new Query("A", "D"),        // 可達 (A -> B -> C -> D)
            new Query("A", "F"),        // 可達 (A -> E -> F)
            new Query("D", "C"),        // 可達 (D -> B -> C，經由環路)
            new Query("C", "A"),        // 不可達 (單向逆行)
            new Query("A", "G"),        // 不可達 (不同連通分量)
            new Query("G", "H"),        // 可達 (獨立子圖內部)
            new Query("H", "G"),        // 不可達 (單向)
            new Query("Isolated", "Isolated"), // 可達 (同點且存在)
            new Query("Isolated", "A"), // 不可達
            new Query("A", "Unknown"),  // 不存在節點
            new Query("Unknown1", "Unknown2") // 不存在節點
        );

        graph.runBatchQueries(testQueries);
    }
}
