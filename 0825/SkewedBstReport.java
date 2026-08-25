class SkewNode {
    int value;
    SkewNode left;
    SkewNode right;

    SkewNode(int value) {
        this.value = value;
    }
}

class SkewBst {
    SkewNode root;

    void add(int value) {
        if (root == null) {
            root = new SkewNode(value);
            return;
        }
        SkewNode current = root;
        while (true) {
            if (value == current.value) return;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new SkewNode(value);
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new SkewNode(value);
                    return;
                }
                current = current.right;
            }
        }
    }

    int size() {
        return size(root);
    }

    private int size(SkewNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(SkewNode node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    int searchComparisonCount(int target) {
        SkewNode current = root;
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

public class SkewedBstReport {
    public static void main(String[] args) {
        int[] sortedData = {10, 20, 30, 40, 50, 60, 70};
        int[] balancedData = {40, 20, 60, 10, 30, 50, 70};

        SkewBst skewedTree = new SkewBst();
        for (int val : sortedData) {
            skewedTree.add(val);
        }

        SkewBst balancedTree = new SkewBst();
        for (int val : balancedData) {
            balancedTree.add(val);
        }

        System.out.println("=================================================");
        System.out.println("            BST SHAPE COMPARISON REPORT          ");
        System.out.println("=================================================");
        System.out.printf("%-20s | %-12s | %-12s\n", "Metric", "Skewed Tree", "Balanced Tree");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-20s | %-12d | %-12d\n", "Size", skewedTree.size(), balancedTree.size());
        System.out.printf("%-20s | %-12d | %-12d\n", "Height", skewedTree.height(), balancedTree.height());
        System.out.println("=================================================");

        System.out.println("\nSearch Comparison Count for Existing Keys:");
        System.out.printf("%-10s | %-18s | %-18s\n", "Key", "Skewed Comparisons", "Balanced Comparisons");
        System.out.println("-------------------------------------------------");
        int totalSkewedComp = 0;
        int totalBalancedComp = 0;
        for (int key : sortedData) {
            int sc = skewedTree.searchComparisonCount(key);
            int bc = balancedTree.searchComparisonCount(key);
            totalSkewedComp += sc;
            totalBalancedComp += bc;
            System.out.printf("%-10d | %-18d | %-18d\n", key, sc, bc);
        }
        System.out.println("-------------------------------------------------");
        System.out.printf("%-10s | %-18.2f | %-18.2f\n", "Average", 
                          (double) totalSkewedComp / sortedData.length, 
                          (double) totalBalancedComp / sortedData.length);
        System.out.println("=================================================");

        System.out.println("\nSearch Comparison Count for Missing Keys (e.g. 5, 45, 75):");
        int[] missingKeys = {5, 45, 75};
        for (int key : missingKeys) {
            System.out.printf("Search %-2d -> Skewed: %d comparisons, Balanced: %d comparisons\n", 
                              key, skewedTree.searchComparisonCount(key), balancedTree.searchComparisonCount(key));
        }
    }
}
