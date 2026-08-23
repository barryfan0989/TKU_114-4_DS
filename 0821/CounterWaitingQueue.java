import java.util.ArrayDeque;
import java.util.Deque;

class Customer {
    private final String id;
    private final String name;

    Customer(String id, String name) {
        this.id = id != null ? id.trim() : "";
        this.name = name != null ? name.trim() : "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

public class CounterWaitingQueue {
    private final Deque<Customer> waitingQueue = new ArrayDeque<>();

    public void enqueue(Customer customer) {
        if (customer == null) {
            return;
        }
        waitingQueue.offerLast(customer);
        System.out.println("顧客加入隊尾：" + customer);
    }

    public Customer peekNext() {
        return waitingQueue.peekFirst();
    }

    public Customer serveNext() {
        Customer served = waitingQueue.pollFirst();
        if (served == null) {
            System.out.println("[提示] 服務中心目前無人在排隊。");
        } else {
            System.out.println("正在服務顧客：" + served);
        }
        return served;
    }

    public int size() {
        return waitingQueue.size();
    }

    public boolean isEmpty() {
        return waitingQueue.isEmpty();
    }

    public void printQueue() {
        System.out.println("目前等候隊列：" + waitingQueue);
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題三：櫃台等候 Queue ===");

        CounterWaitingQueue queue = new CounterWaitingQueue();

        // 1. 測試空隊列時叫號
        System.out.println("隊列為空時進行叫號：");
        Customer emptyServe = queue.serveNext(); // 應輸出無人排隊，回傳 null
        System.out.println("叫號結果：" + emptyServe);

        // 2. 陸續有顧客加入
        System.out.println("\n--- 顧客陸續加入排隊 ---");
        queue.enqueue(new Customer("C001", "Amy"));
        queue.enqueue(new Customer("C002", "Ben"));
        queue.enqueue(new Customer("C003", "Cara"));

        queue.printQueue();
        System.out.println("目前等候人數：" + queue.size()); // 3

        // 3. 查看下一位顧客
        System.out.println("\n查看首位等待的顧客 (不取出)：");
        System.out.println("下一位是：" + queue.peekNext()); // Amy

        // 4. 依序叫號服務 (FIFO)
        System.out.println("\n--- 開始服務顧客 ---");
        queue.serveNext(); // 服務 C001
        System.out.println("目前等候人數：" + queue.size()); // 2
        queue.printQueue();

        queue.serveNext(); // 服務 C002
        System.out.println("下一位是：" + queue.peekNext()); // Cara

        queue.serveNext(); // 服務 C003
        System.out.println("目前等候人數：" + queue.size()); // 0

        // 5. 再次叫號 (已空)
        queue.serveNext(); // 應輸出無人排隊
    }
}
