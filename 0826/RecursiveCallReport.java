public class RecursiveCallReport {
    static int sum(int[] data, int index, int depth) {
        String indent = "  ".repeat(depth);
        if (index >= data.length) {
            System.out.println(indent + "enter sum(index=" + index + ") -> base case, return 0");
            return 0;
        }
        
        int currentValue = data[index];
        System.out.println(indent + "enter sum(index=" + index + ") [current=" + currentValue + "]");
        
        int recursiveResult = sum(data, index + 1, depth + 1);
        int returnVal = currentValue + recursiveResult;
        
        System.out.println(indent + "return sum(index=" + index + "): " + currentValue + " + " + recursiveResult + " = " + returnVal);
        return returnVal;
    }

    public static void testArray(String description, int[] data) {
        System.out.println("--- Test: " + description + " ---");
        int total = sum(data, 0, 0);
        System.out.println("Result Sum: " + total + "\n");
    }

    public static void main(String[] args) {
        // Test Case 1: Normal array
        testArray("Normal Array", new int[]{10, 20, 30, 40});

        // Test Case 2: Single element array
        testArray("Single Element Array", new int[]{5});

        // Test Case 3: Empty array
        testArray("Empty Array", new int[]{});
    }
}
