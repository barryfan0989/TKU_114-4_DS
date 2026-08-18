class DigitalWallet {
    private String walletId;
    private String owner;
    private int balance;
    private int transactionCount;

    DigitalWallet(String walletId, String owner) {
        this.walletId = walletId == null || walletId.trim().isEmpty() ? "UNKNOWN" : walletId.trim();
        this.owner = owner == null || owner.trim().isEmpty() ? "Unknown" : owner.trim();
        this.balance = 0;
        this.transactionCount = 0;
    }

    boolean deposit(int amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        transactionCount++;
        return true;
    }

    boolean pay(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        transactionCount++;
        return true;
    }

    boolean refund(int amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        transactionCount++;
        return true;
    }

    String getWalletId() {
        return walletId;
    }

    String getOwner() {
        return owner;
    }

    int getBalance() {
        return balance;
    }

    int getTransactionCount() {
        return transactionCount;
    }

    @Override
    public String toString() {
        return "DigitalWallet{walletId='" + walletId + "', owner='" + owner + "', balance=" + balance + ", transactionCount=" + transactionCount + "}";
    }
}

public class DigitalWalletSystem {
    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W100", "Amy");
        System.out.println("Initial: " + wallet);

        // Test normal deposit
        System.out.println("\nDeposit $1000: " + wallet.deposit(1000));
        System.out.println(wallet);

        // Test normal pay
        System.out.println("\nPay $250: " + wallet.pay(250));
        System.out.println(wallet);

        // Test insufficient balance pay
        System.out.println("\nPay $900: " + wallet.pay(900));
        System.out.println("After failed payment: " + wallet);

        // Test negative amount deposit
        System.out.println("\nDeposit -$100 (invalid): " + wallet.deposit(-100));
        System.out.println("After failed deposit: " + wallet);

        // Test negative amount payment
        System.out.println("\nPay -$50 (invalid): " + wallet.pay(-50));
        System.out.println("After failed payment: " + wallet);

        // Test normal refund
        System.out.println("\nRefund $50: " + wallet.refund(50));
        System.out.println(wallet);
    }
}
