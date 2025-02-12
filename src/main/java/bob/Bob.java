package bob;

import java.io.IOException;
import java.time.LocalDateTime;

import bob.command.Parser;
import bob.exception.BobException;
import bob.storage.Storage;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.TaskList;
import bob.task.Todo;
import bob.ui.Ui;


/**
 * The main class for the Bob Chatbot.
 */
public class Bob {

    private Storage storage;
    private TaskList taskList;
    private Ui ui;
    private boolean datafileIsFound = false;

    /**
     * Constructor for Bob, checks for existing data file and processes it,
     * creates one if not found.
     *
     * @param filePath The path to the data file.
     */
    public Bob(String filePath) {
        // Initialise bob.Ui
        this.ui = new Ui();

        // Initialise storage
        this.storage = new Storage(filePath);

        try {
            if (!storage.directoryExists()) {
                storage.createDirectory();

                // Initialise empty task list
                taskList = new TaskList();
            } else {
                // If file already exists, import into taskList with overloaded constructor
                taskList = new TaskList(storage.loadTasks());

                datafileIsFound = true;
            }
        } catch (BobException | IOException e) {
            ui.showDataFileError();
            taskList = new TaskList();
        }

    }


    /**
     * The main running logic for Bob.
     *
     * @throws BobException if issues with user input.
     * @throws IOException  if encounters issue with writing data file.
     */
    public String run(String rawInput) throws BobException, IOException {

        // Check data file present or not, creates one if not present
        Storage storage = new Storage("data/bob.txt");

        // Print hello message
        ui.printHelloMessage();

        // Split components of input to find first word command
        String[] input = rawInput.split(" ", 2);

        switch (input[0]) {
        case "list": { // List out the taskList
            if (input.length > 1) {
                throw new BobException("Too many arguments");
            } else if (taskList.isEmpty()) {
                throw new BobException("There are no items in the list");
            } else {
                return ui.printTaskInListMessage() + taskList.listTasks();
            }
        }
        case "unmark": { // unmark item
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
            return ui.printMarkedTaskUndone() + taskList.get(index).toString();
        }
        case "mark": { // Mark item
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
            return ui.printMarkedTaskDone() + taskList.get(index).toString();
        }
        case "deadline": { // Deadline task
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

            return ui.printAddedTaskMessage()
                    + taskList.get(taskList.size() - 1).toString()
                    + ui.printNumOfItemsInList(taskList.size());
        }
        case "event": {
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

            return ui.printAddedTaskMessage()
                    + taskList.get(taskList.size() - 1).toString()
                    + ui.printNumOfItemsInList(taskList.size());
        }
        case "todo": {
            if (input.length < 2) {
                throw new BobException("Task description is empty, please try again");
            } else if (input[1].isEmpty()) {
                throw new BobException("Task description is empty, please try again");
            }
            taskList.addTask(new Todo(input[1]));
            return ui.printAddedTaskMessage()
                    + taskList.get(taskList.size() - 1).toString() + "\n"
                    + ui.printNumOfItemsInList(taskList.size());
        }
        case "delete": {
            // Handle case where incorrect number of arguments given
            if (input.length != 2) {
                throw new BobException("Incorrect number of arguments");
            }
            int index = Integer.parseInt(input[1]) - 1;

            // Handle case when task number not found
            if (index < 0 || index >= taskList.size()) {
                throw new BobException("No such task number");
            }
            taskList.deleteTask(index);
            return ui.printDeletedTaskMessage() + ui.printNumOfItemsInList(taskList.size());
        }
        case "find": {
            if (input.length < 2) {
                throw new BobException("Incorrect number of arguments");
            }
            String toFind = input[1];
            TaskList foundTasks = taskList.findTasks(toFind);
            return ui.printFoundTasks() + foundTasks.listTasks();
        }
        case "bye": {
            // Write lists to file before exit
            saveTasksToFile();

            // Close the bot when user types bye
            // Say bye message when user types bye
            return ui.printByeMessage();
        }
        default: {
            // Invalid input, throw exception
            throw new BobException("""
                    Bob doesn't recognise this, please try again
                    Usage:\s
                    1. todo <description> or\s
                    2. deadline <description> /by <deadline> or
                    3. event <description> \
                    /from <start time> /to <end time>
                    4. taskList
                    5. mark <task number>
                    6. unmark <task number>\
                    7. delete <task number>""");
        }
        }
    }


    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String rawInput) {
        assert rawInput != null;
        try {
            return this.run(rawInput);
        } catch (IOException e) {
            return ui.showDataFileError();
        } catch (BobException e) {
            return e.getMessage();
        }

    }

    /**
     * Returns startup message for the bot.
     *
     * @return message with datafile created message if datafile not found, or just hello message otherwise.
     */
    public String getStartupMessage() {
        return datafileIsFound ? ui.printHelloMessage() : ui.printDatafileNotFoundMessage() + ui.printHelloMessage();
    }

    /**
     * Saves task to file.
     *
     * @throws IOException for any issues writing back to file.
     */
    public void saveTasksToFile() throws IOException {
        storage.writeTasksToFile(taskList);
    }

}
