import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CourseDependencyGraph {
    // outgoing: course -> subsequent courses that require this course (出邊)
    private final Map<String, Set<String>> outgoing = new LinkedHashMap<>();
    // incoming: course -> prerequisites required by this course (入邊)
    private final Map<String, Set<String>> incoming = new LinkedHashMap<>();

    public boolean addCourse(String course) {
        if (course == null || course.isBlank()) return false;
        String name = course.trim();
        outgoing.putIfAbsent(name, new LinkedHashSet<>());
        incoming.putIfAbsent(name, new LinkedHashSet<>());
        return true;
    }

    /**
     * 新增先修相依關係：prerequisite -> targetCourse
     * (修讀 targetCourse 前必須先修過 prerequisite)
     */
    public boolean addPrerequisite(String prerequisite, String targetCourse) {
        if (prerequisite == null || targetCourse == null) return false;
        String prereq = prerequisite.trim();
        String target = targetCourse.trim();

        if (prereq.equalsIgnoreCase(target)) return false; // 避免 self-dependency

        addCourse(prereq);
        addCourse(target);

        boolean addedOut = outgoing.get(prereq).add(target);
        boolean addedIn = incoming.get(target).add(prereq);
        return addedOut || addedIn;
    }

    public Set<String> getPrerequisites(String course) {
        if (course == null) return Collections.emptySet();
        Set<String> prereqs = incoming.get(course.trim());
        return prereqs == null ? Collections.emptySet() : Collections.unmodifiableSet(prereqs);
    }

    public Set<String> getSubsequentCourses(String course) {
        if (course == null) return Collections.emptySet();
        Set<String> subsequent = outgoing.get(course.trim());
        return subsequent == null ? Collections.emptySet() : Collections.unmodifiableSet(subsequent);
    }

    public int getInDegree(String course) {
        return getPrerequisites(course).size();
    }

    public int getOutDegree(String course) {
        return getSubsequentCourses(course).size();
    }

    public void printDependencyReport() {
        System.out.println("==================== 課程相依圖 (Course Dependency Report) ====================");
        System.out.printf("%-15s | %-10s | %-10s | %-25s | %s%n",
                "課程名稱", "In-Degree", "Out-Degree", "先修課程 (Prerequisites)", "後續解鎖課程 (Subsequent)");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (String course : outgoing.keySet()) {
            Set<String> prereqs = incoming.getOrDefault(course, Collections.emptySet());
            Set<String> subs = outgoing.getOrDefault(course, Collections.emptySet());

            System.out.printf("%-15s | %-10d | %-10d | %-25s | %s%n",
                    course, prereqs.size(), subs.size(),
                    prereqs.isEmpty() ? "(無先修)" : prereqs,
                    subs.isEmpty() ? "(無後續課程)" : subs);
        }
        System.out.println("=========================================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題六：課程相依 Graph (CourseDependencyGraph) ===\n");
        CourseDependencyGraph graph = new CourseDependencyGraph();

        // 加入資工系/資訊系典型先修課相依關係
        // 計算機概論 -> 資料結構
        graph.addPrerequisite("計算機概論", "資料結構");
        graph.addPrerequisite("計算機概論", "物件導向程式設計");

        // 資料結構 + 離散數學 -> 演算法
        graph.addPrerequisite("資料結構", "演算法");
        graph.addPrerequisite("離散數學", "演算法");

        // 資料結構 -> 軟體工程 & 作業系統
        graph.addPrerequisite("資料結構", "軟體工程");
        graph.addPrerequisite("資料結構", "作業系統");

        // 線性代數 + 演算法 -> 機器學習
        graph.addPrerequisite("線性代數", "機器學習");
        graph.addPrerequisite("演算法", "機器學習");

        // 獨立通識課 (無任何先修與後續)
        graph.addCourse("大學英文");

        // 產出相依報告
        graph.printDependencyReport();

        // 查詢特定課程分析
        System.out.println("--- 特定課程深度查詢 ---");
        System.out.println("【演算法】先修條件 (In-Degree=" + graph.getInDegree("演算法") + "): "
                + graph.getPrerequisites("演算法"));
        System.out.println("【資料結構】解鎖後續課程 (Out-Degree=" + graph.getOutDegree("資料結構") + "): "
                + graph.getSubsequentCourses("資料結構"));
        System.out.println("【計算機概論】In-Degree (基礎入門課): " + graph.getInDegree("計算機概論"));
        System.out.println("【機器學習】Out-Degree (頂石/進階課): " + graph.getOutDegree("機器學習"));
    }
}
