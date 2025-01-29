import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;




public class Bob {

    public static void main(String[] args) throws BobException, IOException {

        // Check data file present or not, creates one if not present
        Path directory = Paths.get("data");
        Path filePath = Paths.get("data/bob.txt");
        boolean directoryExists = Files.exists(filePath);
        if (!directoryExists) {
            Files.createDirectories(directory);
            Files.createFile(filePath);
            System.out.println("\tData file was not found, created one successfully!");
        }

        String line = "\t____________________________________________________________\n";
        String hello = line + "\tHello! I'm Bob\n\tWhat can I do for you?\n" + line;
        String bye = line + "\tBye. Hope to see you again soon!\n" + line;
        System.out.println(hello);

        // Store list of to do
        ArrayList<Task> list = new ArrayList<>();

        while (true) {
            try {
                // Initialise scanner
                Scanner sc = new Scanner(System.in);

                // Read in user input for first time
                String rawInput = sc.nextLine();
                String[] input = rawInput.split(" ", 2);

                while (!input[0].equalsIgnoreCase("bye")) {
                    if (input[0].equalsIgnoreCase("list")) {
                        // List out the list
                        if (input.length > 1) {
                            throw new BobException("Too many arguments");
                        } else if (list.isEmpty()) {
                            throw new BobException("There are no items in the list");
                        } else {
                            System.out.print(line);
                            System.out.println("\tHere are the tasks in your list:");
                            for (int i = 0; i < list.size(); i++) {
                                System.out.println("\t" + (i + 1) + ". " + list.get(i).toString());
                            }
                            System.out.println(line);
                        }

                    } else if (input[0].contains("unmark")) {
                        // unmark item

                        if (input.length < 2) {
                            throw new BobException("Your command is missing a number to unmark");
                        } else if (input[1].isEmpty() || !input[1].matches("-?\\d+")) {
                            throw new BobException("Your command is missing a number to unmark");
                        }
                        int index = Integer.parseInt(input[1]) - 1;
                        if (index < 0 || index >= list.size()) {
                            throw new BobException("No such task number");
                        }
                        list.get(index).unMarkDone();
                        System.out.println(line + "\t   " + list.get(index).toString() + "\n" + line);

                    } else if (input[0].contains("mark")) {
                        // Mark item

                        // Throw exception if no number given
                        if (input.length < 2) {
                            throw new BobException("Your command is missing a number to mark");
                        } else if (input[1].isEmpty() || !input[1].matches("-?\\d+")) {
                            throw new BobException("Your command is missing a number to mark");
                        }
                        int index = Integer.parseInt(input[1]) - 1;

                        // Handle invalid input
                        if (index < 0 || index >= list.size()) {
                            throw new BobException("No such task number, try again");
                        }
                        list.get(index).markDone();
                        System.out.println(line + "\t   " + list.get(index).toString() + "\n" + line);

                    } else if (input[0].equalsIgnoreCase("deadline")) {
                        // Index 0 is before /by, index 1 is after /by
                        String[] deadLineSplit = input[1].split("/by ");
                        String description = deadLineSplit[0];
                        String deadLine = deadLineSplit[1];
                        list.add(new Deadline(description, deadLine));
                        System.out.println(line + "\t Got it. I've added this task:");
                        System.out.println("\t   " + list.get(list.size() - 1).toString());
                        System.out.println("\t Now you have " + list.size() + " tasks in the list.\n" + line);

                    } else if (input[0].equalsIgnoreCase("event")) {
                        if (input[1].isEmpty()) {
                            throw new BobException("Task description is empty, please try again");
                        }

                        String[] eventSplit = input[1].split("/from ");
                        String description = eventSplit[0];
                        String[] durationSplit = eventSplit[1].split("/to ");
                        String from = durationSplit[0];
                        String to = durationSplit[1];
                        list.add(new Event(description, from, to));
                        System.out.println(line + "\t Got it. I've added this task:");
                        System.out.println("\t   " + list.get(list.size() - 1).toString());
                        System.out.println("\t Now you have " + list.size() + " tasks in the list.\n" + line);

                    } else if (input[0].equalsIgnoreCase("todo")) {
                        if (input.length < 2) {
                            throw new BobException("Task description is empty, please try again");
                        }

                        list.add(new Todo(input[1]));
                        System.out.println(line + "\t Got it. I've added this task:");
                        System.out.println("\t   " + list.get(list.size() - 1).toString());
                        System.out.println("\t Now you have " + list.size() + " tasks in the list.\n" + line);

                    } else if (input[0].equalsIgnoreCase("delete")) {
                        // Handle case where incorrect number of arguments given
                        if (input.length != 2) {
                            throw new BobException("Incorrect number of arguments");
                        }
                        int index = Integer.parseInt(input[1]) - 1;

                        // Handle case when task number not found
                        if (index < 0 || index >= list.size()) {
                            throw new BobException("No such task number");
                        }

                        System.out.println(line + "\t Noted. I've deleted this task:");
                        System.out.println("\t   " + list.get(index).toString());
                        list.remove(index);
                        System.out.println("\t Now you have " + list.size() + " tasks in the list.\n" + line);

                    } else {
                        // Invalid input, throw exception
                        throw new BobException("""
                                Bob doesn't recognise this, please try again
                                \tUsage:\s
                                \t1. todo <description> or\s
                                \t2. deadline <description> /by <deadline> or
                                \t3. event <description> \
                                /from <start time> /to <end time>
                                \t4. list
                                \t5. mark <task number>
                                \t6. unmark <task number>\
                                
                                \t7. delete <task number>""");

                    }

                    // Continue to read next user input
                    rawInput = sc.nextLine();
                    input = rawInput.split(" ", 2);
                }
                // Close the bot when user types bye
                // Say bye message when user types bye
                System.out.println(bye);

                // Write lists to file before exit
                File file = filePath.toFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < list.size(); i++) {
                    writer.write(list.get(i).toString());
                    writer.newLine();
                }
                writer.close();


                // Break out of loop to exit
                break;

            } catch (BobException e) {
                System.out.print(line);
                System.out.println("\t" + e.getMessage());
                System.out.println(line);
            }
        }


    }
}
