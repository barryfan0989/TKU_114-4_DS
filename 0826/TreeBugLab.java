import java.util.ArrayList;
import java.util.List;

class LabNode {
    int value;
    LabNode left;
    LabNode right;

    LabNode(int value) {
        this.value = value;
    }
}

public class TreeBugLab {

    // ==========================================
    // BUG 1: Search Direction Reversed
    // ==========================================
    static boolean searchBroken(LabNode node, int target) {
        LabNode current = node;
        while (current != null) {
            if (target == current.value) return true;
            // BUG: direction is reversed!
            current = (target < current.value) ? current.right : current.left;
        }
        return false;
    }

    static boolean searchFixed(LabNode node, int target) {
        LabNode current = node;
        while (current != null) {
            if (target == current.value) return true;
            current = (target < current.value) ? current.left : current.right;
        }
        return false;
    }

    // ==========================================
    // BUG 2: Inorder Traversal Incorrect Order
    // ==========================================
    static void inorderBroken(LabNode node, List<Integer> list) {
        if (node == null) return;
        // BUG: Preorder order instead of left-node-right
        list.add(node.value);
        inorderBroken(node.left, list);
        inorderBroken(node.right, list);
    }

    static void inorderFixed(LabNode node, List<Integer> list) {
        if (node == null) return;
        inorderFixed(node.left, list);
        list.add(node.value);
        inorderFixed(node.right, list);
    }

    // ==========================================
    // BUG 3: Delete Loses Child Subtree
    // ==========================================
    static LabNode deleteBroken(LabNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = deleteBroken(node.left, target);
        } else if (target > node.value) {
            node.right = deleteBroken(node.right, target);
        } else {
            // BUG: in single-child case, returns null (cuts off the child subtree)
            if (node.left == null) return null; 
            if (node.right == null) return null;
            
            LabNode successor = getMin(node.right);
            node.value = successor.value;
            node.right = deleteBroken(node.right, successor.value);
        }
        return node;
    }

    static LabNode deleteFixed(LabNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = deleteFixed(node.left, target);
        } else if (target > node.value) {
            node.right = deleteFixed(node.right, target);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            LabNode successor = getMin(node.right);
            node.value = successor.value;
            node.right = deleteFixed(node.right, successor.value);
        }
        return node;
    }

    private static LabNode getMin(LabNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ==========================================
    // BUG 4: Validation Only Checks Direct Child
    // ==========================================
    static boolean validateBroken(LabNode node) {
        if (node == null) return true;
        // BUG: Only check parent-child relationship locally
        if (node.left != null && node.left.value >= node.value) return false;
        if (node.right != null && node.right.value <= node.value) return false;
        return validateBroken(node.left) && validateBroken(node.right);
    }

    static boolean validateFixed(LabNode node, long min, long max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) return false;
        return validateFixed(node.left, min, node.value) && validateFixed(node.right, node.value, max);
    }

    // Helper to print inorder traversal list
    static List<Integer> getInorderList(LabNode root, boolean fixed) {
        List<Integer> list = new ArrayList<>();
        if (fixed) inorderFixed(root, list);
        else inorderBroken(root, list);
        return list;
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("               BST BUG LAB DEMO                  ");
        System.out.println("=================================================");

        // --- Lab 1: Reversed Search Direction ---
        System.out.println("Lab 1: Reversed Search Direction");
        LabNode root1 = new LabNode(50);
        root1.left = new LabNode(30);
        root1.right = new LabNode(70);
        System.out.println("  Symptom (Broken search for 30): " + searchBroken(root1, 30));
        System.out.println("  Repair  (Fixed search for 30):  " + searchFixed(root1, 30));
        System.out.println();

        // --- Lab 2: Incorrect Inorder Traversal ---
        System.out.println("Lab 2: Incorrect Inorder Traversal (Order Violation)");
        LabNode root2 = new LabNode(50);
        root2.left = new LabNode(30);
        root2.right = new LabNode(70);
        System.out.println("  Symptom (Broken inorder): " + getInorderList(root2, false));
        System.out.println("  Repair  (Fixed inorder):  " + getInorderList(root2, true));
        System.out.println();

        // --- Lab 3: Single-child Deletion Subtree Loss ---
        System.out.println("Lab 3: Single-child Deletion Subtree Loss");
        // Tree: 50 -> left 30 -> right 40
        LabNode root3B = new LabNode(50);
        root3B.left = new LabNode(30);
        root3B.left.right = new LabNode(40);
        deleteBroken(root3B, 30);
        System.out.println("  Symptom (Broken delete 30): Inorder is " + getInorderList(root3B, true) + " (Lost node 40!)");

        LabNode root3F = new LabNode(50);
        root3F.left = new LabNode(30);
        root3F.left.right = new LabNode(40);
        deleteFixed(root3F, 30);
        System.out.println("  Repair  (Fixed delete 30):  Inorder is " + getInorderList(root3F, true) + " (Retained node 40!)");
        System.out.println();

        // --- Lab 4: Parent-Child Only Validation ---
        System.out.println("Lab 4: Local parent-child vs Global Validation");
        // Tree: 50 -> left 30 -> right 60
        // (60 is right child of 30, locally OK. But 60 is in left subtree of 50, globally INVALID).
        LabNode root4 = new LabNode(50);
        root4.left = new LabNode(30);
        root4.left.right = new LabNode(60);
        System.out.println("  Symptom (Broken validator): Tree is valid? " + validateBroken(root4));
        System.out.println("  Repair  (Fixed validator):  Tree is valid? " + validateFixed(root4, Long.MIN_VALUE, Long.MAX_VALUE));
        System.out.println("=================================================");
    }
}
