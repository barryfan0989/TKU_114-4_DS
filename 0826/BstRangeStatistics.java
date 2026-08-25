import java.util.ArrayList;
import java.util.List;

class StatsNode {
    int value;
    StatsNode left;
    StatsNode right;

    StatsNode(int value) {
        this.value = value;
    }
}

class StatsBst {
    StatsNode root;

    void add(int value) {
        if (root == null) {
            root = new StatsNode(value);
            return;
        }
        StatsNode current = root;
        while (true) {
            if (value == current.value) return;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new StatsNode(value);
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new StatsNode(value);
                    return;
                }
                current = current.right;
            }
        }
    }

    List<Integer> valuesBetween(int low, int high) {
        List<Integer> list = new ArrayList<>();
        if (low > high) {
            System.out.println("  [Warning] Invalid range: low (" + low + ") > high (" + high + ")");
            return list;
        }
        valuesBetween(root, low, high, list);
        return list;
    }

    private void valuesBetween(StatsNode node, int low, int high, List<Integer> list) {
        if (node == null) return;
        if (node.value > low) {
            valuesBetween(node.left, low, high, list);
        }
        if (node.value >= low && node.value <= high) {
            list.add(node.value);
        }
        if (node.value < high) {
            valuesBetween(node.right, low, high, list);
        }
    }

    int countBetween(int low, int high) {
        if (low > high) {
            return 0;
        }
        return countBetween(root, low, high);
    }

    private int countBetween(StatsNode node, int low, int high) {
        if (node == null) return 0;
        int count = 0;
        if (node.value > low) {
            count += countBetween(node.left, low, high);
        }
        if (node.value >= low && node.value <= high) {
            count++;
        }
        if (node.value < high) {
            count += countBetween(node.right, low, high);
        }
        return count;
    }

    int sumBetween(int low, int high) {
        if (low > high) {
            return 0;
        }
        return sumBetween(root, low, high);
    }

    private int sumBetween(StatsNode node, int low, int high) {
        if (node == null) return 0;
        int sum = 0;
        if (node.value > low) {
            sum += sumBetween(node.left, low, high);
        }
        if (node.value >= low && node.value <= high) {
            sum += node.value;
        }
        if (node.value < high) {
            sum += sumBetween(node.right, low, high);
        }
        return sum;
    }
}

public class BstRangeStatistics {
    public static void main(String[] args) {
        StatsBst tree = new StatsBst();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int val : values) {
            tree.add(val);
        }

        System.out.println("BST Values: [20, 30, 40, 50, 60, 70, 80]\n");

        // Test 1: Standard range [30, 70]
        System.out.println("=== Test 1: Range [30, 70] ===");
        System.out.println("Values: " + tree.valuesBetween(30, 70));
        System.out.println("Count:  " + tree.countBetween(30, 70));
        System.out.println("Sum:    " + tree.sumBetween(30, 70));
        System.out.println();

        // Test 2: Empty range [35, 38]
        System.out.println("=== Test 2: Range [35, 38] (No elements) ===");
        System.out.println("Values: " + tree.valuesBetween(35, 38));
        System.out.println("Count:  " + tree.countBetween(35, 38));
        System.out.println("Sum:    " + tree.sumBetween(35, 38));
        System.out.println();

        // Test 3: Invalid range [70, 30]
        System.out.println("=== Test 3: Range [70, 30] (low > high) ===");
        System.out.println("Values: " + tree.valuesBetween(70, 30));
        System.out.println("Count:  " + tree.countBetween(70, 30));
        System.out.println("Sum:    " + tree.sumBetween(70, 30));
        System.out.println();
    }
}
