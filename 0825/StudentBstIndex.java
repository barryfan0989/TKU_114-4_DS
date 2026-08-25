class Student {
    int studentId;
    String name;
    double gpa;

    Student(int studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "ID: " + studentId + ", Name: " + name + ", GPA: " + gpa;
    }
}

class StudentNode {
    Student student;
    StudentNode left;
    StudentNode right;

    StudentNode(Student student) {
        this.student = student;
    }
}

class StudentBst {
    StudentNode root;

    boolean insert(Student student) {
        if (student == null) return false;
        if (root == null) {
            root = new StudentNode(student);
            return true;
        }
        StudentNode current = root;
        while (true) {
            if (student.studentId == current.student.studentId) {
                return false; // Duplicate student ID not allowed
            }
            if (student.studentId < current.student.studentId) {
                if (current.left == null) {
                    current.left = new StudentNode(student);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new StudentNode(student);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Student search(int studentId) {
        StudentNode current = root;
        while (current != null) {
            if (studentId == current.student.studentId) {
                return current.student;
            }
            current = (studentId < current.student.studentId) ? current.left : current.right;
        }
        return null;
    }

    boolean delete(int studentId) {
        if (search(studentId) == null) {
            return false;
        }
        root = delete(root, studentId);
        return true;
    }

    private StudentNode delete(StudentNode node, int studentId) {
        if (node == null) return null;
        if (studentId < node.student.studentId) {
            node.left = delete(node.left, studentId);
        } else if (studentId > node.student.studentId) {
            node.right = delete(node.right, studentId);
        } else {
            // Found node to delete
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Two children case
            StudentNode successor = minimumNode(node.right);
            node.student = successor.student;
            node.right = delete(node.right, successor.student.studentId);
        }
        return node;
    }

    private StudentNode minimumNode(StudentNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    void inorder() {
        inorder(root);
    }

    private void inorder(StudentNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.student);
        inorder(node.right);
    }
}

public class StudentBstIndex {
    public static void main(String[] args) {
        StudentBst index = new StudentBst();

        System.out.println("=== 1. Inserting Students ===");
        System.out.println("Insert 103 Alice: " + index.insert(new Student(103, "Alice", 3.8)));
        System.out.println("Insert 101 Bob:   " + index.insert(new Student(101, "Bob", 3.5)));
        System.out.println("Insert 105 Charlie: " + index.insert(new Student(105, "Charlie", 3.9)));
        System.out.println("Insert 102 David: " + index.insert(new Student(102, "David", 3.2)));
        System.out.println("Insert 104 Eva:   " + index.insert(new Student(104, "Eva", 3.7)));

        System.out.println("\n=== 2. Attempting Duplicate Insertion ===");
        System.out.println("Insert 103 (Duplicate ID) Frank: " + index.insert(new Student(103, "Frank", 3.1)));

        System.out.println("\n=== 3. Current Sorted Index (Inorder) ===");
        index.inorder();

        System.out.println("\n=== 4. Searching for Students ===");
        int[] searchIds = {102, 106};
        for (int id : searchIds) {
            Student s = index.search(id);
            System.out.println("Search ID " + id + ": " + (s != null ? s : "NOT FOUND"));
        }

        System.out.println("\n=== 5. Deleting Students ===");
        // Delete Leaf Node (102)
        System.out.println("Delete 102 (Leaf): " + index.delete(102));
        
        // Delete Single-Child Node (101) (now only has child 102 deleted, wait: 101 had 102 as right child. With 102 deleted, 101 has no children! Let's check: 103's left was 101. 101's right was 102. When 102 is deleted, 101 becomes a leaf. Let's delete 105 instead, which has left 104 and no right child? Yes, 105 has left child 104, and no right child. So 105 is single-child.)
        System.out.println("Delete 105 (Single-Child): " + index.delete(105));
        
        // Delete Root with two children (103)
        System.out.println("Delete 103 (Root with two children): " + index.delete(103));

        System.out.println("\n=== 6. Final Index (Inorder) ===");
        index.inorder();
    }
}
