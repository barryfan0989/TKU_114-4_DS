import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

class StringNode {
    String value;
    StringNode left;
    StringNode right;

    StringNode(String value) {
        this.value = value;
    }
}

public class TraversalResultCollector {
    public static List<String> preorder(StringNode root) {
        List<String> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private static void preorderHelper(StringNode node, List<String> result) {
        if (node == null) return;
        result.add(node.value);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    public static List<String> inorder(StringNode root) {
        List<String> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private static void inorderHelper(StringNode node, List<String> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    public static List<String> postorder(StringNode root) {
        List<String> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private static void postorderHelper(StringNode node, List<String> result) {
        if (node == null) return;
        postorderHelper(node.left, result);
        postorderHelper(node.right, result);
        result.add(node.value);
    }

    public static List<String> levelOrder(StringNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) return result;
        Queue<StringNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            StringNode current = queue.poll();
            result.add(current.value);
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        return result;
    }

    public static void testTree(StringNode root, String treeType) {
        System.out.println("--- Testing " + treeType + " ---");
        System.out.println("  Preorder   : " + preorder(root));
        System.out.println("  Inorder    : " + inorder(root));
        System.out.println("  Postorder  : " + postorder(root));
        System.out.println("  Level-order: " + levelOrder(root));
        System.out.println();
    }

    public static void main(String[] args) {
        // 1. Empty tree
        testTree(null, "Empty Tree");

        // 2. Single-node tree
        StringNode singleNode = new StringNode("A");
        testTree(singleNode, "Single-Node Tree");

        // 3. Left-skewed tree
        //      A
        //     /
        //    B
        //   /
        //  C
        StringNode leftSkewed = new StringNode("A");
        leftSkewed.left = new StringNode("B");
        leftSkewed.left.left = new StringNode("C");
        testTree(leftSkewed, "Left-Skewed Tree");

        // 4. Complete tree
        //       A
        //      / \
        //     B   C
        //    / \
        //   D   E
        StringNode complete = new StringNode("A");
        complete.left = new StringNode("B");
        complete.right = new StringNode("C");
        complete.left.left = new StringNode("D");
        complete.left.right = new StringNode("E");
        testTree(complete, "Complete Tree");
    }
}
