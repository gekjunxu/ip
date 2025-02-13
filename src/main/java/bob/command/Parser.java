package bob.command;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;


/**
 * Handles parsing operations.
 * This class is responsible for operations where input parsing is required,
 * such as reading tasks from the saved file, and processing the dates
 * from String to a LocalDateTime format.
 */
public class Parser {

    /**
     * Parses a line in datafile into the task.
     *
     * @param line The input line to be parsed.
     * @return Task The task that has been parsed.
     * @throws BobException If the task in the line is unknown and cannot be parsed.
     */
    public static Task parseTask(String line) throws BobException {
        char taskType = line.charAt(1); // Extract task type from formatted string
        boolean isDone = line.charAt(4) == 'X'; // Determine if the task is marked done

        switch (taskType) {
        case 'T':
            return parseTodoTask(line, isDone);
        case 'D':
            return parseDeadlineTask(line, isDone);
        case 'E':
            return parseEventTask(line, isDone);
        default:
            throw new BobException("Unknown task type in file: " + line);
        }
    }

    /**
     * Parses an event task from a line of the saved data file.
     *
     * @param line   The line containing event task details.
     * @param isDone Whether the event is marked as done.
     * @return The parsed Event object.
     */
    private static Event parseEventTask(String line, boolean isDone) {
        String[] parts = line.substring(7).split(" \\(from: ");
        String description = parts[0];
        String[] timeParts = parts[1].split(" to: ");
        String from = timeParts[0];
        LocalDateTime fromDate = parseDate(from);
        String[] toParts = timeParts[1].split("\\)");
        String to = toParts[0];
        LocalDateTime toDate = parseDate(to);
        Event event = new Event(description, fromDate, toDate);
        if (isDone) {
            event.markDone();
        }
        return event;
    }

    /**
     * Parses a deadline task from a line of the saved data file.
     *
     * @param line   The line containing deadline task details.
     * @param isDone Whether the deadline is marked as done.
     * @return The parsed Deadline object.
     */
    private static Deadline parseDeadlineTask(String line, boolean isDone) {
        String[] parts = line.substring(7).split(" \\(by: ");
        String description = parts[0];
        String[] deadlineParts = parts[1].split("\\)");
        String deadline = deadlineParts[0];
        LocalDateTime deadlineDate = parseDate(deadline);
        Deadline deadlineTask = new Deadline(description, deadlineDate);
        if (isDone) {
            deadlineTask.markDone();
        }
        return deadlineTask;
    }

    /**
     * Parses a todo task from a line of the saved data file.
     *
     * @param line   The line containing todo task details.
     * @param isDone Whether the todo is marked as done.
     * @return The parsed Todo object.
     */
    private static Todo parseTodoTask(String line, boolean isDone) {
        String description = line.substring(7); // Extract description
        Todo todo = new Todo(description);
        if (isDone) {
            todo.markDone();
        }
        return todo;
    }

    /**
     * Parses a date in String format to a LocalDateTime.
     *
     * @param date The date to be parsed in String.
     * @return The LocalDateTime object corresponding to the input date in String format,
     * returns null if the date cannot be parsed.
     */
    public static LocalDateTime parseDate(String date) {
        // Syntax assisted by ChatGPT, developer documentation page not loading.

        if (date == null || date.trim().isEmpty()) {
            return null; // Reject empty input
        }

        com.joestelmach.natty.Parser nattyParser = new com.joestelmach.natty.Parser();
        List<com.joestelmach.natty.DateGroup> groups = nattyParser.parse(date);

        if (groups.isEmpty() || groups.get(0).getDates().isEmpty()) {
            return null;
        }

        // Get the first parsed date result
        Date parsedDate = groups.get(0).getDates().get(0);

        // Convert to LocalDateTime
        return parsedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
