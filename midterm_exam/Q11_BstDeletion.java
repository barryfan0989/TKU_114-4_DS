import java.util.ArrayList;
import java.util.List;

public class Q11_BstDeletion {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    public boolean add(int value) {
        if (contains(value)) {
            return false;
        }
        root = addHelper(root, value);
        size++;
        return true;
    }

    private Node addHelper(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }
        if (value < node.value) {
            node.left = addHelper(node.left, value);
        } else if (value > node.value) {
            node.right = addHelper(node.right, value);
        }
        return node;
    }

    public boolean remove(int value) {
        if (!contains(value)) {
            return false;
        }
        root = removeHelper(root, value);
        size--;
        return true;
    }

    private Node removeHelper(Node node, int value) {
        if (node == null) {
            return null;
        }
        if (value < node.value) {
            node.left = removeHelper(node.left, value);
        } else if (value > node.value) {
            node.right = removeHelper(node.right, value);
        } else {
            // Found node to delete
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            // Node with two children: replace with right subtree minimum (inorder
            // successor)
            node.value = findMin(node.right);
            node.right = removeHelper(node.right, node.value);
        }
        return node;
    }

    private int findMin(Node node) {
        int min = node.value;
        while (node.left != null) {
            min = node.left.value;
            node = node.left;
        }
        return min;
    }

    public boolean contains(int value) {
        return containsHelper(root, value);
    }

    private boolean containsHelper(Node node, int value) {
        if (node == null) {
            return false;
        }
        if (value == node.value) {
            return true;
        }
        return value < node.value ? containsHelper(node.left, value) : containsHelper(node.right, value);
    }

    public int size() {
        return size;
    }

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    public boolean isValid() {
        return isValidHelper(root, null, null);
    }

    private boolean isValidHelper(Node node, Integer min, Integer max) {
        if (node == null) {
            return true;
        }
        if (min != null && node.value <= min) {
            return false;
        }
        if (max != null && node.value >= max) {
            return false;
        }
        return isValidHelper(node.left, min, node.value) && isValidHelper(node.right, node.value, max);
    }
}
