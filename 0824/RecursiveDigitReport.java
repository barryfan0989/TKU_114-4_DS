public class RecursiveDigitReport {
    public static int digitSum(int number) {
        int absNum = Math.abs(number);
        return digitSumHelper(absNum);
    }

    private static int digitSumHelper(int number) {
        if (number < 10) {
            return number;
        }
        return (number % 10) + digitSumHelper(number / 10);
    }

    public static int digitCount(int number) {
        int absNum = Math.abs(number);
        return digitCountHelper(absNum);
    }

    private static int digitCountHelper(int number) {
        if (number < 10) {
            return 1;
        }
        return 1 + digitCountHelper(number / 10);
    }

    public static int countDigit(int number, int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit must be between 0 and 9");
        }
        int absNum = Math.abs(number);
        return countDigitHelper(absNum, digit);
    }

    private static int countDigitHelper(int number, int digit) {
        if (number < 10) {
            return (number == digit) ? 1 : 0;
        }
        int currentMatch = ((number % 10) == digit) ? 1 : 0;
        return currentMatch + countDigitHelper(number / 10, digit);
    }

    public static void main(String[] args) {
        int[] testCases = {50205, 0, -731};
        for (int tc : testCases) {
            System.out.println("Test Case: " + tc);
            System.out.println("  digitSum: " + digitSum(tc));
            System.out.println("  digitCount: " + digitCount(tc));
            System.out.println("  countDigit (for 0): " + countDigit(tc, 0));
            System.out.println("  countDigit (for 5): " + countDigit(tc, 5));
            System.out.println("  countDigit (for 7): " + countDigit(tc, 7));
            System.out.println();
        }
    }
}
