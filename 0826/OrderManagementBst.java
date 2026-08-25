class Order {
    int orderId;
    String customer;
    double amount;
    String status; // "PENDING", "COMPLETED", "CANCELLED"

    Order(int orderId, String customer, double amount) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.amount = amount;
        this.status = "PENDING";
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + " | Customer: " + customer + " | Amount: $" + amount + " | Status: " + status;
    }
}

class OrderNode {
    Order order;
    OrderNode left;
    OrderNode right;

    OrderNode(Order order) {
        this.order = order;
    }
}

class OrderBst {
    OrderNode root;

    boolean add(Order order) {
        if (order == null) return false;
        if (root == null) {
            root = new OrderNode(order);
            return true;
        }
        OrderNode current = root;
        while (true) {
            if (order.orderId == current.order.orderId) {
                System.out.println("  [Error] Duplicate Order ID: " + order.orderId);
                return false;
            }
            if (order.orderId < current.order.orderId) {
                if (current.left == null) {
                    current.left = new OrderNode(order);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new OrderNode(order);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Order find(int orderId) {
        OrderNode current = root;
        while (current != null) {
            if (orderId == current.order.orderId) {
                return current.order;
            }
            current = (orderId < current.order.orderId) ? current.left : current.right;
        }
        return null;
    }

    boolean updateStatus(int orderId, String newStatus) {
        Order order = find(orderId);
        if (order == null) {
            System.out.println("  [Error] Order ID " + orderId + " not found.");
            return false;
        }
        order.status = newStatus;
        System.out.println("  [Success] Updated Order " + orderId + " status to: " + newStatus);
        return true;
    }

    boolean cancel(int orderId) {
        Order order = find(orderId);
        if (order == null) {
            System.out.println("  [Error] Order ID " + orderId + " not found.");
            return false;
        }
        order.status = "CANCELLED";
        System.out.println("  [Success] Cancelled Order ID: " + orderId);
        return true;
    }

    boolean remove(int orderId) {
        Order order = find(orderId);
        if (order == null) {
            System.out.println("  [Remove Error] Order ID " + orderId + " not found.");
            return false;
        }
        // CRITICAL RULE: Only CANCELLED orders can be removed
        if (!"CANCELLED".equals(order.status)) {
            System.out.println("  [Remove Error] Order ID " + orderId + " is in " + order.status + " status. Only CANCELLED orders can be removed.");
            return false;
        }
        root = remove(root, orderId);
        System.out.println("  [Removed] Order ID " + orderId + " removed from the system.");
        return true;
    }

    private OrderNode remove(OrderNode node, int orderId) {
        if (node == null) return null;
        if (orderId < node.order.orderId) {
            node.left = remove(node.left, orderId);
        } else if (orderId > node.order.orderId) {
            node.right = remove(node.right, orderId);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            OrderNode successor = minimumNode(node.right);
            node.order = successor.order;
            node.right = remove(node.right, successor.order.orderId);
        }
        return node;
    }

    private OrderNode minimumNode(OrderNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    double getTotalAmount() {
        return getTotalAmount(root);
    }

    private double getTotalAmount(OrderNode node) {
        if (node == null) return 0.0;
        return node.order.amount + getTotalAmount(node.left) + getTotalAmount(node.right);
    }

    void printRange(int lowId, int highId) {
        System.out.println("--- Order List in ID Range [" + lowId + ", " + highId + "] ---");
        if (lowId > highId) {
            System.out.println("  [Error] Invalid range parameters.");
            return;
        }
        printRange(root, lowId, highId);
        System.out.println();
    }

    private void printRange(OrderNode node, int lowId, int highId) {
        if (node == null) return;
        if (node.order.orderId > lowId) {
            printRange(node.left, lowId, highId);
        }
        if (node.order.orderId >= lowId && node.order.orderId <= highId) {
            System.out.println("  " + node.order);
        }
        if (node.order.orderId < highId) {
            printRange(node.right, lowId, highId);
        }
    }

    void inorderReport() {
        inorder(root);
    }

    private void inorder(OrderNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.order);
        inorder(node.right);
    }
}

public class OrderManagementBst {
    public static void main(String[] args) {
        OrderBst system = new OrderBst();

        System.out.println("=== 1. Adding Orders ===");
        system.add(new Order(103, "Alice", 150.00));
        system.add(new Order(101, "Bob", 45.50));
        system.add(new Order(105, "Charlie", 999.00));
        system.add(new Order(102, "David", 30.00));
        system.add(new Order(104, "Eva", 250.25));

        // Test Duplicate Order ID Rejection
        system.add(new Order(103, "Duplicate Alice", 150.00));

        // Test Negative Amount Rejection
        try {
            System.out.print("Creating Order with negative amount: ");
            new Order(106, "Frank", -10.00);
        } catch (Exception e) {
            System.out.println("Exception caught -> " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== 2. Current Orders Directory ===");
        system.inorderReport();
        System.out.println("Total Inventory Amount: $" + system.getTotalAmount());
        System.out.println();

        System.out.println("=== 3. Order Transactions & Constraints ===");
        // Try to remove PENDING Order 102 (should fail)
        system.remove(102);
        
        // Cancel Order 102
        system.cancel(102);
        
        // Remove Order 102 now (should succeed since it is CANCELLED)
        system.remove(102);
        
        // Update Order 104 status to COMPLETED
        system.updateStatus(104, "COMPLETED");
        System.out.println();

        System.out.println("=== 4. Final Orders Directory ===");
        system.inorderReport();
        System.out.println("Final Total Amount: $" + system.getTotalAmount());
        System.out.println();

        System.out.println("=== 5. Range Query ===");
        system.printRange(101, 104);
    }
}
