import java.util.Comparator;
import java.util.PriorityQueue;

public class SupportTicketQueue {

    public record Ticket(String id, int severity, long createdOrder) {
        public Ticket {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("Ticket id cannot be null or blank");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題三：Support Ticket Queue 工作排程 ===");

        // 優先順序規則：
        // 1. severity 越大越優先 (降序)
        // 2. severity 相同時，createdOrder 越小越優先 (升序)
        // 3. 若仍相同，id 字典序由小到大 (升序)
        Comparator<Ticket> ticketComparator = Comparator
                .comparingInt(Ticket::severity).reversed()
                .thenComparingLong(Ticket::createdOrder)
                .thenComparing(Ticket::id);

        PriorityQueue<Ticket> queue = new PriorityQueue<>(ticketComparator);

        // 加入測試工單
        Ticket[] testTickets = {
            new Ticket("TCK-101", 1, 1001L), // 低優先級, 較早建立
            new Ticket("TCK-102", 3, 1002L), // 高優先級, 稍後建立
            new Ticket("TCK-103", 2, 1003L), // 中優先級
            new Ticket("TCK-104", 3, 1000L), // 高優先級, 最早建立
            new Ticket("TCK-105", 2, 1004L), // 中優先級, 較晚建立
            new Ticket("TCK-106", 4, 1005L), // 特急件
            new Ticket("TCK-107", 1, 1006L)  // 低優先級, 較晚建立
        };

        System.out.println("已加入 " + testTickets.length + " 筆工單。依優先度順序取出處理：\n");
        for (Ticket t : testTickets) {
            queue.offer(t);
        }

        System.out.println("工單處理順序 (格式: id|severity|createdOrder):");
        System.out.println("----------------------------------------");
        while (!queue.isEmpty()) {
            Ticket t = queue.poll();
            System.out.printf("%s|%d|%d%n", t.id(), t.severity(), t.createdOrder());
        }
        System.out.println("----------------------------------------");
        System.out.println("所有工單排程處理完畢。");
    }
}
