import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q06_AdjacencyMatrixGraph {
    private final List<String> vertexList;
    private final Map<String, Integer> indexMap;
    private final boolean[][] matrix;

    public Q06_AdjacencyMatrixGraph(List<String> vertices) {
        this.vertexList = new ArrayList<>();
        this.indexMap = new HashMap<>();

        if (vertices != null) {
            for (String vertex : vertices) {
                if (vertex != null && !indexMap.containsKey(vertex)) {
                    indexMap.put(vertex, vertexList.size());
                    vertexList.add(vertex);
                }
            }
        }

        int n = vertexList.size();
        this.matrix = new boolean[n][n];
    }

    public boolean addEdge(String first, String second) {
        if (first == null || second == null || first.equals(second)) {
            return false;
        }
        Integer u = indexMap.get(first);
        Integer v = indexMap.get(second);
        if (u == null || v == null) {
            return false;
        }
        if (matrix[u][v]) {
            return false;
        }
        matrix[u][v] = true;
        matrix[v][u] = true;
        return true;
    }

    public boolean removeEdge(String first, String second) {
        if (first == null || second == null || first.equals(second)) {
            return false;
        }
        Integer u = indexMap.get(first);
        Integer v = indexMap.get(second);
        if (u == null || v == null) {
            return false;
        }
        if (!matrix[u][v]) {
            return false;
        }
        matrix[u][v] = false;
        matrix[v][u] = false;
        return true;
    }

    public boolean hasEdge(String first, String second) {
        if (first == null || second == null || first.equals(second)) {
            return false;
        }
        Integer u = indexMap.get(first);
        Integer v = indexMap.get(second);
        if (u == null || v == null) {
            return false;
        }
        return matrix[u][v];
    }

    public int degree(String vertex) {
        if (vertex == null) {
            return 0;
        }
        Integer u = indexMap.get(vertex);
        if (u == null) {
            return 0;
        }
        int count = 0;
        for (int j = 0; j < vertexList.size(); j++) {
            if (matrix[u][j]) {
                count++;
            }
        }
        return count;
    }

    public List<String> neighbors(String vertex) {
        if (vertex == null) {
            return new ArrayList<>();
        }
        Integer u = indexMap.get(vertex);
        if (u == null) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        for (int j = 0; j < vertexList.size(); j++) {
            if (matrix[u][j]) {
                list.add(vertexList.get(j));
            }
        }
        return list;
    }
}
