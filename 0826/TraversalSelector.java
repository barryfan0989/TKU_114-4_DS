class ExpNode {
    String value;
    ExpNode left;
    ExpNode right;

    ExpNode(String value) {
        this.value = value;
    }
}

public class TraversalSelector {

    static String toPrefix(ExpNode node) {
        if (node == null) return "";
        String leftStr = toPrefix(node.left);
        String rightStr = toPrefix(node.right);
        
        StringBuilder sb = new StringBuilder();
        sb.append(node.value);
        if (!leftStr.isEmpty()) sb.append(" ").append(leftStr);
        if (!rightStr.isEmpty()) sb.append(" ").append(rightStr);
        return sb.toString();
    }

    static String toInfix(ExpNode node) {
        if (node == null) return "";
        // If it's a leaf node, just return its value
        if (node.left == null && node.right == null) {
            return node.value;
        }
        // If it's an operator node, bracket the left and right expressions
        return "(" + toInfix(node.left) + " " + node.value + " " + toInfix(node.right) + ")";
    }

    static String toPostfix(ExpNode node) {
        if (node == null) return "";
        String leftStr = toPostfix(node.left);
        String rightStr = toPostfix(node.right);
        
        StringBuilder sb = new StringBuilder();
        if (!leftStr.isEmpty()) sb.append(leftStr).append(" ");
        if (!rightStr.isEmpty()) sb.append(rightStr).append(" ");
        sb.append(node.value);
        return sb.toString();
    }

    public static void main(String[] args) {
        // Construct the expression tree for: (A + B) * (C - (D / E))
        //              *
        //            /   \
        //           +     -
        //          / \   / \
        //         A   B C   /
        //                  / \
        //                 D   E
        ExpNode root = new ExpNode("*");
        root.left = new ExpNode("+");
        root.left.left = new ExpNode("A");
        root.left.right = new ExpNode("B");
        
        root.right = new ExpNode("-");
        root.right.left = new ExpNode("C");
        root.right.right = new ExpNode("/");
        root.right.right.left = new ExpNode("D");
        root.right.right.right = new ExpNode("E");

        System.out.println("Expression Tree Traversals:");
        System.out.println("Prefix (Preorder):   " + toPrefix(root));
        System.out.println("Infix (Inorder):     " + toInfix(root));
        System.out.println("Postfix (Postorder): " + toPostfix(root));
    }
}
