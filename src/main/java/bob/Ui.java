package bob;

public class Ui {

    private final String line = "\t____________________________________________________________";
    String hello = "\tHello! I'm Bob\n\tWhat can I do for you?";
    String bye = "\tBye. Hope to see you again soon!";

    public Ui() {
        // Empty constructor
    }

    public void printDatafileNotFoundMessage() {
        System.out.println("\tData file was not found, created one successfully!");
    }

    public void printHelloMessage() {
        System.out.println(line);
        System.out.println(hello);
        System.out.println(line);
    }

    public void printByeMessage() {
        System.out.println(line);
        System.out.println(bye);
        System.out.println(line);
    }

    public void printLine() {
        System.out.println(line);
    }

    public void printTaskInListMessage() {
        System.out.println(line);
        System.out.println("\tHere are the tasks in your list:");
    }

    public void printAddedTaskMessage() {
        System.out.println(line);
        System.out.println("\t Got it. I've added this task:");
    }

    public void printDeletedTaskMessage() {
        System.out.println(line);
        System.out.println("\t Got it. I've deleted this task:");
    }

    public void printNumOfItemsInList(long size) {
        System.out.println("\t Now you have " + size + " tasks in the list.");
    }

    public void showLoadingFileError() {
        System.out.println(line);
        System.out.println("There was an error loading the file. Tasks in this session may not be saved.");
        System.out.println(line);
    }
}
