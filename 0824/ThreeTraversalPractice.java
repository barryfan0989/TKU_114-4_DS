class CharNode {
    char value;
    CharNode left;
    CharNode right;

    CharNode(char value) {
        this.value = value;
    }
}

public class ThreeTraversalPractice {
    public static void preorder(CharNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + " ");
        preorder(node.left);
        preorder(node.right);
    }

    public static void inorder(CharNode node) {
        if (node == null) {
            return;
        }
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }

    public static void postorder(CharNode node) {
        if (node == null) {
            return;
        }
        postorder(node.left);
        postorder(node.right);
        System.out.print(node.value + " ");
    }

    public static void main(String[] args) {
        // Construct the tree: M(F(B,null), T(R,Z))
        CharNode root = new CharNode('M');
        root.left = new CharNode('F');
        root.left.left = new CharNode('B');
        
        root.right = new CharNode('T');
        root.right.left = new CharNode('R');
        root.right.right = new CharNode('Z');

        System.out.print("Preorder: ");
        preorder(root);
        System.out.println();

        System.out.print("Inorder: ");
        inorder(root);
        System.out.println();

        System.out.print("Postorder: ");
        postorder(root);
        System.out.println();
    }
}
