import java.util.ArrayList;
import java.util.List;

class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;

    TreeNode(int value) {
        this.value = value;
    }
}

public class BinaryTreeStructureReport {
    public static int size(TreeNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    public static int leafCount(TreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leafCount(node.left) + leafCount(node.right);
    }

    public static int height(TreeNode node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    public static List<Integer> getLeaves(TreeNode node) {
        List<Integer> leaves = new ArrayList<>();
        findLeaves(node, leaves);
        return leaves;
    }

    private static void findLeaves(TreeNode node, List<Integer> leaves) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            leaves.add(node.value);
            return;
        }
        findLeaves(node.left, leaves);
        findLeaves(node.right, leaves);
    }

    public static void printReport(TreeNode root, String treeName) {
        System.out.println("=== Report for: " + treeName + " ===");
        if (root == null) {
            System.out.println("Root: null (Empty Tree)");
            System.out.println("Leaves: []");
            System.out.println("Size: " + size(root));
            System.out.println("Leaf Count: " + leafCount(root));
            System.out.println("Height: " + height(root));
        } else {
            System.out.println("Root: " + root.value);
            System.out.println("Leaves: " + getLeaves(root));
            System.out.println("Size: " + size(root));
            System.out.println("Leaf Count: " + leafCount(root));
            System.out.println("Height: " + height(root));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // 1. Create a tree with at least 7 nodes
        //          1
        //        /   \
        //       2     3
        //      / \   / \
        //     4   5 6   7
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        printReport(root, "7-Node Tree");

        // 2. Test empty tree
        printReport(null, "Empty Tree");

        // 3. Test single-node tree
        TreeNode single = new TreeNode(42);
        printReport(single, "Single-Node Tree");
    }
}
