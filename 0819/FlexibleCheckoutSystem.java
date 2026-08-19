interface PricingPolicy {
    int finalPrice(int originalPrice);
}

class StandardPricing implements PricingPolicy {
    @Override
    public int finalPrice(int originalPrice) {
        return Math.max(0, originalPrice);
    }
}

class VipPricing implements PricingPolicy {
    @Override
    public int finalPrice(int originalPrice) {
        return Math.max(0, originalPrice) * 85 / 100;
    }
}

class ThresholdDiscountPricing implements PricingPolicy {
    private final int threshold;
    private final int discount;

    ThresholdDiscountPricing(int threshold, int discount) {
        this.threshold = Math.max(0, threshold);
        this.discount = Math.max(0, discount);
    }

    @Override
    public int finalPrice(int originalPrice) {
        int price = Math.max(0, originalPrice);
        if (price >= threshold) {
            return Math.max(0, price - discount);
        }
        return price;
    }
}

interface NotificationChannel {
    boolean send(String receiver, String message);
}

class EmailChannel implements NotificationChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (receiver == null || !receiver.contains("@")) {
            System.out.println("[Email Error] Invalid email address: " + receiver);
            return false;
        }
        System.out.println("EMAIL " + receiver + " -> " + message);
        return true;
    }
}

class SmsChannel implements NotificationChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (receiver == null || receiver.trim().isEmpty()) {
            System.out.println("[SMS Error] Empty phone number.");
            return false;
        }
        System.out.println("SMS " + receiver.trim() + " -> " + message);
        return true;
    }
}

class ConsoleChannel implements NotificationChannel {
    @Override
    public boolean send(String receiver, String message) {
        System.out.println("CONSOLE " + receiver + " -> " + message);
        return true;
    }
}

class CheckoutResult {
    private final String orderId;
    private final int originalPrice;
    private final int finalPrice;
    private final boolean notificationStatus;

    CheckoutResult(String orderId, int originalPrice, int finalPrice, boolean notificationStatus) {
        this.orderId = orderId;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.notificationStatus = notificationStatus;
    }

    @Override
    public String toString() {
        return String.format("CheckoutResult { OrderID: '%s', Original: %d 元, Final: %d 元, Notification: %s }",
            orderId, originalPrice, finalPrice, notificationStatus ? "SUCCESS" : "FAILED");
    }
}

class CheckoutService {
    private final PricingPolicy pricing;
    private final NotificationChannel channel;

    CheckoutService(PricingPolicy pricing, NotificationChannel channel) {
        // Fallback checks to prevent NullPointerException
        this.pricing = (pricing == null) ? new StandardPricing() : pricing;
        this.channel = (channel == null) ? new ConsoleChannel() : channel;
    }

    public CheckoutResult checkout(String orderId, int originalPrice, String receiver) {
        String safeOrderId = (orderId == null || orderId.trim().isEmpty()) ? "TXN-TEMP" : orderId.trim();
        int safeOriginalPrice = Math.max(0, originalPrice);
        
        int finalPrice = pricing.finalPrice(safeOriginalPrice);
        String message = String.format("Order %s finalized. Total amount: %d 元", safeOrderId, finalPrice);
        
        boolean sent = channel.send(receiver, message);
        return new CheckoutResult(safeOrderId, safeOriginalPrice, finalPrice, sent);
    }
}

public class FlexibleCheckoutSystem {
    public static void main(String[] args) {
        System.out.println("=== Flexible Checkout System ===");

        PricingPolicy standard = new StandardPricing();
        PricingPolicy vip = new VipPricing();
        PricingPolicy threshold = new ThresholdDiscountPricing(2000, 300);

        NotificationChannel email = new EmailChannel();
        NotificationChannel sms = new SmsChannel();
        NotificationChannel console = new ConsoleChannel();

        // 6 Combinations to test
        CheckoutService[] combinations = {
            new CheckoutService(standard, email),   // 1. Standard + Email
            new CheckoutService(vip, email),        // 2. VIP + Email
            new CheckoutService(threshold, sms),    // 3. Threshold + SMS
            new CheckoutService(vip, sms),          // 4. VIP + SMS
            new CheckoutService(standard, console), // 5. Standard + Console
            new CheckoutService(threshold, console) // 6. Threshold + Console
        };

        // Inputs for testing
        String[] orderIds = {"O-101", "O-102", "O-103", "O-104", "O-105", "O-106"};
        int[] prices = {1500, 2500, 2200, 800, 1000, 3000};
        String[] receivers = {
            "alice@example.com", 
            "bob@example.com", 
            "0911222333", 
            "",                  // Testing SMS error
            "counter-main", 
            "admin"
        };

        for (int i = 0; i < combinations.length; i++) {
            System.out.println("\n--- Combination " + (i + 1) + " ---");
            CheckoutResult result = combinations[i].checkout(orderIds[i], prices[i], receivers[i]);
            System.out.println(result);
        }

        // Test safe fallbacks with null policies/channels
        System.out.println("\n--- Test Null Safety Fallbacks ---");
        CheckoutService fallbackService = new CheckoutService(null, null);
        CheckoutResult fallbackResult = fallbackService.checkout(null, -100, null);
        System.out.println(fallbackResult);
    }
}
