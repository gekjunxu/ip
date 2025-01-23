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
        ArrayList<Task> list = new ArrayList<>();

        while (!input.equalsIgnoreCase("bye")) {
            if (input.equalsIgnoreCase("list")) {
                // List out the list
                if (list.isEmpty()) {
                    System.out.println("There are no items in the list");
                } else {
                    System.out.print(line);
                    System.out.println("\tHere are the tasks in your list:");
                    for (int i = 0; i < list.size(); i++) {
                        System.out.println("\t" + (i + 1) + ". " + list.get(i).toString());
                    }
                    System.out.println(line);
                }

            } else if (input.contains("unmark")) {
                // unmark item
                String[] stringArray = input.split(" ");
                int index = Integer.parseInt(stringArray[1]) - 1;
                list.get(index).unMarkDone();
                System.out.println(line + "\t   " + list.get(index).toString() + "\n" + line);

            } else if (input.contains("mark")) {
                // Mark item
                String[] stringArray = input.split(" ");
                int index = Integer.parseInt(stringArray[1]) - 1;
                list.get(index).markDone();
                System.out.println(line + "\t   " + list.get(index).toString() + "\n" + line);

            } else {
                // Add item to list
                list.add(new Task(input));
                System.out.println(line + "\tadded: " + input + "\n" + line);
            }

            // Continue to read next user input
            input = sc.nextLine();
        }
        // Close the bot when user types bye
        System.out.println(bye);

    }
}
