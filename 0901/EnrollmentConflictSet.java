import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EnrollmentConflictSet {

    /**
     * 自訂選課複合鍵 (學號 + 課號)
     */
    public static class EnrollmentKey {
        private final String studentId;
        private final String courseId;

        public EnrollmentKey(String studentId, String courseId) {
            if (studentId == null || studentId.isBlank()) {
                throw new IllegalArgumentException("Student ID cannot be null or blank");
            }
            if (courseId == null || courseId.isBlank()) {
                throw new IllegalArgumentException("Course ID cannot be null or blank");
            }
            this.studentId = studentId.trim().toUpperCase();
            this.courseId = courseId.trim().toUpperCase();
        }

        public String getStudentId() {
            return studentId;
        }

        public String getCourseId() {
            return courseId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EnrollmentKey that)) return false;
            return studentId.equals(that.studentId) && courseId.equals(that.courseId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studentId, courseId);
        }

        @Override
        public String toString() {
            return String.format("[學號: %s | 課號: %s]", studentId, courseId);
        }
    }

    public static class EnrollmentManager {
        private final Set<EnrollmentKey> uniqueEnrollments = new HashSet<>();
        private final List<EnrollmentKey> duplicateSubmissions = new ArrayList<>();
        private final Map<String, Set<String>> studentCourses = new LinkedHashMap<>();
        private final Map<String, Integer> courseHeadcounts = new HashMap<>();

        public void processSubmission(String studentId, String courseId) {
            EnrollmentKey key = new EnrollmentKey(studentId, courseId);
            if (!uniqueEnrollments.add(key)) {
                // 重複選課
                duplicateSubmissions.add(key);
                return;
            }

            // 首次成功選課
            studentCourses.computeIfAbsent(key.getStudentId(), k -> new TreeSet<>())
                    .add(key.getCourseId());
            courseHeadcounts.merge(key.getCourseId(), 1, Integer::sum);
        }

        public void generateReport() {
            System.out.println("==================== 1. 重複選課衝突紀錄 ====================");
            if (duplicateSubmissions.isEmpty()) {
                System.out.println("無重複選課紀錄。");
            } else {
                System.out.printf("共檢測到 %d 筆重複選課提交 (已自動過濾):%n", duplicateSubmissions.size());
                for (EnrollmentKey dup : duplicateSubmissions) {
                    System.out.println("  ! 重複提交: " + dup);
                }
            }

            System.out.println("\n==================== 2. 每位學生選課清單 ====================");
            System.out.printf("%-12s | %-10s | %s%n", "學號", "已選門數", "課程清單");
            System.out.println("------------------------------------------------------------");
            Map<String, Set<String>> sortedStudents = new TreeMap<>(studentCourses);
            for (Map.Entry<String, Set<String>> entry : sortedStudents.entrySet()) {
                System.out.printf("%-12s | %-10d | %s%n",
                        entry.getKey(), entry.getValue().size(), entry.getValue());
            }

            System.out.println("\n==================== 3. 每門課修課人數統計 ====================");
            System.out.printf("%-15s | %s%n", "課號", "修課總人數");
            System.out.println("---------------------------------");
            Map<String, Integer> sortedCourses = new TreeMap<>(courseHeadcounts);
            for (Map.Entry<String, Integer> entry : sortedCourses.entrySet()) {
                System.out.printf("%-15s | %d 人%n", entry.getKey(), entry.getValue());
            }
            System.out.println("==============================================================\n");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業三：選課重複檢查 (EnrollmentConflictSet) ===\n");
        EnrollmentManager manager = new EnrollmentManager();

        // 模擬學生選課日誌 (包含空白字符、大小寫不同與重複選課)
        String[][] rawSubmissions = {
            {"41041001", "CS101"},
            {"41041001", "MATH201"},
            {"41041001", " cs101 "},    // 重複選 CS101 (含空白與小寫)
            {"41041002", "CS101"},
            {"41041002", "ENG102"},
            {"41041003", "DS202"},
            {"41041003", "CS101"},
            {"41041001", "DS202"},
            {"41041002", "ENG102"},      // 重複選 ENG102
            {"41041004", "MATH201"},
            {"41041004", "DS202"},
            {"41041004", "MATH201"}      // 重複選 MATH201
        };

        for (String[] sub : rawSubmissions) {
            manager.processSubmission(sub[0], sub[1]);
        }

        manager.generateReport();
    }
}
