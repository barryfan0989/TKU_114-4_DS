public class RecursiveTextTools {
    public static String reverse(String text) {
        if (text == null) return null;
        if (text.isEmpty()) return "";
        return reverse(text.substring(1)) + text.charAt(0);
    }

    public static boolean isPalindrome(String text) {
        if (text == null) return false;
        // Clean text: remove whitespaces and convert to lowercase
        String cleaned = text.replaceAll("\\s+", "").toLowerCase();
        return isPalindromeHelper(cleaned);
    }

    private static boolean isPalindromeHelper(String text) {
        if (text.length() <= 1) {
            return true;
        }
        if (text.charAt(0) != text.charAt(text.length() - 1)) {
            return false;
        }
        return isPalindromeHelper(text.substring(1, text.length() - 1));
    }

    public static int countCharacter(String text, char target) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        int match = (text.charAt(0) == target) ? 1 : 0;
        return match + countCharacter(text.substring(1), target);
    }

    public static void main(String[] args) {
        // Test cases for reverse
        String[] reverseTestCases = {"hello", "A", "", null};
        System.out.println("=== Reverse Tests ===");
        for (String tc : reverseTestCases) {
            System.out.println("Original: " + (tc == null ? "null" : "\"" + tc + "\"") 
                               + " -> Reversed: " + (reverse(tc) == null ? "null" : "\"" + reverse(tc) + "\""));
        }
        System.out.println();

        // Test cases for isPalindrome
        String[] palindromeTestCases = {"", "A", "Level", "a car a man a maraca", "hello", "step on no pets"};
        System.out.println("=== Palindrome Tests (Ignoring Case & Spaces) ===");
        for (String tc : palindromeTestCases) {
            System.out.println("Text: \"" + tc + "\" -> Palindrome: " + isPalindrome(tc));
        }
        System.out.println();

        // Test cases for countCharacter
        System.out.println("=== Count Character Tests ===");
        String countText = "banana";
        char target = 'a';
        System.out.println("Count of '" + target + "' in \"" + countText + "\": " + countCharacter(countText, target));
        System.out.println("Count of 'n' in \"" + countText + "\": " + countCharacter(countText, 'n'));
        System.out.println("Count of 'z' in \"" + countText + "\": " + countCharacter(countText, 'z'));
    }
}
