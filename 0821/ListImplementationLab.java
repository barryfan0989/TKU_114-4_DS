import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListImplementationLab {

    public static void addElements(List<Integer> list, int count) {
        if (list == null) return;
        for (int i = 1; i <= count; i++) {
            list.add(i * 10);
        }
    }

    public static void insertElements(List<Integer> list, int index, int value) {
        if (list == null) return;
        if (index >= 0 && index <= list.size()) {
            list.add(index, value);
        } else {
            System.out.println("[Warning] insertElements: Index " + index + " out of bounds for size " + list.size());
        }
    }

    public static int findElement(List<Integer> list, int target) {
        if (list == null) return -1;
        return list.indexOf(target);
    }

    public static void removeElement(List<Integer> list, int index) {
        if (list == null) return;
        if (index >= 0 && index < list.size()) {
            list.remove(index);
        } else {
            System.out.println("[Warning] removeElement: Index " + index + " out of bounds for size " + list.size());
        }
    }

    public static int sumList(List<Integer> list) {
        if (list == null) return 0;
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題一：List Implementation 比較 ===");

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // 1. 尾端新增
        addElements(arrayList, 5);
        addElements(linkedList, 5);
        System.out.println("尾端新增 5 個元素後：");
        System.out.println("   ArrayList:  " + arrayList);
        System.out.println("   LinkedList: " + linkedList);

        // 2. 指定位置插入
        insertElements(arrayList, 2, 99);
        insertElements(linkedList, 2, 99);
        System.out.println("\n在 index=2 插入 99 後：");
        System.out.println("   ArrayList:  " + arrayList);
        System.out.println("   LinkedList: " + linkedList);

        // 3. 搜尋
        int target = 40;
        System.out.println("\n搜尋目標 " + target + " 的索引位置：");
        System.out.println("   ArrayList index:  " + findElement(arrayList, target));
        System.out.println("   LinkedList index: " + findElement(linkedList, target));

        // 4. 刪除指定索引
        removeElement(arrayList, 3);
        removeElement(linkedList, 3);
        System.out.println("\n刪除 index=3 元素後：");
        System.out.println("   ArrayList:  " + arrayList);
        System.out.println("   LinkedList: " + linkedList);

        // 5. 加總
        System.out.println("\n加總結果：");
        System.out.println("   ArrayList sum:  " + sumList(arrayList));
        System.out.println("   LinkedList sum: " + sumList(linkedList));

        // 6. 內部成本分析說明
        System.out.println("\n[內部成本與結構差異說明]：");
        System.out.println("1. 隨機存取 (get/set)：");
        System.out.println("   * ArrayList  - O(1)。因底層是連續陣列，可透過索引直接計算記憶體位址取得元素。");
        System.out.println("   * LinkedList - O(N)。必須從 head 或 tail 逐一沿著 Node.next / Node.prev 指標走訪。");
        System.out.println("2. 中間插入與刪除 (add/remove at index)：");
        System.out.println("   * ArrayList  - O(N)。插入或刪除後，該索引之後的所有元素引用皆須向後或向前平移一格。");
        System.out.println("   * LinkedList - 尋找到該位置需 O(N)，但找到後修改 Node 連結只需 O(1)，且無需搬移資料。");
        System.out.println("3. 記憶體開銷與區域性：");
        System.out.println("   * ArrayList  - 記憶體連續，快取友好性高 (Cache locality)，每個槽僅保存物件引用，無指標開銷。");
        System.out.println("   * LinkedList - 每個節點都需要額外包裝成 Node 物件，保存 prev 與 next 指標，記憶體額外開銷較大。");
    }
}
