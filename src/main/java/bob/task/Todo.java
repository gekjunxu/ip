package bob.task;

/**
 * Represents a task with a description.
 * <p>
 * Inherits from the Task class.
 *
 * @see bob.task.Task
 */
public class Todo extends Task {

    /**
     * Creates a new Todo instance.
     *
     * @param description of the todo task.
     */
    public Todo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }


}
