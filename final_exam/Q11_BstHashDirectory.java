import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q11_BstHashDirectory {
    private static class Node {
        int id;
        Node left;
        Node right;

        Node(int id) {
            this.id = id;
        }
    }

    private Node root;
    private final Map<Integer, String> map = new HashMap<>();

    public boolean add(int id, String name) {
        if (id <= 0 || name == null) {
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        if (map.containsKey(id)) {
            return false;
        }

        map.put(id, trimmed);
        root = addHelper(root, id);
        return true;
    }

    private Node addHelper(Node node, int id) {
        if (node == null) {
            return new Node(id);
        }
        if (id < node.id) {
            node.left = addHelper(node.left, id);
        } else if (id > node.id) {
            node.right = addHelper(node.right, id);
        }
        return node;
    }

    public String findName(int id) {
        return map.get(id);
    }

    public boolean remove(int id) {
        if (!map.containsKey(id)) {
            return false;
        }
        map.remove(id);
        root = removeHelper(root, id);
        return true;
    }

    private Node removeHelper(Node node, int id) {
        if (node == null) {
            return null;
        }
        if (id < node.id) {
            node.left = removeHelper(node.left, id);
        } else if (id > node.id) {
            node.right = removeHelper(node.right, id);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            Node successor = findMin(node.right);
            node.id = successor.id;
            node.right = removeHelper(node.right, successor.id);
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public List<Integer> idsBetween(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low > high) {
            return result;
        }
        idsBetweenHelper(root, low, high, result);
        return result;
    }

    private void idsBetweenHelper(Node node, int low, int high, List<Integer> result) {
        if (node == null) {
            return;
        }
        if (node.id > low) {
            idsBetweenHelper(node.left, low, high, result);
        }
        if (node.id >= low && node.id <= high) {
            result.add(node.id);
        }
        if (node.id < high) {
            idsBetweenHelper(node.right, low, high, result);
        }
    }

    public int size() {
        return map.size();
    }
}
