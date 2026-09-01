import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class InterestSetComparison {

    /**
     * 計算兩集合之聯集 (A U B)，不修改原集合。
     */
    public static <T> Set<T> union(Set<T> first, Set<T> second) {
        Set<T> result = new LinkedHashSet<>();
        if (first != null) result.addAll(first);
        if (second != null) result.addAll(second);
        return result;
    }

    /**
     * 計算兩集合之交集 (A ∩ B)，不修改原集合。
     */
    public static <T> Set<T> intersection(Set<T> first, Set<T> second) {
        if (first == null || second == null) return Collections.emptySet();
        Set<T> result = new LinkedHashSet<>(first);
        result.retainAll(second);
        return result;
    }

    /**
     * 計算第一個集合獨有元素 (A \ B)，不修改原集合。
     */
    public static <T> Set<T> firstOnly(Set<T> first, Set<T> second) {
        if (first == null) return Collections.emptySet();
        Set<T> result = new LinkedHashSet<>(first);
        if (second != null) {
            result.removeAll(second);
        }
        return result;
    }

    /**
     * 計算第二個集合獨有元素 (B \ A)，不修改原集合。
     */
    public static <T> Set<T> secondOnly(Set<T> first, Set<T> second) {
        return firstOnly(second, first);
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題三：共同興趣集合運算 (InterestSetComparison) ===\n");

        // 測試案例: 兩位使用者的興趣標籤
        Set<String> userA = Set.of("閱讀", "程式設計", "攝影", "登山", "羽球");
        Set<String> userB = Set.of("攝影", "登山", "電玩", "吉他", "電影");

        System.out.println("使用者 A 的興趣: " + userA);
        System.out.println("使用者 B 的興趣: " + userB);

        // 集合運算
        Set<String> unionSet = union(userA, userB);
        Set<String> commonSet = intersection(userA, userB);
        Set<String> aOnly = firstOnly(userA, userB);
        Set<String> bOnly = secondOnly(userA, userB);

        System.out.println("\n---------------- 運算結果 ----------------");
        System.out.println("1. 聯集 (全部興趣種類): " + unionSet + " (共 " + unionSet.size() + " 種)");
        System.out.println("2. 交集 (共同興趣): " + commonSet + " (共 " + commonSet.size() + " 種)");
        System.out.println("3. A 獨有興趣 (A \\ B): " + aOnly);
        System.out.println("4. B 獨有興趣 (B \\ A): " + bOnly);
        System.out.println("----------------------------------------");

        // 驗證原集合是否未被修改 (Immutability Check)
        System.out.println("\n驗證原始集合未被修改:");
        System.out.println("使用者 A 原始大小是否仍為 5: " + (userA.size() == 5 ? "PASS" : "FAIL"));
        System.out.println("使用者 B 原始大小是否仍為 5: " + (userB.size() == 5 ? "PASS" : "FAIL"));

        // 邊界測試 (空集合與 null)
        System.out.println("\n--- 邊界條件測試 (空集合與 null) ---");
        System.out.println("union(userA, null): " + union(userA, null));
        System.out.println("intersection(userA, Set.of()): " + intersection(userA, Set.of()));
        System.out.println("firstOnly(null, userB): " + firstOnly(null, userB));
    }
}
