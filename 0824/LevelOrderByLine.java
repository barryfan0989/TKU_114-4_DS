import java.util.ArrayDeque;
import java.util.Queue;

class LevelNode {
    String value;
    LevelNode left;
    LevelNode right;

    LevelNode(String value) {
        this.value = value;
    }
}

public class LevelOrderByLine {
    public static void levelOrderByLine(LevelNode root) {
        if (root == null) {
            System.out.println("Empty Tree");
            return;
        }

        Queue<LevelNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 1;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            System.out.print("Level " + level + " (count=" + levelSize + "): ");
            for (int i = 0; i < levelSize; i++) {
                LevelNode current = queue.poll();
                System.out.print(current.value + " ");
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            System.out.println();
            level++;
        }
    }

    public static void main(String[] args) {
        // Construct a sample binary tree:
        //          A
        //        /   \
        //       B     C
        //      / \     \
        //     D   E     F
        LevelNode root = new LevelNode("A");
        root.left = new LevelNode("B");
        root.right = new LevelNode("C");
        root.left.left = new LevelNode("D");
        root.left.right = new LevelNode("E");
        root.right.right = new LevelNode("F");

        System.out.println("Level-order traversal by line:");
        levelOrderByLine(root);
        System.out.println();

        System.out.println("Testing Empty Tree:");
        levelOrderByLine(null);
    }
}
