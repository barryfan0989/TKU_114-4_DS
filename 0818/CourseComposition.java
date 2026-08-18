class Instructor {
    private String id;
    private String name;

    Instructor(String id, String name) {
        this.id = id == null || id.trim().isEmpty() ? "Unknown" : id.trim();
        this.name = name == null || name.trim().isEmpty() ? "Unknown" : name.trim();
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String label() {
        return id + " (" + name + ")";
    }
}

class Course {
    private String courseCode;
    private String title;
    private Instructor instructor;

    Course(String courseCode, String title, Instructor instructor) {
        this.courseCode = courseCode == null || courseCode.trim().isEmpty() ? "Unknown" : courseCode.trim();
        this.title = title == null || title.trim().isEmpty() ? "Untitled" : title.trim();
        this.instructor = instructor;
    }

    String summary() {
        String instructorInfo = (instructor == null) ? "No Instructor" : instructor.label();
        return courseCode + ": " + title + " [Instructor: " + instructorInfo + "]";
    }
}

public class CourseComposition {
    public static void main(String[] args) {
        Instructor instructor = new Instructor("I101", "Dr. Smith");
        
        Course course1 = new Course("CS101", "Introduction to Computer Science", instructor);
        Course course2 = new Course("DS201", "Data Structures", instructor);

        System.out.println(course1.summary());
        System.out.println(course2.summary());
        
        System.out.println("Are both courses sharing the same instructor reference? " 
            + (course1.summary().contains(instructor.label()) && course2.summary().contains(instructor.label())));
    }
}
