import java.util.ArrayList;
import java.util.List;

class FolderNode {
    String name;
    int ownSize; // size of files in this directory directly
    FolderNode left;
    FolderNode right;

    FolderNode(String name, int ownSize) {
        this.name = name;
        this.ownSize = ownSize;
    }
}

class SubtreeInfo {
    FolderNode node;
    int totalSize;

    SubtreeInfo(FolderNode node, int totalSize) {
        this.node = node;
        this.totalSize = totalSize;
    }
}

public class FolderSizeTree {

    // Recursively computes subtree sizes post-order and populates a list of all subtree sizes.
    public static int calculateSubtreeSizes(FolderNode node, List<SubtreeInfo> allSubtrees) {
        if (node == null) {
            return 0;
        }
        
        // Postorder traversal: compute left, then right, then current
        int leftSize = calculateSubtreeSizes(node.left, allSubtrees);
        int rightSize = calculateSubtreeSizes(node.right, allSubtrees);
        
        int totalSize = node.ownSize + leftSize + rightSize;
        allSubtrees.add(new SubtreeInfo(node, totalSize));
        
        return totalSize;
    }

    public static void collectLeaves(FolderNode node, List<FolderNode> leaves) {
        if (node == null) return;
        if (node.left == null && node.right == null) {
            leaves.add(node);
            return;
        }
        collectLeaves(node.left, leaves);
        collectLeaves(node.right, leaves);
    }

    public static void main(String[] args) {
        // Construct directory structure:
        //              /root (ownSize = 100)
        //             /                     \
        //     /var (ownSize = 50)       /usr (ownSize = 200)
        //     /                  \           /                  \
        //  /log (120)        /lib (80)   /bin (300)          /share (150)
        FolderNode root = new FolderNode("root", 100);
        root.left = new FolderNode("var", 50);
        root.left.left = new FolderNode("log", 120);
        root.left.right = new FolderNode("lib", 80);

        root.right = new FolderNode("usr", 200);
        root.right.left = new FolderNode("bin", 300);
        root.right.right = new FolderNode("share", 150);

        List<SubtreeInfo> allSubtrees = new ArrayList<>();
        int totalSize = calculateSubtreeSizes(root, allSubtrees);

        System.out.println("=== Folder Size Traversal Report ===");
        System.out.println("Total root size: " + totalSize + " KB");
        System.out.println();

        System.out.println("All directory sizes (computed postorder):");
        for (SubtreeInfo info : allSubtrees) {
            System.out.println("  Folder: " + info.node.name 
                               + " (Own size: " + info.node.ownSize + " KB, " 
                               + "Total subtree size: " + info.totalSize + " KB)");
        }
        System.out.println();

        // Find maximum subtree:
        // 1. Overall max (which is root, since sizes are non-negative)
        SubtreeInfo overallMax = null;
        for (SubtreeInfo info : allSubtrees) {
            if (overallMax == null || info.totalSize > overallMax.totalSize) {
                overallMax = info;
            }
        }

        // 2. Maximum non-root subtree (i.e. best sub-folder)
        SubtreeInfo subMax = null;
        for (SubtreeInfo info : allSubtrees) {
            if (info.node != root) {
                if (subMax == null || info.totalSize > subMax.totalSize) {
                    subMax = info;
                }
            }
        }

        if (overallMax != null) {
            System.out.println("Maximum Subtree (Overall): " + overallMax.node.name 
                               + " with total size of " + overallMax.totalSize + " KB");
        }
        if (subMax != null) {
            System.out.println("Maximum Subtree (Excluding root): " + subMax.node.name 
                               + " with total size of " + subMax.totalSize + " KB");
        }
        System.out.println();

        // Get leaf folders
        List<FolderNode> leaves = new ArrayList<>();
        collectLeaves(root, leaves);
        System.out.print("Leaf Folders: ");
        for (int i = 0; i < leaves.size(); i++) {
            System.out.print(leaves.get(i).name + (i == leaves.size() - 1 ? "" : ", "));
        }
        System.out.println();
    }
}
