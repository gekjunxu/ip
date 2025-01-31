package bob.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {


    protected LocalDateTime to;
    protected LocalDateTime from;

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
