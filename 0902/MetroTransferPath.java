import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MetroTransferPath {
    private final Map<String, Set<String>> network = new LinkedHashMap<>();

    public void addTrack(String stationA, String stationB) {
        if (stationA == null || stationB == null || stationA.equalsIgnoreCase(stationB)) return;
        String s1 = stationA.trim();
        String s2 = stationB.trim();

        network.computeIfAbsent(s1, k -> new LinkedHashSet<>()).add(s2);
        network.computeIfAbsent(s2, k -> new LinkedHashSet<>()).add(s1);
    }

    public void addStation(String station) {
        if (station != null && !station.isBlank()) {
            network.putIfAbsent(station.trim(), new LinkedHashSet<>());
        }
    }

    /**
     * 使用 BFS 與 Predecessor 查找並還原最少站數之路徑。
     *
     * @return 最短路徑之站點清單，若不可達則回傳空清單。
     */
    public List<String> findShortestPath(String start, String destination) {
        if (start == null || destination == null) return List.of();
        String src = start.trim();
        String dst = destination.trim();

        if (!network.containsKey(src) || !network.containsKey(dst)) {
            return List.of();
        }

        if (src.equals(dst)) {
            return List.of(src);
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessor = new HashMap<>();

        queue.offer(src);
        visited.add(src);

        boolean found = false;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(dst)) {
                found = true;
                break;
            }

            for (String next : network.getOrDefault(current, Set.of())) {
                if (visited.add(next)) {
                    predecessor.put(next, current);
                    queue.offer(next);
                }
            }
        }

        if (!found && !visited.contains(dst)) {
            return List.of();
        }

        // 從 destination 反向追溯至 start
        List<String> path = new ArrayList<>();
        for (String at = dst; at != null; at = predecessor.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public void queryAndPrint(String start, String destination) {
        System.out.printf("【查詢乘車路徑】從 [%s] 到 [%s]:%n", start, destination);
        List<String> path = findShortestPath(start, destination);

        if (path.isEmpty()) {
            System.out.println("  -> 查無可達乘車路線 (車站不存在或處於不同獨立路網)。\n");
            return;
        }

        int edges = path.size() - 1;
        System.out.printf("  -> 最短站數路徑: %s%n", String.join(" -> ", path));
        System.out.printf("  -> 行經站數: %d 站 | 軌道段數 (Edge Count): %d 段%n%n", path.size(), edges);
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題四：Metro Transfer Path (捷運最少轉乘路徑) ===\n");
        MetroTransferPath metro = new MetroTransferPath();

        // 建立捷運路網連通 (無向圖)
        metro.addTrack("淡水", "北投");
        metro.addTrack("北投", "士林");
        metro.addTrack("士林", "台北車站");
        metro.addTrack("台北車站", "中正紀念堂");
        metro.addTrack("中正紀念堂", "大安");

        // 橫向與環狀轉乘
        metro.addTrack("士林", "大直");
        metro.addTrack("大直", "南京復興");
        metro.addTrack("南京復興", "忠孝復興");
        metro.addTrack("忠孝復興", "大安");

        metro.addTrack("台北車站", "忠孝復興");

        // 獨立輕軌系統 (未連通)
        metro.addTrack("輕軌A站", "輕軌B站");
        metro.addStation("孤立預定站");

        // 測試 1: 正常多路徑求最短 (淡水 -> 大安)
        // 路線可能為: 淡水 -> 北投 -> 士林 -> 台北車站 -> 忠孝復興 -> 大安 (5 edges)
        metro.queryAndPrint("淡水", "大安");

        // 測試 2: 鄰近站點 (台北車站 -> 忠孝復興)
        metro.queryAndPrint("台北車站", "忠孝復興");

        // 測試 3: 同一站點 (起點等於終點)
        metro.queryAndPrint("台北車站", "台北車站");

        // 測試 4: 未連通獨立路網
        metro.queryAndPrint("台北車站", "輕軌B站");

        // 測試 5: 不存在站點
        metro.queryAndPrint("台北車站", "高雄巨蛋");
    }
}
