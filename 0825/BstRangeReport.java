class RangeNode {
    int value;
    RangeNode left;
    RangeNode right;

    RangeNode(int value) {
        this.value = value;
    }
}

class RangeBst {
    RangeNode root;

    void add(int value) {
        if (root == null) {
            root = new RangeNode(value);
            return;
        }
        RangeNode current = root;
        while (true) {
            if (value == current.value) return;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new RangeNode(value);
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new RangeNode(value);
                    return;
                }
                current = current.right;
            }
        }
    }

    Integer minimum() {
        if (root == null) return null;
        RangeNode current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        RangeNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    void printRange(int low, int high) {
        System.out.println("Range [" + low + ", " + high + "]:");
        if (low > high) {
            System.out.println("  [Error] Invalid range: low (" + low + ") > high (" + high + ")");
            return;
        }
        printRange(root, low, high);
        System.out.println();
    }

    private void printRange(RangeNode node, int low, int high) {
        if (node == null) return;
        if (node.value > low) {
            printRange(node.left, low, high);
        }
        if (node.value >= low && node.value <= high) {
            System.out.print(node.value + " ");
        }
        if (node.value < high) {
            printRange(node.right, low, high);
        }
    }
}

public class BstRangeReport {
    public static void main(String[] args) {
        RangeBst tree = new RangeBst();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int val : values) {
            tree.add(val);
        }

        System.out.println("BST Minimum: " + tree.minimum());
        System.out.println("BST Maximum: " + tree.maximum());
        System.out.println();

        // Normal ranges
        tree.printRange(30, 70);
        tree.printRange(25, 65);
        tree.printRange(10, 100);

        // Invalid range
        tree.printRange(60, 40);
    }
}
