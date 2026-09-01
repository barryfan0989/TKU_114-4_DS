import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SocialNetworkGraph {
    private final Map<String, Set<String>> network = new LinkedHashMap<>();

    public boolean addUser(String user) {
        if (user == null || user.isBlank()) {
            return false;
        }
        String normalized = user.trim();
        return network.putIfAbsent(normalized, new LinkedHashSet<>()) == null;
    }

    public boolean addFriendship(String user1, String user2) {
        if (user1 == null || user2 == null || user1.trim().equalsIgnoreCase(user2.trim())) {
            return false;
        }
        String u1 = user1.trim();
        String u2 = user2.trim();

        if (!network.containsKey(u1) || !network.containsKey(u2)) {
            return false;
        }

        boolean added1 = network.get(u1).add(u2);
        boolean added2 = network.get(u2).add(u1);
        return added1 || added2;
    }

    public boolean removeFriendship(String user1, String user2) {
        if (user1 == null || user2 == null) return false;
        String u1 = user1.trim();
        String u2 = user2.trim();

        if (!network.containsKey(u1) || !network.containsKey(u2)) {
            return false;
        }

        boolean removed1 = network.get(u1).remove(u2);
        boolean removed2 = network.get(u2).remove(u1);
        return removed1 || removed2;
    }

    public Set<String> getFriends(String user) {
        if (user == null) return Collections.emptySet();
        Set<String> friends = network.get(user.trim());
        return friends == null ? Collections.emptySet() : Collections.unmodifiableSet(friends);
    }

    public Set<String> getMutualFriends(String user1, String user2) {
        if (user1 == null || user2 == null) return Collections.emptySet();
        Set<String> f1 = network.get(user1.trim());
        Set<String> f2 = network.get(user2.trim());
        if (f1 == null || f2 == null) return Collections.emptySet();

        Set<String> mutual = new TreeSet<>(f1);
        mutual.retainAll(f2);
        return mutual;
    }

    public List<String> getIsolatedUsers() {
        List<String> isolated = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : network.entrySet()) {
            if (entry.getValue().isEmpty()) {
                isolated.add(entry.getKey());
            }
        }
        return isolated;
    }

    public int userCount() {
        return network.size();
    }

    public int friendshipCount() {
        int degreeSum = 0;
        for (Set<String> friends : network.values()) {
            degreeSum += friends.size();
        }
        return degreeSum / 2; // 無向圖每條邊算兩次度數
    }

    public void printNetwork() {
        System.out.println("==================== 社群網路好友關係列表 ====================");
        System.out.printf("總使用者數: %d | 總好友關係對數: %d%n", userCount(), friendshipCount());
        System.out.println("-------------------------------------------------------------");
        for (Map.Entry<String, Set<String>> entry : network.entrySet()) {
            System.out.printf("%-10s -> 好友數: %d | 好友清單: %s%n",
                    entry.getKey(), entry.getValue().size(), entry.getValue());
        }
        System.out.println("=============================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題五：社群 Adjacency List (SocialNetworkGraph) ===\n");
        SocialNetworkGraph graph = new SocialNetworkGraph();

        // 註冊使用者 (含孤立使用者)
        for (String user : List.of("Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Hermit")) {
            graph.addUser(user);
        }

        // 建立好友關係 (Undirected Edges)
        graph.addFriendship("Alice", "Bob");
        graph.addFriendship("Alice", "Charlie");
        graph.addFriendship("Alice", "David");
        graph.addFriendship("Bob", "Charlie");
        graph.addFriendship("Bob", "Emma");
        graph.addFriendship("Charlie", "David");
        graph.addFriendship("Charlie", "Emma");
        graph.addFriendship("David", "Emma");
        graph.addFriendship("Emma", "Frank");

        // 顯示社群網路
        graph.printNetwork();

        // 查詢共同好友 (Mutual Friends)
        System.out.println("--- 共同好友 (Mutual Friends) 查詢 ---");
        System.out.println("Alice 與 Charlie 的共同好友: " + graph.getMutualFriends("Alice", "Charlie"));
        System.out.println("Bob 與 David 的共同好友: " + graph.getMutualFriends("Bob", "David"));
        System.out.println("Alice 與 Frank 的共同好友: " + graph.getMutualFriends("Alice", "Frank"));

        // 查詢孤立使用者 (Degree = 0)
        System.out.println("\n--- 孤立使用者 (無任何好友) 查詢 ---");
        System.out.println("目前孤立使用者: " + graph.getIsolatedUsers());

        // 解除好友關係
        System.out.println("\n--- 解除好友關係: [Alice <-> Bob] ---");
        graph.removeFriendship("Alice", "Bob");
        System.out.println("Alice 的好友清單: " + graph.getFriends("Alice"));
        System.out.println("Bob 的好友清單: " + graph.getFriends("Bob"));
        System.out.println("解除後總好友關係數: " + graph.friendshipCount());
    }
}
