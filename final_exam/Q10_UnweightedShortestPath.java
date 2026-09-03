import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Q10_UnweightedShortestPath {
    public static List<String> shortestPath(Map<String, List<String>> graph, String start, String target) {
        if (graph == null || start == null || target == null) {
            return new ArrayList<>();
        }
        if (!graph.containsKey(start) || !graph.containsKey(target)) {
            return new ArrayList<>();
        }
        if (start.equals(target)) {
            List<String> singlePath = new ArrayList<>();
            singlePath.add(start);
            return singlePath;
        }

        Queue<String> queue = new ArrayDeque<>();
        Map<String, String> predecessor = new HashMap<>();
        Set<String> visited = new HashSet<>();

        visited.add(start);
        queue.offer(start);

        boolean found = false;
        while (!queue.isEmpty()) {
            String curr = queue.poll();
            if (curr.equals(target)) {
                found = true;
                break;
            }

            List<String> neighbors = graph.get(curr);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (neighbor != null && !visited.contains(neighbor)) {
                        visited.add(neighbor);
                        predecessor.put(neighbor, curr);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        if (!found) {
            return new ArrayList<>();
        }

        List<String> path = new ArrayList<>();
        String step = target;
        while (step != null) {
            path.add(step);
            step = predecessor.get(step);
        }
        Collections.reverse(path);
        return path;
    }
}
