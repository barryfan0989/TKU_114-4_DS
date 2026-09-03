import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Q08_BfsTraversal {
    public static List<String> bfs(Map<String, List<String>> graph, String start) {
        if (graph == null || start == null || !graph.containsKey(start)) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            result.add(curr);

            List<String> neighbors = graph.get(curr);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (neighbor != null && !visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }

    public static Map<String, Integer> distanceFrom(Map<String, List<String>> graph, String start) {
        if (graph == null || start == null || !graph.containsKey(start)) {
            return new LinkedHashMap<>();
        }

        Map<String, Integer> distanceMap = new LinkedHashMap<>();
        Queue<String> queue = new ArrayDeque<>();

        distanceMap.put(start, 0);
        queue.offer(start);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            int currentDistance = distanceMap.get(curr);

            List<String> neighbors = graph.get(curr);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (neighbor != null && !distanceMap.containsKey(neighbor)) {
                        distanceMap.put(neighbor, currentDistance + 1);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return distanceMap;
    }
}
