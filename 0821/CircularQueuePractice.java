import java.util.Arrays;

class CircularQueue<T> {
    private final Object[] data;
    private int front;
    private int rear;
    private int size;

    public CircularQueue(int capacity) {
        int cap = Math.max(1, capacity);
        this.data = new Object[cap];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    public boolean enqueue(T value) {
        if (value == null) {
            return false;
        }
        if (isFull()) {
            return false;
        }
        data[rear] = value;
        rear = (rear + 1) % data.length;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T value = (T) data[front];
        data[front] = null; // 釋放物件引用，避免記憶體洩漏
        front = (front + 1) % data.length;
        size--;
        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == data.length;
    }

    public int size() {
        return size;
    }

    public void printState(String label) {
        System.out.println(label + " -> Array: " + Arrays.toString(data) + 
                           ", front=" + front + ", rear=" + rear + ", size=" + size);
    }
}

public class CircularQueuePractice {
    public static void main(String[] args) {
        System.out.println("=== 課堂實作題六：Circular queue 狀態追蹤 ===");

        // 容量為 4
        CircularQueue<String> queue = new CircularQueue<>(4);
        queue.printState("初始狀態");

        // 1. enqueue A, B, C
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.printState("enqueue A, B, C");

        // 2. dequeue, dequeue
        System.out.println("   取出: " + queue.dequeue()); // A
        System.out.println("   取出: " + queue.dequeue()); // B
        queue.printState("dequeue 2 次");

        // 3. enqueue D, E, F
        System.out.println("   enqueue D: " + queue.enqueue("D")); // true
        System.out.println("   enqueue E: " + queue.enqueue("E")); // true
        System.out.println("   enqueue F (預期為 false，因容量為 4，此時 size=3，再加一個就滿)：");
        System.out.println("   enqueue F: " + queue.enqueue("F")); // true (此時滿了，size=4)
        queue.printState("enqueue D, E, F");

        // 4. dequeue
        System.out.println("   取出: " + queue.dequeue()); // C
        queue.printState("dequeue 1 次");

        // 5. enqueue G
        System.out.println("   enqueue G: " + queue.enqueue("G")); // true
        queue.printState("enqueue G");

        // 6. 依 FIFO 順序取出所有剩餘元素
        System.out.println("\n依 FIFO 順序取出剩餘的所有元素：");
        while (!queue.isEmpty()) {
            System.out.println("   取出：" + queue.dequeue());
        }
        queue.printState("最終清空狀態");
    }
}
