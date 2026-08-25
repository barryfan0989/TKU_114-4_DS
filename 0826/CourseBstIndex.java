class Course {
    String courseCode;
    String courseName;
    int credit;

    Course(String courseCode, String courseName, int credit) {
        if (credit < 1 || credit > 6) {
            throw new IllegalArgumentException("Credit must be between 1 and 6.");
        }
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Code: " + courseCode + " | Name: " + courseName + " | Credits: " + credit;
    }
}

class CourseNode {
    Course course;
    CourseNode left;
    CourseNode right;

    CourseNode(Course course) {
        this.course = course;
    }
}

class CourseBst {
    CourseNode root;

    boolean add(Course course) {
        if (course == null) return false;
        if (root == null) {
            root = new CourseNode(course);
            return true;
        }
        CourseNode current = root;
        while (true) {
            int cmp = course.courseCode.compareTo(current.course.courseCode);
            if (cmp == 0) {
                System.out.println("  [Error] Duplicate Course Code: " + course.courseCode);
                return false;
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new CourseNode(course);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CourseNode(course);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Course find(String courseCode) {
        CourseNode current = root;
        while (current != null) {
            int cmp = courseCode.compareTo(current.course.courseCode);
            if (cmp == 0) return current.course;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    boolean remove(String courseCode) {
        if (find(courseCode) == null) {
            System.out.println("  [Error] Cannot remove: Course " + courseCode + " not found.");
            return false;
        }
        root = remove(root, courseCode);
        return true;
    }

    private CourseNode remove(CourseNode node, String courseCode) {
        if (node == null) return null;
        int cmp = courseCode.compareTo(node.course.courseCode);
        if (cmp < 0) {
            node.left = remove(node.left, courseCode);
        } else if (cmp > 0) {
            node.right = remove(node.right, courseCode);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            CourseNode successor = minimumNode(node.right);
            node.course = successor.course;
            node.right = remove(node.right, successor.course.courseCode);
        }
        return node;
    }

    private CourseNode minimumNode(CourseNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    boolean updateCredit(String courseCode, int newCredit) {
        if (newCredit < 1 || newCredit > 6) {
            System.out.println("  [Error] Cannot update credits: Value " + newCredit + " must be between 1 and 6.");
            return false;
        }
        Course course = find(courseCode);
        if (course == null) {
            System.out.println("  [Error] Course Code " + courseCode + " not found.");
            return false;
        }
        course.credit = newCredit;
        System.out.println("  [Success] Updated course " + courseCode + " credit to: " + newCredit);
        return true;
    }

    void printRange(String lowCode, String highCode) {
        System.out.println("--- Course Code Range [" + lowCode + ", " + highCode + "] ---");
        if (lowCode.compareTo(highCode) > 0) {
            System.out.println("  [Error] Invalid range parameters.");
            return;
        }
        printRange(root, lowCode, highCode);
        System.out.println();
    }

    private void printRange(CourseNode node, String lowCode, String highCode) {
        if (node == null) return;
        if (node.course.courseCode.compareTo(lowCode) >= 0) {
            printRange(node.left, lowCode, highCode);
        }
        if (node.course.courseCode.compareTo(lowCode) >= 0 && node.course.courseCode.compareTo(highCode) <= 0) {
            System.out.println("  " + node.course);
        }
        if (node.course.courseCode.compareTo(highCode) <= 0) {
            printRange(node.right, lowCode, highCode);
        }
    }

    void inorderReport() {
        inorder(root);
    }

    private void inorder(CourseNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.course);
        inorder(node.right);
    }
}

public class CourseBstIndex {
    public static void main(String[] args) {
        CourseBst index = new CourseBst();

        System.out.println("=== 1. Inserting Courses ===");
        try {
            index.add(new Course("CS-101", "Introduction to Computer Science", 3));
            index.add(new Course("MATH-201", "Calculus I", 4));
            index.add(new Course("ENG-102", "English Composition", 2));
            index.add(new Course("CS-302", "Data Structures & Algorithms", 4));
            index.add(new Course("PHY-202", "General Physics", 4));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // Test duplicate code
        try {
            index.add(new Course("CS-101", "Duplicate CS", 3));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // Test credit limits on initialization
        try {
            System.out.print("Creating Course with invalid credits (7): ");
            new Course("CHEM-101", "Chemistry", 7);
        } catch (Exception e) {
            System.out.println("Exception caught -> " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== 2. Current Courses (Inorder) ===");
        index.inorderReport();
        System.out.println();

        System.out.println("=== 3. Search Course ===");
        Course found = index.find("CS-302");
        System.out.println("Search CS-302: " + (found != null ? found : "NOT FOUND"));
        System.out.println("Search BIO-101: " + (index.find("BIO-101") != null ? index.find("BIO-101") : "NOT FOUND"));
        System.out.println();

        System.out.println("=== 4. Updating Credits ===");
        index.updateCredit("MATH-201", 3);
        index.updateCredit("ENG-102", 0); // should fail
        System.out.println();

        System.out.println("=== 5. Range Query ===");
        index.printRange("CS-100", "MATH-200");
        
        System.out.println("=== 6. Deleting Course ===");
        index.remove("MATH-201");
        index.remove("BIO-101"); // should fail
        System.out.println();

        System.out.println("=== 7. Final Courses (Inorder) ===");
        index.inorderReport();
    }
}
