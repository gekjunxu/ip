package bob.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a description, start and end date and time.
 *
 * @see bob.task.Task
 */
public class Event extends Task {


    protected LocalDateTime to;
    protected LocalDateTime from;

    /**
     * Creates a new Event task instance.
     *
     * @param description of the event.
     * @param from        The start date and time of the event.
     * @param to          The end date and time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.to = to;
        this.from = from;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " +
                from.format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm")) + " to: "
                + to.format(DateTimeFormatter.ofPattern("MMM d yyyy HH:mm")) + ")";
    }

}
