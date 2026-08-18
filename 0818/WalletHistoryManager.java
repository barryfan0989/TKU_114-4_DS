public class WalletHistoryManager {

    static class WalletTransaction {
        private final int sequence;
        private final String type;
        private final int amount;
        private final int balanceAfter;

        WalletTransaction(int sequence, String type, int amount, int balanceAfter) {
            this.sequence = sequence;
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
        }

        int getSequence() {
            return sequence;
        }

        String getType() {
            return type;
        }

        int getAmount() {
            return amount;
        }

        int getBalanceAfter() {
            return balanceAfter;
        }

        @Override
        public String toString() {
            return sequence + " " + type + " " + amount + " balance=" + balanceAfter;
        }
    }

    static class DigitalWallet {
        private final String walletId;
        private final String owner;
        private int balance;
        private final WalletTransaction[] transactions;
        private int transactionCount;

        DigitalWallet(String walletId, String owner, int historyCapacity) {
            this.walletId = walletId == null || walletId.isBlank() ? "UNKNOWN" : walletId.trim();
            this.owner = owner == null || owner.isBlank() ? "Unknown" : owner.trim();
            this.balance = 0;
            this.transactions = new WalletTransaction[Math.max(1, historyCapacity)];
            this.transactionCount = 0;
        }

        boolean isFull() {
            return transactionCount >= transactions.length;
        }

        boolean deposit(int amount) {
            if (amount <= 0 || isFull()) {
                return false;
            }
            balance += amount;
            record("DEPOSIT", amount);
            return true;
        }

        boolean pay(int amount) {
            if (amount <= 0 || amount > balance || isFull()) {
                return false;
            }
            balance -= amount;
            record("PAY", amount);
            return true;
        }

        boolean refund(int amount) {
            if (amount <= 0 || isFull()) {
                return false;
            }
            balance += amount;
            record("REFUND", amount);
            return true;
        }

        // Atomic transfer operation
        boolean transferTo(DigitalWallet target, int amount) {
            if (target == null || target == this || amount <= 0) {
                return false;
            }
            if (this.balance < amount) {
                return false;
            }
            // Both wallets must have space for transactions
            if (this.isFull() || target.isFull()) {
                return false;
            }

            // Perform debit and credit
            this.balance -= amount;
            target.balance += amount;

            // Record on both sides
            this.record("TRANSFER_OUT", amount);
            target.record("TRANSFER_IN", amount);

            return true;
        }

        private void record(String type, int amount) {
            transactions[transactionCount] = new WalletTransaction(
                    transactionCount + 1, type, amount, balance);
            transactionCount++;
        }

        WalletTransaction findTransaction(int sequence) {
            for (int i = 0; i < transactionCount; i++) {
                if (transactions[i] != null && transactions[i].getSequence() == sequence) {
                    return transactions[i];
                }
            }
            return null;
        }

        int totalByType(String type) {
            if (type == null) {
                return 0;
            }
            int total = 0;
            for (int i = 0; i < transactionCount; i++) {
                if (transactions[i] != null && type.equalsIgnoreCase(transactions[i].getType())) {
                    total += transactions[i].getAmount();
                }
            }
            return total;
        }

        int getBalance() {
            return balance;
        }

        void printStatement() {
            System.out.println(walletId + " owner=" + owner + " balance=" + balance);
            for (int i = 0; i < transactionCount; i++) {
                System.out.println("  " + transactions[i]);
            }
        }
    }

    public static void main(String[] args) {
        DigitalWallet w1 = new DigitalWallet("W001", "Amy", 5);
        DigitalWallet w2 = new DigitalWallet("W002", "Bob", 5);

        System.out.println("--- Setup and Initial Deposits ---");
        w1.deposit(1000);
        w2.deposit(500);
        w1.pay(200);

        w1.printStatement();
        w2.printStatement();

        System.out.println("\n--- Testing Transfer (w1 transfer 300 to w2) ---");
        boolean transferOk = w1.transferTo(w2, 300);
        System.out.println("Transfer result: " + transferOk);
        w1.printStatement();
        w2.printStatement();

        System.out.println("\n--- Testing Find Transaction in w1 ---");
        WalletTransaction tx = w1.findTransaction(2); // Should be PAY 200
        System.out.println("Found sequence 2: " + tx);
        WalletTransaction txMissing = w1.findTransaction(99);
        System.out.println("Found sequence 99: " + txMissing);

        System.out.println("\n--- Testing Total By Type in w1 ---");
        System.out.println("Total DEPOSIT: " + w1.totalByType("DEPOSIT"));
        System.out.println("Total PAY: " + w1.totalByType("PAY"));
        System.out.println("Total TRANSFER_OUT: " + w1.totalByType("TRANSFER_OUT"));

        System.out.println("\n--- Testing Transaction Array Full Limit ---");
        // w1 capacity is 5. We have recorded: DEPOSIT (1), PAY (2), TRANSFER_OUT (3).
        // Let's add two more to make it full (5/5).
        System.out.println("Refund 50 (4th tx): " + w1.refund(50));
        System.out.println("Pay 100 (5th tx): " + w1.pay(100));
        System.out.println("w1 isFull: " + w1.isFull());
        
        // Try 6th tx, should fail and state remains unchanged
        int balanceBeforeFail = w1.getBalance();
        System.out.println("Deposit 100 (6th tx - should fail): " + w1.deposit(100));
        System.out.println("Balance after failed deposit: " + w1.getBalance() + " (was " + balanceBeforeFail + ")");

        System.out.println("\n--- Testing Atomic Transfer failure when one is full ---");
        // w1 is full. Transfer from w2 to w1 should fail because target (w1) cannot record transaction.
        int w2BalanceBefore = w2.getBalance();
        int w1BalanceBefore = w1.getBalance();
        System.out.println("Transfer 50 from w2 to w1 (should fail due to w1 full): " + w2.transferTo(w1, 50));
        System.out.println("w1 Balance: " + w1.getBalance() + " (expected " + w1BalanceBefore + ")");
        System.out.println("w2 Balance: " + w2.getBalance() + " (expected " + w2BalanceBefore + ")");

        System.out.println("\n--- Final Statements ---");
        w1.printStatement();
        w2.printStatement();
    }
}
