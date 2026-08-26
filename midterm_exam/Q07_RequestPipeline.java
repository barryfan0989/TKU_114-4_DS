import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Q07_RequestPipeline {
    public static boolean isBalanced(String text) {
        if (text == null) {
            return false;
        }
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : text.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char open = stack.pop();
                if ((c == ')' && open != '(') ||
                        (c == ']' && open != '[') ||
                        (c == '}' && open != '{')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static List<String> process(String[] commands) {
        List<String> result = new ArrayList<>();
        if (commands == null) {
            return result;
        }
        Deque<String> urgentQueue = new ArrayDeque<>();
        Deque<String> normalQueue = new ArrayDeque<>();

        for (String cmd : commands) {
            if (cmd == null) {
                continue;
            }
            String trimmed = cmd.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.equals("PROCESS")) {
                if (!urgentQueue.isEmpty()) {
                    result.add(urgentQueue.pollFirst());
                } else if (!normalQueue.isEmpty()) {
                    result.add(normalQueue.pollFirst());
                } else {
                    result.add("EMPTY");
                }
            } else {
                String[] parts = trimmed.split("\\s+", 2);
                if (parts.length == 2) {
                    String action = parts[0];
                    String id = parts[1].trim();
                    if (!id.isEmpty()) {
                        if (action.equals("NORMAL")) {
                            normalQueue.addLast(id);
                        } else if (action.equals("URGENT")) {
                            urgentQueue.addLast(id);
                        }
                    }
                }
            }
        }
        return result;
    }
}
