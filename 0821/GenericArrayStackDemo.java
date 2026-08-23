class ArrayStack<T> {
    private final Object[] data;
    private int size;

    public ArrayStack(int capacity) {
        int cap = Math.max(1, capacity);
        this.data = new Object[cap];
        this.size = 0;
    }

    public boolean push(T item) {
        if (item == null) {
            return false;
        }
        if (isFull()) {
            return false;
        }
        data[size] = item;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        size--;
        T value = (T) data[size];
        data[size] = null; // 釋放物件引用，避免記憶體洩漏
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) data[size - 1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == data.length;
    }

    public int capacity() {
        return data.length;
    }
}

public class GenericArrayStackDemo {
    public static void main(String[] args) {
        System.out.println("=== 課堂實作題四：固定容量 Generic Stack ===");

        // 1. 測試 String Stack
        System.out.println("--- 測試 ArrayStack<String> (容量 = 3) ---");
        ArrayStack<String> stringStack = new ArrayStack<>(3);
        System.out.println("是否為空：" + stringStack.isEmpty()); // true

        System.out.println("push 'A'：" + stringStack.push("A")); // true
        System.out.println("push 'B'：" + stringStack.push("B")); // true
        System.out.println("push 'C'：" + stringStack.push("C")); // true
        System.out.println("是否已滿：" + stringStack.isFull());   // true
        System.out.println("在已滿時 push 'D' (預期為 false)：" + stringStack.push("D")); // false

        System.out.println("peek 頂端：" + stringStack.peek()); // C
        System.out.println("pop 彈出：" + stringStack.pop());   // C
        System.out.println("pop 彈出：" + stringStack.pop());   // B
        System.out.println("目前 size：" + stringStack.size()); // 1
        System.out.println("pop 彈出：" + stringStack.pop());   // A

        // 測試 underflow
        System.out.println("在空棧時 pop (預期為 null)：" + stringStack.pop()); // null
        System.out.println("是否為空：" + stringStack.isEmpty()); // true

        // 2. 測試 Integer Stack
        System.out.println("\n--- 測試 ArrayStack<Integer> (容量 = 2) ---");
        ArrayStack<Integer> intStack = new ArrayStack<>(2);
        System.out.println("push 100：" + intStack.push(100)); // true
        System.out.println("push 200：" + intStack.push(200)); // true
        System.out.println("push 300 (預期為 false)：" + intStack.push(300)); // false
        System.out.println("peek 頂端：" + intStack.peek()); // 200
        System.out.println("pop 彈出：" + intStack.pop()); // 200
        System.out.println("pop 彈出：" + intStack.pop()); // 100
        System.out.println("pop 彈出 (預期為 null)：" + intStack.pop()); // null
    }
}
