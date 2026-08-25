class ShapeNode {
    int value;
    ShapeNode left;
    ShapeNode right;

    ShapeNode(int value) {
        this.value = value;
    }
}

class ShapeBst {
    ShapeNode root;

    void add(int value) {
        if (root == null) {
            root = new ShapeNode(value);
            return;
        }
        ShapeNode current = root;
        while (true) {
            if (value == current.value) return;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new ShapeNode(value);
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ShapeNode(value);
                    return;
                }
                current = current.right;
            }
        }
    }

    int size() {
        return size(root);
    }

    private int size(ShapeNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(ShapeNode node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    int searchComparisonCount(int target) {
        ShapeNode current = root;
        int count = 0;
        while (current != null) {
            count++;
            if (target == current.value) {
                return count;
            }
            current = target < current.value ? current.left : current.right;
        }
        return count;
    }
}

public class BstShapeExperiment {
    public static void main(String[] args) {
        int[] sortedOrder = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] balancedOrder = {8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15};
        int[] randomOrder = {7, 12, 2, 14, 5, 9, 1, 11, 15, 3, 8, 13, 4, 6, 10};

        ShapeBst sortedTree = new ShapeBst();
        for (int v : sortedOrder) sortedTree.add(v);

        ShapeBst balancedTree = new ShapeBst();
        for (int v : balancedOrder) balancedTree.add(v);

        ShapeBst randomTree = new ShapeBst();
        for (int v : randomOrder) randomTree.add(v);

        // Calculate comparison counts
        int sortedTotal = 0;
        int balancedTotal = 0;
        int randomTotal = 0;

        for (int i = 1; i <= 15; i++) {
            sortedTotal += sortedTree.searchComparisonCount(i);
            balancedTotal += balancedTree.searchComparisonCount(i);
            randomTotal += randomTree.searchComparisonCount(i);
        }

        System.out.println("=========================================================");
        System.out.println("            BST SHAPE EXPERIMENT REPORT                  ");
        System.out.println("=========================================================");
        System.out.printf("%-18s | %-10s | %-10s | %-12s\n", "Insertion Order", "Size", "Height", "Total Search Comp");
        System.out.println("---------------------------------------------------------");
        System.out.printf("%-18s | %-10d | %-10d | %-12d\n", "Sorted (Skewed)", sortedTree.size(), sortedTree.height(), sortedTotal);
        System.out.printf("%-18s | %-10d | %-10d | %-12d\n", "Balanced", balancedTree.size(), balancedTree.height(), balancedTotal);
        System.out.printf("%-18s | %-10d | %-10d | %-12d\n", "Random", randomTree.size(), randomTree.height(), randomTotal);
        System.out.println("=========================================================");
        System.out.println("\nAverage comparisons per search:");
        System.out.printf("  - Sorted Tree:   %.2f comparisons\n", (double) sortedTotal / 15);
        System.out.printf("  - Balanced Tree: %.2f comparisons\n", (double) balancedTotal / 15);
        System.out.printf("  - Random Tree:   %.2f comparisons\n", (double) randomTotal / 15);
        System.out.println("=========================================================");
    }
}
