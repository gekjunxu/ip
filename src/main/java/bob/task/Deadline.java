package bob.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a class with a deadline.
 *
 * @see bob.task.Task
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Creates a new instance of a deadline task.
     *
     * @param description of the task.
     * @param by          The deadline of the task.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        assert by != null : "Deadline should have a valid by";
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + by.format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm")) + ")";
    }

}
