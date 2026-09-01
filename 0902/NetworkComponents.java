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
import java.util.TreeSet;

public class NetworkComponents {
    private final Map<String, Set<String>> adjacency = new LinkedHashMap<>();

    public void addNode(String node) {
        if (node != null && !node.isBlank()) {
            adjacency.putIfAbsent(node.trim(), new LinkedHashSet<>());
        }
    }

    public void addEdge(String nodeA, String nodeB) {
        if (nodeA == null || nodeB == null || nodeA.equalsIgnoreCase(nodeB)) return;
        String a = nodeA.trim();
        String b = nodeB.trim();

        addNode(a);
        addNode(b);
        adjacency.get(a).add(b);
        adjacency.get(b).add(a);
    }

    public List<List<String>> findConnectedComponents() {
        List<List<String>> components = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String node : adjacency.keySet()) {
            if (visited.contains(node)) {
                continue;
            }

            List<String> currentComponent = new ArrayList<>();
            Queue<String> queue = new ArrayDeque<>();

            queue.offer(node);
            visited.add(node);

            while (!queue.isEmpty()) {
                String curr = queue.poll();
                currentComponent.add(curr);

                for (String next : adjacency.getOrDefault(curr, Set.of())) {
                    if (visited.add(next)) {
                        queue.offer(next);
                    }
                }
            }

            Collections.sort(currentComponent); // 保持元件內排序易讀
            components.add(currentComponent);
        }

        return components;
    }

    public void printComponentReport() {
        System.out.println("==================== 網路連通元件 (Connected Components) 分析報告 ====================");
        List<List<String>> components = findConnectedComponents();

        if (components.isEmpty()) {
            System.out.println("網路為空，無任何節點與連通元件。\n");
            return;
        }

        System.out.printf("節點總數: %d | 連通元件總數 (Component Count): %d%n", adjacency.size(), components.size());
        System.out.println("-------------------------------------------------------------------------------------");

        List<String> largestComponent = List.of();
        List<String> isolatedNodes = new ArrayList<>();

        for (int i = 0; i < components.size(); i++) {
            List<String> comp = components.get(i);
            if (comp.size() > largestComponent.size()) {
                largestComponent = comp;
            }
            if (comp.size() == 1) {
                isolatedNodes.add(comp.get(0));
            }
            System.out.printf("元件 [%2d] (規模: %2d 個節點): %s%n", i + 1, comp.size(), comp);
        }

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.printf("★ 最大連通群組 (Largest Component, 規模 %d): %s%n", largestComponent.size(), largestComponent);
        System.out.printf("★ 孤立節點清單 (Isolated Vertices, 規模 1): %s%n",
                isolatedNodes.isEmpty() ? "(無孤立節點)" : isolatedNodes);
        System.out.println("=====================================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題五：Network Components (網路連通元件分析) ===\n");
        NetworkComponents net = new NetworkComponents();

        // 群組 1: 區域伺服器叢集 A (A1, A2, A3, A4)
        net.addEdge("Srv-A1", "Srv-A2");
        net.addEdge("Srv-A2", "Srv-A3");
        net.addEdge("Srv-A3", "Srv-A4");
        net.addEdge("Srv-A4", "Srv-A1");

        // 群組 2: 區域伺服器叢集 B (B1, B2)
        net.addEdge("Srv-B1", "Srv-B2");

        // 群組 3: 區域伺服器叢集 C (C1, C2, C3)
        net.addEdge("Srv-C1", "Srv-C2");
        net.addEdge("Srv-C2", "Srv-C3");

        // 孤立節點
        net.addNode("StandAlone-01");
        net.addNode("StandAlone-02");

        // 產出分析報告
        net.printComponentReport();
    }
}
