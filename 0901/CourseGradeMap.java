import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CourseGradeMap {
    private final Map<String, List<Integer>> courseGrades = new HashMap<>();

    public void addGrade(String courseId, int score) {
        if (courseId == null || courseId.isBlank()) {
            throw new IllegalArgumentException("Course ID cannot be null or blank");
        }
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        String normalizedId = courseId.trim().toUpperCase();
        courseGrades.computeIfAbsent(normalizedId, k -> new ArrayList<>()).add(score);
    }

    public double getAverage(String courseId) {
        List<Integer> grades = getGrades(courseId);
        if (grades.isEmpty()) {
            return 0.0;
        }
        int sum = 0;
        for (int score : grades) {
            sum += score;
        }
        return (double) sum / grades.size();
    }

    public int getHighest(String courseId) {
        List<Integer> grades = getGrades(courseId);
        if (grades.isEmpty()) {
            return -1;
        }
        return Collections.max(grades);
    }

    public List<Integer> getGrades(String courseId) {
        if (courseId == null) return List.of();
        String normalizedId = courseId.trim().toUpperCase();
        List<Integer> list = courseGrades.get(normalizedId);
        return list == null ? List.of() : List.copyOf(list);
    }

    public void generateReport() {
        System.out.println("==================== 課程成績統計報告 (依課號排序) ====================");
        System.out.printf("%-12s | %-8s | %-10s | %-8s | %s%n",
                "課號", "修課人數", "平均成績", "最高分", "成績清單");
        System.out.println("------------------------------------------------------------------------");

        // 依課號字典序排序
        Map<String, List<Integer>> sortedCourses = new TreeMap<>(courseGrades);

        for (Map.Entry<String, List<Integer>> entry : sortedCourses.entrySet()) {
            String courseId = entry.getKey();
            List<Integer> grades = entry.getValue();
            int count = grades.size();
            double avg = getAverage(courseId);
            int max = getHighest(courseId);

            System.out.printf("%-12s | %-8d | %-10.2f | %-8d | %s%n",
                    courseId, count, avg, max, grades);
        }
        System.out.println("========================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題二：課程成績統計 (CourseGradeMap) ===\n");
        CourseGradeMap gradeMap = new CourseGradeMap();

        // 加入各課程學生成績
        gradeMap.addGrade("CS101", 85);
        gradeMap.addGrade("CS101", 92);
        gradeMap.addGrade("CS101", 78);
        gradeMap.addGrade("CS101", 95);

        gradeMap.addGrade("MATH201", 90);
        gradeMap.addGrade("MATH201", 88);
        gradeMap.addGrade("MATH201", 94);

        gradeMap.addGrade("ENG102", 70);
        gradeMap.addGrade("ENG102", 65);
        gradeMap.addGrade("ENG102", 82);
        gradeMap.addGrade("ENG102", 90);
        gradeMap.addGrade("ENG102", 75);

        gradeMap.addGrade("DS202", 98);
        gradeMap.addGrade("DS202", 100);
        gradeMap.addGrade("DS202", 91);
        gradeMap.addGrade("DS202", 89);

        // 產出排序統計報告
        gradeMap.generateReport();

        // 查詢單一課程統計
        System.out.println("--- 單科即時查詢 ---");
        System.out.printf("DS202 -> 平均分: %.2f, 最高分: %d%n",
                gradeMap.getAverage("DS202"), gradeMap.getHighest("DS202"));
        System.out.printf("未知課程 CS999 -> 平均分: %.2f, 最高分: %d%n",
                gradeMap.getAverage("CS999"), gradeMap.getHighest("CS999"));
    }
}
