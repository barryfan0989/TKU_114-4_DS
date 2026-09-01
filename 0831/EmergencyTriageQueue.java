import java.util.Comparator;
import java.util.PriorityQueue;

public class EmergencyTriageQueue {

    /**
     * 急診病患紀錄：
     * @param patientId    病歷號
     * @param name         病患姓名
     * @param triageLevel  檢傷分類等級 (1: 復甦急救, 2: 危急, 3: 緊急, 4: 次緊急, 5: 非緊急; 數值越小越緊急)
     * @param arrivalOrder 到院掛號順序 (數值越小越早到)
     */
    public record Patient(String patientId, String name, int triageLevel, long arrivalOrder) {
        public Patient {
            if (patientId == null || patientId.isBlank()) {
                throw new IllegalArgumentException("Patient ID cannot be null or blank");
            }
            if (triageLevel < 1 || triageLevel > 5) {
                throw new IllegalArgumentException("Triage level must be between 1 and 5");
            }
        }

        @Override
        public String toString() {
            return String.format("[%s] %s (檢傷等級: Level %d, 到院序: #%d)",
                    patientId, name, triageLevel, arrivalOrder);
        }
    }

    private final PriorityQueue<Patient> queue;

    public EmergencyTriageQueue() {
        // 優先度規則：
        // 1. triageLevel 越小越優先 (升序)
        // 2. triageLevel 相同時，arrivalOrder 越小越優先 (升序)
        // 3. 若仍相同，patientId 字典序 (升序)
        Comparator<Patient> comparator = Comparator
                .comparingInt(Patient::triageLevel)
                .thenComparingLong(Patient::arrivalOrder)
                .thenComparing(Patient::patientId);

        this.queue = new PriorityQueue<>(comparator);
    }

    public void checkIn(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        queue.offer(patient);
        System.out.println("【病患報到】" + patient + "，目前候診人數: " + queue.size());
    }

    public Patient peekNext() {
        return queue.peek();
    }

    public Patient callNext() {
        if (isEmpty()) {
            System.out.println("【叫號提示】目前候診佇列為空，無等待病患。");
            return null;
        }
        Patient p = queue.poll();
        System.out.println("【叫號看診】請 " + p + " 進入急診診間！剩餘候診: " + queue.size());
        return p;
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業一：Emergency Triage Queue (急診候診佇列) ===\n");
        EmergencyTriageQueue triageQueue = new EmergencyTriageQueue();

        // 測試 1: 空佇列叫號與查看
        System.out.println("--- 測試 1: 空佇列操作 ---");
        System.out.println("查看下一位病患: " + triageQueue.peekNext());
        triageQueue.callNext();

        // 測試 2: 多位不同檢傷等級病患依序掛號
        System.out.println("\n--- 測試 2: 病患陸續報到 ---");
        long seq = 1;
        triageQueue.checkIn(new Patient("P-001", "張三 (發燒)", 4, seq++));
        triageQueue.checkIn(new Patient("P-002", "李四 (胸痛呼吸困難)", 2, seq++));
        triageQueue.checkIn(new Patient("P-003", "王五 (擦傷)", 5, seq++));
        triageQueue.checkIn(new Patient("P-004", "趙六 (急性大量出血)", 1, seq++));
        triageQueue.checkIn(new Patient("P-005", "錢七 (嚴重骨折)", 2, seq++)); // 與李四同為 Level 2，但較晚到
        triageQueue.checkIn(new Patient("P-006", "孫八 (心跳停止 OHCA)", 1, seq++)); // 與趙六同為 Level 1，但較晚到

        // 測試 3: 查看下一位最緊急病患
        System.out.println("\n--- 測試 3: 查看目前最優先病患 (peekNext) ---");
        System.out.println("預計下一位就診者: " + triageQueue.peekNext());

        // 測試 4: 依序叫號就診
        System.out.println("\n--- 測試 4: 急診醫師依序叫號處理 ---");
        while (!triageQueue.isEmpty()) {
            triageQueue.callNext();
        }

        // 測試 5: 全部處理完後的空佇列防呆
        System.out.println("\n--- 測試 5: 全部病患就診完畢後叫號 ---");
        triageQueue.callNext();
    }
}
