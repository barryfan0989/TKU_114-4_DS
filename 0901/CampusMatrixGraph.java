import java.util.ArrayList;
import java.util.List;

public class CampusMatrixGraph {
    private final List<String> vertices;
    private final boolean[][] adjacencyMatrix;

    public CampusMatrixGraph(List<String> vertices) {
        if (vertices == null || vertices.isEmpty()) {
            throw new IllegalArgumentException("Vertices list cannot be null or empty");
        }
        this.vertices = List.copyOf(vertices);
        int n = vertices.size();
        this.adjacencyMatrix = new boolean[n][n];
    }

    private int indexOf(String vertex) {
        int index = vertices.indexOf(vertex);
        if (index < 0) {
            throw new IllegalArgumentException("Unknown campus location: " + vertex);
        }
        return index;
    }

    public boolean addEdge(String first, String second) {
        if (first == null || second == null || first.equals(second)) {
            return false; // 無向圖不包含 self-loop
        }
        int a = indexOf(first);
        int b = indexOf(second);

        if (adjacencyMatrix[a][b]) {
            return false; // 邊已存在，不重複新增
        }

        adjacencyMatrix[a][b] = true;
        adjacencyMatrix[b][a] = true;
        return true;
    }

    public boolean removeEdge(String first, String second) {
        if (first == null || second == null) return false;
        int a = indexOf(first);
        int b = indexOf(second);

        if (!adjacencyMatrix[a][b]) {
            return false;
        }

        adjacencyMatrix[a][b] = false;
        adjacencyMatrix[b][a] = false;
        return true;
    }

    public boolean hasEdge(String first, String second) {
        if (first == null || second == null) return false;
        return adjacencyMatrix[indexOf(first)][indexOf(second)];
    }

    public int degree(String vertex) {
        int row = indexOf(vertex);
        int deg = 0;
        for (boolean connected : adjacencyMatrix[row]) {
            if (connected) deg++;
        }
        return deg;
    }

    public List<String> neighbors(String vertex) {
        int row = indexOf(vertex);
        List<String> list = new ArrayList<>();
        for (int col = 0; col < vertices.size(); col++) {
            if (adjacencyMatrix[row][col]) {
                list.add(vertices.get(col));
            }
        }
        return list;
    }

    public int edgeCount() {
        int count = 0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adjacencyMatrix[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    public void printMatrix() {
        System.out.println("================= 校園地圖 Adjacency Matrix =================");
        System.out.printf("%-12s", "地點");
        for (String v : vertices) {
            System.out.printf("%-10s", v);
        }
        System.out.println();
        System.out.println("-".repeat(12 + vertices.size() * 10));

        for (int i = 0; i < vertices.size(); i++) {
            System.out.printf("%-12s", vertices.get(i));
            for (int j = 0; j < vertices.size(); j++) {
                System.out.printf("%-10d", adjacencyMatrix[i][j] ? 1 : 0);
            }
            System.out.println();
        }
        System.out.println("===========================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題四：校園 Matrix Graph (CampusMatrixGraph) ===\n");

        List<String> spots = List.of("校門口", "圖書館", "工學館", "商管大樓", "學生活動中心", "體育館");
        CampusMatrixGraph campus = new CampusMatrixGraph(spots);

        // 建立校園步道連通 (無向邊)
        campus.addEdge("校門口", "圖書館");
        campus.addEdge("校門口", "商管大樓");
        campus.addEdge("圖書館", "工學館");
        campus.addEdge("工學館", "學生活動中心");
        campus.addEdge("商管大樓", "學生活動中心");
        campus.addEdge("學生活動中心", "體育館");

        // 測試重複加入邊 (不應增加邊數)
        boolean dupAdd = campus.addEdge("校門口", "圖書館");
        System.out.println("重複新增邊 [校門口 <-> 圖書館] 結果: " + dupAdd + " (預期: false)");

        // 顯示矩陣
        campus.printMatrix();

        // 查詢度數與相鄰地點
        System.out.println("--- 地點連接度數 (Degree) 與相鄰地點 (Neighbors) ---");
        for (String spot : spots) {
            System.out.printf("%-10s -> Degree: %d, 相鄰步道可達: %s%n",
                    spot, campus.degree(spot), campus.neighbors(spot));
        }

        System.out.println("\n校園步道總段數 (Edge Count): " + campus.edgeCount() + " (預期: 6)");

        // 測試移除步道
        System.out.println("\n--- 施工關閉步道: [工學館 <-> 學生活動中心] ---");
        campus.removeEdge("工學館", "學生活動中心");
        System.out.println("工學館與活動中心是否連通: " + campus.hasEdge("工學館", "學生活動中心"));
        System.out.println("施工後步道總段數: " + campus.edgeCount() + " (預期: 5)");
        System.out.println("工學館目前相鄰地點: " + campus.neighbors("工學館"));
    }
}
