class Customer {
    private String id;
    private String name;

    Customer(String id, String name) {
        this.id = id == null || id.trim().isEmpty() ? "Unknown" : id.trim();
        this.name = name == null || name.trim().isEmpty() ? "Unknown" : name.trim();
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String label() {
        return id + " (" + name + ")";
    }
}

class OrderItem {
    private String itemName;
    private int price;
    private int quantity;

    OrderItem(String itemName, int price, int quantity) {
        this.itemName = itemName == null || itemName.trim().isEmpty() ? "Unknown" : itemName.trim();
        this.price = Math.max(0, price);
        this.quantity = Math.max(0, quantity);
    }

    String getItemName() {
        return itemName;
    }

    int getPrice() {
        return price;
    }

    int getQuantity() {
        return quantity;
    }

    int getSubtotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return itemName + " x " + quantity + " ($" + price + " each) = $" + getSubtotal();
    }
}

class CustomerOrder {
    private String orderId;
    private Customer customer;
    private OrderItem[] items;

    CustomerOrder(String orderId, Customer customer, OrderItem[] items) {
        this.orderId = orderId == null || orderId.trim().isEmpty() ? "Unknown" : orderId.trim();
        this.customer = customer;
        this.items = items == null ? new OrderItem[0] : items;
    }

    int calculateTotal() {
        int total = 0;
        for (OrderItem item : items) {
            if (item != null) {
                total += item.getSubtotal();
            }
        }
        return total;
    }

    int calculateTotalItems() {
        int count = 0;
        for (OrderItem item : items) {
            if (item != null) {
                count += item.getQuantity();
            }
        }
        return count;
    }

    void printSummary() {
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + (customer != null ? customer.label() : "None"));
        System.out.println("Items:");
        for (OrderItem item : items) {
            if (item != null) {
                System.out.println("  - " + item);
            }
        }
        System.out.println("Total Quantity of Items: " + calculateTotalItems());
        System.out.println("Order Total Value: $" + calculateTotal());
    }
}

public class CustomerOrderSystem {
    public static void main(String[] args) {
        Customer customer = new Customer("C101", "Amy");
        
        OrderItem[] items = {
            new OrderItem("Wireless Mouse", 500, 2),
            new OrderItem("Mechanical Keyboard", 1500, 1),
            new OrderItem("USB-C Hub", 800, 3)
        };

        CustomerOrder order = new CustomerOrder("O9001", customer, items);
        order.printSummary();
    }
}
