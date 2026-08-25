class Order {
    String orderId;
    String customerName;
    double amount;
    String status; // "PENDING", "COMPLETED", "CANCELLED"

    Order(String orderId, String customerName, double amount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
        this.status = "PENDING";
    }

    @Override
    public String toString() {
        return "OrderID: " + orderId + " | Customer: " + customerName + " | Amount: $" + amount + " | Status: " + status;
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
            int cmp = order.orderId.compareTo(current.order.orderId);
            if (cmp == 0) {
                return false; // Duplicate order ID not allowed
            }
            if (cmp < 0) {
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

    Order find(String orderId) {
        OrderNode current = root;
        while (current != null) {
            int cmp = orderId.compareTo(current.order.orderId);
            if (cmp == 0) return current.order;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    boolean cancel(String orderId) {
        Order order = find(orderId);
        if (order == null) {
            System.out.println("  [Cancel Error] Order " + orderId + " not found.");
            return false;
        }
        order.status = "CANCELLED";
        System.out.println("  [Cancelled] Order " + orderId + " has been cancelled.");
        return true;
    }

    boolean updateAmount(String orderId, double amount) {
        Order order = find(orderId);
        if (order == null) {
            System.out.println("  [Update Error] Order " + orderId + " not found.");
            return false;
        }
        order.amount = amount;
        System.out.println("  [Updated] Order " + orderId + " amount set to $" + amount);
        return true;
    }

    void printRange(String lowId, String highId) {
        System.out.println("--- Orders in ID Range [" + lowId + ", " + highId + "] ---");
        if (lowId.compareTo(highId) > 0) {
            System.out.println("  [Error] Invalid range: lowId > highId");
            return;
        }
        printRange(root, lowId, highId);
        System.out.println();
    }

    private void printRange(OrderNode node, String lowId, String highId) {
        if (node == null) return;
        
        if (node.order.orderId.compareTo(lowId) >= 0) {
            printRange(node.left, lowId, highId);
        }
        
        if (node.order.orderId.compareTo(lowId) >= 0 && node.order.orderId.compareTo(highId) <= 0) {
            System.out.println("  " + node.order);
        }
        
        if (node.order.orderId.compareTo(highId) <= 0) {
            printRange(node.right, lowId, highId);
        }
    }

    void printSummary() {
        SummaryResult result = new SummaryResult();
        computeSummary(root, result);
        System.out.println("=================================================");
        System.out.println("            ORDER SYSTEM SUMMARY                 ");
        System.out.println("=================================================");
        System.out.println("  Total Orders:            " + result.totalCount);
        System.out.println("  Active Orders (Pending): " + result.activeCount);
        System.out.println("  Cancelled Orders:        " + result.cancelledCount);
        System.out.println("  Total Active Amount:     $" + result.totalActiveAmount);
        System.out.println("=================================================");
    }

    private void computeSummary(OrderNode node, SummaryResult res) {
        if (node == null) return;
        res.totalCount++;
        if ("CANCELLED".equals(node.order.status)) {
            res.cancelledCount++;
        } else {
            res.activeCount++;
            res.totalActiveAmount += node.order.amount;
        }
        computeSummary(node.left, res);
        computeSummary(node.right, res);
    }

    private static class SummaryResult {
        int totalCount = 0;
        int activeCount = 0;
        int cancelledCount = 0;
        double totalActiveAmount = 0.0;
    }
}

public class OrderBstSystem {
    public static void main(String[] args) {
        OrderBst system = new OrderBst();

        System.out.println("=== 1. Adding Orders ===");
        system.add(new Order("ORD-003", "Alice", 120.50));
        system.add(new Order("ORD-001", "Bob", 45.00));
        system.add(new Order("ORD-005", "Charlie", 350.00));
        system.add(new Order("ORD-002", "David", 89.99));
        system.add(new Order("ORD-004", "Eva", 15.75));

        System.out.println("All Orders Inorder:");
        system.printRange("ORD-000", "ORD-999");

        System.out.println("=== 2. Order Modifications ===");
        // Update amount
        system.updateAmount("ORD-002", 99.99);
        // Cancel order
        system.cancel("ORD-004");
        // Cancel missing order
        system.cancel("ORD-999");
        System.out.println();

        System.out.println("=== 3. Range Query ===");
        // Query ORD-002 to ORD-004
        system.printRange("ORD-002", "ORD-004");

        System.out.println("=== 4. Final Aggregated Summary ===");
        system.printSummary();
    }
}
