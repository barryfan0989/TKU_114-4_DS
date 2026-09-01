import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CampusNavigationSystem {

    public record Location(String code, String name, String category, String description) {
        public Location {
            if (code == null || code.isBlank()) throw new IllegalArgumentException("Code cannot be blank");
            if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        }

        @Override
        public String toString() {
            return String.format("%s (%s - %s)", name, code, description);
        }
    }

    // 1. HashMap: 快速 O(1) 依代碼查詢地點詳細資訊
    private final Map<String, Location> locationRegistry = new LinkedHashMap<>();
    // 2. Adjacency List: 儲存步道連通關係 (無向圖)
    private final Map<String, List<String>> pathwayMap = new LinkedHashMap<>();

    public void registerLocation(String code, String name, String category, String description) {
        String c = code.trim().toUpperCase();
        Location loc = new Location(c, name.trim(), category.trim(), description.trim());
        locationRegistry.put(c, loc);
        pathwayMap.putIfAbsent(c, new ArrayList<>());
    }

    public void addPathway(String code1, String code2) {
        String c1 = code1.trim().toUpperCase();
        String c2 = code2.trim().toUpperCase();

        if (!locationRegistry.containsKey(c1) || !locationRegistry.containsKey(c2)) {
            throw new IllegalArgumentException("Both locations must be registered before adding pathway");
        }
        if (c1.equals(c2)) return;

        if (!pathwayMap.get(c1).contains(c2)) pathwayMap.get(c1).add(c2);
        if (!pathwayMap.get(c2).contains(c1)) pathwayMap.get(c2).add(c1);
    }

    /**
     * 3. BFS 最短路徑搜尋與導航指引產生
     */
    public List<Location> findShortestNavigationRoute(String fromCode, String toCode) {
        if (fromCode == null || toCode == null) return List.of();
        String src = fromCode.trim().toUpperCase();
        String dst = toCode.trim().toUpperCase();

        if (!locationRegistry.containsKey(src) || !locationRegistry.containsKey(dst)) {
            return List.of();
        }

        if (src.equals(dst)) {
            return List.of(locationRegistry.get(src));
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

            for (String next : pathwayMap.getOrDefault(current, List.of())) {
                if (visited.add(next)) {
                    predecessor.put(next, current);
                    queue.offer(next);
                }
            }
        }

        if (!found && !visited.contains(dst)) {
            return List.of();
        }

        List<Location> route = new ArrayList<>();
        for (String at = dst; at != null; at = predecessor.get(at)) {
            route.add(locationRegistry.get(at));
        }
        Collections.reverse(route);
        return route;
    }

    public void navigate(String fromCode, String toCode) {
        System.out.printf("【校園導航指示】規劃路徑: [%s] -> [%s]%n", fromCode, toCode);
        List<Location> route = findShortestNavigationRoute(fromCode, toCode);

        if (route.isEmpty()) {
            System.out.println("  ❌ 無法規劃導航路徑 (地點代碼無效或兩地無連通步道)。\n");
            return;
        }

        if (route.size() == 1) {
            System.out.println("  📍 您已在目的地: " + route.get(0) + "，無須移動。\n");
            return;
        }

        System.out.printf("  ✔ 成功規劃！最少步行路段數: %d 段 (行經 %d 處地標)%n",
                route.size() - 1, route.size());
        for (int i = 0; i < route.size(); i++) {
            Location loc = route.get(i);
            if (i == 0) {
                System.out.printf("    起點 [0]: 從【%s】出發 (%s)%n", loc.name(), loc.description());
            } else if (i == route.size() - 1) {
                System.out.printf("    終點 [%d]: 抵達目的地【%s】(%s)%n", i, loc.name(), loc.description());
            } else {
                System.out.printf("    途經 [%d]: 穿過【%s】(%s)%n", i, loc.name(), loc.description());
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== 期末綜合練習一：校園地圖導航系統 (CampusNavigationSystem) ===\n");
        CampusNavigationSystem nav = new CampusNavigationSystem();

        // 1. 註冊校園地點 (HashMap 儲存)
        nav.registerLocation("GATE", "正門大門口", "出入口", "校園主要進出口與公車接駁站");
        nav.registerLocation("LIB", "覺生紀念圖書館", "學術資源", "圖書總館與自修室");
        nav.registerLocation("ENG", "工學大樓", "教學大樓", "資工系與各工程學系實驗室");
        nav.registerLocation("BBA", "商管大樓", "教學大樓", "商管學院各系所教室");
        nav.registerLocation("ACT", "學生活動中心", "學生活動", "社團辦公室與美食街廣場");
        nav.registerLocation("GYM", "紹謨體育館", "體育設施", "室內球場與健身中心");
        nav.registerLocation("DORM", "松濤學生宿舍", "住宿區", "學生宿舍生活區");

        // 2. 建立連通步道 (Graph Adjacency List)
        nav.addPathway("GATE", "LIB");
        nav.addPathway("GATE", "BBA");
        nav.addPathway("LIB", "ENG");
        nav.addPathway("ENG", "ACT");
        nav.addPathway("BBA", "ACT");
        nav.addPathway("ACT", "GYM");
        nav.addPathway("GYM", "DORM");

        // 測試 1: 跨校園導航 (GATE -> DORM)
        nav.navigate("GATE", "DORM");

        // 測試 2: 教室至體育館 (ENG -> GYM)
        nav.navigate("ENG", "GYM");

        // 測試 3: 原地導航 (LIB -> LIB)
        nav.navigate("LIB", "LIB");

        // 測試 4: 不存在代碼防呆
        nav.navigate("GATE", "UNKNOWN_BLD");
    }
}
