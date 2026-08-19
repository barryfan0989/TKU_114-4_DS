interface Exportable {
    String export();
}

class BackupDocument implements Exportable, Compressible {
    private final String title;
    private final String content;

    BackupDocument(String title, String content) {
        this.title = (title == null) ? "Untitled" : title;
        this.content = (content == null) ? "" : content;
    }

    @Override
    public String export() {
        return "[CSV/JSON Document] Title: " + title + ", Characters: " + content.length();
    }

    @Override
    public void compress() {
        System.out.println("Compressing document '" + title + "' -> " + title.replaceAll("\\s+", "_").toLowerCase() + ".zip");
    }
}

public class DocumentCapabilityDemo {
    public static void main(String[] args) {
        BackupDocument doc = new BackupDocument("Project Report 2026", "This is the system design documentation.");

        // Upcasting to interface references
        Exportable exportable = doc;
        Compressible compressible = doc;

        System.out.println("=== Document Capability Demo ===");

        // 1. Show functionality
        System.out.println("\nUsing Exportable reference:");
        System.out.println("Export output: " + exportable.export());
        // exportable.compress(); // Compile error: The method compress() is undefined for the type Exportable

        System.out.println("\nUsing Compressible reference:");
        compressible.compress();
        // compressible.export(); // Compile error: The method export() is undefined for the type Compressible

        // 2. Show they point to the same object
        System.out.println("\n--- Identity Check ---");
        System.out.println("Is 'exportable' reference the same object as 'compressible'? " + (exportable == compressible));
        System.out.println("Identity of exportable: " + System.identityHashCode(exportable));
        System.out.println("Identity of compressible: " + System.identityHashCode(compressible));
    }
}
