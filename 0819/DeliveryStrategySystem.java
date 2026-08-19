interface DeliveryMethod {
    int calculateDeliveryFee(int weight, int distance);
    String getDeliveryEstimate(int weight, int distance);
}

class HomeDelivery implements DeliveryMethod {
    @Override
    public int calculateDeliveryFee(int weight, int distance) {
        int w = Math.max(0, weight);
        int d = Math.max(0, distance);
        // Base 100 + 10 per kg + 5 per km
        return 100 + w * 10 + d * 5;
    }

    @Override
    public String getDeliveryEstimate(int weight, int distance) {
        return "Home Delivery: 1-2 business days (Door-to-door)";
    }
}

class StorePickup implements DeliveryMethod {
    @Override
    public int calculateDeliveryFee(int weight, int distance) {
        int w = Math.max(0, weight);
        // Base 60. If weight > 5kg, extra 40 fee.
        return (w > 5) ? 100 : 60;
    }

    @Override
    public String getDeliveryEstimate(int weight, int distance) {
        return "Store Pickup: 2-3 business days (Arrives at chosen convenience store)";
    }
}

class SelfPickup implements DeliveryMethod {
    @Override
    public int calculateDeliveryFee(int weight, int distance) {
        return 0; // Free
    }

    @Override
    public String getDeliveryEstimate(int weight, int distance) {
        return "Self Pickup: Ready in 2 hours at Main Warehouse";
    }
}

class OrderService {
    private DeliveryMethod deliveryMethod;

    OrderService(DeliveryMethod deliveryMethod) {
        setDeliveryMethod(deliveryMethod);
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = (deliveryMethod == null) ? new SelfPickup() : deliveryMethod;
    }

    public void processOrder(String orderId, int weight, int distance) {
        System.out.println("Processing Order [" + orderId + "]:");
        System.out.println("  Weight: " + weight + " kg, Distance: " + distance + " km");
        
        int fee = deliveryMethod.calculateDeliveryFee(weight, distance);
        String estimate = deliveryMethod.getDeliveryEstimate(weight, distance);
        
        System.out.println("  Delivery Fee: " + fee + " 元");
        System.out.println("  Delivery Estimate: " + estimate);
    }
}

public class DeliveryStrategySystem {
    public static void main(String[] args) {
        System.out.println("=== Delivery Strategy System ===");

        // Create order service with HomeDelivery
        OrderService orderService = new OrderService(new HomeDelivery());
        orderService.processOrder("ORD-1001", 3, 15);

        // Switch strategy to StorePickup
        System.out.println("\n--- Switching to Store Pickup ---");
        orderService.setDeliveryMethod(new StorePickup());
        orderService.processOrder("ORD-1001", 3, 15);

        // Test overweight package with StorePickup
        System.out.println("\n--- Testing overweight package with Store Pickup ---");
        orderService.processOrder("ORD-1002", 8, 15);

        // Switch strategy to SelfPickup
        System.out.println("\n--- Switching to Self Pickup ---");
        orderService.setDeliveryMethod(new SelfPickup());
        orderService.processOrder("ORD-1001", 3, 15);

        // Test null protection (defaults to SelfPickup)
        System.out.println("\n--- Testing null delivery method fallback ---");
        orderService.setDeliveryMethod(null);
        orderService.processOrder("ORD-1003", 2, 5);
    }
}
