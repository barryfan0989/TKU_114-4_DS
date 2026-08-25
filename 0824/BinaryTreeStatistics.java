class IntTreeNode {
    int value;
    IntTreeNode left;
    IntTreeNode right;

    IntTreeNode(int value) {
        this.value = value;
    }
}

public class BinaryTreeStatistics {
    public static int size(IntTreeNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    public static int sum(IntTreeNode node) {
        return node == null ? 0 : node.value + sum(node.left) + sum(node.right);
    }

    public static int maximum(IntTreeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot compute maximum of an empty tree.");
        }
        return maximumHelper(node);
    }

    private static int maximumHelper(IntTreeNode node) {
        int max = node.value;
        if (node.left != null) {
            max = Math.max(max, maximumHelper(node.left));
        }
        if (node.right != null) {
            max = Math.max(max, maximumHelper(node.right));
        }
        return max;
    }

    public static int leafCount(IntTreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leafCount(node.left) + leafCount(node.right);
    }

    public static int height(IntTreeNode node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    public static boolean contains(IntTreeNode node, int target) {
        if (node == null) return false;
        if (node.value == target) return true;
        return contains(node.left, target) || contains(node.right, target);
    }

    public static void main(String[] args) {
        // Construct a sample binary tree:
        //          10
        //        /    \
        //      -5      25
        //      / \     /
        //     8  12  -30
        IntTreeNode root = new IntTreeNode(10);
        root.left = new IntTreeNode(-5);
        root.right = new IntTreeNode(25);
        root.left.left = new IntTreeNode(8);
        root.left.right = new IntTreeNode(12);
        root.right.left = new IntTreeNode(-30);

        System.out.println("Tree Size: " + size(root));
        System.out.println("Tree Sum: " + sum(root));
        System.out.println("Tree Maximum: " + maximum(root));
        System.out.println("Tree Leaf Count: " + leafCount(root));
        System.out.println("Tree Height: " + height(root));
        System.out.println("Contains 12: " + contains(root, 12));
        System.out.println("Contains 99: " + contains(root, 99));
        System.out.println();

        // Test empty tree
        System.out.println("Testing Empty Tree:");
        System.out.println("  Size: " + size(null));
        System.out.println("  Sum: " + sum(null));
        System.out.println("  Leaf Count: " + leafCount(null));
        System.out.println("  Height: " + height(null));
        
        try {
            maximum(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected exception for empty tree max: " + e.getMessage());
        }
    }
}
