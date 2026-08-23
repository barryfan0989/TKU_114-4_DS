import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EnrollmentCleanup {
    public static void main(String[] args) {
        System.out.println("=== 課後作業三：安全清理名單 ===");

        // 初始化包含重複、空白與 null 的名單
        List<String> names = new ArrayList<>(java.util.Arrays.asList("Amy", null, "Ben", "   ", "Cara", "Amy", "", "Ben", "David"));
        List<String> rawNames = new ArrayList<>(names); // 用於保留原始名單以便印出

        System.out.println("清理前名單 (原始資料)：");
        System.out.println(names);

        // 1. 使用 Iterator 進行安全過濾與清理 (移除 null、空值、空白)
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name == null || name.trim().isEmpty()) {
                iterator.remove();
            }
        }

        System.out.println("\n清理後名單 (已移除空值與 null)：");
        System.out.println(names);

        // 2. 使用 Set 找出重複的名單
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        for (String name : names) {
            if (!seen.add(name)) {
                duplicates.add(name);
            }
        }

        System.out.println("\n重複的姓名報告 (至少出現過兩次)：");
        if (duplicates.isEmpty()) {
            System.out.println("沒有發現重複的姓名。");
        } else {
            for (String duplicateName : duplicates) {
                // 計算出現次數
                int count = 0;
                for (String name : names) {
                    if (name.equals(duplicateName)) {
                        count++;
                    }
                }
                System.out.println("   姓名 [" + duplicateName + "] 重複，在清理後名單中出現次數: " + count);
            }
        }
    }
}
