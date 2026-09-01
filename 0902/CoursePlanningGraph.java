import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CoursePlanningGraph {
    // outgoing: course -> subsequent courses that require this course (出邊)
    private final Map<String, Set<String>> outgoing = new LinkedHashMap<>();
    // incoming: course -> prerequisites required before taking this course (入邊)
    private final Map<String, Set<String>> incoming = new LinkedHashMap<>();

    public boolean addCourse(String course) {
        if (course == null || course.isBlank()) return false;
        String name = course.trim();
        outgoing.putIfAbsent(name, new LinkedHashSet<>());
        incoming.putIfAbsent(name, new LinkedHashSet<>());
        return true;
    }

    public boolean addPrerequisite(String prerequisite, String targetCourse) {
        if (prerequisite == null || targetCourse == null) return false;
        String prereq = prerequisite.trim();
        String target = targetCourse.trim();

        addCourse(prereq);
        addCourse(target);

        boolean addedOut = outgoing.get(prereq).add(target);
        boolean addedIn = incoming.get(target).add(prereq);
        return addedOut || addedIn;
    }

    /**
     * 1. 使用 DFS 檢查 prerequisite 是否為 targetCourse 的先修課程 (直接或間接)
     */
    public boolean isPrerequisiteOf(String prerequisite, String targetCourse) {
        if (prerequisite == null || targetCourse == null) return false;
        String p = prerequisite.trim();
        String t = targetCourse.trim();

        if (!outgoing.containsKey(p) || !outgoing.containsKey(t)) return false;
        if (p.equals(t)) return false;

        Set<String> visited = new HashSet<>();
        return dfsSearch(p, t, visited);
    }

    private boolean dfsSearch(String current, String target, Set<String> visited) {
        if (current.equals(target)) return true;
        if (!visited.add(current)) return false;

        for (String next : outgoing.getOrDefault(current, Set.of())) {
            if (dfsSearch(next, target, visited)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 2. 若學生某門課不及格 (failedCourse)，使用 DFS 找出所有受到直接或間接阻擋無法修讀的後續課程清單。
     */
    public Set<String> getAllAffectedCourses(String failedCourse) {
        if (failedCourse == null) return Collections.emptySet();
        String c = failedCourse.trim();
        if (!outgoing.containsKey(c)) return Collections.emptySet();

        Set<String> affected = new TreeSet<>();
        dfsCollect(c, affected, new HashSet<>());
        affected.remove(c); // 不包含自身
        return affected;
    }

    private void dfsCollect(String current, Set<String> result, Set<String> visited) {
        if (!visited.add(current)) return;
        result.add(current);

        for (String next : outgoing.getOrDefault(current, Set.of())) {
            dfsCollect(next, result, visited);
        }
    }

    /**
     * 3. 找出要修讀某一門課前，所有必須完成的前置先修課程鏈 (Upstream Prerequisites)。
     */
    public Set<String> getAllRequiredPrerequisites(String targetCourse) {
        if (targetCourse == null) return Collections.emptySet();
        String c = targetCourse.trim();
        if (!incoming.containsKey(c)) return Collections.emptySet();

        Set<String> allPrereqs = new TreeSet<>();
        dfsCollectUpstream(c, allPrereqs, new HashSet<>());
        allPrereqs.remove(c);
        return allPrereqs;
    }

    private void dfsCollectUpstream(String current, Set<String> result, Set<String> visited) {
        if (!visited.add(current)) return;
        result.add(current);

        for (String prev : incoming.getOrDefault(current, Set.of())) {
            dfsCollectUpstream(prev, result, visited);
        }
    }

    /**
     * 4. 偵測先修關係中是否存在循環依賴 (Cycle Detection)。
     */
    public boolean hasCircularDependency() {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String node : outgoing.keySet()) {
            if (checkCycleDfs(node, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCycleDfs(String node, Set<String> visited, Set<String> recStack) {
        if (recStack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        recStack.add(node);

        for (String next : outgoing.getOrDefault(node, Set.of())) {
            if (checkCycleDfs(next, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node);
        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== 期末綜合練習三：課程相依與衝擊評估規劃圖 (CoursePlanningGraph) ===\n");
        CoursePlanningGraph planner = new CoursePlanningGraph();

        // 建立先修關係：
        // 程式設計(一) -> 程式設計(二) -> 資料結構 -> 演算法 -> 機器學習
        // 微積分 -> 線性代數 -> 機器學習
        // 資料結構 -> 軟體工程
        // 資料結構 -> 作業系統 -> 分散式系統
        planner.addPrerequisite("程式設計(一)", "程式設計(二)");
        planner.addPrerequisite("程式設計(二)", "資料結構");
        planner.addPrerequisite("資料結構", "演算法");
        planner.addPrerequisite("資料結構", "軟體工程");
        planner.addPrerequisite("資料結構", "作業系統");
        planner.addPrerequisite("作業系統", "分散式系統");
        planner.addPrerequisite("微積分", "線性代數");
        planner.addPrerequisite("線性代數", "機器學習");
        planner.addPrerequisite("演算法", "機器學習");

        // 測試 1: 循環依賴檢測
        System.out.println("1. 課表是否有循環死鎖先修相依: " + planner.hasCircularDependency() + " (預期: false)");

        // 測試 2: 先修依賴判定
        System.out.println("\n2. 先修關係驗證:");
        System.out.println("【程式設計(一)】是否為【演算法】先修: "
                + planner.isPrerequisiteOf("程式設計(一)", "演算法"));
        System.out.println("【微積分】是否為【機器學習】先修: "
                + planner.isPrerequisiteOf("微積分", "機器學習"));
        System.out.println("【軟體工程】是否為【作業系統】先修: "
                + planner.isPrerequisiteOf("軟體工程", "作業系統"));

        // 測試 3: 當不幸當掉某門課時，衝擊分析 (Impact Analysis)
        System.out.println("\n3. 當科衝擊影響分析 (Affected Downstream Courses):");
        System.out.println("若【程式設計(一)】被當，將直接/間接導致無法選修: "
                + planner.getAllAffectedCourses("程式設計(一)"));
        System.out.println("若【資料結構】被當，將直接/間接導致無法選修: "
                + planner.getAllAffectedCourses("資料結構"));
        System.out.println("若【線性代數】被當，將直接/間接導致無法選修: "
                + planner.getAllAffectedCourses("線性代數"));

        // 測試 4: 修讀頂石課程前，全部先修條件展開 (Upstream Chain)
        System.out.println("\n4. 欲修讀【機器學習】，必須先行完成的所有前置課程鏈:");
        System.out.println("完整先修課程集合: " + planner.getAllRequiredPrerequisites("機器學習"));
        System.out.println("欲修讀【分散式系統】先修課程集合: "
                + planner.getAllRequiredPrerequisites("分散式系統"));
    }
}
