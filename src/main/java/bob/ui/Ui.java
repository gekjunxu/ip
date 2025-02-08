package bob.ui;

/**
 * Handles UI for the chatbot.
 */
public class Ui {

    private final String line = "\t____________________________________________________________";
    private String hello = "\tHello! I'm Bob\n\tWhat can I do for you?";
    private String bye = "\tBye. Hope to see you again soon!";

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
        return "\tData file was not found, created one successfully!\n";
    }

    /**
     * Prints hello message.
     */
    public String printHelloMessage() {
        return hello;
    }

    /**
     * Prints bye message before chatbot terminates.
     */
    public String printByeMessage() {
        return bye;
    }

    /**
     * Prints a line of underscores.
     */
    public String printLine() {
        return line;
    }

    /**
     * Prints message to tell user that below are the tasks in his/her list.
     */
    public String printTaskInListMessage() {
        return "Here are the tasks in your list:\n";
    }

    /**
     * Prints message notifying user that task has been added
     */
    public String printAddedTaskMessage() {
        return "Got it. I've added this task:\n";
    }

    /**
     * Prints message notifying user that task has been deleted
     */
    public String printDeletedTaskMessage() {
        return "Got it. I've deleted this task:\n";
    }

    /**
     * Prints message to show the number of tasks present in the list
     */
    public String printNumOfItemsInList(long size) {
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
