class CountNode {
    int value;
    int count;
    CountNode left;
    CountNode right;

    CountNode(int value) {
        this.value = value;
        this.count = 1;
    }
}

class CountBst {
    CountNode root;

    void add(int value) {
        if (root == null) {
            root = new CountNode(value);
            return;
        }
        CountNode current = root;
        while (true) {
            if (value == current.value) {
                current.count++;
                return;
            }
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new CountNode(value);
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CountNode(value);
                    return;
                }
                current = current.right;
            }
        }
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(CountNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + "(" + node.count + ") ");
        inorder(node.right);
    }
}

public class BstDuplicateCounter {
    public static void main(String[] args) {
        CountBst tree = new CountBst();
        int[] values = {50, 30, 70, 30, 50, 30, 20, 70};
        System.out.println("Inserting elements in order:");
        for (int val : values) {
            System.out.println("Adding: " + val);
            tree.add(val);
        }
        
        System.out.print("\nInorder Traversal with counts: ");
        tree.inorder();
    }
}
