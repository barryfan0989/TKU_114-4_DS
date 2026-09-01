import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogisticsWeightedGraph {

    public record Route(String to, double cost) {
        public Route {
            if (to == null || to.isBlank()) throw new IllegalArgumentException("Destination cannot be null");
            if (cost < 0) throw new IllegalArgumentException("Logistics cost cannot be negative: " + cost);
        }

        @Override
        public String toString() {
            return String.format("%s (運費: $%.1f)", to, cost);
        }
    }

    private final Map<String, List<Route>> network = new LinkedHashMap<>();

    public boolean addHub(String hub) {
        if (hub == null || hub.isBlank()) return false;
        String name = hub.trim();
        return network.putIfAbsent(name, new ArrayList<>()) == null;
    }

    public boolean addOrUpdateRoute(String from, String to, double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative: " + cost);
        }
        if (from == null || to == null) return false;
        String src = from.trim();
        String dst = to.trim();

        if (!network.containsKey(src)) {
            throw new IllegalArgumentException("Source hub does not exist: " + src);
        }
        if (!network.containsKey(dst)) {
            throw new IllegalArgumentException("Destination hub does not exist: " + dst);
        }

        List<Route> routes = network.get(src);
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).to().equalsIgnoreCase(dst)) {
                // 既有路線，更新成本
                routes.set(i, new Route(dst, cost));
                return false; // 代表更新而非新增
            }
        }

        // 新路線
        routes.add(new Route(dst, cost));
        return true;
    }

    public boolean removeRoute(String from, String to) {
        if (from == null || to == null) return false;
        List<Route> routes = network.get(from.trim());
        if (routes == null) return false;

        return routes.removeIf(r -> r.to().equalsIgnoreCase(to.trim()));
    }

    public double getCost(String from, String to) {
        if (from == null || to == null) return -1.0;
        List<Route> routes = network.get(from.trim());
        if (routes != null) {
            for (Route r : routes) {
                if (r.to().equalsIgnoreCase(to.trim())) {
                    return r.cost();
                }
            }
        }
        return -1.0; // 無直達路線
    }

    public List<Route> getOutgoingRoutes(String hub) {
        if (hub == null) return List.of();
        List<Route> list = network.get(hub.trim());
        return list == null ? List.of() : List.copyOf(list);
    }

    public void printLogisticsReport() {
        System.out.println("============================== 物流轉運中心航線成本總覽 ==============================");
        System.out.printf("%-12s | %-10s | %s%n", "轉運樞紐 (Hub)", "直達航線數", "直達目的地與運費");
        System.out.println("-------------------------------------------------------------------------------------");

        for (Map.Entry<String, List<Route>> entry : network.entrySet()) {
            System.out.printf("%-12s | %-10d | %s%n",
                    entry.getKey(), entry.getValue().size(),
                    entry.getValue().isEmpty() ? "(無直達出口航線)" : entry.getValue());
        }
        System.out.println("=====================================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業六：物流成本網路 (LogisticsWeightedGraph) ===\n");
        LogisticsWeightedGraph logistics = new LogisticsWeightedGraph();

        // 註冊主要物流轉運樞紐
        for (String hub : List.of("台北中心", "桃園機場", "台中轉運站", "高雄港", "花蓮集散點")) {
            logistics.addHub(hub);
        }

        // 新增帶權重有向路線 (from -> to, cost)
        logistics.addOrUpdateRoute("台北中心", "桃園機場", 120.0);
        logistics.addOrUpdateRoute("台北中心", "台中轉運站", 350.0);
        logistics.addOrUpdateRoute("台北中心", "花蓮集散點", 450.0);

        logistics.addOrUpdateRoute("桃園機場", "高雄港", 800.0);
        logistics.addOrUpdateRoute("桃園機場", "台中轉運站", 280.0);

        logistics.addOrUpdateRoute("台中轉運站", "高雄港", 320.0);
        logistics.addOrUpdateRoute("台中轉運站", "台北中心", 340.0); // 雙向但成本不同

        logistics.addOrUpdateRoute("高雄港", "花蓮集散點", 600.0);

        // 印出物流網路報告
        logistics.printLogisticsReport();

        // 測試路線成本查詢
        System.out.println("--- 運費查詢 ---");
        System.out.printf("台北中心 -> 台中轉運站: $%.1f%n", logistics.getCost("台北中心", "台中轉運站"));
        System.out.printf("台中轉運站 -> 台北中心: $%.1f%n", logistics.getCost("台中轉運站", "台北中心"));
        System.out.printf("台北中心 -> 高雄港 (無直達): $%.1f%n", logistics.getCost("台北中心", "高雄港"));

        // 測試路線更新 (油價上漲/降價)
        System.out.println("\n--- 調整運費: 台北中心 -> 桃園機場 (優惠特價 $99.0) ---");
        logistics.addOrUpdateRoute("台北中心", "桃園機場", 99.0);
        System.out.printf("更新後 台北中心 -> 桃園機場: $%.1f%n", logistics.getCost("台北中心", "桃園機場"));

        // 測試負權重防呆
        System.out.println("\n--- 異常防呆測試 (負權重與不存在站點) ---");
        try {
            logistics.addOrUpdateRoute("台北中心", "桃園機場", -50.0);
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: 成功攔截負權重: " + e.getMessage());
        }

        try {
            logistics.addOrUpdateRoute("台北中心", "未知城市", 100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: 成功攔截未知節點: " + e.getMessage());
        }
    }
}
