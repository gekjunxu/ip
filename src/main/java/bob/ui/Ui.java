package bob.ui;

/**
 * Handles UI for the chatbot.
 */
public class Ui {

    /**
     * Creates a new UI instance.
     * <p>
     * Empty constructor for UI.
     */
    public Ui() {
        // Empty constructor
    }

    /**
     * Prints message if data file was not found, notifies user that one
     * is created successfully.
     */
    public String printDatafileNotFoundMessage() {
        return "Data file was not found, created one successfully!\n";
    }

    /**
     * Prints hello message.
     */
    public String printHelloMessage() {
        return "Hello! I'm Bob\nWhat can I do for you?";
    }

    /**
     * Prints bye message before chatbot terminates.
     */
    public String printByeMessage() {
        return "Bob will close in 5 seconds. Hope to see you again soon!";
    }

    /**
     * Prints a line of underscores.
     */
    public String printLine() {
        return "\t____________________________________________________________";
    }

    /**
     * Prints message to tell user that below is the tasks in his/her list.
     */
    public String printTaskInListMessage() {
        return "Here are the tasks in your list:\n";
    }

    /**
     * Prints message notifying user that task has been added
     */
    public String printAddedTaskMessage(String task, int numberOfTasks) {
        return "Got it. I've added this task:\n" + task + "\n" + printNumOfItemsInList(numberOfTasks);
    }

    /**
     * Prints message notifying user that task has been deleted
     */
    public String printDeletedTaskMessage(int numberOfTasks) {
        return "Got it. I've deleted this task:\n" + printNumOfItemsInList(numberOfTasks);
    }

    /**
     * Prints message to show the number of tasks present in the list
     */
    public String printNumOfItemsInList(int size) {
        return "Now you have " + size + " tasks in the list.\n";
    }

    /**
     * Prints message to notify user there was an error loading the data file.
     */
    public String showDataFileError() {
        return "There was an error with loading / saving the data file. Tasks in this session may not be saved.\n";
    }

    /**
     * Prints message to notify user task has been marked done.
     */
    public String printMarkedTaskDone() {
        return "Nice! I've marked this task as done:\n";
    }

    /**
     * Prints message to notify user task has been marked as undone.
     */
    public String printMarkedTaskUndone() {
        return "OK, I've marked this task as not done yet:\n";
    }

    /**
     * Prints message about the tasks found.
     */
    public String printFoundTasks() {
        return "\tHere are the matching tasks in your list:\n";
    }
}
