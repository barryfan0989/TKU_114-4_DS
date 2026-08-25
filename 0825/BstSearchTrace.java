class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean searchWithTrace(int target) {
        System.out.println("--- Searching for: " + target + " ---");
        IntNode current = root;
        int count = 0;
        while (current != null) {
            count++;
            System.out.print("Step " + count + ": Current Node = " + current.value + ". ");
            if (target == current.value) {
                System.out.println("Match found!");
                System.out.println("Result: FOUND. Total comparisons: " + count + "\n");
                return true;
            } else if (target < current.value) {
                System.out.println(target + " < " + current.value + " -> Go LEFT.");
                current = current.left;
            } else {
                System.out.println(target + " > " + current.value + " -> Go RIGHT.");
                current = current.right;
            }
        }
        System.out.println("Reached null. Match not found.");
        System.out.println("Result: NOT FOUND. Total comparisons: " + count + "\n");
        return false;
    }
}

public class BstSearchTrace {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        System.out.print("Inserting values: ");
        for (int value : values) {
            tree.add(value);
            System.out.print(value + " ");
        }
        System.out.println("\n");

        // 1. Search for root
        tree.searchWithTrace(50);

        // 2. Search for leaf
        tree.searchWithTrace(20);

        // 3. Search for internal node
        tree.searchWithTrace(30);

        // 4. Search for missing value
        tree.searchWithTrace(65);
    }
}
