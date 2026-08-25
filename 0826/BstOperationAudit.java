import java.util.ArrayList;
import java.util.List;

class AuditNode {
    int value;
    AuditNode left;
    AuditNode right;

    AuditNode(int value) {
        this.value = value;
    }
}

class AuditBst {
    AuditNode root;

    boolean add(int value) {
        if (root == null) {
            root = new AuditNode(value);
            return true;
        }
        AuditNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new AuditNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new AuditNode(value);
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

    private AuditNode remove(AuditNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            AuditNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private AuditNode minimumNode(AuditNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    boolean contains(int value) {
        AuditNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    int size() {
        return size(root);
    }

    private int size(AuditNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(AuditNode node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(AuditNode node, long min, long max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) return false;
        return isValid(node.left, min, node.value) && isValid(node.right, node.value, max);
    }

    List<Integer> inorder() {
        List<Integer> list = new ArrayList<>();
        inorder(root, list);
        return list;
    }

    private void inorder(AuditNode node, List<Integer> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.value);
        inorder(node.right, list);
    }
}

public class BstOperationAudit {
    private static AuditBst tree = new AuditBst();

    private static void auditAdd(int value) {
        boolean success = tree.add(value);
        printAudit("Add(" + value + ")", success);
    }

    private static void auditRemove(int value) {
        boolean success = tree.remove(value);
        printAudit("Remove(" + value + ")", success);
    }

    private static void printAudit(String action, boolean success) {
        System.out.printf("%-12s | Status: %-5b | Inorder: %-25s | Size: %d | Height: %2d | Valid: %b\n",
                action, success, tree.inorder(), tree.size(), tree.height(), tree.isValid());
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println("                               BST OPERATION AUDIT REPORT                                 ");
        System.out.println("==========================================================================================");
        
        // 1. Insert standard keys
        auditAdd(50);
        auditAdd(30);
        auditAdd(70);
        auditAdd(20);
        auditAdd(40);
        auditAdd(60);
        auditAdd(80);

        System.out.println("------------------------------------------------------------------------------------------");
        
        // 2. Duplicate Insert Test
        auditAdd(30);

        // 3. Missing Delete Test
        auditRemove(99);

        System.out.println("------------------------------------------------------------------------------------------");

        // 4. Deleting Leaf Case (20)
        auditRemove(20);

        // 5. Deleting Single-Child Case (30) (it now has only child 40)
        auditRemove(30);

        // 6. Deleting Two-Children Case (50) (Root)
        auditRemove(50);
        
        System.out.println("==========================================================================================");
    }
}
