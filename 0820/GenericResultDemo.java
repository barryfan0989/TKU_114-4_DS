class Result<T> {
    private final boolean success;
    private final String message;
    private final T data;

    private Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

public class GenericResultDemo {
    public static void main(String[] args) {
        System.out.println("=== 課堂實作題一：Generic Result ===");

        // 1. 建立成功的 Result<String>
        Result<String> stringResult = Result.success("Hello Java Generics", "取得字串成功");
        System.out.println(stringResult);
        // 取出資料時不需要 cast
        String text = stringResult.getData();
        System.out.println("成功取得的字串 (不用轉型)：" + text.toUpperCase());

        // 2. 建立成功的 Result<Integer>
        Result<Integer> intResult = Result.success(999, "取得整數成功");
        System.out.println(intResult);
        // 取出資料時不需要 cast
        Integer number = intResult.getData();
        System.out.println("成功取得的整數值 (不用轉型)：" + (number + 1));

        // 3. 建立失敗的 Result<Integer>，其 data 應為 null
        Result<Integer> failedResult = Result.fail("資料庫連線失敗");
        System.out.println(failedResult);
        System.out.println("失敗結果的資料 (應為 null)：" + failedResult.getData());

        // 4. 編譯期型態檢查防範說明
        System.out.println("\n[型態安全說明]：");
        System.out.println("如果試圖寫出：`Integer val = stringResult.getData();`，編譯器將會直接報錯：");
        System.out.println("Incompatible types: String cannot be converted to Integer. 這在編譯期就確保了安全。");
    }
}
