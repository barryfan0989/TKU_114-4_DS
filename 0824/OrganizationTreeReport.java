import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

class OrgNode {
    String name;
    OrgNode left;
    OrgNode right;

    OrgNode(String name) {
        this.name = name;
    }
}

public class OrganizationTreeReport {

    public static OrgNode findParent(OrgNode root, String target) {
        if (root == null || target == null || root.name.equals(target)) {
            return null; // Root has no parent or target is null/root itself
        }
        return findParentHelper(root, target);
    }

    private static OrgNode findParentHelper(OrgNode current, String target) {
        if (current == null) return null;
        if ((current.left != null && current.left.name.equals(target)) ||
            (current.right != null && current.right.name.equals(target))) {
            return current;
        }
        OrgNode leftResult = findParentHelper(current.left, target);
        if (leftResult != null) return leftResult;
        return findParentHelper(current.right, target);
    }

    public static int findDepth(OrgNode root, String target) {
        return findDepthHelper(root, target, 0);
    }

    private static int findDepthHelper(OrgNode node, String target, int currentDepth) {
        if (node == null || target == null) return -1;
        if (node.name.equals(target)) return currentDepth;
        int leftDepth = findDepthHelper(node.left, target, currentDepth + 1);
        if (leftDepth != -1) return leftDepth;
        return findDepthHelper(node.right, target, currentDepth + 1);
    }

    public static List<String> pathFromRoot(OrgNode root, String target) {
        List<String> path = new ArrayList<>();
        if (root == null || target == null) return path;
        if (findPathHelper(root, target, path)) {
            return path;
        }
        return new ArrayList<>(); // Return empty list if target not found
    }

    private static boolean findPathHelper(OrgNode node, String target, List<String> path) {
        if (node == null) return false;
        path.add(node.name);
        if (node.name.equals(target)) return true;
        if (findPathHelper(node.left, target, path) || findPathHelper(node.right, target, path)) {
            return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    public static void printByLevel(OrgNode root) {
        if (root == null) {
            System.out.println("Empty Organization");
            return;
        }
        Queue<OrgNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            System.out.print("Level " + level + ": ");
            for (int i = 0; i < size; i++) {
                OrgNode cur = queue.poll();
                System.out.print(cur.name + " ");
                if (cur.left != null) queue.offer(cur.left);
                if (cur.right != null) queue.offer(cur.right);
            }
            System.out.println();
            level++;
        }
    }

    public static void main(String[] args) {
        // Construct Organization Tree:
        //             HeadOffice
        //             /        \
        //       Sales           Technology
        //       /   \            /      \
        // Domestic Export   Platform    Support
        OrgNode root = new OrgNode("HeadOffice");
        root.left = new OrgNode("Sales");
        root.right = new OrgNode("Technology");
        root.left.left = new OrgNode("Domestic");
        root.left.right = new OrgNode("Export");
        root.right.left = new OrgNode("Platform");
        root.right.right = new OrgNode("Support");

        System.out.println("=== Organization Layout ===");
        printByLevel(root);
        System.out.println();

        // 1. Test findParent
        String[] targets = {"Domestic", "Technology", "HeadOffice", "HR"};
        System.out.println("=== Find Parent Tests ===");
        for (String target : targets) {
            OrgNode parent = findParent(root, target);
            System.out.println("Parent of \"" + target + "\": " + (parent == null ? "None/Not Found" : parent.name));
        }
        System.out.println();

        // 2. Test findDepth
        System.out.println("=== Find Depth Tests ===");
        for (String target : targets) {
            System.out.println("Depth of \"" + target + "\": " + findDepth(root, target));
        }
        System.out.println();

        // 3. Test pathFromRoot
        System.out.println("=== Path From Root Tests ===");
        for (String target : targets) {
            System.out.println("Path to \"" + target + "\": " + pathFromRoot(root, target));
        }
    }
}
