interface ReportExporter {
    void export(String title, int[] values);
}

class CsvExporter implements ReportExporter {
    @Override
    public void export(String title, int[] values) {
        String safeTitle = (title == null || title.trim().isEmpty()) ? "Unnamed Report" : title.trim();
        int[] safeValues = (values == null) ? new int[0] : values;

        StringBuilder sb = new StringBuilder();
        sb.append("CSV Export -> \"").append(safeTitle).append("\"\n");
        sb.append("Data: ");
        for (int i = 0; i < safeValues.length; i++) {
            sb.append(safeValues[i]);
            if (i < safeValues.length - 1) {
                sb.append(", ");
            }
        }
        System.out.println(sb.toString());
    }
}

class JsonExporter implements ReportExporter {
    @Override
    public void export(String title, int[] values) {
        String safeTitle = (title == null || title.trim().isEmpty()) ? "Unnamed Report" : title.trim();
        int[] safeValues = (values == null) ? new int[0] : values;

        StringBuilder sb = new StringBuilder();
        sb.append("JSON Export -> {\n");
        sb.append("  \"title\": \"").append(safeTitle).append("\",\n");
        sb.append("  \"values\": [");
        for (int i = 0; i < safeValues.length; i++) {
            sb.append(safeValues[i]);
            if (i < safeValues.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]\n}");
        System.out.println(sb.toString());
    }
}

class TextExporter implements ReportExporter {
    @Override
    public void export(String title, int[] values) {
        String safeTitle = (title == null || title.trim().isEmpty()) ? "Unnamed Report" : title.trim();
        int[] safeValues = (values == null) ? new int[0] : values;

        StringBuilder sb = new StringBuilder();
        sb.append("TEXT Export -> Report Title: ").append(safeTitle).append("\n");
        sb.append("Values list: ");
        if (safeValues.length == 0) {
            sb.append("(empty)");
        } else {
            for (int val : safeValues) {
                sb.append("[").append(val).append("] ");
            }
        }
        System.out.println(sb.toString());
    }
}

public class ReportExporterFactory {
    public static ReportExporter createExporter(String format) {
        if (format == null) {
            return new TextExporter();
        }
        switch (format.trim().toLowerCase()) {
            case "csv":
                return new CsvExporter();
            case "json":
                return new JsonExporter();
            case "text":
            default:
                // Default fallback: return TextExporter
                return new TextExporter();
        }
    }

    public static void exportReport(ReportExporter exporter, String title, int[] values) {
        if (exporter == null) {
            System.out.println("[Error] Exporter is null. Cannot export report.");
            return;
        }
        // Safely invoke polymorphic export
        exporter.export(title, values);
    }

    public static void main(String[] args) {
        System.out.println("=== Report Exporter Factory ===");

        String title = "Quarterly Sales 2026";
        int[] data = {120, 150, 90, 200, 250};

        // 1. CSV Format
        System.out.println("\n--- Requesting CSV Exporter ---");
        ReportExporter csv = createExporter("CSV");
        exportReport(csv, title, data);

        // 2. JSON Format
        System.out.println("\n--- Requesting JSON Exporter ---");
        ReportExporter json = createExporter("json");
        exportReport(json, title, data);

        // 3. Unsupported Format -> Falls back to TextExporter
        System.out.println("\n--- Requesting XML Exporter (Unsupported) ---");
        ReportExporter xml = createExporter("xml");
        exportReport(xml, title, data);

        // 4. Null Format -> Falls back to TextExporter
        System.out.println("\n--- Requesting Null Exporter ---");
        ReportExporter nullFormat = createExporter(null);
        exportReport(nullFormat, title, data);

        // 5. Test Null Data Protection
        System.out.println("\n--- Testing Safe Null-Data Export ---");
        ReportExporter txt = createExporter("text");
        exportReport(txt, "Empty Data Test", null);
    }
}
