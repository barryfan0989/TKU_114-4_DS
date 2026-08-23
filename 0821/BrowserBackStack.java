import java.util.ArrayDeque;
import java.util.Deque;

public class BrowserBackStack {
    private final Deque<String> backStack = new ArrayDeque<>();
    private String currentUrl = "NONE";

    public void visit(String url) {
        if (url == null || url.trim().isEmpty()) {
            return;
        }
        if (!currentUrl.equals("NONE")) {
            backStack.push(currentUrl);
        }
        currentUrl = url;
        System.out.println("造訪網頁：" + currentUrl);
    }

    public String back() {
        if (backStack.isEmpty()) {
            System.out.println("[提示] 無法返回：歷史紀錄為空");
            return "EMPTY";
        }
        currentUrl = backStack.pop();
        System.out.println("返回網頁：" + currentUrl);
        return currentUrl;
    }

    public String current() {
        return currentUrl;
    }

    public int historySize() {
        return backStack.size();
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題二：瀏覽器返回功能 ===");

        BrowserBackStack browser = new BrowserBackStack();

        // 進行連續 5 個以上的操作
        System.out.println("目前網頁：" + browser.current()); // 應為 NONE

        // 1. 造訪 Google
        browser.visit("https://www.google.com");
        System.out.println("目前網頁：" + browser.current());
        System.out.println("歷史回退堆疊大小：" + browser.historySize()); // 0

        // 2. 造訪 GitHub
        browser.visit("https://github.com");
        System.out.println("目前網頁：" + browser.current());
        System.out.println("歷史回退堆疊大小：" + browser.historySize()); // 1 (Google 進入 backStack)

        // 3. 造訪 Dev.java
        browser.visit("https://dev.java");
        System.out.println("目前網頁：" + browser.current());

        // 4. 按下返回鍵 (回退到 GitHub)
        String back1 = browser.back();
        System.out.println("回退結果：" + back1); // github
        System.out.println("目前網頁：" + browser.current()); // github

        // 5. 按下返回鍵 (回退到 Google)
        String back2 = browser.back();
        System.out.println("回退結果：" + back2); // google

        // 6. 按下返回鍵 (已無歷史紀錄，不應拋出例外，應回傳 EMPTY)
        String back3 = browser.back();
        System.out.println("回退結果 (空歷史測試)：" + back3); // EMPTY
        System.out.println("目前網頁 (應維持在 Google)：" + browser.current());

        // 7. 再次造訪 StackOverflow
        browser.visit("https://stackoverflow.com");
        System.out.println("目前網頁：" + browser.current());
        System.out.println("歷史回退堆疊大小：" + browser.historySize()); // 1 (Google 再次進入 backStack)
    }
}
