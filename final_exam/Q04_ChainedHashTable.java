import java.util.ArrayList;
import java.util.List;

public class Q04_ChainedHashTable {
    private static class Entry {
        final int key;
        String value;

        Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private final List<List<Entry>> buckets;
    private final int bucketCount;
    private int size;

    public Q04_ChainedHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }
        this.bucketCount = bucketCount;
        this.buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            this.buckets.add(new ArrayList<>());
        }
        this.size = 0;
    }

    private int getIndex(int key) {
        return Math.floorMod(key, bucketCount);
    }

    public void put(int key, String value) {
        int index = getIndex(key);
        List<Entry> chain = buckets.get(index);
        for (Entry entry : chain) {
            if (entry.key == key) {
                entry.value = value;
                return;
            }
        }
        chain.add(new Entry(key, value));
        size++;
    }

    public String get(int key) {
        int index = getIndex(key);
        List<Entry> chain = buckets.get(index);
        for (Entry entry : chain) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean remove(int key) {
        int index = getIndex(key);
        List<Entry> chain = buckets.get(index);
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key == key) {
                chain.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public int longestChain() {
        int max = 0;
        for (List<Entry> chain : buckets) {
            if (chain.size() > max) {
                max = chain.size();
            }
        }
        return max;
    }
}
