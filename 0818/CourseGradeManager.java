class CourseGrade {
    private String studentId;
    private String name;
    private int dailyGrade;       // 平時 50%
    private int midtermGrade;     // 期中 20%
    private int finalGrade;       // 期末 20%
    private int attendanceGrade;  // 出席 10%

    CourseGrade(String studentId, String name, int dailyGrade, int midtermGrade, int finalGrade, int attendanceGrade) {
        this.studentId = studentId == null || studentId.trim().isEmpty() ? "Unknown" : studentId.trim();
        this.name = name == null || name.trim().isEmpty() ? "Unknown" : name.trim();
        this.dailyGrade = clamp(dailyGrade);
        this.midtermGrade = clamp(midtermGrade);
        this.finalGrade = clamp(finalGrade);
        this.attendanceGrade = clamp(attendanceGrade);
    }

    private int clamp(int grade) {
        if (grade < 0) return 0;
        if (grade > 100) return 100;
        return grade;
    }

    double calculateFinalScore() {
        return (dailyGrade * 0.5) + (midtermGrade * 0.2) + (finalGrade * 0.2) + (attendanceGrade * 0.1);
    }

    String getLevel() {
        double finalScore = calculateFinalScore();
        if (finalScore >= 90.0) return "A";
        if (finalScore >= 80.0) return "B";
        if (finalScore >= 70.0) return "C";
        if (finalScore >= 60.0) return "D";
        return "F";
    }

    String getStudentId() {
        return studentId;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s %s | Daily=%d, Mid=%d, Final=%d, Att=%d | Total=%.1f (%s)",
                studentId, name, dailyGrade, midtermGrade, finalGrade, attendanceGrade, calculateFinalScore(), getLevel());
    }
}

public class CourseGradeManager {
    public static void main(String[] args) {
        CourseGrade[] grades = {
            new CourseGrade("S001", "Amy", 90, 85, 80, 100),
            new CourseGrade("S002", "Bob", 50, 60, 55, 70),
            new CourseGrade("S003", "Cara", 95, 90, 92, 98),
            new CourseGrade("S004", "David", 70, 65, 58, 80),
            new CourseGrade("S005", "Emma", 40, 50, 45, 50)
        };

        System.out.println("Student Grades Records:");
        for (CourseGrade record : grades) {
            System.out.println(record);
        }

        // Calculate Average
        double sum = 0;
        for (CourseGrade record : grades) {
            sum += record.calculateFinalScore();
        }
        double average = sum / grades.length;
        System.out.printf("\nAverage Final Score: %.2f%n", average);

        // Find Highest
        CourseGrade highest = grades[0];
        for (CourseGrade record : grades) {
            if (record.calculateFinalScore() > highest.calculateFinalScore()) {
                highest = record;
            }
        }
        System.out.printf("Highest Scoring Student: %s (%.1f)%n", highest.getName(), highest.calculateFinalScore());

        // Failing students
        System.out.println("\nFailing Students (Final Score < 60.0):");
        int failCount = 0;
        for (CourseGrade record : grades) {
            if (record.calculateFinalScore() < 60.0) {
                System.out.println("  - " + record.getName() + " | Final Score: " + record.calculateFinalScore());
                failCount++;
            }
        }
        if (failCount == 0) {
            System.out.println("  None");
        }
    }
}
