package bob;

import bob.command.Parser;
import bob.exception.BobException;
import bob.storage.Storage;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.TaskList;
import bob.task.Todo;
import bob.ui.Ui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Bob {

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    public Bob(String filePath) {
        // Initialise bob.Ui
        this.ui = new Ui();

        // Initialise storage
        this.storage = new Storage(filePath);

        try {
            if (!storage.directoryExists()) {
                storage.createDirectory();
                ui.printDatafileNotFoundMessage();

                // Initialise empty task list
                taskList = new TaskList();
            } else {
                // If file already exists, import into taskList with overloaded constructor
                taskList = new TaskList(storage.loadTasks());
            }
        } catch (BobException | IOException e) {
            ui.showLoadingFileError();
            taskList = new TaskList();
        }

    }

    public static void main(String[] args) throws IOException, BobException {
        new Bob("data/bob.txt").run();
    }

    public void run() throws IOException {

        // Check data file present or not, creates one if not present
        Storage storage = new Storage("data/bob.txt");

        // Print hello message
        ui.printHelloMessage();

        // Initialise scanner
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                // Read in user input for first time
                String rawInput = sc.nextLine();
                String[] input = rawInput.split(" ", 2);

                while (!input[0].equalsIgnoreCase("bye")) {
                    if (input[0].equalsIgnoreCase("list")) {
                        // List out the taskList
                        if (input.length > 1) {
                            throw new BobException("Too many arguments");
                        } else if (taskList.isEmpty()) {
                            throw new BobException("There are no items in the list");
                        } else {
                            ui.printTaskInListMessage();
                            for (int i = 0; i < taskList.size(); i++) {
                                System.out.println("\t" + (i + 1) + ". " + taskList.get(i).toString());
                            }
                            ui.printLine();
                        }

                    } else if (input[0].contains("unmark")) {
                        // unmark item

                        if (input.length < 2) {
                            throw new BobException("Your command is missing a number to unmark");
                        } else if (input[1].isEmpty() || !input[1].matches("-?\\d+")) {
                            throw new BobException("Your command is missing a number to unmark");
                        }
                        int index = Integer.parseInt(input[1]) - 1;
                        if (index < 0 || index >= taskList.size()) {
                            throw new BobException("No such task number");
                        }
                        taskList.get(index).unMarkDone();
                        ui.printLine();
                        System.out.println("\t   " + taskList.get(index).toString());
                        ui.printLine();

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
                        if (index < 0 || index >= taskList.size()) {
                            throw new BobException("No such task number, try again");
                        }
                        taskList.get(index).markDone();
                        ui.printLine();
                        System.out.println("\t   " + taskList.get(index).toString());
                        ui.printLine();

                    } else if (input[0].equalsIgnoreCase("deadline")) {
                        if (input.length < 2) {
                            throw new BobException("Your command is incorrect, try again.");
                        }
                        // Index 0 is before /by, index 1 is after /by
                        String[] deadlineSplit = input[1].split("/by ");
                        if (deadlineSplit.length < 2) {
                            throw new BobException("Your command is incorrect, try again.");
                        }
                        String description = deadlineSplit[0];
                        String deadline = deadlineSplit[1];
                        LocalDateTime deadlineDate = Parser.parseDate(deadline);
                        if (deadlineDate == null) {
                            throw new BobException("Deadline date format is wrong, please try again.");
                        }
                        taskList.addTask(new Deadline(description, deadlineDate));
                        ui.printAddedTaskMessage();
                        System.out.println("\t   " + taskList.get(taskList.size() - 1).toString());

                        ui.printNumOfItemsInList(taskList.size());
                        ui.printLine();

                    } else if (input[0].equalsIgnoreCase("event")) {
                        if (input[1].isEmpty()) {
                            throw new BobException("Task description is empty, please try again");
                        }

                        String[] eventSplit = input[1].split("/from ");
                        String description = eventSplit[0];
                        String[] durationSplit = eventSplit[1].split(" /to ");
                        String from = durationSplit[0];
                        LocalDateTime fromDate = Parser.parseDate(from);
                        if (fromDate == null) {

                            throw new BobException("From date/time format is wrong, please try again.");
                        }
                        String to = durationSplit[1];
                        LocalDateTime toDate = Parser.parseDate(to);
                        if (toDate == null) {
                            throw new BobException("To date/time format is wrong, please try again.");
                        }
                        taskList.addTask(new Event(description, fromDate, toDate));
                        ui.printAddedTaskMessage();
                        System.out.println("\t   " + taskList.get(taskList.size() - 1).toString());
                        ui.printNumOfItemsInList(taskList.size());
                        ui.printLine();

                    } else if (input[0].equalsIgnoreCase("todo")) {
                        if (input.length < 2) {
                            throw new BobException("Task description is empty, please try again");
                        } else if (input[1].isEmpty()) {
                            throw new BobException("Task description is empty, please try again");
                        }

                        taskList.addTask(new Todo(input[1]));
                        ui.printAddedTaskMessage();
                        System.out.println("\t   " + taskList.get(taskList.size() - 1).toString());
                        ui.printNumOfItemsInList(taskList.size());
                        ui.printLine();

                    } else if (input[0].equalsIgnoreCase("delete")) {
                        // Handle case where incorrect number of arguments given
                        if (input.length != 2) {
                            throw new BobException("Incorrect number of arguments");
                        }
                        int index = Integer.parseInt(input[1]) - 1;

                        // Handle case when task number not found
                        if (index < 0 || index >= taskList.size()) {
                            throw new BobException("No such task number");
                        }

                        ui.printDeletedTaskMessage();
                        taskList.deleteTask(index);
                        ui.printNumOfItemsInList(taskList.size());
                        ui.printLine();

                    } else {
                        // Invalid input, throw exception
                        throw new BobException("""
                                Bob doesn't recognise this, please try again
                                \tUsage:\s
                                \t1. todo <description> or\s
                                \t2. deadline <description> /by <deadline> or
                                \t3. event <description> \
                                /from <start time> /to <end time>
                                \t4. taskList
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
                ui.printByeMessage();

                // Write lists to file before exit
                storage.writeTasksToFile(taskList);

                // Break out of loop to exit
                break;

            } catch (BobException e) {
                ui.printLine();
                System.out.println("\t" + e.getMessage());
                ui.printLine();
            }
        }


    }


}