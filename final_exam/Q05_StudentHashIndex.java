import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Q05_StudentHashIndex {
    private final Map<String, Set<String>> studentToCourses = new HashMap<>();
    private final Map<String, Set<String>> courseToStudents = new HashMap<>();
    private int totalEnrollments = 0;

    private String normalize(String s) {
        if (s == null) {
            return null;
        }
        String trimmed = s.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed.toUpperCase();
    }

    public boolean enroll(String studentId, String courseId) {
        String sid = normalize(studentId);
        String cid = normalize(courseId);
        if (sid == null || cid == null) {
            return false;
        }

        Set<String> courses = studentToCourses.computeIfAbsent(sid, k -> new HashSet<>());
        if (courses.contains(cid)) {
            return false;
        }

        courses.add(cid);
        courseToStudents.computeIfAbsent(cid, k -> new HashSet<>()).add(sid);
        totalEnrollments++;
        return true;
    }

    public boolean drop(String studentId, String courseId) {
        String sid = normalize(studentId);
        String cid = normalize(courseId);
        if (sid == null || cid == null) {
            return false;
        }

        Set<String> courses = studentToCourses.get(sid);
        if (courses == null || !courses.contains(cid)) {
            return false;
        }

        courses.remove(cid);
        if (courses.isEmpty()) {
            studentToCourses.remove(sid);
        }

        Set<String> students = courseToStudents.get(cid);
        if (students != null) {
            students.remove(sid);
            if (students.isEmpty()) {
                courseToStudents.remove(cid);
            }
        }

        totalEnrollments--;
        return true;
    }

    public Set<String> coursesOf(String studentId) {
        String sid = normalize(studentId);
        if (sid == null || !studentToCourses.containsKey(sid)) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet<>(studentToCourses.get(sid)));
    }

    public Set<String> studentsIn(String courseId) {
        String cid = normalize(courseId);
        if (cid == null || !courseToStudents.containsKey(cid)) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet<>(courseToStudents.get(cid)));
    }

    public int enrollmentCount() {
        return totalEnrollments;
    }
}
