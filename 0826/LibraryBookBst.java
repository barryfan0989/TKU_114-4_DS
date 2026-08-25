class Book {
    String isbn;
    String title;
    String author;
    boolean available;

    Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn + " | Title: " + title + " | Author: " + author + " | Available: " + available;
    }
}

class BookNode {
    Book book;
    BookNode left;
    BookNode right;

    BookNode(Book book) {
        this.book = book;
    }
}

class BookBst {
    BookNode root;

    boolean add(Book book) {
        if (book == null) return false;
        if (root == null) {
            root = new BookNode(book);
            return true;
        }
        BookNode current = root;
        while (true) {
            int cmp = book.isbn.compareTo(current.book.isbn);
            if (cmp == 0) {
                System.out.println("  [Error] Duplicate ISBN: " + book.isbn);
                return false;
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new BookNode(book);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new BookNode(book);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Book find(String isbn) {
        BookNode current = root;
        while (current != null) {
            int cmp = isbn.compareTo(current.book.isbn);
            if (cmp == 0) return current.book;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    boolean borrowBook(String isbn) {
        Book book = find(isbn);
        if (book == null) {
            System.out.println("  [Borrow Error] ISBN " + isbn + " not found.");
            return false;
        }
        if (!book.available) {
            System.out.println("  [Borrow Error] Book " + book.title + " is already borrowed.");
            return false;
        }
        book.available = false;
        System.out.println("  [Borrowed] Book: " + book.title + " (ISBN " + isbn + ")");
        return true;
    }

    boolean returnBook(String isbn) {
        Book book = find(isbn);
        if (book == null) {
            System.out.println("  [Return Error] ISBN " + isbn + " not found.");
            return false;
        }
        if (book.available) {
            System.out.println("  [Return Error] Book " + book.title + " is already in the library.");
            return false;
        }
        book.available = true;
        System.out.println("  [Returned] Book: " + book.title + " (ISBN " + isbn + ")");
        return true;
    }

    boolean remove(String isbn) {
        Book book = find(isbn);
        if (book == null) {
            System.out.println("  [Remove Error] ISBN " + isbn + " not found.");
            return false;
        }
        // CRITICAL RULE: Borrowed book (available == false) cannot be removed
        if (!book.available) {
            System.out.println("  [Remove Error] Book: " + book.title + " (ISBN " + isbn + ") is currently BORROWED and cannot be removed.");
            return false;
        }
        root = remove(root, isbn);
        System.out.println("  [Removed] Book: " + book.title + " (ISBN " + isbn + ") removed from library index.");
        return true;
    }

    private BookNode remove(BookNode node, String isbn) {
        if (node == null) return null;
        int cmp = isbn.compareTo(node.book.isbn);
        if (cmp < 0) {
            node.left = remove(node.left, isbn);
        } else if (cmp > 0) {
            node.right = remove(node.right, isbn);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            BookNode successor = minimumNode(node.right);
            node.book = successor.book;
            node.right = remove(node.right, successor.book.isbn);
        }
        return node;
    }

    private BookNode minimumNode(BookNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    void printRange(String lowIsbn, String highIsbn) {
        System.out.println("--- Library Catalog Range [" + lowIsbn + ", " + highIsbn + "] ---");
        if (lowIsbn.compareTo(highIsbn) > 0) {
            System.out.println("  [Error] Invalid range parameters.");
            return;
        }
        printRange(root, lowIsbn, highIsbn);
        System.out.println();
    }

    private void printRange(BookNode node, String lowIsbn, String highIsbn) {
        if (node == null) return;
        if (node.book.isbn.compareTo(lowIsbn) >= 0) {
            printRange(node.left, lowIsbn, highIsbn);
        }
        if (node.book.isbn.compareTo(lowIsbn) >= 0 && node.book.isbn.compareTo(highIsbn) <= 0) {
            System.out.println("  " + node.book);
        }
        if (node.book.isbn.compareTo(highIsbn) <= 0) {
            printRange(node.right, lowIsbn, highIsbn);
        }
    }

    void inorderReport() {
        inorder(root);
    }

    private void inorder(BookNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.book);
        inorder(node.right);
    }
}

public class LibraryBookBst {
    public static void main(String[] args) {
        BookBst library = new BookBst();

        System.out.println("=== 1. Adding Books to Library ===");
        library.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch"));
        library.add(new Book("978-0132350884", "Clean Code", "Robert C. Martin"));
        library.add(new Book("978-0596007126", "Head First Design Patterns", "Eric Freeman"));
        library.add(new Book("978-0262033848", "Introduction to Algorithms", "Thomas H. Cormen"));
        System.out.println();

        System.out.println("=== 2. Library Catalog ===");
        library.inorderReport();
        System.out.println();

        System.out.println("=== 3. Library Transactions ===");
        // Borrow Clean Code
        library.borrowBook("978-0132350884");
        // Try to remove Clean Code (should fail since it is checked out)
        library.remove("978-0132350884");
        System.out.println();

        // Return Clean Code
        library.returnBook("978-0132350884");
        // Remove Clean Code now (should succeed)
        library.remove("978-0132350884");
        System.out.println();

        System.out.println("=== 4. Library Catalog after transactions ===");
        library.inorderReport();
        System.out.println();

        System.out.println("=== 5. Range Search (ISBNs) ===");
        library.printRange("978-0200000000", "978-0600000000");
    }
}
