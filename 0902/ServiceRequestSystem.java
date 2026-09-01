import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ServiceRequestSystem {

    public enum Status {
        PENDING, PROCESSING, COMPLETED, CANCELLED
    }

    public static class Request {
        private final String id;
        private final String clientName;
        private final int priority;
        private final long createdTime;
        private final String description;
        private Status status;

        public Request(String id, String clientName, int priority, long createdTime, String description) {
            if (id == null || id.isBlank()) throw new IllegalArgumentException("ID cannot be blank");
            this.id = id.trim();
            this.clientName = clientName.trim();
            this.priority = priority;
            this.createdTime = createdTime;
            this.description = description.trim();
            this.status = Status.PENDING;
        }

        public String getId() { return id; }
        public String getClientName() { return clientName; }
        public int getPriority() { return priority; }
        public long getCreatedTime() { return createdTime; }
        public String getDescription() { return description; }
        public Status getStatus() { return status; }
        public void setStatus(Status status) { this.status = status; }

        @Override
        public String toString() {
            return String.format("[工單 %s] 客戶: %s | 優先級: %d | 時間: %d | 狀態: %s | 內容: %s",
                    id, clientName, priority, createdTime, status, description);
        }
    }

    // 1. HashMap: O(1) 快速依 ID 查找、狀態更新與取消
    private final Map<String, Request> requestMap = new HashMap<>();

    // 2. PriorityQueue: 優先佇列排程 (優先度大者優先；同優先度時間早者優先)
    private final PriorityQueue<Request> priorityQueue;

    public ServiceRequestSystem() {
        Comparator<Request> comparator = Comparator
                .comparingInt(Request::getPriority).reversed()
                .thenComparingLong(Request::getCreatedTime)
                .thenComparing(Request::getId);

        this.priorityQueue = new PriorityQueue<>(comparator);
    }

    public boolean submitRequest(String id, String client, int priority, String description) {
        if (requestMap.containsKey(id)) {
            System.out.printf("  [提交失敗] 工單 ID: %s 已存在！%n", id);
            return false;
        }
        long timestamp = System.currentTimeMillis();
        Request req = new Request(id, client, priority, timestamp, description);

        requestMap.put(id, req);
        priorityQueue.offer(req);
        System.out.println("【工單提交成功】" + req);
        return true;
    }

    public boolean cancelRequest(String id) {
        Request req = requestMap.get(id);
        if (req == null) {
            System.out.printf("  [取消失敗] 找不到工單 ID: %s%n", id);
            return false;
        }
        if (req.getStatus() != Status.PENDING) {
            System.out.printf("  [取消失敗] 工單 %s 狀態為 %s，無法取消！%n", id, req.getStatus());
            return false;
        }

        req.setStatus(Status.CANCELLED);
        System.out.printf("【工單已取消】ID: %s (已於 HashMap 標記為 CANCELLED，將於 Queue 叫號時自動略過)%n", id);
        return true;
    }

    /**
     * 取出並處理下一張最高優先且未被取消的有效工單 (雙重結構同步驗證)。
     */
    public Request processNextRequest() {
        while (!priorityQueue.isEmpty()) {
            Request candidate = priorityQueue.poll();
            Request actual = requestMap.get(candidate.getId());

            // 雙結構同步檢查：若已被取消則跳過 (Lazy Deletion 模式)
            if (actual == null || actual.getStatus() == Status.CANCELLED) {
                System.out.printf("  -> [跳過作廢工單] %s (已取消)%n", candidate.getId());
                continue;
            }

            actual.setStatus(Status.COMPLETED);
            System.out.println("【開始處理並完成工單】" + actual);
            return actual;
        }

        System.out.println("【排程提示】目前無待處理工單。");
        return null;
    }

    public Request queryRequest(String id) {
        return requestMap.get(id);
    }

    public int getPendingCount() {
        int count = 0;
        for (Request r : requestMap.values()) {
            if (r.getStatus() == Status.PENDING) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== 期末綜合練習二：工單排程與雙重索引同步系統 (ServiceRequestSystem) ===\n");
        ServiceRequestSystem system = new ServiceRequestSystem();

        // 提交多筆工單
        System.out.println("--- 1. 客戶陸續提交服務工單 ---");
        system.submitRequest("REQ-101", "台灣大哥大", 2, "一般網路連線障礙回報");
        system.submitRequest("REQ-102", "台積電", 5, "核心資料庫伺服器停機特急件");
        system.submitRequest("REQ-103", "淡江大學", 3, "校園選課系統尖峰流量監控");
        system.submitRequest("REQ-104", "國泰世華", 5, "ATM 跨行轉帳超時異常排查"); // 同為優先級 5，稍後建立
        system.submitRequest("REQ-105", "個人用戶", 1, "帳號密碼重設諮詢");

        System.out.printf("\n目前待處理工單數: %d 筆%n%n", system.getPendingCount());

        // 測試取消操作 (HashMap 狀態更新)
        System.out.println("--- 2. 客戶申請取消工單 (REQ-103 與 REQ-105) ---");
        system.cancelRequest("REQ-103");
        system.cancelRequest("REQ-105");

        // 依優先度處理工單 (Queue 自動過濾已取消的工單)
        System.out.println("\n--- 3. 系統依優先級動態叫號處理 ---");
        while (system.getPendingCount() > 0) {
            system.processNextRequest();
        }

        System.out.println("\n--- 4. 再次叫號 (空佇列檢查) ---");
        system.processNextRequest();

        // 查詢特定工單狀態 (HashMap O(1) 驗證)
        System.out.println("\n--- 5. 依 ID 直接查詢最終工單狀態 (HashMap 直查) ---");
        System.out.println("查詢 REQ-102: " + system.queryRequest("REQ-102"));
        System.out.println("查詢 REQ-103 (已取消): " + system.queryRequest("REQ-103"));
    }
}
