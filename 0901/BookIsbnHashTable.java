import java.util.ArrayList;
import java.util.List;

public class BookIsbnHashTable {

    public record Book(String isbn, String title, String author, double price) {
        public Book {
            if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("ISBN cannot be null or blank");
            if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be null or blank");
            isbn = normalizeIsbn(isbn);
        }

        public static String normalizeIsbn(String raw) {
            return raw.replaceAll("[-\\s]", "").toUpperCase();
        }

        @Override
        public String toString() {
            return String.format("《%s》[ISBN: %s | 作者: %s | 定價: $%.1f]", title, isbn, author, price);
        }
    }

    private record Entry(String normalizedIsbn, Book book) {}

    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private List<List<Entry>> buckets;
    private int size;

    public BookIsbnHashTable() {
        this(5);
    }

    public BookIsbnHashTable(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.buckets = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            this.buckets.add(new ArrayList<>());
        }
        this.size = 0;
    }

    private int getIndex(String normalizedIsbn, int capacity) {
        return Math.floorMod(normalizedIsbn.hashCode(), capacity);
    }

    public void put(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");
        String key = book.isbn();
        int idx = getIndex(key, buckets.size());
        List<Entry> chain = buckets.get(idx);

        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).normalizedIsbn().equals(key)) {
                // 更新既有圖書資訊，size 不增加
                chain.set(i, new Entry(key, book));
                return;
            }
        }

        // 新書新增
        chain.add(new Entry(key, book));
        size++;

        if (loadFactor() > LOAD_FACTOR_THRESHOLD) {
            rehash(buckets.size() * 2 + 1);
        }
    }

    public Book get(String isbn) {
        if (isbn == null) return null;
        String key = Book.normalizeIsbn(isbn);
        int idx = getIndex(key, buckets.size());
        for (Entry entry : buckets.get(idx)) {
            if (entry.normalizedIsbn().equals(key)) {
                return entry.book();
            }
        }
        return null;
    }

    public boolean containsKey(String isbn) {
        return get(isbn) != null;
    }

    public Book remove(String isbn) {
        if (isbn == null) return null;
        String key = Book.normalizeIsbn(isbn);
        int idx = getIndex(key, buckets.size());
        List<Entry> chain = buckets.get(idx);
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).normalizedIsbn().equals(key)) {
                Book removed = chain.remove(i).book();
                size--;
                return removed;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public double loadFactor() {
        return (double) size / buckets.size();
    }

    private void rehash(int newCapacity) {
        List<List<Entry>> newBuckets = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(new ArrayList<>());
        }

        for (List<Entry> chain : buckets) {
            for (Entry entry : chain) {
                int newIdx = getIndex(entry.normalizedIsbn(), newCapacity);
                newBuckets.get(newIdx).add(entry);
            }
        }
        this.buckets = newBuckets;
    }

    public void bucketReport() {
        System.out.println("==================== 圖書 ISBN 雜湊表分佈報告 ====================");
        System.out.printf("藏書總量 (size): %d | Bucket 數量: %d | 負載因子: %.2f%n",
                size, buckets.size(), loadFactor());

        int collisions = 0;
        int maxChain = 0;
        for (int i = 0; i < buckets.size(); i++) {
            List<Entry> chain = buckets.get(i);
            int len = chain.size();
            if (len > maxChain) maxChain = len;
            if (len > 1) collisions += (len - 1);

            System.out.printf("Bucket [%2d] (長度: %d): ", i, len);
            if (chain.isEmpty()) {
                System.out.println("[]");
            } else {
                List<String> titles = new ArrayList<>();
                for (Entry e : chain) {
                    titles.add(e.book().title() + " (" + e.normalizedIsbn() + ")");
                }
                System.out.println(titles);
            }
        }
        System.out.printf("統計指標: 總 Collision 次數 = %d | 最長 Chain 長度 = %d%n", collisions, maxChain);
        System.out.println("=================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業一：圖書索引 (BookIsbnHashTable) ===\n");
        BookIsbnHashTable library = new BookIsbnHashTable(5);

        // 插入多本圖書 (包含不同格式的 ISBN)
        library.put(new Book("978-0134685991", "Effective Java", "Joshua Bloch", 54.9));
        library.put(new Book("978-0-13-235088-4", "Clean Code", "Robert C. Martin", 48.0));
        library.put(new Book("9780262033848", "Introduction to Algorithms", "CLRS", 89.5));
        library.put(new Book("978-0321573513", "Algorithms 4th Edition", "Robert Sedgewick", 75.0));
        library.put(new Book("978-0132350884", "Clean Code (二刷修訂版)", "Robert C. Martin", 52.0)); // 相同 ISBN 更新

        library.bucketReport();

        // 查詢書籍
        System.out.println("--- 圖書檢索測試 ---");
        System.out.println("以帶連字號 ISBN 查詢: " + library.get("978-0-13468599-1"));
        System.out.println("查詢修訂版書籍 (驗證更新): " + library.get("9780132350884"));
        System.out.println("查詢未收錄書籍: " + library.get("978-0000000000"));

        // 刪除書籍
        System.out.println("\n--- 圖書借出/註銷測試 ---");
        System.out.println("註銷: " + library.remove("978-0262033848"));
        System.out.println("註銷後查詢 containsKey: " + library.containsKey("9780262033848"));
        System.out.println("目前藏書量: " + library.size());

        library.bucketReport();
    }
}
