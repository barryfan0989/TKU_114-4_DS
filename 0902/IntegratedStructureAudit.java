import java.util.ArrayList;
import java.util.List;

public class IntegratedStructureAudit {

    public enum Verdict {
        OPTIMAL, ACCEPTABLE, SUBOPTIMAL, INCORRECT
    }

    public record AuditCase(
            String caseId,
            String scenarioDescription,
            String proposedDataStructure,
            String optimalDataStructure,
            Verdict verdict,
            String diagnosticAdvice
    ) {
        public void printDiagnosis() {
            String badge = switch (verdict) {
                case OPTIMAL -> "【✔ 最佳選型 (OPTIMAL)】";
                case ACCEPTABLE -> "【▲ 可接受但非最優 (ACCEPTABLE)】";
                case SUBOPTIMAL -> "【⚠ 效能低落/反模式 (SUBOPTIMAL)】";
                case INCORRECT -> "【❌ 錯誤選型 (INCORRECT)】";
            };

            System.out.printf("[%s] %s%n", caseId, scenarioDescription);
            System.out.printf("  - 提議資料結構: %s%n", proposedDataStructure);
            System.out.printf("  - 最佳建議結構: %s%n", optimalDataStructure);
            System.out.printf("  - 審查判定: %s%n", badge);
            System.out.printf("  - 診斷分析: %s%n%n", diagnosticAdvice);
        }
    }

    public static List<AuditCase> runAuditSuite() {
        List<AuditCase> suite = new ArrayList<>();

        suite.add(new AuditCase(
                "AUDIT-01",
                "頻繁需要依據商品 ID (String) 快速精確查詢價格與庫存",
                "HashMap",
                "HashMap",
                Verdict.OPTIMAL,
                "使用 Key-Value 雜湊映射，提供平均 O(1) 的極速查找效能。"
        ));

        suite.add(new AuditCase(
                "AUDIT-02",
                "頻繁依賴商品 ID 查詢，卻使用未排序的 List 進行線性搜尋 (Linear Scan)",
                "ArrayList (with linear search)",
                "HashMap",
                Verdict.SUBOPTIMAL,
                "每次搜尋需要 O(N) 時間，當資料量龐大時將造成系統效能瓶頸，應改用 HashMap 建立主鍵索引。"
        ));

        suite.add(new AuditCase(
                "AUDIT-03",
                "實時串流需要動態取出目前最高銷售量的 Top 10 商品，卻對全部 100 萬筆商品進行完整排序 (Full Sort)",
                "ArrayList.sort (每次呼叫完整排序)",
                "PriorityQueue (Min Heap of size 10)",
                Verdict.SUBOPTIMAL,
                "每次排序需 O(N log N) 時間；使用固定大小為 10 的 Min Heap 僅需 O(N log K)，大幅節省 CPU 與空間。"
        ));

        suite.add(new AuditCase(
                "AUDIT-04",
                "系統需要查詢年齡在 [20, 30] 歲區間的所有使用者，並依年齡遞增輸出",
                "TreeMap / Balanced BST (紅黑樹)",
                "TreeMap / Balanced BST",
                Verdict.OPTIMAL,
                "TreeMap 維持鍵值排序性，支援 subMap 範圍快速檢索與中序遍歷。"
        ));

        suite.add(new AuditCase(
                "AUDIT-05",
                "儲存多對多社群追蹤與捷運路網轉乘分析",
                "Graph (Adjacency List)",
                "Graph (Adjacency List)",
                Verdict.OPTIMAL,
                "圖形結構能完整表達頂點與邊之拓撲關係，並支援 BFS/DFS 最短路徑搜尋。"
        ));

        suite.add(new AuditCase(
                "AUDIT-06",
                "印表機任務列印，要求嚴格按照送出時間先後順序處理",
                "ArrayDeque (Queue)",
                "ArrayDeque (Queue)",
                Verdict.OPTIMAL,
                "FIFO 先進先出保證任務處理公平性，入列與出列均為常數時間 O(1)。"
        ));

        suite.add(new AuditCase(
                "AUDIT-07",
                "需要多執行緒高頻隨機存取陣列中間元素，卻選擇了 LinkedList",
                "LinkedList (頻繁 get(index))",
                "ArrayList",
                Verdict.INCORRECT,
                "LinkedList 的 get(i) 為 O(N) 遍歷且快取局部性極差，隨機存取應唯一指定 ArrayList。"
        ));

        suite.add(new AuditCase(
                "AUDIT-08",
                "需要維護會員註冊黑名單，判斷使用者是否已被封禁",
                "HashSet",
                "HashSet",
                Verdict.OPTIMAL,
                "利用 Hash Membership Test 提供 O(1) 存在性檢查與去重保證。"
        ));

        return suite;
    }

    public static void main(String[] args) {
        System.out.println("============================== 期末綜合練習四：資料結構綜合診斷與架構審查 ==============================");
        System.out.println("本審查套件針對系統架構中常見的資料結構使用模式進行效能診斷與合理性審查：\n");

        List<AuditCase> suite = runAuditSuite();
        int optimalCount = 0;
        int suboptimalCount = 0;
        int incorrectCount = 0;

        for (AuditCase c : suite) {
            c.printDiagnosis();
            if (c.verdict() == Verdict.OPTIMAL) optimalCount++;
            else if (c.verdict() == Verdict.SUBOPTIMAL) suboptimalCount++;
            else if (c.verdict() == Verdict.INCORRECT) incorrectCount++;
        }

        System.out.println("======================================== 綜合審查統計 ========================================");
        System.out.printf("審查情境總數: %d | 最佳選型: %d | 待優化/次佳: %d | 嚴重錯誤: %d%n",
                suite.size(), optimalCount, suboptimalCount, incorrectCount);
        System.out.println("【架構決策核心原則】：");
        System.out.println("1. 永遠不要為了「以防萬一要排序」而放棄 HashMap 的 O(1) 查找優勢。");
        System.out.println("2. 串流 Top-K 絕不進行全量排序，優先使用固定容量 Heap。");
        System.out.println("3. 複雜系統中通常需要「多重索引共存」(如 HashMap + PriorityQueue + Graph)，並維持狀態同步。");
        System.out.println("============================================================================================\n");
    }
}
