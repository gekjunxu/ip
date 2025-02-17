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
    private boolean isDatafileFound = false;

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

                isDatafileFound = true;
            }
        } catch (BobException | IOException e) {
            ui.showDataFileError();
            taskList = new TaskList();
        }

    }


    /**
     * Executes the main running logic for Bob.
     *
     * @throws BobException if issues with user input.
     * @throws IOException  if encounters issue with writing data file.
     */
    public String run(String rawInput) throws BobException, IOException {

        // Check data file present or not, creates one if not present
        Storage storage = new Storage("data/bob.txt");

        // Print hello message
        ui.printHelloMessage();

        // Split components of input to find first command word
        String[] input = rawInput.split(" ", 2);

        switch (input[0]) {
        case "list":
            return listOutTasks(input);
        case "unmark":
            return unmarkTask(input);
        case "mark":
            return markTask(input);
        case "deadline":
            return deadlineTask(input);
        case "event":
            return eventTask(input);
        case "todo":
            return todoTask(input);
        case "delete":
            return deleteTask(input);
        case "find":
            return findTask(input);
        case "bye":
            return byeTask();
        default:
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

    /**
     * Saves tasks to file before closing the program.
     *
     * @return message to say bye to the user.
     * @throws IOException if issue occurs when saving file.
     */
    private String byeTask() throws IOException {
        // Write lists to file before exit
        saveTasksToFile();

        // Close the bot when user types bye
        // Say bye message when user types bye
        return ui.printByeMessage();
    }

    /**
     * Marks a task as not done.
     *
     * @param input The user input containing the task number.
     * @return A message indicating the task has been unmarked.
     * @throws BobException If the input is invalid or the task number is out of range.
     */
    private String unmarkTask(String[] input) throws BobException {
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

    /**
     * Marks a task as done.
     *
     * @param input The user input containing the task number.
     * @return A message indicating the task has been marked as done.
     * @throws BobException If the input is invalid or the task number is out of range.
     */
    private String markTask(String[] input) throws BobException {
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

    /**
     * Adds an event task to the task list.
     *
     * @param input The user input containing the task description and event duration.
     * @return A message confirming the task has been added.
     * @throws BobException If the input format is incorrect or date parsing fails.
     */
    private String eventTask(String[] input) throws BobException {
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

        String taskString = taskList.get(taskList.size() - 1).toString();
        int numberOfTasks = taskList.size();
        return ui.printAddedTaskMessage(taskString, numberOfTasks);
    }

    /**
     * Adds a todo task to the task list.
     *
     * @param input The user input containing the task description.
     * @return A message confirming the task has been added.
     * @throws BobException If the task description is missing.
     */
    private String todoTask(String[] input) throws BobException {
        if (input.length < 2) {
            throw new BobException("Task description is empty, please try again");
        } else if (input[1].isEmpty()) {
            throw new BobException("Task description is empty, please try again");
        }
        taskList.addTask(new Todo(input[1]));

        String taskString = taskList.get(taskList.size() - 1).toString();
        int numberOfTasks = taskList.size();
        return ui.printAddedTaskMessage(taskString, numberOfTasks);
    }

    /**
     * Deletes a task from the task list.
     *
     * @param input The user input containing the task number.
     * @return A message confirming the task has been deleted.
     * @throws BobException If the input is invalid or the task number is out of range.
     */
    private String deleteTask(String[] input) throws BobException {
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
        return ui.printDeletedTaskMessage(taskList.size());
    }

    /**
     * Finds tasks in the task list matching a keyword.
     *
     * @param input The user input containing the search keyword.
     * @return A message listing the found tasks.
     * @throws BobException If the input format is incorrect.
     */
    private String findTask(String[] input) throws BobException {
        if (input.length < 2) {
            throw new BobException("Incorrect number of arguments");
        }
        String toFind = input[1];
        TaskList foundTasks = taskList.findTasks(toFind);
        return ui.printFoundTasks() + foundTasks.listTasks();
    }

    /**
     * Adds a deadline task to the task list.
     *
     * @param input The user input containing the task description and deadline.
     * @return A message confirming the task has been added.
     * @throws BobException If the input format is incorrect or date parsing fails.
     */
    private String deadlineTask(String[] input) throws BobException {
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

        String taskString = taskList.get(taskList.size() - 1).toString();
        int numberOfTasks = taskList.size();
        return ui.printAddedTaskMessage(taskString, numberOfTasks);
    }


    /**
     * Lists all tasks in the task list.
     *
     * @param input The user input (should contain no extra arguments).
     * @return A message listing all tasks.
     * @throws BobException If extra arguments are provided or the task list is empty.
     */
    private String listOutTasks(String[] input) throws BobException {
        if (input.length > 1) {
            throw new BobException("Too many arguments");
        } else if (taskList.isEmpty()) {
            throw new BobException("There are no items in the list");
        } else {
            return ui.printTaskInListMessage() + taskList.listTasks();
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
        return isDatafileFound ? ui.printHelloMessage() : ui.printDatafileNotFoundMessage() + ui.printHelloMessage();
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
