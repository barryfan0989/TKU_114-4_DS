import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Enrollment {
    private final String studentId;
    private final String courseCode;

    Enrollment(String studentId, String courseCode) {
        this.studentId = studentId != null ? studentId.trim() : "";
        this.courseCode = courseCode != null ? courseCode.trim() : "";
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        Enrollment other = (Enrollment) o;
        return Objects.equals(studentId, other.studentId) &&
               Objects.equals(courseCode, other.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseCode);
    }

    @Override
    public String toString() {
        return "Enrollment{studentId='" + studentId + "', courseCode='" + courseCode + "'}";
    }
}

public class EnrollmentSetSystem {
    public static void main(String[] args) {
        System.out.println("=== 課後作業四：課程報名身分集合 ===");

        Set<Enrollment> enrollmentSet = new HashSet<>();

        // 1. 同一人可加入不同課程
        System.out.println("1. 學生 S101 報名課程 CS101：");
        boolean res1 = enrollmentSet.add(new Enrollment("S101", "CS101"));
        System.out.println("   結果：" + res1 + " | 目前集合：" + enrollmentSet);

        System.out.println("\n2. 同一人 S101 報名另一門課程 CS102 (應成功)：");
        boolean res2 = enrollmentSet.add(new Enrollment("S101", "CS102"));
        System.out.println("   結果：" + res2 + " | 目前集合：" + enrollmentSet);

        // 2. 不同人可加入同一課程
        System.out.println("\n3. 學生 S102 報名同課程 CS101 (應成功)：");
        boolean res3 = enrollmentSet.add(new Enrollment("S102", "CS101"));
        System.out.println("   結果：" + res3 + " | 目前集合：" + enrollmentSet);

        // 3. 同一人不可重複加入同一課程
        System.out.println("\n4. 學生 S101 再次報名課程 CS101 (應失敗)：");
        boolean res4 = enrollmentSet.add(new Enrollment("S101", "CS101"));
        System.out.println("   結果 (預期為 false)：" + res4 + " | 目前集合大小：" + enrollmentSet.size());

        // 4. 以新建立但身分相同的 object 測試 contains() 與 remove()
        System.out.println("\n5. 測試使用全新建立但資料相同的 Enrollment 物件進行 contains 檢索：");
        Enrollment checkObj = new Enrollment("S101", "CS101");
        boolean hasItem = enrollmentSet.contains(checkObj);
        System.out.println("   集合中是否包含 S101-CS101 項目：" + hasItem); // 應為 true

        System.out.println("\n6. 測試使用全新建立但資料相同的 Enrollment 物件進行 remove 刪除：");
        Enrollment removeObj = new Enrollment("S101", "CS101");
        boolean removeRes = enrollmentSet.remove(removeObj);
        System.out.println("   刪除結果 (預期為 true)：" + removeRes);
        System.out.println("   刪除後集合內容：" + enrollmentSet);
        System.out.println("   集合最終大小：" + enrollmentSet.size());
    }
}
