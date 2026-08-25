class CompareNode {
    int value;
    CompareNode left;
    CompareNode right;

    CompareNode(int value) {
        this.value = value;
    }
}

class CompareBst {
    CompareNode root;

    void add(int value) {
        if (root == null) {
            root = new CompareNode(value);
            return;
        }
        CompareNode current = root;
        while (true) {
            if (value == current.value) return;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new CompareNode(value);
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CompareNode(value);
                    return;
                }
                current = current.right;
            }
        }
    }

    int size() {
        return size(root);
    }

    private int size(CompareNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(CompareNode node) {
        return node == null ? -1 : 1 + Math.max(height(node.left), height(node.right));
    }

    int searchComparisonCount(int target) {
        CompareNode current = root;
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

public class TreeShapeComparison {
    public static void main(String[] args) {
        int[] sortedAsc = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150};
        int[] sortedDesc = {150, 140, 130, 120, 110, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10};
        int[] balanced = {80, 40, 120, 20, 60, 100, 140, 10, 30, 50, 70, 90, 110, 130, 150};

        CompareBst treeAsc = new CompareBst();
        for (int v : sortedAsc) treeAsc.add(v);

        CompareBst treeDesc = new CompareBst();
        for (int v : sortedDesc) treeDesc.add(v);

        CompareBst treeBal = new CompareBst();
        for (int v : balanced) treeBal.add(v);

        // Sum search comparisons for all 15 elements
        int totalAsc = 0;
        int totalDesc = 0;
        int totalBal = 0;

        for (int val : sortedAsc) {
            totalAsc += treeAsc.searchComparisonCount(val);
            totalDesc += treeDesc.searchComparisonCount(val);
            totalBal += treeBal.searchComparisonCount(val);
        }

        System.out.println("========================================================================");
        System.out.println("                 BST TREE SHAPE COMPARISON REPORT                       ");
        System.out.println("========================================================================");
        System.out.printf("%-20s | %-8s | %-8s | %-16s | %-16s\n", 
                          "Tree Shape", "Size", "Height", "Total Search Comp", "Avg Search Comp");
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-20s | %-8d | %-8d | %-16d | %-16.2f\n", 
                          "Sorted Ascending", treeAsc.size(), treeAsc.height(), totalAsc, (double) totalAsc / 15);
        System.out.printf("%-20s | %-8d | %-8d | %-16d | %-16.2f\n", 
                          "Sorted Descending", treeDesc.size(), treeDesc.height(), totalDesc, (double) totalDesc / 15);
        System.out.printf("%-20s | %-8d | %-8d | %-16d | %-16.2f\n", 
                          "Balanced", treeBal.size(), treeBal.height(), totalBal, (double) totalBal / 15);
        System.out.println("========================================================================");

        System.out.println("\nSearch Comparisons for Missing Keys:");
        System.out.printf("%-10s | %-18s | %-18s | %-18s\n", "Missing Key", "Ascending Tree", "Descending Tree", "Balanced Tree");
        System.out.println("------------------------------------------------------------------------");
        int[] missingKeys = {5, 85, 155};
        for (int key : missingKeys) {
            System.out.printf("%-11d | %-18d | %-18d | %-18d\n", 
                              key, 
                              treeAsc.searchComparisonCount(key), 
                              treeDesc.searchComparisonCount(key), 
                              treeBal.searchComparisonCount(key));
        }
        System.out.println("========================================================================");
    }
}
