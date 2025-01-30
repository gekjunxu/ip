import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Path;

public class Bob {

    public static void main(String[] args) throws BobException, IOException {

        // Store list of to do
        ArrayList<Task> list = new ArrayList<>();

        // Check data file present or not, creates one if not present
        Path directory = Paths.get("data");
        Path filePath = Paths.get("data/bob.txt");
        boolean directoryExists = Files.exists(filePath);
        if (!directoryExists) {
            Files.createDirectories(directory);
            Files.createFile(filePath);
            System.out.println("\tData file was not found, created one successfully!");
        } else {
            // If file already exists, import into list
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line); // Parse each line into a Task
                list.add(task);
            }

        }

        String line = "\t____________________________________________________________\n";
        String hello = line + "\tHello! I'm Bob\n\tWhat can I do for you?\n" + line;
        String bye = line + "\tBye. Hope to see you again soon!\n" + line;
        System.out.println(hello);

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
                        String[] deadlineSplit = input[1].split("/by ");
                        String description = deadlineSplit[0];
                        String deadline = deadlineSplit[1];
                        LocalDateTime deadlineDate = parseDate(deadline);
                        if (deadlineDate == null) {
                            throw new BobException("Deadline date format is wrong, please try again.");
                        }
                        list.add(new Deadline(description, deadlineDate));
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

    private static LocalDateTime parseDate(String date) {
        // Support multiple date formats, generated with some help from GPT
        List<String> patterns = List.of(
// Standard Numeric Formats
                "yyyy-MM-dd", "dd/MM/yyyy", "d/M/yyyy", "MM-dd-yyyy", "M-d-yyyy",
                "dd/MM/yy", "d/M/yy", "MM/dd/yyyy", "M/d/yyyy", "MM.dd.yyyy", "M.d.yyyy",

                // Date with Single-Digit Month & Two-Digit Day (with time options)
                "dd/M/yyyy", "dd/M/yy", "d/MM/yyyy", "d/MM/yy", // Two-digit day, one-digit month
                "dd/MM/yyyy", "dd/M/yyyy", // Two-digit day, one-digit month for different separators

                // Date & Time (24-hour format with colon)
                "yyyy-MM-dd HH:mm", "dd/MM/yyyy HH:mm", "d/M/yyyy HH:mm", "MM-dd-yyyy HH:mm", "M-d-yyyy HH:mm",
                "dd/MM/yy HH:mm", "d/M/yy HH:mm", "MM/dd/yyyy HH:mm", "M/d/yyyy HH:mm",
                "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "d/M/yyyy HH:mm:ss", "MM-dd-yyyy HH:mm:ss", "M-d-yyyy HH:mm:ss",
                "dd/MM/yy HH:mm:ss", "d/M/yy HH:mm:ss", "MM/dd/yyyy HH:mm:ss", "M/d/yyyy HH:mm:ss",

                // **24-hour time formats without colon (HHmm) for various date formats**
                "yyyy-MM-dd HHmm", "dd/MM/yyyy HHmm", "d/M/yyyy HHmm", "MM-dd-yyyy HHmm", "M-d-yyyy HHmm",
                "dd/MM/yy HHmm", "d/M/yy HHmm", "MM/dd/yyyy HHmm", "M/d/yyyy HHmm",
                "yyyy-MM-dd HHmm:ss", "dd/MM/yyyy HHmm:ss", "d/M/yyyy HHmm:ss", "MM-dd-yyyy HHmm:ss", "M-d-yyyy HHmm:ss",
                "dd/MM/yy HHmm:ss", "d/M/yy HHmm:ss", "MM/dd/yyyy HHmm:ss", "M/d/yyyy HHmm:ss",

                // Date & Time (12-hour format with AM/PM)
                "yyyy-MM-dd h:mm a", "dd/MM/yyyy h:mm a", "d/M/yyyy h:mm a", "MM-dd-yyyy h:mm a", "M-d-yyyy h:mm a",
                "dd/MM/yy h:mm a", "d/M/yy h:mm a", "MMM d yyyy h:mm a", "MMMM d yyyy h:mm a", "d MMM yyyy h:mm a",
                "MMMM dd yyyy h:mm a", "d MMMM yyyy h:mm a", "h:mm a", "h:mm a, MMM d yyyy", "h:mm a, d MMM yyyy",
                "h:mm a, MMMM d yyyy", "h:mm a, MMMM dd yyyy", "h:mm a, dd MMM yyyy", "h:mm a, dd/MM/yyyy",
                "h:mm a, d/M/yyyy", "h:mm a, MM/dd/yyyy", "h:mm a, M/d/yyyy",

                // Alternative AM/PM Formats
                "h:mma", "h.mma", "h.mma, MMM d yyyy", "h.mma, d MMM yyyy", "h.mma, MMMM d yyyy", "h.mma, MMMM dd yyyy",
                "h.mma, dd MMM yyyy", "h.mma, dd/MM/yyyy", "h.mma, d/M/yyyy", "h.mma, MM/dd/yyyy", "h.mma, M/d/yyyy",

                // Date & Time Permutations with 24-hour formats (with and without colon)
                "dd/M/yy HH:mm", "dd/M/yy HHmm", "dd/M/yy HH:mm:ss", "dd/M/yy HHmm:ss",
                "dd/M/yyyy HH:mm", "dd/M/yyyy HHmm", "dd/M/yyyy HH:mm:ss", "dd/M/yyyy HHmm:ss",

                // Date & Time Permutations with AM/PM (with 24-hour times)
                "dd/M/yy h:mm a", "dd/M/yy h:mma", "dd/M/yy h.mma", "dd/M/yy h:mm a, MMM d yyyy", "dd/M/yy h.mma, MMM d yyyy",
                "dd/M/yyyy h:mm a", "dd/M/yyyy h:mma", "dd/M/yyyy h.mma", "dd/M/yyyy h:mm a, MMM d yyyy", "dd/M/yyyy h.mma, MMM d yyyy",

                // **New Formats with Time Separating Hour and Minute by Period (e.g., "6.30pm")**
                "yyyy-MM-dd h.mma", "dd/MM/yyyy h.mma", "d/M/yyyy h.mma", "MM-dd-yyyy h.mma", "M-d-yyyy h.mma",
                "dd/MM/yy h.mma", "d/M/yy h.mma", "MMM d yyyy h.mma", "MMMM d yyyy h.mma", "d MMM yyyy h.mma",
                "MMMM dd yyyy h.mma", "d MMMM yyyy h.mma", "h.mma", "h.mma, MMM d yyyy", "h.mma, d MMM yyyy",
                "h.mma, MMMM d yyyy", "h.mma, MMMM dd yyyy", "h.mma, dd MMM yyyy", "h.mma, dd/MM/yyyy",
                "h.mma, d/M/yyyy", "h.mma, MM/dd/yyyy", "h.mma, M/d/yyyy",

                // **New Patterns for "6pm" (Only Hour Specified)**
                "yyyy-MM-dd h a", "dd/MM/yyyy h a", "d/M/yyyy h a", "MM-dd-yyyy h a", "M-d-yyyy h a",
                "dd/MM/yy h a", "d/M/yy h a", "MMM d yyyy h a", "MMMM d yyyy h a", "d MMM yyyy h a",
                "MMMM dd yyyy h a", "d MMMM yyyy h a", "h a", "h a, MMM d yyyy", "h a, d MMM yyyy",
                "h a, MMMM d yyyy", "h a, MMMM dd yyyy", "h a, dd MMM yyyy", "h a, dd/MM/yyyy",
                "h a, d/M/yyyy", "h a, MM/dd/yyyy", "h a, M/d/yyyy",

                // Permutations for "h a" with Date Formats (with single or two digits in day and month)
                "dd/M/yyyy h a", "d/MM/yyyy h a", "dd/M/yy h a", "d/M/yy h a", "dd/MM/yyyy h a, MMM d yyyy",
                "dd/M/yyyy h a, MMM d yyyy", "dd/MM/yy h a, MMM d yyyy", "d/M/yy h a, MMM d yyyy",

                // International & Alternative Styles
                "yyyy年MM月dd日", "d MMM yyyy HH:mm:ss", "d MMM yyyy h:mm a", "MMMM d yyyy, h:mm a",
                "dd.MM.yyyy", "d.MM.yyyy", "d.M.yyyy", "dd.MM.yy", "d.MM.yy", "MMM d yyyy HH:mm"
                );


        for (String pattern : patterns) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                    if (pattern.contains("HH") || pattern.contains("mm")) {
                        return LocalDateTime.parse(date, formatter);
                    } else {
                        LocalDate localDate = LocalDate.parse(date, formatter);
                        return localDate.atTime(LocalTime.MIDNIGHT); // Add default time midnight if no date specified
                    }
                } catch (DateTimeParseException ignored) {
                    // Ignore the exception as other formats may be correct
                    // Continue to loop through the list of formats
                }
            }
            // Returns null if no formatter pattern matches
            return null;

    }

    private static Task parseTask(String line) throws BobException {
        char taskType = line.charAt(1); // The character after the first '['
        boolean isDone = line.charAt(4) == 'X'; // Determine if the task is marked done

        switch (taskType) {
        case 'T': { // Todo task
            String description = line.substring(7); // Extract description
            Todo todo = new Todo(description);
            if (isDone) {
                todo.markDone();
            }
            return todo;
        }
        case 'D': { // Deadline task
            String[] parts = line.substring(7).split(" \\(by: ");
            String description = parts[0];
            String[] deadlineParts = parts[1].split("\\)");
            String deadline = deadlineParts[0];
            LocalDateTime deadlineDate = parseDate(deadline);
            Deadline deadlineTask = new Deadline(description, deadlineDate);
            if (isDone) {
                deadlineTask.markDone();
            }
            return deadlineTask;
        }
        case 'E': { // Event task
            String[] parts = line.substring(7).split(" \\(from: ");
            String description = parts[0];
            String[] timeParts = parts[1].split("to: ");
            String from = timeParts[0];
            String[] toParts = timeParts[1].split("\\)");
            String to = toParts[0];
            Event event = new Event(description, from, to);
            if (isDone) {
                event.markDone();
            }
            return event;
        }
        default:
            throw new BobException("Unknown task type in file: " + line);
        }
    }

}