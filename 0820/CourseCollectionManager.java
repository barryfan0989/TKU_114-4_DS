import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CourseEnrollment {
    private final String studentId;
    private final String name;
    private int score;
    private final Set<String> tags = new HashSet<>();

    CourseEnrollment(String studentId, String name, int score) {
        this.studentId = studentId != null ? studentId.trim() : "";
        this.name = name != null ? name.trim() : "";
        setScore(score);
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        // 分數限制在 0 ~ 100 之間
        this.score = Math.max(0, Math.min(100, score));
    }

    public void addTag(String tag) {
        if (tag != null && !tag.trim().isEmpty()) {
            tags.add(tag.trim().toLowerCase());
        }
    }

    public boolean hasTag(String tag) {
        if (tag == null) return false;
        return tags.contains(tag.trim().toLowerCase());
    }

    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, Score: %d, Tags: %s]", studentId, name, score, tags);
    }
}

public class CourseCollectionManager {
    private final List<CourseEnrollment> order = new ArrayList<>();
    private final Set<String> registeredIds = new HashSet<>();
    private final Map<String, CourseEnrollment> byId = new HashMap<>();

    public boolean enroll(CourseEnrollment enrollment) {
        if (enrollment == null) {
            return false;
        }
        String id = enrollment.getStudentId();
        if (id.isEmpty() || !registeredIds.add(id)) {
            // 學號為空或是學號重複，拒絕報名
            return false;
        }
        order.add(enrollment);
        byId.put(id, enrollment);
        return true;
    }

    public boolean updateScore(String studentId, int score) {
        CourseEnrollment enrollment = byId.get(studentId);
        if (enrollment == null) {
            System.out.println("[Warning] updateScore: Student ID " + studentId + " not found.");
            return false;
        }
        enrollment.setScore(score);
        return true;
    }

    public List<CourseEnrollment> findByTag(String tag) {
        List<CourseEnrollment> result = new ArrayList<>();
        for (CourseEnrollment enrollment : order) {
            if (enrollment.hasTag(tag)) {
                result.add(enrollment);
            }
        }
        return result;
    }

    public Map<String, Integer> scoreDistribution() {
        Map<String, Integer> dist = new HashMap<>();
        dist.put("A", 0);
        dist.put("B", 0);
        dist.put("C", 0);
        dist.put("D", 0);
        dist.put("F", 0);

        for (CourseEnrollment enrollment : order) {
            int score = enrollment.getScore();
            if (score >= 90) {
                dist.put("A", dist.get("A") + 1);
            } else if (score >= 80) {
                dist.put("B", dist.get("B") + 1);
            } else if (score >= 70) {
                dist.put("C", dist.get("C") + 1);
            } else if (score >= 60) {
                dist.put("D", dist.get("D") + 1);
            } else {
                dist.put("F", dist.get("F") + 1);
            }
        }
        return dist;
    }

    public List<CourseEnrollment> top(int count) {
        if (count <= 0) {
            return new ArrayList<>();
        }
        List<CourseEnrollment> copy = new ArrayList<>(order);
        // 分數降冪，同分以學號升冪
        copy.sort(Comparator.comparingInt(CourseEnrollment::getScore)
                .reversed()
                .thenComparing(CourseEnrollment::getStudentId));
        
        if (count >= copy.size()) {
            return copy;
        }
        return new ArrayList<>(copy.subList(0, count));
    }

    public void removeBelow(int minimum) {
        // 1. 從有序的 List 移除非法資料
        order.removeIf(enrollment -> enrollment.getScore() < minimum);

        // 2. 清理並重新同步 Set 與 Map 以確保資料一致性
        registeredIds.clear();
        byId.clear();
        for (CourseEnrollment enrollment : order) {
            registeredIds.add(enrollment.getStudentId());
            byId.put(enrollment.getStudentId(), enrollment);
        }
    }

