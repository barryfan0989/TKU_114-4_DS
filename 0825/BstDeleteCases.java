class DeleteNode {
    int value;
    DeleteNode left;
    DeleteNode right;

    DeleteNode(int value) {
        this.value = value;
    }
}

class DeleteBst {
    DeleteNode root;

    boolean add(int value) {
        if (root == null) {
            root = new DeleteNode(value);
            return true;
        }
        DeleteNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new DeleteNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new DeleteNode(value);
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

    private DeleteNode remove(DeleteNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            // Case 1 & 2: Leaf or single child
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Case 3: Two children
            DeleteNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private DeleteNode minimumNode(DeleteNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    boolean contains(int value) {
        DeleteNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    int size() {
        return size(root);
    }

    private int size(DeleteNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(DeleteNode node, long min, long max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) return false;
        return isValid(node.left, min, node.value) && isValid(node.right, node.value, max);
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(DeleteNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }
}

public class BstDeleteCases {
    public static void main(String[] args) {
        DeleteBst tree = new DeleteBst();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        System.out.print("Inserting values: ");
        for (int val : values) {
            tree.add(val);
            System.out.print(val + " ");
        }
        System.out.println("\n");

        System.out.println("=== Initial Tree ===");
        System.out.print("Inorder: "); tree.inorder();
        System.out.println("Size: " + tree.size());
        System.out.println("Valid BST: " + tree.isValid());
        System.out.println();

        // 1. Delete Leaf Node (20)
        System.out.println("=== Deleting Leaf Node (20) ===");
        tree.remove(20);
        System.out.print("Inorder: "); tree.inorder();
        System.out.println("Size: " + tree.size());
        System.out.println("Valid BST: " + tree.isValid());
        System.out.println();

        // 2. Delete Single-Child Node (30) (now only has child 40)
        System.out.println("=== Deleting Single-Child Node (30) ===");
        tree.remove(30);
        System.out.print("Inorder: "); tree.inorder();
        System.out.println("Size: " + tree.size());
        System.out.println("Valid BST: " + tree.isValid());
        System.out.println();

        // 3. Delete Two-Children Node (50 - Root)
        System.out.println("=== Deleting Two-Children Node (50 - Root) ===");
        tree.remove(50);
        System.out.print("Inorder: "); tree.inorder();
        System.out.println("Size: " + tree.size());
        System.out.println("Valid BST: " + tree.isValid());
        System.out.println();
    }
}
