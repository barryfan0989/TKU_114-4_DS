class Task {
    private final String id;
    private final String name;

    Task(String id, String name) {
        this.id = id != null ? id.trim() : "";
        this.name = name != null ? name.trim() : "";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Task{ID='" + id + "', 名稱='" + name + "'}";
    }
}

class TaskNode {
    Task task;
    TaskNode next;

    TaskNode(Task task) {
        this.task = task;
        this.next = null;
    }
}

class TaskLinkedList {
    private TaskNode head;
    private int size;

    public boolean addFirst(Task task) {
        if (task == null || task.getId().isEmpty()) {
            return false;
        }
        if (containsId(task.getId())) {
            System.out.println("   [錯誤] addFirst: 任務 ID " + task.getId() + " 已存在，拒絕重複加入。");
            return false;
        }
        TaskNode newNode = new TaskNode(task);
        newNode.next = head;
        head = newNode;
        size++;
        return true;
    }

    public boolean addLast(Task task) {
        if (task == null || task.getId().isEmpty()) {
            return false;
        }
        if (containsId(task.getId())) {
            System.out.println("   [錯誤] addLast: 任務 ID " + task.getId() + " 已存在，拒絕重複加入。");
            return false;
        }
        TaskNode newNode = new TaskNode(task);
        if (head == null) {
            head = newNode;
        } else {
            TaskNode curr = head;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = newNode;
        }
        size++;
        return true;
    }

    public Task findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        String cleanId = id.trim();
        TaskNode curr = head;
        while (curr != null) {
            if (curr.task.getId().equals(cleanId)) {
                return curr.task;
            }
            curr = curr.next;
        }
        return null;
    }

    public boolean removeById(String id) {
        if (id == null || id.trim().isEmpty() || head == null) {
            return false;
        }
        String cleanId = id.trim();

        // 情況 1：刪除 head 節點
        if (head.task.getId().equals(cleanId)) {
            head = head.next;
            size--;
            return true;
        }

        // 情況 2：遍歷刪除中間或尾端節點
        TaskNode curr = head;
        while (curr.next != null) {
            if (curr.next.task.getId().equals(cleanId)) {
                curr.next = curr.next.next;
                size--;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public boolean insertAfter(String existingId, Task newTask) {
        if (existingId == null || newTask == null || newTask.getId().isEmpty()) {
            return false;
        }
        if (containsId(newTask.getId())) {
            System.out.println("   [錯誤] insertAfter: 新任務 ID " + newTask.getId() + " 已存在，拒絕重複加入。");
            return false;
        }
        String cleanExistId = existingId.trim();
        TaskNode curr = head;
        while (curr != null) {
            if (curr.task.getId().equals(cleanExistId)) {
                TaskNode newNode = new TaskNode(newTask);
                newNode.next = curr.next;
                curr.next = newNode;
                size++;
                return true;
            }
            curr = curr.next;
        }
        System.out.println("   [提示] insertAfter: 找不到基準任務 ID " + cleanExistId);
        return false;
    }

    public int size() {
        return size;
    }

    public boolean containsId(String id) {
        return findById(id) != null;
    }

    public void printAll() {
        if (head == null) {
            System.out.println("[]");
            return;
        }
        TaskNode curr = head;
        System.out.print("[");
        while (curr != null) {
            System.out.print(curr.task);
            curr = curr.next;
            if (curr != null) {
                System.out.print(" -> ");
            }
        }
        System.out.println("]");
    }
}

public class LinkedTaskListSystem {
    public static void main(String[] args) {
        System.out.println("=== 課後作業五：單向鏈結清單 ===");

        TaskLinkedList list = new TaskLinkedList();

        // 1. 測試空 list 的操作與輸出
        System.out.println("--- 測試空鏈結狀態 ---");
        System.out.print("初始鏈結內容：");
        list.printAll(); // []
        System.out.println("嘗試對空鏈結進行刪除 'T001' (預期為 false)：" + list.removeById("T001")); // false

        // 2. 測試新增功能 (addFirst 與 addLast)
        System.out.println("\n--- 測試新增元素 ---");
        list.addLast(new Task("T002", "寫作業"));
        list.addFirst(new Task("T001", "看教學影片"));
        list.addLast(new Task("T003", "吃晚餐"));
        
        System.out.print("新增後鏈結內容 (應為 T001 -> T002 -> T003)：\n   ");
        list.printAll();
        System.out.println("目前鏈結大小：" + list.size()); // 3

        // 3. 測試重複 ID 防範
        System.out.println("\n--- 測試重複 ID 拒絕 ---");
        list.addFirst(new Task("T002", "重複的工作")); // 應失敗

        // 4. 測試指定位置插入 (insertAfter)
        System.out.println("\n--- 測試指定節點後方插入 ---");
        // 在 T002 後方插入 T004
        boolean insertRes = list.insertAfter("T002", new Task("T004", "洗碗"));
        System.out.println("在 T002 後插入 T004 (預期為 true)：" + insertRes);
        System.out.print("插入後鏈結內容：\n   ");
        list.printAll(); // T001 -> T002 -> T004 -> T003

        // 5. 測試查詢
        System.out.println("\n--- 測試查詢功能 ---");
        System.out.println("查詢 T004: " + list.findById("T004"));
        System.out.println("查詢不存在的 T099: " + list.findById("T099"));

        // 6. 測試刪除的四個邊界情境
        System.out.println("\n--- 測試刪除的邊界條件 ---");

        // (a) 刪除不存在的 ID
        System.out.println("刪除不存在的 T099 (預期為 false)：" + list.removeById("T099"));

        // (b) 刪除中間節點 (T002)
        System.out.println("刪除中間節點 T002 (預期為 true)：" + list.removeById("T002"));
        System.out.print("刪除後鏈結內容：\n   ");
        list.printAll(); // T001 -> T004 -> T003

        // (c) 刪除 tail 節點 (T003)
        System.out.println("刪除 tail 節點 T003 (預期為 true)：" + list.removeById("T003"));
        System.out.print("刪除後鏈結內容：\n   ");
        list.printAll(); // T001 -> T004

        // (d) 刪除 head 節點 (T001)
        System.out.println("刪除 head 節點 T001 (預期為 true)：" + list.removeById("T001"));
        System.out.print("刪除後鏈結內容 (應只剩 T004)：\n   ");
        list.printAll(); // T004
        System.out.println("目前鏈結大小：" + list.size()); // 1
    }
}
