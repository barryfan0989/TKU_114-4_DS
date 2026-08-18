class Book {
    private String id;
    private String name;
    private int price;
    private int stock;

    Book(String id, String name, int price, int stock) {
        this.id = id == null || id.trim().isEmpty() ? "Unknown" : id.trim();
        this.name = name == null || name.trim().isEmpty() ? "Untitled" : name.trim();
        this.price = Math.max(0, price);
        this.stock = Math.max(0, stock);
    }

    int getTotalValue() {
        return price * stock;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "Book{id='" + id + "', name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}

public class BookArrayReport {
    public static void main(String[] args) {
        Book[] books = {
            new Book("B001", "Java Programming", 500, 5),
            new Book("B002", "Data Structures & Algorithms", 600, 2),
            new Book("B003", "Design Patterns", 700, 10),
            new Book("B004", "Database System Concepts", 800, 1)
        };

        System.out.println("1. All Books:");
        for (Book book : books) {
            System.out.println(book);
        }

        System.out.println("\n2. Total Inventory Value:");
        int totalValue = 0;
        for (Book book : books) {
            totalValue += book.getTotalValue();
        }
        System.out.println("Total Value: $" + totalValue);

        System.out.println("\n3. Highest Priced Book:");
        Book highest = books[0];
        for (Book book : books) {
            if (book.getPrice() > highest.getPrice()) {
                highest = book;
            }
        }
        System.out.println(highest);

        System.out.println("\n4. Books with stock <= 3:");
        for (Book book : books) {
            if (book.getStock() <= 3) {
                System.out.println(book);
            }
        }
    }
}
