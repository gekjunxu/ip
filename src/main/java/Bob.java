import java.util.Scanner;
import java.io.*;

public class Bob {
    public static void main(String[] args) {
        String line = "\t____________________________________________________________\n";
        String hello = line + "\tHello! I'm Bob\n\tWhat can I do for you?\n" + line;
        String bye = line + "\tBye. Hope to see you again soon!\n" + line;
        System.out.println(hello);

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (!input.equalsIgnoreCase("bye")) {
            String output = String.format(line + "\t" + input + "\n" +  line);
            System.out.println(output);
            input = sc.nextLine();
        }
        System.out.println(bye);

    }
}
