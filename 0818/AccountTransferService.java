class Account {
    private String id;
    private int balance;

    Account(String id, int balance) {
        this.id = id == null || id.trim().isEmpty() ? "Unknown" : id.trim();
        this.balance = Math.max(0, balance);
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    String getId() {
        return id;
    }

    int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{id='" + id + "', balance=" + balance + "}";
    }
}

class TransferService {
    static boolean transfer(Account source, Account target, int amount) {
        if (source == null || target == null) {
            return false;
        }
        if (source == target) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        // To guarantee atomicity (neither change if one fails):
        // First check if withdraw will succeed
        if (source.getBalance() < amount) {
            return false;
        }
        
        // Execute the state change
        boolean withdrawn = source.withdraw(amount);
        if (withdrawn) {
            target.deposit(amount);
            return true;
        }
        return false;
    }
}

public class AccountTransferService {
    public static void main(String[] args) {
        Account a1 = new Account("A101", 1000);
        Account a2 = new Account("A102", 500);

        System.out.println("Initial State:");
        System.out.println("a1: " + a1);
        System.out.println("a2: " + a2);

        // Test 1: Successful transfer
        System.out.println("\n--- Test 1: Transfer $300 from a1 to a2 ---");
        boolean result1 = TransferService.transfer(a1, a2, 300);
        System.out.println("Transfer Success: " + result1);
        System.out.println("a1: " + a1);
        System.out.println("a2: " + a2);

        // Test 2: Insufficient balance
        System.out.println("\n--- Test 2: Transfer $900 from a1 to a2 (insufficient) ---");
        boolean result2 = TransferService.transfer(a1, a2, 900);
        System.out.println("Transfer Success: " + result2);
        System.out.println("a1: " + a1);
        System.out.println("a2: " + a2);

        // Test 3: Same account transfer
        System.out.println("\n--- Test 3: Transfer $100 from a1 to a1 ---");
        boolean result3 = TransferService.transfer(a1, a1, 100);
        System.out.println("Transfer Success: " + result3);
        System.out.println("a1: " + a1);

        // Test 4: Null target transfer
        System.out.println("\n--- Test 4: Transfer $100 from a1 to null ---");
        boolean result4 = TransferService.transfer(a1, null, 100);
        System.out.println("Transfer Success: " + result4);
        System.out.println("a1: " + a1);
        
        // Test 5: Negative amount transfer
        System.out.println("\n--- Test 5: Transfer -$50 from a1 to a2 (invalid amount) ---");
        boolean result5 = TransferService.transfer(a1, a2, -50);
        System.out.println("Transfer Success: " + result5);
        System.out.println("a1: " + a1);
        System.out.println("a2: " + a2);
    }
}
