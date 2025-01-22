import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Bob {
    public static void main(String[] args) {
        String line = "\t____________________________________________________________\n";
        String hello = line + "\tHello! I'm Bob\n\tWhat can I do for you?\n" + line;
        String bye = line + "\tBye. Hope to see you again soon!\n" + line;
        System.out.println(hello);

        // Initialise scanner
        Scanner sc = new Scanner(System.in);

        // Read in user input for first time
        String input = sc.nextLine();

        // Store list of to do
        ArrayList<String> list = new ArrayList<String>();

        while (!input.equalsIgnoreCase("bye")) {
            if (input.equalsIgnoreCase("list")) {
                // List out the list
               if (list.isEmpty()) {
                   System.out.println("There are no items in the list");
               } else {
                   for (int i = 0; i < list.size(); i++) {
                       System.out.println("\t" + (i+1) + ". " + list.get(i));
                   }
               }

            } else {
                // Add item to list
                list.add(input);
                System.out.println(line + "\tadded: " + input + "\n" + line);
            }

            // Continue to read next user input
            input = sc.nextLine();
        }
        // Close the bot when user types bye
        System.out.println(bye);

    }
}
