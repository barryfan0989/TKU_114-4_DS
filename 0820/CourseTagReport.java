import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseTagReport {
    public static void main(String[] args) {
        System.out.println("=== 課堂實作題三：課程標籤統計 ===");

        // 模擬輸入一組可能重複的課程標籤
        String[] inputTags = {"Java", "Data Structures", "Java", "Algorithm", "Algorithm", "java", "Tree", "Tree"};

        List<String> rawTags = new ArrayList<>();
        Set<String> uniqueTags = new HashSet<>();
        Map<String, Integer> tagCounts = new HashMap<>();

        for (String tag : inputTags) {
            // 加入 List (保留原始順序與重複值)
            rawTags.add(tag);
            // 加入 Set (不重複，且在此處我們統一轉小寫處理，以便在去重與統計時具有更好的語意)
            String lowerTag = tag.toLowerCase();
            uniqueTags.add(lowerTag);
            // 加入 Map (統計出現次數)
            tagCounts.put(lowerTag, tagCounts.getOrDefault(lowerTag, 0) + 1);
        }

        // 1. 輸出結果
        System.out.println("1. 原始標籤清單 (List, 保存順序與重複)：");
        System.out.println("   " + rawTags);
        System.out.println("2. 不重複標籤集合 (Set, 不重複，已轉小寫)：");
        System.out.println("   " + uniqueTags);
        System.out.println("3. 標籤出現次數統計 (Map, 鍵值對對應)：");
        System.out.println("   " + tagCounts);

        // 2. 解釋各自用途與差異
        System.out.println("\n[集合特性與用途說明]：");
        System.out.println("- List (如 ArrayList)：");
        System.out.println("  * 特性：有序、可重複、支援 index 隨機存取。");
        System.out.println("  * 用途：適合需要完整保留資料加入順序、歷史歷程或允許重複資料的場景。");
        System.out.println("- Set (如 HashSet)：");
        System.out.println("  * 特性：無序、不重複、查詢「是否存在」的速度極快。");
        System.out.println("  * 用途：適合用來去重、身份排他（如不重複學號、不重複標籤名單）。");
        System.out.println("- Map (如 HashMap)：");
        System.out.println("  * 特性：以 Key-Value 鍵值對儲存，Key 唯一，可快速透過 Key 尋找 Value。");
        System.out.println("  * 用途：適合建立索引、計數器、快取對照表（如學號對應學生、單字對應次數）。");
    }
}
