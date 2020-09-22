import java.util.Scanner;

public class Main {

    public static long fib(long n){
        // write your code here
        long value = 0;
        if (n == 0) {
            value = 0;
        } else if (n == 1) {
            value = 1;
        }
        else if (n > 1) {
            value = fib(n - 2) - fib(n - 1);
        }
        return value;
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(fib(n));
    }
}