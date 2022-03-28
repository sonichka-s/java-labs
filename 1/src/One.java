import java.util.Scanner;
public class One {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {

            boolean simple = true;

            for (int j = 2; j * j <= i; j++) {
                if (i % j == 0) {
                    simple = false;
                    break;
                }
            }

            if (simple) {
                System.out.println(i);
            }
        }
    }
}