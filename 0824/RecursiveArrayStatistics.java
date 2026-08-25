public class RecursiveArrayStatistics {
    public static int maximum(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        return maximumHelper(values, 0);
    }

    private static int maximumHelper(int[] values, int index) {
        if (index == values.length - 1) {
            return values[index];
        }
        return Math.max(values[index], maximumHelper(values, index + 1));
    }

    public static int minimum(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        return minimumHelper(values, 0);
    }

    private static int minimumHelper(int[] values, int index) {
        if (index == values.length - 1) {
            return values[index];
        }
        return Math.min(values[index], minimumHelper(values, index + 1));
    }

    public static int countAbove(int[] values, int threshold) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        return countAboveHelper(values, threshold, 0);
    }

    private static int countAboveHelper(int[] values, int threshold, int index) {
        if (index == values.length) {
            return 0;
        }
        int count = (values[index] > threshold) ? 1 : 0;
        return count + countAboveHelper(values, threshold, index + 1);
    }

    public static void main(String[] args) {
        int[] values = {3, 9, 2, 8, 5, 7, 1};
        System.out.println("Array: java.util.Arrays.toString(values) is not needed but let's print manually or format:");
        System.out.print("Array elements: ");
        for (int v : values) System.out.print(v + " ");
        System.out.println();
        
        System.out.println("Maximum: " + maximum(values));
        System.out.println("Minimum: " + minimum(values));
        System.out.println("Count above 5: " + countAbove(values, 5));
        
        // Test exceptions
        try {
            maximum(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for null array: " + e.getMessage());
        }
        
        try {
            minimum(new int[0]);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception for empty array: " + e.getMessage());
        }
    }
}
