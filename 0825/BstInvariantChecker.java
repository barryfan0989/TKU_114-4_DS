class CheckerNode {
    int value;
    CheckerNode left;
    CheckerNode right;

    CheckerNode(int value) {
        this.value = value;
    }
}

class CheckerBst {
    CheckerNode root;

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(CheckerNode node, long min, long max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) {
            System.out.println("  [Violation] Node " + node.value + " is out of bounds (" + min + ", " + max + ")");
            return false;
        }
        return isValid(node.left, min, node.value) && isValid(node.right, node.value, max);
    }
}

public class BstInvariantChecker {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("          BST INVARIANT CHECKER DEMO             ");
        System.out.println("=================================================");

        // 1. Valid Tree
        //         50
        //       /    \
        //      30     70
        //     /  \   /  \
        //    20  40 60  80
        CheckerBst validTree = new CheckerBst();
        validTree.root = new CheckerNode(50);
        validTree.root.left = new CheckerNode(30);
        validTree.root.right = new CheckerNode(70);
        validTree.root.left.left = new CheckerNode(20);
        validTree.root.left.right = new CheckerNode(40);
        validTree.root.right.left = new CheckerNode(60);
        validTree.root.right.right = new CheckerNode(80);

        System.out.println("1. Checking Valid Tree:");
        boolean isV1 = validTree.isValid();
        System.out.println("   Result -> " + (isV1 ? "VALID BST ✅" : "INVALID ❌"));
        System.out.println();

        // 2. Invalid Tree 1 (Deep violation: left subtree contains key > root)
        //         50
        //       /    \
        //      30     70
        //        \
        //         60  <-- Violates root's upper bound (must be < 50)
        CheckerBst invalidTree1 = new CheckerBst();
        invalidTree1.root = new CheckerNode(50);
        invalidTree1.root.left = new CheckerNode(30);
        invalidTree1.root.right = new CheckerNode(70);
        invalidTree1.root.left.right = new CheckerNode(60);

        System.out.println("2. Checking Invalid Tree 1 (Left subtree contains key > root):");
        boolean isV2 = invalidTree1.isValid();
        System.out.println("   Result -> " + (isV2 ? "VALID BST ✅" : "INVALID ❌"));
        System.out.println();

        // 3. Invalid Tree 2 (Deep violation: right subtree contains key < root)
        //         50
        //       /    \
        //      30     70
        //            /
        //           40 <-- Violates root's lower bound (must be > 50)
        CheckerBst invalidTree2 = new CheckerBst();
        invalidTree2.root = new CheckerNode(50);
        invalidTree2.root.left = new CheckerNode(30);
        invalidTree2.root.right = new CheckerNode(70);
        invalidTree2.root.right.left = new CheckerNode(40);

        System.out.println("3. Checking Invalid Tree 2 (Right subtree contains key < root):");
        boolean isV3 = invalidTree2.isValid();
        System.out.println("   Result -> " + (isV3 ? "VALID BST ✅" : "INVALID ❌"));
        System.out.println();

        // 4. Invalid Tree 3 (Deep violation: sub-ancestral constraint violation)
        //         50
        //       /    \
        //      30     70
        //     /
        //    20
        //      \
        //       32 <-- Violates node 30's upper bound (must be < 30, even though 32 > 20 is correct)
        CheckerBst invalidTree3 = new CheckerBst();
        invalidTree3.root = new CheckerNode(50);
        invalidTree3.root.left = new CheckerNode(30);
        invalidTree3.root.right = new CheckerNode(70);
        invalidTree3.root.left.left = new CheckerNode(20);
        invalidTree3.root.left.left.right = new CheckerNode(32);

        System.out.println("4. Checking Invalid Tree 3 (Descendant of 30 violates 30's upper bound):");
        boolean isV4 = invalidTree3.isValid();
        System.out.println("   Result -> " + (isV4 ? "VALID BST ✅" : "INVALID ❌"));
        System.out.println("=================================================");
    }
}
