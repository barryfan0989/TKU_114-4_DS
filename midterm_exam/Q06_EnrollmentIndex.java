import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Q06_EnrollmentIndex {
    private final Map<String, Set<String>> courseToStudents = new HashMap<>();

    public boolean enroll(String courseCode, String studentId) {
        if (courseCode == null || studentId == null || courseCode.trim().isEmpty() || studentId.trim().isEmpty()) {
            return false;
        }
        Set<String> students = courseToStudents.get(courseCode);
        if (students == null) {
            students = new TreeSet<>();
            courseToStudents.put(courseCode, students);
        }
        return students.add(studentId);
    }

    public boolean drop(String courseCode, String studentId) {
        if (courseCode == null || studentId == null || courseCode.trim().isEmpty() || studentId.trim().isEmpty()) {
            return false;
        }
        Set<String> students = courseToStudents.get(courseCode);
        if (students == null) {
            return false;
        }
        boolean removed = students.remove(studentId);
        if (removed) {
            if (students.isEmpty()) {
                courseToStudents.remove(courseCode);
            }
            return true;
        }
        return false;
    }

    public int courseSize(String courseCode) {
        if (courseCode == null) {
            return 0;
        }
        Set<String> students = courseToStudents.get(courseCode);
        return students == null ? 0 : students.size();
    }

    public List<String> studentsOf(String courseCode) {
        if (courseCode == null) {
            return new ArrayList<>();
        }
        Set<String> students = courseToStudents.get(courseCode);
        if (students == null) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>(students);
        Collections.sort(list);
        return Collections.unmodifiableList(list);
    }

    public List<String> coursesOf(String studentId) {
        List<String> courses = new ArrayList<>();
        if (studentId == null) {
            return courses;
        }
        for (Map.Entry<String, Set<String>> entry : courseToStudents.entrySet()) {
            if (entry.getValue().contains(studentId)) {
                courses.add(entry.getKey());
            }
        }
        Collections.sort(courses);
        return Collections.unmodifiableList(courses);
    }

    public Map<String, Integer> summary() {
        Map<String, Integer> sortedMap = new TreeMap<>();
        for (Map.Entry<String, Set<String>> entry : courseToStudents.entrySet()) {
            sortedMap.put(entry.getKey(), entry.getValue().size());
        }
        return Collections.unmodifiableMap(sortedMap);
    }
}
