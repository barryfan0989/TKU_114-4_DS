import java.util.ArrayList;
import java.util.List;

class SuiteNode {
    int value;
    SuiteNode left;
    SuiteNode right;

    SuiteNode(int value) {
        this.value = value;
    }
}

class SuiteBst {
    SuiteNode root;

    boolean add(int value) {
        if (root == null) {
            root = new SuiteNode(value);
            return true;
        }
        SuiteNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new SuiteNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new SuiteNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        SuiteNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        return true;
    }

    private SuiteNode remove(SuiteNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            SuiteNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private SuiteNode minimumNode(SuiteNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    int size() {
        return size(root);
    }

    private int size(SuiteNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(SuiteNode node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(SuiteNode node, long min, long max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) return false;
        return isValid(node.left, min, node.value) && isValid(node.right, node.value, max);
    }

    List<Integer> range(int low, int high) {
        List<Integer> list = new ArrayList<>();
        range(root, low, high, list);
        return list;
    }

    private void range(SuiteNode node, int low, int high, List<Integer> list) {
        if (node == null) return;
        if (node.value > low) range(node.left, low, high, list);
        if (node.value >= low && node.value <= high) list.add(node.value);
        if (node.value < high) range(node.right, low, high, list);
    }
}

public class CompleteBstTestSuite {
    private static int passCount = 0;
    private static int failCount = 0;

    static void check(String description, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + description);
            passCount++;
        } else {
            System.out.println("  [FAIL] " + description);
            failCount++;
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("          STARTING BST TEST SUITE                ");
        System.out.println("=================================================");

        SuiteBst tree = new SuiteBst();

        // --- GROUP 1: Empty Tree Assertions ---
        check("Empty Tree: size should be 0", tree.size() == 0);
        check("Empty Tree: height should be -1", tree.height() == -1);
        check("Empty Tree: should be valid BST", tree.isValid());
        check("Empty Tree: contains(10) should be false", !tree.contains(10));
        check("Empty Tree: remove(10) should be false", !tree.remove(10));

        // --- GROUP 2: Single Node Assertions ---
        check("Add(50): should return true", tree.add(50));
        check("Single Node: size should be 1", tree.size() == 1);
        check("Single Node: height should be 0", tree.height() == 0);
        check("Single Node: should contain 50", tree.contains(50));
        check("Single Node: should be valid BST", tree.isValid());

        // --- GROUP 3: Duplicate and Missing Key Assertions ---
        check("Add(50) Duplicate: should return false", !tree.add(50));
        check("Duplicate Add: size should still be 1", tree.size() == 1);
        check("Remove(99) Missing: should return false", !tree.remove(99));
        check("Missing Remove: size should still be 1", tree.size() == 1);

        // --- GROUP 4: Deleting Single Root Assertions ---
        check("Remove(50) Root: should return true", tree.remove(50));
        check("After Deletion: size should be 0", tree.size() == 0);
        check("After Deletion: height should be -1", tree.height() == -1);

        // --- GROUP 5: Multi-node Construction & Range Queries ---
        // Inserting: 50, 30, 70, 20, 40, 60, 80
        int[] vals = {50, 30, 70, 20, 40, 60, 80};
        for (int v : vals) tree.add(v);
        check("Multi-Node: size should be 7", tree.size() == 7);
        check("Multi-Node: height should be 2", tree.height() == 2);
        check("Multi-Node: should be valid BST", tree.isValid());
        
        List<Integer> expectedRange = List.of(30, 40, 50, 60, 70);
        check("Range [30, 70]: check elements", tree.range(30, 70).equals(expectedRange));

        // --- GROUP 6: Delete Cases Assertions ---
        // 1. Delete Leaf (20)
        check("Delete Leaf (20): should return true", tree.remove(20));
        check("Delete Leaf: size should be 6", tree.size() == 6);
        check("Delete Leaf: remains valid", tree.isValid());

        // 2. Delete One-child (30) (now has child 40)
        check("Delete One-child (30): should return true", tree.remove(30));
        check("Delete One-child: size should be 5", tree.size() == 5);
        check("Delete One-child: remains valid", tree.isValid());

        // 3. Delete Two-children (50) (Root)
        check("Delete Two-children (50): should return true", tree.remove(50));
        check("Delete Two-children: size should be 4", tree.size() == 4);
        check("Delete Two-children: remains valid", tree.isValid());

        // --- GROUP 7: Invariant Invalidation Assertion ---
        // Manually break the tree's BST property
        // tree current nodes are 40, 60, 70, 80 (root is 60, left is 40, right is 70, 70.right is 80)
        // Let's modify 40's value to 99, making it larger than root 60
        if (tree.root != null && tree.root.left != null) {
            tree.root.left.value = 99; // Violation!
        }
        check("Invariant Violation: isValid() should return false", !tree.isValid());

        System.out.println("=================================================");
        System.out.println("             TEST SUITE SUMMARY                  ");
        System.out.println("=================================================");
        System.out.println("  Total Tests Executed: " + (passCount + failCount));
        System.out.println("  Passed Assertions:    " + passCount);
        System.out.println("  Failed Assertions:    " + failCount);
        System.out.println("=================================================");
    }
}
