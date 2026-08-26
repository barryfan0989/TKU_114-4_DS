import java.util.ArrayList;
import java.util.List;

public class Q12_StudentBstSystem {
    public static class Student {
        private final int id;
        private final String name;
        private int score;

        public Student(int id, String name, int score) {
            if (id <= 0 || name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid ID or name");
            }
            this.id = id;
            this.name = name;
            this.score = Math.max(0, Math.min(100, score));
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = Math.max(0, Math.min(100, score));
        }

        @Override
        public String toString() {
            return id + "|" + name + "|" + score;
        }
    }

    private static class Node {
        Student student;
        Node left;
        Node right;

        Node(Student student) {
            this.student = student;
        }
    }

    private Node root;
    private int size = 0;

    public boolean add(Student student) {
        if (student == null) {
            return false;
        }
        if (contains(student.getId())) {
            return false;
        }
        root = addHelper(root, student);
        size++;
        return true;
    }

    private Node addHelper(Node node, Student student) {
        if (node == null) {
            return new Node(student);
        }
        if (student.getId() < node.student.getId()) {
            node.left = addHelper(node.left, student);
        } else if (student.getId() > node.student.getId()) {
            node.right = addHelper(node.right, student);
        }
        return node;
    }

    public Student find(int id) {
        return findHelper(root, id);
    }

    private Student findHelper(Node node, int id) {
        if (node == null) {
            return null;
        }
        if (id == node.student.getId()) {
            return node.student;
        }
        return id < node.student.getId() ? findHelper(node.left, id) : findHelper(node.right, id);
    }

    private boolean contains(int id) {
        return find(id) != null;
    }

    public boolean updateScore(int id, int score) {
        Student student = find(id);
        if (student == null) {
            return false;
        }
        student.setScore(score);
        return true;
    }

    public boolean remove(int id) {
        if (!contains(id)) {
            return false;
        }
        root = removeHelper(root, id);
        size--;
        return true;
    }

    private Node removeHelper(Node node, int id) {
        if (node == null) {
            return null;
        }
        if (id < node.student.getId()) {
            node.left = removeHelper(node.left, id);
        } else if (id > node.student.getId()) {
            node.right = removeHelper(node.right, id);
        } else {
            // Found node to delete
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            // Node with two children
            Node successor = findMinNode(node.right);
            node.student = successor.student;
            node.right = removeHelper(node.right, successor.student.getId());
        }
        return node;
    }

    private Node findMinNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public List<Student> studentsBetween(int lowId, int highId) {
        List<Student> result = new ArrayList<>();
        if (lowId > highId) {
            return result;
        }
        studentsBetweenHelper(root, lowId, highId, result);
        return result;
    }

    private void studentsBetweenHelper(Node node, int lowId, int highId, List<Student> result) {
        if (node == null) {
            return;
        }
        if (node.student.getId() > lowId) {
            studentsBetweenHelper(node.left, lowId, highId, result);
        }
        if (node.student.getId() >= lowId && node.student.getId() <= highId) {
            result.add(node.student);
        }
        if (node.student.getId() < highId) {
            studentsBetweenHelper(node.right, lowId, highId, result);
        }
    }

    public List<Student> inorder() {
        List<Student> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node node, List<Student> result) {
        if (node == null) {
            return;
        }
        inorderHelper(node.left, result);
        result.add(node.student);
        inorderHelper(node.right, result);
    }
}
