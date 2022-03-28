public class a {
    for(int i = 0; i < 100; i++) {

        boolean simple = true;

        for (int j = 2; j <= i / 2; i++ ) {
            if (i % j == 0) {
                simple = false;
            }
        }

        if (simple) {
            System.out.println(i);
        }
    }
}
