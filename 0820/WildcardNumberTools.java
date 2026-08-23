import java.util.ArrayList;
import java.util.List;

public class WildcardNumberTools {

    public static double average(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Number number : values) {
            if (number != null) {
                sum += number.doubleValue();
            }
        }
        return sum / values.size();
    }

    public static double maximum(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return Double.NaN;
        }
        double max = Double.NEGATIVE_INFINITY;
        boolean hasValidNumber = false;
        for (Number number : values) {
            if (number != null) {
                double val = number.doubleValue();
                if (val > max) {
                    max = val;
                }
                hasValidNumber = true;
            }
        }
        return hasValidNumber ? max : Double.NaN;
    }

    public static void addRange(List<? super Integer> target, int start, int end) {
        if (target == null) {
            System.out.println("[Warning] addRange: Target list is null.");
            return;
        }
        if (start > end) {
            return;
        }
        for (int i = start; i <= end; i++) {
            target.add(i);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 課堂實作題四：Wildcard 數值工具 ===");

        // 1. 測試 average 與 maximum 接收 List<Integer>
        List<Integer> intList = List.of(10, 20, 30, 40);
        System.out.println("整數列表：" + intList);
        System.out.println("整數平均值 (應為 25.0)：" + average(intList));
        System.out.println("整數最大值 (應為 40.0)：" + maximum(intList));

        // 2. 測試 average 與 maximum 接收 List<Double>
        List<Double> doubleList = List.of(1.5, 2.5, 3.5, 4.5);
        System.out.println("\n浮點數列表：" + doubleList);
        System.out.println("浮點數平均值 (應為 3.0)：" + average(doubleList));
        System.out.println("浮點數最大值 (應為 4.5)：" + maximum(doubleList));

        // 3. 測試空列表與包含 null 的列表
        List<Integer> emptyList = new ArrayList<>();
        System.out.println("\n空列表平均值：" + average(emptyList)); // 應為 0.0
        System.out.println("空列表最大值：" + maximum(emptyList)); // 應為 NaN

        List<Integer> listWithNull = new ArrayList<>();
        listWithNull.add(10);
        listWithNull.add(null);
        listWithNull.add(20);
        System.out.println("含 null 列表：" + listWithNull);
        System.out.println("含 null 列表平均值 (排除 null 加總，除以總 size=3)：" + average(listWithNull)); // 應為 10.0
        System.out.println("含 null 列表最大值：" + maximum(listWithNull)); // 應為 20.0

        // 4. 測試 addRange
        List<Integer> integers = new ArrayList<>();
        addRange(integers, 5, 10);
        System.out.println("\n測試 addRange(List<Integer>) 加入 5 到 10：");
        System.out.println(integers); // 應為 [5, 6, 7, 8, 9, 10]

        List<Number> numbers = new ArrayList<>();
        addRange(numbers, 1, 3);
        System.out.println("測試 addRange(List<Number>) 加入 1 到 3：");
        System.out.println(numbers);  // 應為 [1, 2, 3]

        List<Integer> invalidRange = new ArrayList<>();
        addRange(invalidRange, 10, 5);
        System.out.println("測試 addRange 當 start > end：");
        System.out.println(invalidRange); // 應為空 []
    }
}