    public List<CourseEnrollment> getAll() {
        return new ArrayList<>(order);
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業五：課程管理集合系統 ===");

        CourseCollectionManager manager = new CourseCollectionManager();

        // 建立六筆報名資料
        CourseEnrollment e1 = new CourseEnrollment("S101", "Amy", 88);
        e1.addTag("Java");
        e1.addTag("OOP");

        CourseEnrollment e2 = new CourseEnrollment("S102", "Ben", 55);
        e2.addTag("Basic");

        // 測試大小寫 java 標籤
        CourseEnrollment e3 = new CourseEnrollment("S103", "Cara", 92);
        e3.addTag("Data Structures");
        e3.addTag("java");

        CourseEnrollment e4 = new CourseEnrollment("S104", "David", 75);
        e4.addTag("Algorithm");

        // 測試同分狀況 (92分) 與 tag
        CourseEnrollment e5 = new CourseEnrollment("S105", "Eva", 92);
        e5.addTag("Basic");
        e5.addTag("JAVA");

        // 測試低分與空白 tag
        CourseEnrollment e6 = new CourseEnrollment("S106", "Frank", 45);
        e6.addTag("   "); // 空白 tag 測試，應被忽略

        // 註冊學生
        System.out.println("註冊 S101：" + manager.enroll(e1));
        System.out.println("註冊 S102：" + manager.enroll(e2));
        System.out.println("註冊 S103：" + manager.enroll(e3));
        System.out.println("註冊 S104：" + manager.enroll(e4));
        System.out.println("註冊 S105：" + manager.enroll(e5));
        System.out.println("註冊 S106：" + manager.enroll(e6));

        // 測試重複學號註冊
        CourseEnrollment duplicateStudent = new CourseEnrollment("S101", "Amy Duplicate", 95);
        System.out.println("註冊重複學號 S101 (預期為 false)：" + manager.enroll(duplicateStudent));

        System.out.println("\n初始所有學生清單 (依報名順序)：");
        manager.getAll().forEach(System.out::println);

        // 1. 測試更新分數
        System.out.println("\n1. 測試更新 S102 (Ben) 的分數至 65：");
        boolean updateSuccess = manager.updateScore("S102", 65);
        System.out.println("   更新結果：" + updateSuccess);
        System.out.println("   更新後 Ben 資料：" + manager.byId.get("S102"));

        // 2. 測試 findByTag (查詢 "java"，不分大小寫，應查出 Amy, Cara, Eva)
        System.out.println("\n2. 測試依標籤 'java' 搜尋 (應包含 S101, S103, S105)：");
        List<CourseEnrollment> javaStudents = manager.findByTag("java");
        javaStudents.forEach(e -> System.out.println("   符合者：" + e));

        // 3. 測試 scoreDistribution
        System.out.println("\n3. 統計目前成績分布 (A: >=90, B: 80-89, C: 70-79, D: 60-69, F: <60)：");
        System.out.println("   " + manager.scoreDistribution());

        // 4. 測試 top(count)
        System.out.println("\n4. 測試取得前 3 名排名 (S103 與 S105 同分 92，S103學號小應排在前面)：");
        manager.top(3).forEach(e -> System.out.println("   " + e));

        System.out.println("   測試取得前 10 名排名 (超額 count 測試，應列出目前所有 6 筆)：");
        System.out.println("   排名列表大小：" + manager.top(10).size());

        // 5. 測試 removeBelow(60)
        System.out.println("\n5. 測試移除低於 60 分的學生 (S106 只有 45 分，應被移除；Ben 之前被改為 65 分，應保留)：");
        manager.removeBelow(60);
        
        System.out.println("   清理低分後的所有學生清單：");
        manager.getAll().forEach(System.out::println);

        System.out.println("   清理低分後的成績分布 (F 應為 0)：");
        System.out.println("   " + manager.scoreDistribution());

        // 驗證 Map 一致性，嘗試搜尋 S106
        System.out.println("   在 Map 內檢索已移除的 S106 (應為 null)：" + manager.byId.get("S106"));
    }
}
