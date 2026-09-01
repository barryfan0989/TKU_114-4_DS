import java.util.ArrayList;
import java.util.List;

public class DataStructureDecisionReport {

    public record Decision(
            int id,
            String scenario,
            String coreOperation,
            String recommendedDS,
            String timeComplexity,
            String spaceComplexity,
            String justification
    ) {
        public void printRow() {
            System.out.printf("%2d. 情境: %s%n", id, scenario);
            System.out.printf("    - 核心操作: %s%n", coreOperation);
            System.out.printf("    - 推薦結構: 【%s】%n", recommendedDS);
            System.out.printf("    - 複雜度: 時間 %s | 空間 %s%n", timeComplexity, spaceComplexity);
            System.out.printf("    - 選擇理由: %s%n%n", justification);
        }
    }

    public static List<Decision> getDecisions() {
        List<Decision> list = new ArrayList<>();

        list.add(new Decision(
                1, "高頻率隨機索引存取 (Random Index Access)",
                "依整數 index O(1) 讀取元素",
                "ArrayList", "讀取 O(1) / 尾端加入 O(1)", "O(n)",
                "內部使用連續記憶體陣列，具備最佳快取局部性與常數時間索引存取。"
        ));

        list.add(new Decision(
                2, "異步任務先進先出佇列 (FIFO Task Scheduling)",
                "尾端 enqueue、前端 dequeue",
                "ArrayDeque (作為 Queue)", "入列/出列均為 O(1)", "O(n)",
                "雙端陣列無節點物件包裝開銷，效能優於 LinkedList。"
        ));

        list.add(new Decision(
                3, "文字編輯器復原歷史記錄 (Undo/Redo Stack)",
                "頂端 push、pop (LIFO)",
                "ArrayDeque (作為 Stack)", "push / pop 均為 O(1)", "O(n)",
                "提供高效 LIFO 操作，比傳統 synchronized Vector/Stack 更輕量高效。"
        ));

        list.add(new Decision(
                4, "實時串流排行榜 Top-K 篩選 (Top-K Ranking)",
                "維持大小為 K 的極值候選堆積",
                "PriorityQueue (Min/Max Heap)", "插入/替換 O(log K) / 查頂 O(1)", "O(K)",
                "無需對全部 N 筆資料做 O(N log N) 排序，大幅節省時間與記憶體。"
        ));

        list.add(new Decision(
                5, "使用者帳號/商品快速 Key 查找 (Key-Value Caching)",
                "依 Unique Key 直接尋找 Value",
                "HashMap", "平均查詢/更新/刪除 O(1)", "O(n)",
                "以 Hash Function 與 Bucket 鏈結索引，平均提供極佳常數時間查找。"
        ));

        list.add(new Decision(
                6, "年齡/薪資範圍區間查詢 (Sorted Range Queries)",
                "查找介於 [min, max] 間之所有元素並依序遍歷",
                "TreeMap / Balanced BST (紅黑樹)", "查詢/範圍搜尋 O(log n + k)", "O(n)",
                "內部維持 Inorder 排序，支援 subMap / ceiling / floor 等範圍搜尋。"
        ));

        list.add(new Decision(
                7, "社群網路好友關係與最短路徑 (Social Pathfinding)",
                "保存多對多關係並執行 BFS/DFS 搜尋",
                "Graph Adjacency List (Map<K, Set<V>>)", "走訪 O(V + E) / 加邊 O(1)", "O(V + E)",
                "適合稀疏關係圖 (Sparse Graph)，空間節省且列出鄰居效率高。"
        ));

        list.add(new Decision(
                8, "黑名單/不重複標籤快速判定 (Set Membership Test)",
                "快速檢查元素是否存在 (contains)",
                "HashSet", "查詢/加入 O(1)", "O(n)",
                "自動去重並以 Hash Code 提供高速 Membership 判定。"
        ));

        list.add(new Decision(
                9, "急診多欄位檢傷動態叫號 (Multi-key Emergency Triage)",
                "依多維度權重 (危急度 > 到院時間) 動態取出最優先",
                "PriorityQueue 搭配自訂 Comparator", "叫號 O(log n) / 查看下一位 O(1)", "O(n)",
                "支援任意客製化 Tie-breaker 比較規則，動態維持最小/最大堆積順序。"
        ));

        list.add(new Decision(
                10, "檔案系統階層樹遍歷 (File Directory Tree Traversal)",
                "由根目錄向下展開子目錄與檔案",
                "Tree (N-ary Tree) 搭配 DFS/BFS", "遍歷全樹 O(n)", "O(n)",
                "完美對應一對多父子階層結構，遞迴或佇列遍歷皆自然直觀。"
        ));

        list.add(new Decision(
                11, "搜尋引擎輸入框前綴自動補全 (Prefix Autocomplete)",
                "依使用者輸入的前綴 (Prefix) 查找所有匹配字詞",
                "Trie (字典樹 / 前綴樹)", "搜尋前綴 O(L)，L 為字詞長度", "O(總字元數 * 字母表)",
                "字串公用前綴共享節點，搜尋時間僅與字詞長度相關，與資料庫總量無關。"
        ));

        list.add(new Decision(
                12, "即時音訊/感測器固定大小環狀緩衝 (Fixed Ring Buffer)",
                "固定記憶體容量，新資料自動覆蓋最舊資料",
                "Circular Array (環狀陣列)", "寫入/讀取 O(1)，零動態配置", "O(Capacity)",
                "避免頻繁記憶體配置與 GC 負擔，指標取模 (index % cap) 速度極快。"
        ));

        return list;
    }

    public static void main(String[] args) {
        System.out.println("============================== 課堂實作題六：資料結構決策與 Big-O 評估報告 ==============================");
        System.out.println("本報告針對現代軟體工程 12 組經典需求場景，依據操作特性、時間與空間複雜度進行結構選型：\n");

        List<Decision> decisions = getDecisions();
        for (Decision d : decisions) {
            d.printRow();
        }

        System.out.println("======================================== 評估總結 ========================================");
        System.out.println("【選型準則核心心法】：");
        System.out.println("1. 依「最頻繁執行的主要操作」選結構，而非依名稱挑選。");
        System.out.println("2. 隨機讀取 -> 陣列 (ArrayList)；兩端進出 -> 雙端佇列 (ArrayDeque)。");
        System.out.println("3. 依 Key 直查 -> 雜湊表 (HashMap)；範圍排序 -> 平衡樹 (TreeMap)。");
        System.out.println("4. 取極值 -> 堆積 (Heap/PriorityQueue)；多對多關係 -> 關係圖 (Graph)。");
        System.out.println("=========================================================================================\n");
    }
}
