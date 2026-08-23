import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class StoreProduct implements Comparable<StoreProduct> {
    private final String id;
    private final String name;
    private final double price;
    private final int stock;

    StoreProduct(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public int compareTo(StoreProduct other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, Price: %.2f, Stock: %d]", id, name, price, stock);
    }
}

public class ProductComparatorPractice {
    public static void main(String[] args) {
        System.out.println("=== 課堂實作題五：多規則商品排序 ===");

        // 建立 5 筆商品資料，包含價格相同與庫存相同的案例
        List<StoreProduct> products = new ArrayList<>();
        products.add(new StoreProduct("P003", "Wireless Mouse", 29.99, 50));
        products.add(new StoreProduct("P001", "Mechanical Keyboard", 49.99, 100));
        products.add(new StoreProduct("P005", "Gaming Headset", 79.99, 100));
        products.add(new StoreProduct("P002", "IPS Monitor", 199.99, 50));
        products.add(new StoreProduct("P004", "Bluetooth Speaker", 49.99, 150));

        System.out.println("原始商品名單：");
        products.forEach(System.out::println);

        // 1. Natural Order: 依 id 升冪排序
        List<StoreProduct> sortedById = new ArrayList<>(products);
        sortedById.sort(null); // sort(null) 會使用 Comparable 進行排序
        System.out.println("\n1. 依 ID 升冪排序 (Natural Order)：");
        sortedById.forEach(System.out::println);

        // 2. 依 Price 升冪排序，若同價則依 Name 排序
        List<StoreProduct> sortedByPrice = new ArrayList<>(products);
        Comparator<StoreProduct> priceComparator = Comparator
                .comparingDouble(StoreProduct::getPrice)
                .thenComparing(StoreProduct::getName);
        sortedByPrice.sort(priceComparator);
        System.out.println("\n2. 依價格升冪，同價依名稱排序 (價格 49.99 有兩個)：");
        sortedByPrice.forEach(System.out::println);

        // 3. 依 Stock 降冪排序，若同庫存則依 ID 排序
        List<StoreProduct> sortedByStock = new ArrayList<>(products);
        Comparator<StoreProduct> stockComparator = Comparator
                .comparingInt(StoreProduct::getStock)
                .reversed()
                .thenComparing(StoreProduct::getId);
        sortedByStock.sort(stockComparator);
        System.out.println("\n3. 依庫存降冪，同庫存依 ID 排序 (庫存 100 與 50 均有同庫存商品)：");
        sortedByStock.forEach(System.out::println);

        // 驗證原始列表是否被更改
        System.out.println("\n驗證原始名單是否被破壞：");
        products.forEach(System.out::println);
    }
}
