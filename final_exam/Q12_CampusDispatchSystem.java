import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Q12_CampusDispatchSystem {
    public record Request(String id, String location, int priority, long sequence) {}

    private final Map<String, Set<String>> graph = new LinkedHashMap<>();
    private final Map<String, Request> requests = new HashMap<>();
    private final PriorityQueue<Request> pq;

    public Q12_CampusDispatchSystem() {
        Comparator<Request> comparator = (a, b) -> {
            int p = Integer.compare(a.priority(), b.priority());
            if (p != 0) {
                return p;
            }
            int s = Long.compare(a.sequence(), b.sequence());
            if (s != 0) {
                return s;
            }
            if (a.id() == null && b.id() == null) {
                return 0;
            }
            if (a.id() == null) {
                return -1;
            }
            if (b.id() == null) {
                return 1;
            }
            return a.id().compareTo(b.id());
        };
        this.pq = new PriorityQueue<>(comparator);
    }

    public boolean addLocation(String location) {
        if (location == null) {
            return false;
        }
        String loc = location.trim();
        if (loc.isEmpty() || graph.containsKey(loc)) {
            return false;
        }
        graph.put(loc, new LinkedHashSet<>());
        return true;
    }

    public boolean addRoad(String first, String second) {
        if (first == null || second == null) {
            return false;
        }
        String u = first.trim();
        String v = second.trim();
        if (u.isEmpty() || v.isEmpty() || u.equals(v)) {
            return false;
        }
        if (!graph.containsKey(u) || !graph.containsKey(v)) {
            return false;
        }
        Set<String> uNeighbors = graph.get(u);
        if (uNeighbors.contains(v)) {
            return false;
        }
        uNeighbors.add(v);
        graph.get(v).add(u);
        return true;
    }

    public boolean submit(Request request) {
        if (request == null || request.id() == null || request.location() == null) {
            return false;
        }
        String id = request.id().trim();
        String loc = request.location().trim();
        if (id.isEmpty() || loc.isEmpty()) {
            return false;
        }
        if (!graph.containsKey(loc)) {
            return false;
        }
        if (requests.containsKey(id)) {
            return false;
        }

        Request normalized = new Request(id, loc, request.priority(), request.sequence());
        requests.put(id, normalized);
        pq.offer(normalized);
        return true;
    }

    public Request nextReachable(String serviceCenter) {
        if (serviceCenter == null) {
            return null;
        }
        String start = serviceCenter.trim();
        if (!graph.containsKey(start)) {
            return null;
        }

        Set<String> reachable = getReachableLocations(start);
        List<Request> skipped = new ArrayList<>();
        Request matched = null;

        while (!pq.isEmpty()) {
            Request candidate = pq.poll();
            if (reachable.contains(candidate.location())) {
                matched = candidate;
                break;
            } else {
                skipped.add(candidate);
            }
        }

        for (Request req : skipped) {
            pq.offer(req);
        }

        if (matched != null) {
            requests.remove(matched.id());
        }

        return matched;
    }

    private Set<String> getReachableLocations(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            Set<String> neighbors = graph.get(curr);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return visited;
    }

    public List<String> route(String start, String target) {
        if (start == null || target == null) {
            return new ArrayList<>();
        }
        String s = start.trim();
        String t = target.trim();
        if (!graph.containsKey(s) || !graph.containsKey(t)) {
            return new ArrayList<>();
        }
        if (s.equals(t)) {
            List<String> path = new ArrayList<>();
            path.add(s);
            return path;
        }

        Queue<String> queue = new ArrayDeque<>();
        Map<String, String> predecessor = new HashMap<>();
        Set<String> visited = new HashSet<>();

        visited.add(s);
        queue.offer(s);

        boolean found = false;
        while (!queue.isEmpty()) {
            String curr = queue.poll();
            if (curr.equals(t)) {
                found = true;
                break;
            }

            Set<String> neighbors = graph.get(curr);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
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
        String step = t;
        while (step != null) {
            path.add(step);
            step = predecessor.get(step);
        }
        Collections.reverse(path);
        return path;
    }

    public int pendingCount() {
        return pq.size();
    }
}
