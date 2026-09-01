import java.util.ArrayList;
import java.util.List;

public class MetroMatrixGraph {
    private final List<String> stations;
    private final boolean[][] matrix;

    public MetroMatrixGraph(List<String> stations) {
        if (stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException("Stations list cannot be null or empty");
        }
        this.stations = List.copyOf(stations);
        int n = stations.size();
        this.matrix = new boolean[n][n];
    }

    private int indexOf(String station) {
        int idx = stations.indexOf(station);
        if (idx < 0) {
            throw new IllegalArgumentException("Unknown metro station: " + station);
        }
        return idx;
    }

    public boolean addTrack(String station1, String station2) {
        if (station1 == null || station2 == null || station1.equals(station2)) {
            return false;
        }
        int a = indexOf(station1);
        int b = indexOf(station2);

        if (matrix[a][b]) {
            return false; // 軌道已存在
        }

        matrix[a][b] = true;
        matrix[b][a] = true;
        return true;
    }

    public boolean isConnected(String station1, String station2) {
        return matrix[indexOf(station1)][indexOf(station2)];
    }

    public int degree(String station) {
        int row = indexOf(station);
        int count = 0;
        for (boolean direct : matrix[row]) {
            if (direct) count++;
        }
        return count;
    }

    public List<String> adjacentStations(String station) {
        int row = indexOf(station);
        List<String> list = new ArrayList<>();
        for (int col = 0; col < stations.size(); col++) {
            if (matrix[row][col]) {
                list.add(stations.get(col));
            }
        }
        return list;
    }

    public int edgeCount() {
        int total = 0;
        int n = stations.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j]) total++;
            }
        }
        return total;
    }

    public void printMatrixReport() {
        System.out.println("============================== 台北捷運路網 Adjacency Matrix ==============================");
        System.out.printf("%-12s", "站名");
        for (String s : stations) {
            System.out.printf("%-10s", s);
        }
        System.out.println();
        System.out.println("-".repeat(12 + stations.size() * 10));

        for (int i = 0; i < stations.size(); i++) {
            System.out.printf("%-12s", stations.get(i));
            for (int j = 0; j < stations.size(); j++) {
                System.out.printf("%-10d", matrix[i][j] ? 1 : 0);
            }
            System.out.println();
        }
        System.out.println("========================================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業四：捷運路網 Matrix Graph (MetroMatrixGraph) ===\n");

        // 模擬台北捷運部分關鍵樞紐站
        List<String> stations = List.of("台北車站", "中山", "雙連", "西門", "中正紀念堂", "東門", "忠孝新生");
        MetroMatrixGraph metro = new MetroMatrixGraph(stations);

        // 紅線 (淡水信義線): 雙連 <-> 中山 <-> 台北車站 <-> 中正紀念堂 <-> 東門
        metro.addTrack("雙連", "中山");
        metro.addTrack("中山", "台北車站");
        metro.addTrack("台北車站", "中正紀念堂");
        metro.addTrack("中正紀念堂", "東門");

        // 藍線 (板南線): 西門 <-> 台北車站 <-> 忠孝新生
        metro.addTrack("西門", "台北車站");
        metro.addTrack("台北車站", "忠孝新生");

        // 綠線 (松山新店線): 中山 <-> 西門 <-> 中正紀念堂
        metro.addTrack("中山", "西門");
        metro.addTrack("西門", "中正紀念堂");

        // 橘線 (中和新蘆線): 中山 <-> 忠孝新生 <-> 東門
        metro.addTrack("忠孝新生", "東門");

        // 輸出矩陣
        metro.printMatrixReport();

        // 站點度數與轉乘能力分析
        System.out.println("--- 站點相鄰站 (Adjacent Stations) 與連通度 (Degree) 分析 ---");
        for (String station : stations) {
            int deg = metro.degree(station);
            String transferTag = deg >= 3 ? " ★【主要轉乘樞紐】" : "";
            System.out.printf("%-10s -> 連通軌道數 (Degree): %d, 相鄰站點: %s%s%n",
                    station, deg, metro.adjacentStations(station), transferTag);
        }

        System.out.println("\n全路網軌道總段數 (Total Edges): " + metro.edgeCount() + " 段");
    }
}
