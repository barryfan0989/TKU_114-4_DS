import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class EventSimulationQueue {

    public record Event(String eventId, long timestamp, String type, int sequence, String description) {
        public Event {
            if (eventId == null || eventId.isBlank()) {
                throw new IllegalArgumentException("Event ID cannot be null or blank");
            }
        }

        @Override
        public String toString() {
            return String.format("[時間: %04d | 序號: %d | ID: %s | 類型: %-10s] %s",
                    timestamp, sequence, eventId, type, description);
        }
    }

    private final PriorityQueue<Event> eventQueue;
    private final Set<String> cancelledEventIds = new HashSet<>();
    private final List<String> executionLogs = new ArrayList<>();

    public EventSimulationQueue() {
        // 事件優先順序：
        // 1. timestamp 越早越優先 (升序)
        // 2. timestamp 相同時，sequence 越小越優先 (升序)
        // 3. 若仍相同，eventId 字典序 (升序)
        Comparator<Event> comparator = Comparator
                .comparingLong(Event::timestamp)
                .thenComparingInt(Event::sequence)
                .thenComparing(Event::eventId);

        this.eventQueue = new PriorityQueue<>(comparator);
    }

    public void schedule(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        eventQueue.offer(event);
        String log = "【已排程事件】" + event;
        System.out.println(log);
        executionLogs.add(log);
    }

    public boolean cancel(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            return false;
        }
        cancelledEventIds.add(eventId);
        String log = "【已取消事件】Event ID: " + eventId;
        System.out.println(log);
        executionLogs.add(log);
        return true;
    }

    public void runSimulation() {
        System.out.println("\n========== 開始執行事件模擬器 ==========");
        executionLogs.add("========== 開始執行事件模擬器 ==========");

        int executedCount = 0;
        int cancelledCount = 0;

        while (!eventQueue.isEmpty()) {
            Event current = eventQueue.poll();
            if (cancelledEventIds.contains(current.eventId())) {
                String log = String.format(">>> [跳過取消事件] ID=%s, Type=%s, Timestamp=%d",
                        current.eventId(), current.type(), current.timestamp());
                System.out.println(log);
                executionLogs.add(log);
                cancelledCount++;
            } else {
                String log = String.format(">>> [執行事件] %s", current);
                System.out.println(log);
                executionLogs.add(log);
                executedCount++;
            }
        }

        String summary = String.format("\n模擬結束！成功執行: %d 個事件，取消/跳過: %d 個事件。",
                executedCount, cancelledCount);
        System.out.println(summary);
        executionLogs.add(summary);
    }

    public List<String> getExecutionLogs() {
        return List.copyOf(executionLogs);
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業二：Event Simulation Queue (活動事件模擬器) ===\n");
        EventSimulationQueue simulator = new EventSimulationQueue();

        // 排程多個模擬事件 (包含相同時間但不同序號的事件)
        simulator.schedule(new Event("EVT-01", 100, "INIT", 1, "系統開機初始化"));
        simulator.schedule(new Event("EVT-02", 200, "NETWORK", 1, "建立 WebSocket 連線"));
        simulator.schedule(new Event("EVT-03", 200, "AUTH", 2, "使用者登入驗證")); // 同時間 200，序號較大
        simulator.schedule(new Event("EVT-04", 150, "DATA_SYNC", 1, "同步本地快取"));
        simulator.schedule(new Event("EVT-05", 300, "TASK", 1, "定時批次數據運算"));
        simulator.schedule(new Event("EVT-06", 300, "TASK", 2, "定時報表寄送 (即將取消)"));
        simulator.schedule(new Event("EVT-07", 500, "SHUTDOWN", 1, "伺服器優雅關機"));

        // 動態取消事件 EVT-06
        System.out.println();
        simulator.cancel("EVT-06");

        // 執行模擬
        simulator.runSimulation();
    }
}
