class ScoreRecord implements Comparable<ScoreRecord> {
    int score;
    String studentId;
    String name;

    ScoreRecord(int score, String studentId, String name) {
        this.score = score;
        this.studentId = studentId;
        this.name = name;
    }

    @Override
    public int compareTo(ScoreRecord other) {
        if (this.score != other.score) {
            return Integer.compare(this.score, other.score);
        }
        return this.studentId.compareTo(other.studentId);
    }

    @Override
    public String toString() {
        return "Score: " + score + " | ID: " + studentId + " | Name: " + name;
    }
}

class ScoreNode {
    ScoreRecord record;
    ScoreNode left;
    ScoreNode right;

    ScoreNode(ScoreRecord record) {
        this.record = record;
    }
}

class ScoreBst {
    ScoreNode root;

    boolean insert(ScoreRecord record) {
        if (record == null) return false;
        if (root == null) {
            root = new ScoreNode(record);
            return true;
        }
        ScoreNode current = root;
        while (true) {
            int cmp = record.compareTo(current.record);
            if (cmp == 0) {
                return false; // Duplicate composite key (same score and ID)
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new ScoreNode(record);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ScoreNode(record);
                    return true;
                }
                current = current.right;
            }
        }
    }

    void inorder() {
        inorder(root);
    }

    private void inorder(ScoreNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.record);
        inorder(node.right);
    }

    void printRange(int lowScore, int highScore) {
        System.out.println("--- Students with Scores in Range [" + lowScore + ", " + highScore + "] ---");
        if (lowScore > highScore) {
            System.out.println("  [Error] Invalid range: lowScore > highScore");
            return;
        }
        printRange(root, lowScore, highScore);
        System.out.println();
    }

    private void printRange(ScoreNode node, int lowScore, int highScore) {
        if (node == null) return;
        
        // Traverse left subtree if there could be scores >= lowScore
        if (node.record.score >= lowScore) {
            printRange(node.left, lowScore, highScore);
        }
        
        // Check current node
        if (node.record.score >= lowScore && node.record.score <= highScore) {
            System.out.println("  " + node.record);
        }
        
        // Traverse right subtree if there could be scores <= highScore
        if (node.record.score <= highScore) {
            printRange(node.right, lowScore, highScore);
        }
    }
}

public class ScoreRangeBst {
    public static void main(String[] args) {
        ScoreBst tree = new ScoreBst();

        System.out.println("=== 1. Inserting Score Records ===");
        tree.insert(new ScoreRecord(90, "S01", "Alice"));
        tree.insert(new ScoreRecord(85, "S02", "Bob"));
        tree.insert(new ScoreRecord(90, "S03", "Charlie"));
        tree.insert(new ScoreRecord(85, "S04", "David"));
        tree.insert(new ScoreRecord(75, "S05", "Eva"));
        tree.insert(new ScoreRecord(95, "S06", "Frank"));
        tree.insert(new ScoreRecord(85, "S07", "Grace"));

        System.out.println("All Score Records Inorder:");
        tree.inorder();
        System.out.println();

        System.out.println("=== 2. Range Queries ===");
        // Range 80 to 90
        tree.printRange(80, 90);

        // Range 90 to 100
        tree.printRange(90, 100);

        // Range 70 to 80
        tree.printRange(70, 80);

        // Range 90 to 80 (Invalid)
        tree.printRange(90, 80);
    }
}
