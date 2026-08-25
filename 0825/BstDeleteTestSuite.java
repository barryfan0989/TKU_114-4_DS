class TestSuiteNode {
    int value;
    TestSuiteNode left;
    TestSuiteNode right;

    TestSuiteNode(int value) {
        this.value = value;
    }
}

class TestSuiteBst {
    TestSuiteNode root;

    boolean add(int value) {
        if (root == null) {
            root = new TestSuiteNode(value);
            return true;
        }
        TestSuiteNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new TestSuiteNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new TestSuiteNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        return true;
    }

    private TestSuiteNode remove(TestSuiteNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            TestSuiteNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private TestSuiteNode minimumNode(TestSuiteNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    boolean contains(int value) {
        TestSuiteNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    int size() {
        return size(root);
    }

    private int size(TestSuiteNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(TestSuiteNode node, long min, long max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) return false;
        return isValid(node.left, min, node.value) && isValid(node.right, node.value, max);
    }

    String getInorder() {
        StringBuilder sb = new StringBuilder();
        getInorder(root, sb);
        return sb.toString().trim();
    }

    private void getInorder(TestSuiteNode node, StringBuilder sb) {
        if (node == null) return;
        getInorder(node.left, sb);
        sb.append(node.value).append(" ");
        getInorder(node.right, sb);
    }
}

public class BstDeleteTestSuite {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("           BST DELETION TEST SUITE               ");
        System.out.println("=================================================");

        // Test 1: Empty Tree
        System.out.println("Test 1: Deletion on Empty Tree");
        TestSuiteBst tree = new TestSuiteBst();
        boolean res1 = tree.remove(10);
        assertResult("remove(10) returned false", !res1);
        assertResult("size is 0", tree.size() == 0);
        assertResult("tree is valid", tree.isValid());
        System.out.println("Status: PASSED\n");

        // Test 2: Missing Key
        System.out.println("Test 2: Deletion of Missing Key");
        tree.add(50);
        boolean res2 = tree.remove(10);
        assertResult("remove(10) returned false", !res2);
        assertResult("size remains 1", tree.size() == 1);
        assertResult("inorder is '50'", tree.getInorder().equals("50"));
        assertResult("tree is valid", tree.isValid());
        System.out.println("Status: PASSED\n");

        // Test 3: Single Root Node
        System.out.println("Test 3: Deletion of Single Root Node");
        boolean res3 = tree.remove(50);
        assertResult("remove(50) returned true", res3);
        assertResult("size becomes 0", tree.size() == 0);
        assertResult("inorder is empty", tree.getInorder().isEmpty());
        assertResult("tree is valid", tree.isValid());
        System.out.println("Status: PASSED\n");

        // Test 4: Root with One Child
        System.out.println("Test 4: Deletion of Root with One Child");
        tree.add(50);
        tree.add(30);
        boolean res4 = tree.remove(50);
        assertResult("remove(50) returned true", res4);
        assertResult("size is 1", tree.size() == 1);
        assertResult("inorder is '30'", tree.getInorder().equals("30"));
        assertResult("new root is 30", tree.root != null && tree.root.value == 30);
        assertResult("tree is valid", tree.isValid());
        System.out.println("Status: PASSED\n");

        // Test 5: Root with Two Children
        System.out.println("Test 5: Deletion of Root with Two Children");
        // Tree: Root is 30. Add 10 (left), 50 (right).
        tree.add(10);
        tree.add(50);
        boolean res5 = tree.remove(30); // Delete Root (30) which has left 10 and right 50.
        assertResult("remove(30) returned true", res5);
        assertResult("size is 2", tree.size() == 2);
        assertResult("inorder is '10 50'", tree.getInorder().equals("10 50"));
        assertResult("new root is 50 (successor)", tree.root != null && tree.root.value == 50);
        assertResult("tree is valid", tree.isValid());
        System.out.println("Status: PASSED\n");

        // Test 6: Consecutive Deletions to Empty Tree
        System.out.println("Test 6: Consecutive Deletions to Empty Tree");
        TestSuiteBst largeTree = new TestSuiteBst();
        int[] insertValues = {50, 30, 70, 20, 40, 60, 80};
        for (int v : insertValues) largeTree.add(v);
        
        System.out.println("  Initial: " + largeTree.getInorder() + " | size: " + largeTree.size());
        
        int[] deleteOrder = {20, 30, 40, 50, 60, 70, 80};
        for (int dv : deleteOrder) {
            largeTree.remove(dv);
            System.out.println("  Deleted " + dv + " -> Inorder: [" + largeTree.getInorder() + "] | size: " + largeTree.size() + " | valid: " + largeTree.isValid());
            assertResult("Tree remains valid after deleting " + dv, largeTree.isValid());
        }
        
        assertResult("Final size is 0", largeTree.size() == 0);
        assertResult("Final inorder is empty", largeTree.getInorder().isEmpty());
        System.out.println("Status: PASSED\n");
        
        System.out.println("=================================================");
        System.out.println("          ALL TEST SUITE CASES PASSED!           ");
        System.out.println("=================================================");
    }

    private static void assertResult(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion Failed: " + message);
        }
        System.out.println("  [OK] " + message);
    }
}
