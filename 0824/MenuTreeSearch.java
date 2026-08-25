class MenuNode {
    String name;
    MenuNode left;
    MenuNode right;

    MenuNode(String name) {
        this.name = name;
    }
}

public class MenuTreeSearch {
    public static boolean contains(MenuNode node, String target) {
        if (node == null || target == null) return false;
        if (node.name.equals(target)) return true;
        return contains(node.left, target) || contains(node.right, target);
    }

    public static int findDepth(MenuNode root, String target) {
        return findDepthHelper(root, target, 0);
    }

    private static int findDepthHelper(MenuNode node, String target, int currentDepth) {
        if (node == null || target == null) return -1;
        if (node.name.equals(target)) return currentDepth;
        int leftDepth = findDepthHelper(node.left, target, currentDepth + 1);
        if (leftDepth != -1) return leftDepth;
        return findDepthHelper(node.right, target, currentDepth + 1);
    }

    public static int countLeaves(MenuNode node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return countLeaves(node.left) + countLeaves(node.right);
    }

    public static void preorderDisplay(MenuNode node, int indent) {
        if (node == null) return;
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        System.out.println("- " + node.name);
        preorderDisplay(node.left, indent + 1);
        preorderDisplay(node.right, indent + 1);
    }

    public static void main(String[] args) {
        // Build a Menu Tree:
        //             Main Menu
        //            /         \
        //        Drinks        Foods
        //        /    \        /    \
        //     Coffee  Tea   Burger Pizza
        MenuNode root = new MenuNode("Main Menu");
        root.left = new MenuNode("Drinks");
        root.left.left = new MenuNode("Coffee");
        root.left.right = new MenuNode("Tea");
        
        root.right = new MenuNode("Foods");
        root.right.left = new MenuNode("Burger");
        root.right.right = new MenuNode("Pizza");

        System.out.println("Menu Tree Display:");
        preorderDisplay(root, 0);
        System.out.println();

        System.out.println("Contains 'Coffee': " + contains(root, "Coffee"));
        System.out.println("Contains 'Pasta': " + contains(root, "Pasta"));
        System.out.println();

        System.out.println("Depth of 'Main Menu': " + findDepth(root, "Main Menu"));
        System.out.println("Depth of 'Drinks': " + findDepth(root, "Drinks"));
        System.out.println("Depth of 'Pizza': " + findDepth(root, "Pizza"));
        System.out.println("Depth of 'Pasta': " + findDepth(root, "Pasta"));
        System.out.println();

        System.out.println("Leaf Count of Menu Tree: " + countLeaves(root));
    }
}
