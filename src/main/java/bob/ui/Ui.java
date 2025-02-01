package bob.ui;

/**
 * Handles UI for the chatbot.
 */
public class Ui {

    private final String line = "\t____________________________________________________________";
    String hello = "\tHello! I'm Bob\n\tWhat can I do for you?";
    String bye = "\tBye. Hope to see you again soon!";

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
    public void printDatafileNotFoundMessage() {
        System.out.println("\tData file was not found, created one successfully!");
    }

    /**
     * Prints hello message.
     */
    public void printHelloMessage() {
        System.out.println(line);
        System.out.println(hello);
        System.out.println(line);
    }

    /**
     * Prints bye message before chatbot terminates.
     */
    public void printByeMessage() {
        System.out.println(line);
        System.out.println(bye);
        System.out.println(line);
    }

    /**
     * Prints a line of underscores.
     */
    public void printLine() {
        System.out.println(line);
    }

    /**
     * Prints message to tell user that below are the tasks in his/her list.
     */
    public void printTaskInListMessage() {
        System.out.println(line);
        System.out.println("\tHere are the tasks in your list:");
    }

    /**
     * Prints message notifying user that task has been added
     */
    public void printAddedTaskMessage() {
        System.out.println(line);
        System.out.println("\t Got it. I've added this task:");
    }

    /**
     * Prints message notifying user that task has been deleted
     */
    public void printDeletedTaskMessage() {
        System.out.println(line);
        System.out.println("\t Got it. I've deleted this task:");
    }

    /**
     * Prints message to show the number of tasks present in the list
     */
    public void printNumOfItemsInList(long size) {
        System.out.println("\t Now you have " + size + " tasks in the list.");
    }

    /**
     * Prints message to notify user there was an error loading the data file.
     */
    public void showLoadingFileError() {
        System.out.println(line);
        System.out.println("There was an error loading the file. Tasks in this session may not be saved.");
        System.out.println(line);
    }

    /**
     * Prints message to notify user task has been marked done.
     */
    public void printMarkedTaskDone() {
        System.out.println("\tNice! I've marked this task as done:");
    }

    /**
     * Prints message to notify user task has been marked as undone.
     */
    public void printMarkedTaskUndone() {
        System.out.println("\tOK, I've marked this task as not done yet:");
    }

    /**
     * Prints message about the tasks found.
     */
    public void printFoundTasks() {
        System.out.println("\tHere are the matching tasks in your list:");
    }
}
