import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class WebsiteLinkGraph {
    private final Map<String, Set<String>> outgoingLinks = new LinkedHashMap<>();
    private final Map<String, Set<String>> incomingLinks = new LinkedHashMap<>();

    public boolean addPage(String url) {
        if (url == null || url.isBlank()) return false;
        String page = url.trim();
        outgoingLinks.putIfAbsent(page, new LinkedHashSet<>());
        incomingLinks.putIfAbsent(page, new LinkedHashSet<>());
        return true;
    }

    public boolean addLink(String fromPage, String toPage) {
        if (fromPage == null || toPage == null) return false;
        String from = fromPage.trim();
        String to = toPage.trim();

        if (from.equalsIgnoreCase(to)) return false; // 忽略自連結

        addPage(from);
        addPage(to);

        boolean added = outgoingLinks.get(from).add(to);
        incomingLinks.get(to).add(from);
        return added;
    }

    public Set<String> getOutgoingLinks(String page) {
        if (page == null) return Collections.emptySet();
        Set<String> set = outgoingLinks.get(page.trim());
        return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
    }

    public int getIncomingCount(String page) {
        if (page == null) return 0;
        Set<String> set = incomingLinks.get(page.trim());
        return set == null ? 0 : set.size();
    }

    public List<String> getDeadEndPages() {
        List<String> deadEnds = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : outgoingLinks.entrySet()) {
            if (entry.getValue().isEmpty()) {
                deadEnds.add(entry.getKey());
            }
        }
        return deadEnds;
    }

    public List<String> getOrphanPages() {
        List<String> orphans = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : incomingLinks.entrySet()) {
            if (entry.getValue().isEmpty()) {
                orphans.add(entry.getKey());
            }
        }
        return orphans;
    }

    public void generateWebReport() {
        System.out.println("============================== 網站超連結結構分析報告 ==============================");
        System.out.printf("%-18s | %-10s | %-10s | %s%n",
                "頁面 URL", "In-Degree", "Out-Degree", "連出頁面清單 (Outgoing Links)");
        System.out.println("-------------------------------------------------------------------------------------");

        for (String page : outgoingLinks.keySet()) {
            int inCount = incomingLinks.get(page).size();
            Set<String> outSet = outgoingLinks.get(page);
            System.out.printf("%-18s | %-10d | %-10d | %s%n",
                    page, inCount, outSet.size(), outSet.isEmpty() ? "(無連出連結)" : outSet);
        }

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("【結構診斷結論】");
        System.out.println("1. 終點頁面 (Dead Ends, Out-degree = 0): " + getDeadEndPages());
        System.out.println("2. 孤立/起點頁面 (Orphan Pages, In-degree = 0): " + getOrphanPages());
        System.out.println("=====================================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業五：網站連結 Graph (WebsiteLinkGraph) ===\n");
        WebsiteLinkGraph web = new WebsiteLinkGraph();

        // 建立網站結構
        web.addLink("/home", "/products");
        web.addLink("/home", "/about");
        web.addLink("/home", "/blog");

        web.addLink("/products", "/cart");
        web.addLink("/products", "/home");

        web.addLink("/blog", "/home");
        web.addLink("/blog", "/article-1");
        web.addLink("/blog", "/article-2");

        web.addLink("/cart", "/checkout");
        // /checkout 是 dead end (只進不出)

        // 獨立行銷推廣頁面 (Landing page，無任何內部頁面連入)
        web.addLink("/promo-event", "/products");

        // 獨立隱私政策頁面 (無連入且無連出)
        web.addPage("/privacy-policy");

        // 產出報告
        web.generateWebReport();
    }
}
