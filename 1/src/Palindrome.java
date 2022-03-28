import java.util.Scanner;
public class Palindrome {

    public static boolean isPalindrome(String str) {
        for (int i = 0; i < str.length() / 2; i++) {
            if (str.charAt(i) != str.charAt(str.length() - i - 1))
                return false;
        }

        return true;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        if (isPalindrome(input))
            System.out.println(input + " is palindrome");
        else
            System.out.println(input + " is NOT palindrome");
    }
}