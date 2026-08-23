import java.util.ArrayDeque;
import java.util.Deque;

public class TextEditorHistory {
    private final Deque<String> undoStack = new ArrayDeque<>();
    private final Deque<String> redoStack = new ArrayDeque<>();
    private String currentText = "";

    public void type(String text) {
        if (text == null) return;
        // 把目前的狀態推入 undoStack
        undoStack.push(currentText);
        // 更新當前文字
        currentText += text;
        // 一旦有新輸入，必須清空 redoStack
        redoStack.clear();
        System.out.println("輸入: \"" + text + "\" | 目前內容: \"" + currentText + "\"");
        printStacks();
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("復原 (Undo): [提示] 已到達最先狀態，無法復原");
            return;
        }
        // 將目前狀態推入 redoStack
        redoStack.push(currentText);
        // 從 undoStack 取回前一次狀態
        currentText = undoStack.pop();
        System.out.println("復原 (Undo) -> 目前內容: \"" + currentText + "\"");
        printStacks();
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("重做 (Redo): [提示] 已到達最新狀態，無法重做");
            return;
        }
        // 將目前狀態推回 undoStack
        undoStack.push(currentText);
        // 從 redoStack 取回被復原的狀態
        currentText = redoStack.pop();
        System.out.println("重做 (Redo) -> 目前內容: \"" + currentText + "\"");
        printStacks();
    }

    public String getCurrentText() {
        return currentText;
    }

    private void printStacks() {
        System.out.println("   [Undo 棧]: " + undoStack);
        System.out.println("   [Redo 棧]: " + redoStack);
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== 課後作業一：文字編輯 Undo/Redo ===");

        TextEditorHistory editor = new TextEditorHistory();
        System.out.println("初始狀態: \"" + editor.getCurrentText() + "\"\n");

        // 1. 輸入一些文字
        editor.type("Java");
        editor.type(" is");
        editor.type(" awesome!");

        // 2. 連續執行 Undo
        editor.undo(); // 回退到 "Java is"
        editor.undo(); // 回退到 "Java"
        editor.undo(); // 回退到 ""
        editor.undo(); // 空棧測試，應顯示提示

        // 3. 執行 Redo
        editor.redo(); // 重回到 "Java"
        editor.redo(); // 重回到 "Java is"

        // 4. 在此時輸入新內容，驗證 Redo 棧會被清空
        editor.type(" is cool."); // 應清空 redoStack

        // 5. 嘗試對剛清空的 Redo 棧執行 Redo (應無法重做)
        editor.redo(); 

        // 6. 再執行一次 Undo
        editor.undo(); // 回到 "Java"
    }
}
