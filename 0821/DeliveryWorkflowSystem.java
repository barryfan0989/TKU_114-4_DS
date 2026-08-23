import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class DeliveryTask {
    private final String deliveryId;
    private final String address;
    private String status;

    DeliveryTask(String deliveryId, String address) {
        this.deliveryId = deliveryId != null ? deliveryId.trim() : "";
        this.address = address != null ? address.trim() : "";
        this.status = "WAITING";
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DeliveryTask{" +
                "ID='" + deliveryId + '\'' +
                ", 地址='" + address + '\'' +
                ", 狀態='" + status + '\'' +
                '}';
    }
}

public class DeliveryWorkflowSystem {
    private final Map<String, DeliveryTask> taskMap = new HashMap<>();
    private final Deque<DeliveryTask> waitingQueue = new ArrayDeque<>();
    private final Deque<DeliveryTask> completedStack = new ArrayDeque<>();

    public boolean addTask(DeliveryTask task) {
        if (task == null || task.getDeliveryId().isEmpty()) {
            return false;
        }
        // 檢查 ID 重複
        if (taskMap.containsKey(task.getDeliveryId())) {
            System.out.println("新增配送任務失敗：重複的任務 ID " + task.getDeliveryId());
            return false;
        }
        taskMap.put(task.getDeliveryId(), task);
        waitingQueue.offerLast(task);
        System.out.println("成功新增配送任務：" + task);
        return true;
    }

    public DeliveryTask processNext() {
        if (waitingQueue.isEmpty()) {
            System.out.println("處理提示：目前無等待配送之任務。");
            return null;
        }
        DeliveryTask task = waitingQueue.pollFirst();
        task.setStatus("COMPLETED");
        completedStack.push(task);
        System.out.println("已處理配送任務：" + task);
        return task;
    }

    public boolean undoLastCompletion() {
        if (completedStack.isEmpty()) {
            System.out.println("復原提示：無已完成的任務，無法復原。");
            return false;
        }
        DeliveryTask lastTask = completedStack.pop();
        lastTask.setStatus("WAITING");
        // 推回等待隊列的前端，保留其處理順序
        waitingQueue.offerFirst(lastTask);
        System.out.println("成功復原最後完成的任務，已放回等待隊列首位：" + lastTask);
        return true;
    }

    public DeliveryTask queryTask(String id) {
        if (id == null) return null;
        return taskMap.get(id.trim());
    }

    public void printSummary() {
        System.out.println("--- 配送系統狀態摘要 ---");
        System.out.println("   等待中任務數量: " + waitingQueue.size() + " " + waitingQueue);
        System.out.println("   已完成任務數量: " + completedStack.size() + " " + completedStack);
        System.out.println("   總任務註冊數量: " + taskMap.size());
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業三：物流工作流程 ===");

        DeliveryWorkflowSystem system = new DeliveryWorkflowSystem();

        // 1. 新增配送任務
        System.out.println("--- 開始建立配送工單 ---");
        system.addTask(new DeliveryTask("D001", "台北市信義路五段"));
        system.addTask(new DeliveryTask("D002", "新北市板橋區文化路"));
        system.addTask(new DeliveryTask("D003", "台中市西屯區台灣大道"));

        // 2. 測試重複 ID 拒絕
        system.addTask(new DeliveryTask("D001", "台北市和平東路")); // 應失敗

        system.printSummary();

        // 3. 處理任務
        System.out.println("--- 配送員依序出車配送 ---");
        system.processNext(); // 處理 D001
        system.processNext(); // 處理 D002

        system.printSummary();

        // 4. 查詢任務
        System.out.println("--- 查詢任務詳情 ---");
        System.out.println("查詢 D001: " + system.queryTask("D001")); // 狀態為 COMPLETED
        System.out.println("查詢 D003: " + system.queryTask("D003")); // 狀態為 WAITING

        // 5. 復原最後一個完成 (Undo)
        System.out.println("\n--- 復原操作 ---");
        system.undoLastCompletion(); // 復原 D002，使其狀態變回 WAITING 並加入 waitingQueue 最前

        system.printSummary(); // 此時等待中：[D002, D003]

        // 6. 再次處理任務 (D002 應先被處理)
        System.out.println("--- 再次依序處理 ---");
        system.processNext(); // 此時應處理 D002
    }
}
