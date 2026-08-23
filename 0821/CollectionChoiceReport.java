import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CollectionChoiceReport {
    public static void main(String[] args) {
        System.out.println("=== 課後作業四：集合選擇報告與實作 ===\n");

        // ----------------------------------------------------
        // 需求 1：保留搜尋紀錄且允許重複
        // ----------------------------------------------------
        System.out.println("需求 1：保留搜尋紀錄且允許重複");
        System.out.println("   [選擇介面]：java.util.List");
        System.out.println("   [選擇實作]：java.util.ArrayList");
        System.out.println("   [原因分析]：搜尋紀錄需要維護使用者查詢的時間順序，且使用者有可能重複搜尋同一個關鍵字，List 最符合此特性。ArrayList 在尾端新增 (add) 的均攤時間複雜度為 O(1)，適合頻繁寫入搜尋紀錄。");
        
        List<String> searchHistory = new ArrayList<>();
        searchHistory.add("Java Tutorial");
        searchHistory.add("Data Structures");
        searchHistory.add("Java Tutorial"); // 重複搜尋
        searchHistory.add("Stack vs Queue");
        System.out.println("   [操作結果]：搜尋歷史紀錄 = " + searchHistory);
        System.out.println();

        // ----------------------------------------------------
        // 需求 2：保存不重複會員編號
        // ----------------------------------------------------
        System.out.println("需求 2：保存不重複會員編號");
        System.out.println("   [選擇介面]：java.util.Set");
        System.out.println("   [選擇實作]：java.util.HashSet");
        System.out.println("   [原因分析]：會員編號在系統中必須是唯一的，不允許重複。HashSet 利用 Hash Table 實作，新增與查詢是否存在 (contains) 的時間複雜度均為 O(1)，能高效保證去重與校驗身份。");
        
        Set<String> memberIds = new HashSet<>();
        memberIds.add("M1001");
        memberIds.add("M1002");
        memberIds.add("M1001"); // 重複加入
        memberIds.add("M1003");
        System.out.println("   [操作結果]：註冊會員編號集合 = " + memberIds);
        System.out.println();

        // ----------------------------------------------------
        // 需求 3：以學號查詢成績
        // ----------------------------------------------------
        System.out.println("需求 3：以學號查詢成績");
        System.out.println("   [選擇介面]：java.util.Map");
        System.out.println("   [選擇實作]：java.util.HashMap");
        System.out.println("   [原因分析]：這是一個典型的「鍵-值」(Key-Value) 對照查詢需求（學號對應成績）。HashMap 能提供平均 O(1) 的超高速 Key 定位與 Value 檢索。");
        
        Map<String, Integer> studentScores = new HashMap<>();
        studentScores.put("S001", 95);
        studentScores.put("S002", 82);
        studentScores.put("S003", 90);
        System.out.println("   [操作結果]：查詢 S002 成績 = " + studentScores.get("S002") + " | 查詢不存在的 S099 = " + studentScores.get("S099"));
        System.out.println();

        // ----------------------------------------------------
        // 需求 4：依到達順序處理列印工作
        // ----------------------------------------------------
        System.out.println("需求 4：依到達順序處理列印工作");
        System.out.println("   [選擇介面]：java.util.Queue (或 Deque)");
        System.out.println("   [選擇實作]：java.util.ArrayDeque");
        System.out.println("   [原因分析]：列印工作必須遵循「先到先處理」的先進先出 (FIFO) 規則。ArrayDeque 作為雙端佇列，用來實作單向 Queue 時具有比 LinkedList 更優異的記憶體效率與常數成本。");
        
        Queue<String> printJobs = new ArrayDeque<>();
        printJobs.offer("Homework_v1.pdf");
        printJobs.offer("Resume.docx");
        printJobs.offer("Invoice.jpg");
        System.out.println("   [操作結果]：初始工作隊列 = " + printJobs);
        System.out.println("   處理工作：" + printJobs.poll());
        System.out.println("   處理後工作隊列 = " + printJobs);
        System.out.println();

        // ----------------------------------------------------
        // 需求 5：復原最近操作
        // ----------------------------------------------------
        System.out.println("需求 5：復原最近操作 (Undo)");
        System.out.println("   [選擇介面]：java.util.Deque (作為 Stack 使用)");
        System.out.println("   [選擇實作]：java.util.ArrayDeque");
        System.out.println("   [原因分析]：復原操作是典型的「後進先出」(LIFO) 堆疊運作模式。Java 官方不推薦使用過時的 `java.util.Stack` (因為它是同步的且繼承自 Vector)，而推薦使用 `Deque` 的子類別 `ArrayDeque` 作為 Stack。");
        
        Deque<String> undoStack = new ArrayDeque<>();
        undoStack.push("Draw Circle");
        undoStack.push("Fill Blue Color");
        undoStack.push("Resize Shape");
        System.out.println("   [操作結果]：目前操作棧 = " + undoStack);
        System.out.println("   復原 (Pop) 最近操作：" + undoStack.pop());
        System.out.println("   復原後操作棧 = " + undoStack);
        System.out.println();
    }
}
