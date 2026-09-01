import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LoginActivityReport {

    public record LoginRecord(String username, String ipAddress, String timestamp, boolean success) {
        public LoginRecord {
            if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be null");
            if (ipAddress == null || ipAddress.isBlank()) throw new IllegalArgumentException("IP cannot be null");
        }
    }

    public static class Analyzer {
        private final Map<String, Integer> totalAttempts = new HashMap<>();
        private final Map<String, Integer> failedAttempts = new HashMap<>();
        private final Map<String, Set<String>> distinctIps = new HashMap<>();

        public void processLog(LoginRecord record) {
            if (record == null) return;
            String user = record.username().trim().toLowerCase();
            String ip = record.ipAddress().trim();

            totalAttempts.merge(user, 1, Integer::sum);
            if (!record.success()) {
                failedAttempts.merge(user, 1, Integer::sum);
            }
            distinctIps.computeIfAbsent(user, k -> new HashSet<>()).add(ip);
        }

        public void processLogs(List<LoginRecord> logs) {
            if (logs != null) {
                for (LoginRecord log : logs) {
                    processLog(log);
                }
            }
        }

        public void printActivityReport() {
            System.out.println("============================== 帳號登入活動總覽 ==============================");
            System.out.printf("%-15s | %-12s | %-12s | %-12s | %s%n",
                    "帳號", "總登入嘗試", "失敗次數", "不同 IP 數", "登入來源 IP 清單");
            System.out.println("-------------------------------------------------------------------------------");

            Map<String, Integer> sortedUsers = new TreeMap<>(totalAttempts);
            for (String user : sortedUsers.keySet()) {
                int total = totalAttempts.get(user);
                int failed = failedAttempts.getOrDefault(user, 0);
                Set<String> ips = distinctIps.getOrDefault(user, Set.of());

                System.out.printf("%-15s | %-12d | %-12d | %-12d | %s%n",
                        user, total, failed, ips.size(), ips);
            }
            System.out.println("===============================================================================\n");
        }

        public void printSecurityAlerts(int maxAllowedIps, int maxFailedAttempts) {
            System.out.println("============================== 資安異常警示分析報告 ==============================");
            System.out.printf("觸發條件: 來源 IP 數 >= %d 或 失敗次數 >= %d%n", maxAllowedIps, maxFailedAttempts);
            System.out.println("----------------------------------------------------------------------------------");

            int alertCount = 0;
            for (String user : totalAttempts.keySet()) {
                int failed = failedAttempts.getOrDefault(user, 0);
                Set<String> ips = distinctIps.getOrDefault(user, Set.of());
                List<String> reasons = new ArrayList<>();

                if (ips.size() >= maxAllowedIps) {
                    reasons.add("異地登入警報: 來自 " + ips.size() + " 個不同 IP (" + ips + ")");
                }
                if (failed >= maxFailedAttempts) {
                    reasons.add("暴力破解疑慮: 登入失敗 " + failed + " 次");
                }

                if (!reasons.isEmpty()) {
                    alertCount++;
                    System.out.printf("[警告] 帳號【%-12s】異常原因: %s%n", user, String.join(" | ", reasons));
                }
            }

            if (alertCount == 0) {
                System.out.println("所有帳號登入狀態正常，無觸發資安警報。");
            }
            System.out.println("==================================================================================\n");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業二：登入紀錄分析 (LoginActivityReport) ===\n");

        List<LoginRecord> logs = List.of(
            new LoginRecord("alice", "192.168.1.10", "10:00:01", true),
            new LoginRecord("alice", "192.168.1.10", "10:05:22", true),
            new LoginRecord("bob", "140.112.1.1", "10:01:15", false),
            new LoginRecord("bob", "140.112.1.1", "10:01:40", false),
            new LoginRecord("bob", "140.112.1.1", "10:02:10", false), // Bob 失敗 3 次
            new LoginRecord("charlie", "10.0.0.1", "10:10:00", true),
            new LoginRecord("charlie", "172.16.0.5", "10:15:30", true),
            new LoginRecord("charlie", "203.74.1.20", "10:20:00", true), // Charlie 來自 3 個不同 IP
            new LoginRecord("david", "192.168.1.50", "10:30:10", true),
            new LoginRecord("hacker", "185.220.101.1", "11:00:00", false),
            new LoginRecord("hacker", "185.220.101.2", "11:00:05", false),
            new LoginRecord("hacker", "185.220.101.3", "11:00:10", false),
            new LoginRecord("hacker", "185.220.101.4", "11:00:15", false)  // Hacker 多 IP 且多次失敗
        );

        Analyzer analyzer = new Analyzer();
        analyzer.processLogs(logs);

        // 產出登入彙總
        analyzer.printActivityReport();

        // 產出異常安全警示 (IP >= 2 或 失敗 >= 3)
        analyzer.printSecurityAlerts(2, 3);
    }
}
