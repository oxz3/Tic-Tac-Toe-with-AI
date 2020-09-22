import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // write your code here
        int n = scanner.nextInt();
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        Random random = new Random(a + b);

        int runningTotal = 0;
        for (int i = 0; i < n; i++) {
            runningTotal = runningTotal + random.nextInt(b - a + 1) + a;
        }
        System.out.print(runningTotal);
    }
}