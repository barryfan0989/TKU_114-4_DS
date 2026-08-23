import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordIndexSystem {
    public static void main(String[] args) {
        System.out.println("=== 課後作業二：文字索引系統 ===");

        // 內建句子陣列
        String[] sentences = {
            "Java is a popular programming language.",
            "Java is object-oriented, and it is widely used.",
            "Learn Java, build structures, and solve problems."
        };

        Map<String, Integer> wordCounts = new HashMap<>();
        Set<String> uniqueWords = new HashSet<>();

        // 處理並解析單字
        for (String sentence : sentences) {
            // 以空白字元分割
            String[] rawWords = sentence.split("\\s+");
            for (String rawWord : rawWords) {
                // 清理單字：轉小寫、去除句點與逗號
                String cleanWord = rawWord.toLowerCase().replaceAll("[.,]", "").trim();
                
                if (!cleanWord.isEmpty()) {
                    uniqueWords.add(cleanWord);
                    wordCounts.put(cleanWord, wordCounts.getOrDefault(cleanWord, 0) + 1);
                }
            }
        }

        // 輸出統計結果
        System.out.println("1. 所有不重複單字 (Set, 共 " + uniqueWords.size() + " 個)：");
        System.out.println("   " + uniqueWords);

        System.out.println("\n2. 所有單字出現次數 (Map)：");
        wordCounts.forEach((word, count) -> System.out.println("   " + word + ": " + count));

        System.out.println("\n3. 出現至少兩次 (>= 2) 的單字：");
        boolean foundDuplicateWord = false;
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            if (entry.getValue() >= 2) {
                System.out.println("   單字 [" + entry.getKey() + "] 出現次數: " + entry.getValue());
                foundDuplicateWord = true;
            }
        }
        if (!foundDuplicateWord) {
            System.out.println("   未發現出現至少兩次的單字。");
        }
    }
}
