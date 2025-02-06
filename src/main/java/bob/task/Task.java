package bob.task;

import bob.ui.Ui;

/**
 * An abstraction for the different types of tasks, but is not an abstract class.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new Task instance
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task depending on whether it
     * has been marked done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks the task as done and prints to screen, notifying the user.
     */
    public void markDone() {
        isDone = true;
        new Ui().printMarkedTaskDone();
    }

    /**
     * Marks the task as undone and prints to screen, notifying the user.
     */
    public void unMarkDone() {
        isDone = false;
        new Ui().printMarkedTaskUndone();
    }


    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
    //...
}

