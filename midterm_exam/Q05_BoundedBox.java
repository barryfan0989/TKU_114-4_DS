import java.util.ArrayList;
import java.util.List;

public class Q05_BoundedBox<T extends Comparable<T>> {
    private final int capacity;
    private final List<T> items;

    public Q05_BoundedBox(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    public boolean add(T value) {
        if (value == null || isFull()) {
            return false;
        }
        items.add(value);
        return true;
    }

    public int size() {
        return items.size();
    }

    public boolean isFull() {
        return items.size() >= capacity;
    }

    public T minimum() {
        if (items.isEmpty()) {
            return null;
        }
        T min = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T current = items.get(i);
            if (current.compareTo(min) < 0) {
                min = current;
            }
        }
        return min;
    }

    public T maximum() {
        if (items.isEmpty()) {
            return null;
        }
        T max = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            T current = items.get(i);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }

    public int countGreaterThan(T threshold) {
        if (threshold == null) {
            return 0;
        }
        int count = 0;
        for (T item : items) {
            if (item.compareTo(threshold) > 0) {
                count++;
            }
        }
        return count;
    }

    public List<T> snapshot() {
        return new ArrayList<>(items);
    }
}
