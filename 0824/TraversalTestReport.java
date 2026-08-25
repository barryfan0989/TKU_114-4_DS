import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

class ReportNode {
    String value;
    ReportNode left;
    ReportNode right;

    ReportNode(String value) {
        this.value = value;
    }
}

public class TraversalTestReport {

    public static List<String> preorder(ReportNode root) {
        List<String> list = new ArrayList<>();
        preorderHelper(root, list);
        return list;
    }

    private static void preorderHelper(ReportNode node, List<String> list) {
        if (node == null) return;
        list.add(node.value);
        preorderHelper(node.left, list);
        preorderHelper(node.right, list);
    }

    public static List<String> inorder(ReportNode root) {
        List<String> list = new ArrayList<>();
        inorderHelper(root, list);
        return list;
    }

    private static void inorderHelper(ReportNode node, List<String> list) {
        if (node == null) return;
        inorderHelper(node.left, list);
        list.add(node.value);
        inorderHelper(node.right, list);
    }

    public static List<String> postorder(ReportNode root) {
        List<String> list = new ArrayList<>();
        postorderHelper(root, list);
        return list;
    }

    private static void postorderHelper(ReportNode node, List<String> list) {
        if (node == null) return;
        postorderHelper(node.left, list);
        postorderHelper(node.right, list);
        list.add(node.value);
    }

    public static List<String> levelOrder(ReportNode root) {
        List<String> list = new ArrayList<>();
        if (root == null) return list;
        Queue<ReportNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            ReportNode current = queue.poll();
            list.add(current.value);
            if (current.left != null) queue.offer(current.left);
            if (current.right != null) queue.offer(current.right);
        }
        return list;
    }

    public static void verifyTraversal(String treeName, ReportNode root, 
                                       List<String> expPre, List<String> expIn, 
                                       List<String> expPost, List<String> expLvl) {
        System.out.println("=== Test Tree: " + treeName + " ===");

        List<String> actPre = preorder(root);
        List<String> actIn = inorder(root);
        List<String> actPost = postorder(root);
        List<String> actLvl = levelOrder(root);

        boolean preOk = actPre.equals(expPre);
        boolean inOk = actIn.equals(expIn);
        boolean postOk = actPost.equals(expPost);
        boolean lvlOk = actLvl.equals(expLvl);

        System.out.printf("  %-12s | Expected: %-30s | Actual: %-30s | Match: %b\n", 
                          "Preorder", expPre, actPre, preOk);
        System.out.printf("  %-12s | Expected: %-30s | Actual: %-30s | Match: %b\n", 
                          "Inorder", expIn, actIn, inOk);
        System.out.printf("  %-12s | Expected: %-30s | Actual: %-30s | Match: %b\n", 
                          "Postorder", expPost, actPost, postOk);
        System.out.printf("  %-12s | Expected: %-30s | Actual: %-30s | Match: %b\n", 
                          "Level-order", expLvl, actLvl, lvlOk);
        
        boolean allOk = preOk && inOk && postOk && lvlOk;
        System.out.println("  Overall Status: " + (allOk ? "SUCCESS ✅" : "FAILED ❌"));
        System.out.println();
    }

    public static void main(String[] args) {
        // 1. Empty tree
        verifyTraversal("1. Empty Tree", null, 
                        Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList());

        // 2. Single-node tree
        ReportNode single = new ReportNode("A");
        verifyTraversal("2. Single Node Tree", single, 
                        Arrays.asList("A"), Arrays.asList("A"), Arrays.asList("A"), Arrays.asList("A"));

        // 3. Only-left tree (left-skewed)
        ReportNode leftSkewed = new ReportNode("A");
        leftSkewed.left = new ReportNode("B");
        leftSkewed.left.left = new ReportNode("C");
        verifyTraversal("3. Left-Skewed Tree", leftSkewed, 
                        Arrays.asList("A", "B", "C"), Arrays.asList("C", "B", "A"), 
                        Arrays.asList("C", "B", "A"), Arrays.asList("A", "B", "C"));

        // 4. Only-right tree (right-skewed)
        ReportNode rightSkewed = new ReportNode("A");
        rightSkewed.right = new ReportNode("B");
        rightSkewed.right.right = new ReportNode("C");
        verifyTraversal("4. Right-Skewed Tree", rightSkewed, 
                        Arrays.asList("A", "B", "C"), Arrays.asList("A", "B", "C"), 
                        Arrays.asList("C", "B", "A"), Arrays.asList("A", "B", "C"));

        // 5. Complete tree
        //          A
        //        /   \
        //       B     C
        //      / \   / \
        //     D   E F   G
        ReportNode complete = new ReportNode("A");
        complete.left = new ReportNode("B");
        complete.right = new ReportNode("C");
        complete.left.left = new ReportNode("D");
        complete.left.right = new ReportNode("E");
        complete.right.left = new ReportNode("F");
        complete.right.right = new ReportNode("G");
        verifyTraversal("5. Complete Tree", complete, 
                        Arrays.asList("A", "B", "D", "E", "C", "F", "G"), 
                        Arrays.asList("D", "B", "E", "A", "F", "C", "G"), 
                        Arrays.asList("D", "E", "B", "F", "G", "C", "A"), 
                        Arrays.asList("A", "B", "C", "D", "E", "F", "G"));

        // 6. Irregular tree
        //          A
        //         /
        //        B
        //         \
        //          C
        //         /
        //        D
        ReportNode irregular = new ReportNode("A");
        irregular.left = new ReportNode("B");
        irregular.left.right = new ReportNode("C");
        irregular.left.right.left = new ReportNode("D");
        verifyTraversal("6. Irregular Tree", irregular, 
                        Arrays.asList("A", "B", "C", "D"), 
                        Arrays.asList("B", "D", "C", "A"), 
                        Arrays.asList("D", "C", "B", "A"), 
                        Arrays.asList("A", "B", "C", "D"));
    }
}
