class FsNode {
    String name;
    boolean isDirectory;
    int size;
    FsNode left;
    FsNode right;

    // Constructor for directories
    FsNode(String name) {
        this.name = name;
        this.isDirectory = true;
        this.size = 0;
    }

    // Constructor for files
    FsNode(String name, int size) {
        this.name = name;
        this.isDirectory = false;
        this.size = size;
    }
}

public class DirectoryTreeReport {

    // Postorder traversal to calculate directory sizes based on child subtrees
    static int calculateSizes(FsNode node) {
        if (node == null) return 0;
        int leftSize = calculateSizes(node.left);
        int rightSize = calculateSizes(node.right);
        if (node.isDirectory) {
            node.size = leftSize + rightSize;
        }
        return node.size;
    }

    static int countNodes(FsNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    static int countFiles(FsNode node) {
        if (node == null) return 0;
        int count = node.isDirectory ? 0 : 1;
        return count + countFiles(node.left) + countFiles(node.right);
    }

    static int countDirectories(FsNode node) {
        if (node == null) return 0;
        int count = node.isDirectory ? 1 : 0;
        return count + countDirectories(node.left) + countDirectories(node.right);
    }

    static int height(FsNode node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    static FsNode findMaxFile(FsNode node) {
        if (node == null) return null;
        FsNode maxNode = node.isDirectory ? null : node;
        
        FsNode leftMax = findMaxFile(node.left);
        FsNode rightMax = findMaxFile(node.right);
        
        if (leftMax != null) {
            if (maxNode == null || leftMax.size > maxNode.size) {
                maxNode = leftMax;
            }
        }
        if (rightMax != null) {
            if (maxNode == null || rightMax.size > maxNode.size) {
                maxNode = rightMax;
            }
        }
        return maxNode;
    }

    static void printTree(FsNode node, int depth) {
        if (node == null) return;
        String indent = "  ".repeat(depth);
        if (node.isDirectory) {
            System.out.println(indent + "📁 " + node.name + "/ [Size: " + node.size + " bytes]");
        } else {
            System.out.println(indent + "📄 " + node.name + " (" + node.size + " bytes)");
        }
        printTree(node.left, depth + 1);
        printTree(node.right, depth + 1);
    }

    public static void main(String[] args) {
        // Construct Binary Directory Structure
        //             root (dir)
        //            /          \
        //        src (dir)      resources (dir)
        //        /      \        /           \
        //  Main.java  utils(dir) config.json logo.png
        //             /        \
        //         Logger.java  Parser.java
        
        FsNode root = new FsNode("root");
        
        root.left = new FsNode("src");
        root.left.left = new FsNode("Main.java", 1500);
        root.left.right = new FsNode("utils");
        root.left.right.left = new FsNode("Logger.java", 900);
        root.left.right.right = new FsNode("Parser.java", 3500);
        
        root.right = new FsNode("resources");
        root.right.left = new FsNode("config.json", 450);
        root.right.right = new FsNode("logo.png", 12000);

        // Compute directory sizes
        calculateSizes(root);

        System.out.println("=================================================");
        System.out.println("            FILE SYSTEM STRUCTURE                ");
        System.out.println("=================================================");
        printTree(root, 0);
        System.out.println("=================================================");

        System.out.println("\n=================================================");
        System.out.println("            FILE SYSTEM STATISTICS               ");
        System.out.println("=================================================");
        System.out.println("  Total Nodes:       " + countNodes(root));
        System.out.println("  File Count:        " + countFiles(root));
        System.out.println("  Directory Count:   " + countDirectories(root));
        System.out.println("  Tree Height:       " + height(root));
        
        FsNode maxFile = findMaxFile(root);
        if (maxFile != null) {
            System.out.println("  Largest File:      " + maxFile.name + " (" + maxFile.size + " bytes)");
        } else {
            System.out.println("  Largest File:      None");
        }
        System.out.println("=================================================");
    }
}
