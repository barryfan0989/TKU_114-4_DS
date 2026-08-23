import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Repository<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        if (item != null) {
            items.add(item);
        }
    }

    public T get(int index) {
        if (index < 0 || index >= items.size()) {
            System.out.println("[Warning] Repository.get: Index " + index + " out of bounds.");
            return null;
        }
        return items.get(index);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public List<T> getAll() {
        // 回傳複本，以防內部 list 被外部直接修改
        return new ArrayList<>(items);
    }

    @Override
    public String toString() {
        return "Repository: " + items.toString();
    }
}

class Product {
    private final String id;
    private final String name;

    Product(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "'}";
    }
}

public class GenericRepositorySystem {
    public static void main(String[] args) {
        System.out.println("=== 課後作業一：Generic Repository ===");

        // 1. 測試 Repository<String>
        System.out.println("--- 測試 Repository<String> ---");
        Repository<String> stringRepo = new Repository<>();
        stringRepo.add("Java");
        stringRepo.add("Python");
        stringRepo.add("C++");

        System.out.println("初始 Repository 大小：" + stringRepo.size());
        System.out.println("所有元素：" + stringRepo.getAll());
        System.out.println("索引 1 的元素：" + stringRepo.get(1)); // 應為 Python

        System.out.println("移除 'Python' 是否成功：" + stringRepo.remove("Python")); // true
        System.out.println("移除後所有元素：" + stringRepo.getAll());
        System.out.println("越界獲取測試：" + stringRepo.get(5)); // 應印出警告並回傳 null

        // 2. 測試 Repository<Product>
        System.out.println("\n--- 測試 Repository<Product> ---");
        Repository<Product> productRepo = new Repository<>();
        Product p1 = new Product("P001", "Laptop");
        Product p2 = new Product("P002", "SmartPhone");
        Product p3 = new Product("P003", "Tablet");

        productRepo.add(p1);
        productRepo.add(p2);
        productRepo.add(p3);

        System.out.println("初始 Repository 大小：" + productRepo.size());
        System.out.println("所有商品：" + productRepo.getAll());

        // 藉由建立一個內容相同 (id相同) 的 Product 物件來測試移除
        Product duplicateP2 = new Product("P002", "SmartPhone Different Name");
        System.out.println("利用相同 ID 物件移除 P002 是否成功：" + productRepo.remove(duplicateP2)); // true，因為重寫了 equals
        System.out.println("移除後所有商品：" + productRepo.getAll());
    }
}
