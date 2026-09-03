import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Q07_AdjacencyListGraph {
    private final Map<String, Set<String>> adj = new LinkedHashMap<>();
    private int totalEdges = 0;

    public boolean addVertex(String vertex) {
        if (vertex == null || adj.containsKey(vertex)) {
            return false;
        }
        adj.put(vertex, new LinkedHashSet<>());
        return true;
    }

    public boolean addEdge(String from, String to) {
        if (from == null || to == null || from.equals(to)) {
            return false;
        }
        if (!adj.containsKey(from) || !adj.containsKey(to)) {
            return false;
        }
        Set<String> out = adj.get(from);
        if (out.contains(to)) {
            return false;
        }
        out.add(to);
        totalEdges++;
        return true;
    }

    public boolean removeEdge(String from, String to) {
        if (from == null || to == null || from.equals(to)) {
            return false;
        }
        if (!adj.containsKey(from) || !adj.containsKey(to)) {
            return false;
        }
        Set<String> out = adj.get(from);
        if (!out.contains(to)) {
            return false;
        }
        out.remove(to);
        totalEdges--;
        return true;
    }

    public List<String> outgoing(String vertex) {
        if (vertex == null || !adj.containsKey(vertex)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(adj.get(vertex));
    }

    public int inDegree(String vertex) {
        if (vertex == null || !adj.containsKey(vertex)) {
            return 0;
        }
        int count = 0;
        for (Set<String> out : adj.values()) {
            if (out.contains(vertex)) {
                count++;
            }
        }
        return count;
    }

    public int edgeCount() {
        return totalEdges;
    }
}
