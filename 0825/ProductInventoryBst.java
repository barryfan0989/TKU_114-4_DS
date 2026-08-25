class Product {
    int id;
    String name;
    int stock;

    Product(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = Math.max(0, stock);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Stock: " + stock;
    }
}

class ProductNode {
    Product product;
    ProductNode left;
    ProductNode right;

    ProductNode(Product product) {
        this.product = product;
    }
}

class ProductBst {
    ProductNode root;

    boolean add(Product product) {
        if (product == null) return false;
        if (root == null) {
            root = new ProductNode(product);
            return true;
        }
        ProductNode current = root;
        while (true) {
            if (product.id == current.product.id) {
                return false; // Duplicate product ID not allowed
            }
            if (product.id < current.product.id) {
                if (current.left == null) {
                    current.left = new ProductNode(product);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ProductNode(product);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Product find(int id) {
        ProductNode current = root;
        while (current != null) {
            if (id == current.product.id) {
                return current.product;
            }
            current = (id < current.product.id) ? current.left : current.right;
        }
        return null;
    }

    boolean restock(int id, int amount) {
        if (amount <= 0) return false;
        Product p = find(id);
        if (p == null) {
            System.out.println("  [Restock Error] Product ID " + id + " not found.");
            return false;
        }
        p.stock += amount;
        System.out.println("  [Restocked] " + p.name + " (ID " + id + ") + " + amount + " units. New stock: " + p.stock);
        return true;
    }

    boolean sell(int id, int amount) {
        if (amount <= 0) return false;
        Product p = find(id);
        if (p == null) {
            System.out.println("  [Sell Error] Product ID " + id + " not found.");
            return false;
        }
        if (p.stock < amount) {
            System.out.println("  [Sell Error] Insufficient stock for " + p.name + ". Requested: " + amount + ", Available: " + p.stock);
            return false;
        }
        p.stock -= amount;
        System.out.println("  [Sold] " + p.name + " (ID " + id + ") - " + amount + " units. New stock: " + p.stock);
        return true;
    }

    boolean remove(int id) {
        if (find(id) == null) {
            return false;
        }
        root = remove(root, id);
        return true;
    }

    private ProductNode remove(ProductNode node, int id) {
        if (node == null) return null;
        if (id < node.product.id) {
            node.left = remove(node.left, id);
        } else if (id > node.product.id) {
            node.right = remove(node.right, id);
        } else {
            // Found node to delete
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Two children case
            ProductNode successor = minimumNode(node.right);
            node.product = successor.product;
            node.right = remove(node.right, successor.product.id);
        }
        return node;
    }

    private ProductNode minimumNode(ProductNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    void inorderReport() {
        inorder(root);
    }

    private void inorder(ProductNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.product);
        inorder(node.right);
    }
}

public class ProductInventoryBst {
    public static void main(String[] args) {
        ProductBst inventory = new ProductBst();

        System.out.println("=== 1. Initializing Products ===");
        inventory.add(new Product(200, "Mechanical Keyboard", 15));
        inventory.add(new Product(100, "Wireless Mouse", 50));
        inventory.add(new Product(300, "Gaming Monitor", 8));
        inventory.add(new Product(150, "USB-C Hub", 30));
        
        System.out.println("Current Inventory Report:");
        inventory.inorderReport();
        System.out.println();

        System.out.println("=== 2. Stock Transactions ===");
        // Restock
        inventory.restock(200, 10);
        // Sell success
        inventory.sell(150, 5);
        // Sell fail (insufficient stock)
        inventory.sell(300, 12);
        // Restock missing item
        inventory.restock(999, 5);
        System.out.println();

        System.out.println("=== 3. Deleting Discontinued Product ===");
        System.out.println("Discontinuing ID 200 (Keyboard): " + inventory.remove(200));
        System.out.println("Discontinuing ID 999 (Missing):  " + inventory.remove(999));
        System.out.println();

        System.out.println("=== 4. Final Inventory Report ===");
        inventory.inorderReport();
    }
}
