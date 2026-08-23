import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class ServiceTicket {
    private final String id;
    private final String description;
    private boolean completed;

    ServiceTicket(String id, String description) {
        this.id = id != null ? id.trim() : "";
        this.description = description != null ? description.trim() : "";
        this.completed = false;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ServiceTicket{" +
                "ID='" + id + '\'' +
                ", 說明='" + description + '\'' +
                ", 已完成=" + completed +
                '}';
    }
}

public class ServiceCenterWorkflow {
    private final Map<String, ServiceTicket> ticketMap = new HashMap<>();
    private final Deque<ServiceTicket> waitingQueue = new ArrayDeque<>();
    private final Deque<ServiceTicket> completedStack = new ArrayDeque<>();
    private final Set<String> activeIds = new HashSet<>();

    public boolean createTicket(String id, String desc) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        String cleanId = id.trim();
        // 檢查重複工單 ID
        if (!activeIds.add(cleanId)) {
            System.out.println("建立工單失敗：重複的工單 ID " + cleanId);
            return false;
        }
        ServiceTicket ticket = new ServiceTicket(cleanId, desc);
        ticketMap.put(cleanId, ticket);
        waitingQueue.offerLast(ticket);
        System.out.println("成功建立工單：" + ticket);
        return true;
    }

    public ServiceTicket processNext() {
        if (waitingQueue.isEmpty()) {
            System.out.println("處理提示：目前沒有等待處理的工單。");
            return null;
        }
        ServiceTicket ticket = waitingQueue.pollFirst();
        ticket.setCompleted(true);
        completedStack.push(ticket);
        System.out.println("工單處理完成：" + ticket);
        return ticket;
    }

    public boolean cancelWaiting(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        String cleanId = id.trim();
        // 檢查該工單是否存在且未完成
        ServiceTicket ticket = ticketMap.get(cleanId);
        if (ticket == null) {
            System.out.println("取消失敗：找不到工單 ID " + cleanId);
            return false;
        }
        if (ticket.isCompleted()) {
            System.out.println("取消失敗：工單 ID " + cleanId + " 已處理完畢，無法取消！");
            return false;
        }

        // 從等待隊列中移除
        boolean removed = false;
        Iterator<ServiceTicket> iterator = waitingQueue.iterator();
        while (iterator.hasNext()) {
            ServiceTicket t = iterator.next();
            if (t.getId().equals(cleanId)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            // 從 map 與 activeIds 移除
            ticketMap.remove(cleanId);
            activeIds.remove(cleanId);
            System.out.println("成功取消等待中的工單：ID " + cleanId);
            return true;
        }
        return false;
    }

    public boolean undoLastCompletion() {
        if (completedStack.isEmpty()) {
            System.out.println("復原提示：目前無已完成工單，無法復原。");
            return false;
        }
        ServiceTicket ticket = completedStack.pop();
        ticket.setCompleted(false);
        // 放回 waiting queue 的最前端以保留其優先處理順序
        waitingQueue.offerFirst(ticket);
        System.out.println("已復原最後完成的工單，放回等待隊列首位：" + ticket);
        return true;
    }

    public ServiceTicket findById(String id) {
        if (id == null) return null;
        return ticketMap.get(id.trim());
    }

    public void printSummary() {
        System.out.println("--- 服務中心狀態摘要 ---");
        System.out.println("   [Active IDs]  (去重集合): " + activeIds);
        System.out.println("   [Waiting Queue](等待佇列): " + waitingQueue);
        System.out.println("   [Completed Stack](已完成): " + completedStack);
        System.out.println("   [Ticket Map]  (對照總表): " + ticketMap.keySet());
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業六：服務中心排隊與取消 ===");

        ServiceCenterWorkflow center = new ServiceCenterWorkflow();

        // 1. 建立工單
        System.out.println("--- 建立工單測試 ---");
        center.createTicket("TK-01", "網路連線異常");
        center.createTicket("TK-02", "印表機卡紙");
        center.createTicket("TK-03", "忘記開機密碼");

        // 2. 測試重複 ID 建立 (應被拒絕)
        center.createTicket("TK-01", "重複的網絡報修");

        center.printSummary();

        // 3. 處理工單 (處理第一筆 TK-01)
        System.out.println("--- 依序服務工單 (FIFO) ---");
        center.processNext(); // 處理 TK-01

        // 4. 測試取消功能
        System.out.println("\n--- 測試取消工單 ---");
        // (a) 取消等待中的工單 TK-03，應成功
        center.cancelWaiting("TK-03"); 
        // (b) 嘗試取消已完成的工單 TK-01，應失敗
        center.cancelWaiting("TK-01");
        // (c) 嘗試取消不存在的工單 TK-99，應失敗
        center.cancelWaiting("TK-99");

        center.printSummary(); // 等待中應只剩下 TK-02

        // 5. 測試連續兩次復原 (Undo)
        System.out.println("--- 測試連續復原 (Undo) ---");
        // 目前已完成：[TK-01]，等待中：[TK-02]
        center.undoLastCompletion(); // 復原 TK-01，使其狀態變為未完成，並插回等待隊列首位

        // 此時已完成為空，等待中：[TK-01, TK-02]
        center.printSummary();

        // 再次嘗試復原 (此時已完成為空，應顯示提示且不動作)
        center.undoLastCompletion(); 

        // 6. 再次處理
        System.out.println("\n--- 再次依序服務 ---");
        center.processNext(); // 應處理剛才被復原的 TK-01
        
        System.out.println("\n--- 檢索工單 ---");
        System.out.println("查詢工單 TK-01: " + center.findById("TK-01"));
        System.out.println("查詢已被取消的 TK-03 (應為 null): " + center.findById("TK-03"));
    }
}
