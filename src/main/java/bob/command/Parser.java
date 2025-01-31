package bob.command;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Parser {

    public static Task parseTask(String line) throws BobException {
        char taskType = line.charAt(1); // The character after the first '['
        boolean isDone = line.charAt(4) == 'X'; // Determine if the task is marked done

        switch (taskType) {
        case 'T': { // bob.Todo task
            String description = line.substring(7); // Extract description
            Todo todo = new Todo(description);
            if (isDone) {
                todo.markDone();
            }
            return todo;
        }
        case 'D': { // Deadline task
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
        case 'E': { // Event task
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
        default:
            throw new BobException("Unknown task type in file: " + line);
        }
    }

    public static LocalDateTime parseDate(String date) {
        // Support multiple date formats, generated with some help from GPT
        List<String> patterns = List.of(
                // Standard Numeric Formats
                "yyyy-MM-dd", "dd/MM/yyyy", "d/M/yyyy", "MM-dd-yyyy", "M-d-yyyy",
                "dd/MM/yy", "d/M/yy", "MM/dd/yyyy", "M/d/yyyy", "MM.dd.yyyy", "M.d.yyyy",

                // Date with Single-Digit Month & Two-Digit Day (with time options)
                "dd/M/yyyy", "dd/M/yy", "d/MM/yyyy", "d/MM/yy", // Two-digit day, one-digit month
                "dd/MM/yyyy", "dd/M/yyyy", // Two-digit day, one-digit month for different separators

                // Date & Time (24-hour format with colon)
                "yyyy-MM-dd HH:mm", "dd/MM/yyyy HH:mm", "d/M/yyyy HH:mm", "MM-dd-yyyy HH:mm", "M-d-yyyy HH:mm",
                "dd/MM/yy HH:mm", "d/M/yy HH:mm", "MM/dd/yyyy HH:mm", "M/d/yyyy HH:mm",
                "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "d/M/yyyy HH:mm:ss", "MM-dd-yyyy HH:mm:ss", "M-d-yyyy HH:mm:ss",
                "dd/MM/yy HH:mm:ss", "d/M/yy HH:mm:ss", "MM/dd/yyyy HH:mm:ss", "M/d/yyyy HH:mm:ss",

                // **24-hour time formats without colon (HHmm) for various date formats**
                "yyyy-MM-dd HHmm", "dd/MM/yyyy HHmm", "d/M/yyyy HHmm", "MM-dd-yyyy HHmm", "M-d-yyyy HHmm",
                "dd/MM/yy HHmm", "d/M/yy HHmm", "MM/dd/yyyy HHmm", "M/d/yyyy HHmm",
                "yyyy-MM-dd HHmm:ss", "dd/MM/yyyy HHmm:ss", "d/M/yyyy HHmm:ss", "MM-dd-yyyy HHmm:ss", "M-d-yyyy HHmm:ss",
                "dd/MM/yy HHmm:ss", "d/M/yy HHmm:ss", "MM/dd/yyyy HHmm:ss", "M/d/yyyy HHmm:ss",

                // Date & Time (12-hour format with AM/PM)
                "yyyy-MM-dd h:mm a", "dd/MM/yyyy h:mm a", "d/M/yyyy h:mm a", "MM-dd-yyyy h:mm a", "M-d-yyyy h:mm a",
                "dd/MM/yy h:mm a", "d/M/yy h:mm a", "MMM d yyyy h:mm a", "MMMM d yyyy h:mm a", "d MMM yyyy h:mm a",
                "MMMM dd yyyy h:mm a", "d MMMM yyyy h:mm a", "h:mm a", "h:mm a, MMM d yyyy", "h:mm a, d MMM yyyy",
                "h:mm a, MMMM d yyyy", "h:mm a, MMMM dd yyyy", "h:mm a, dd MMM yyyy", "h:mm a, dd/MM/yyyy",
                "h:mm a, d/M/yyyy", "h:mm a, MM/dd/yyyy", "h:mm a, M/d/yyyy",

                // Alternative AM/PM Formats
                "h:mma", "h.mma", "h.mma, MMM d yyyy", "h.mma, d MMM yyyy", "h.mma, MMMM d yyyy", "h.mma, MMMM dd yyyy",
                "h.mma, dd MMM yyyy", "h.mma, dd/MM/yyyy", "h.mma, d/M/yyyy", "h.mma, MM/dd/yyyy", "h.mma, M/d/yyyy",

                // Date & Time Permutations with 24-hour formats (with and without colon)
                "dd/M/yy HH:mm", "dd/M/yy HHmm", "dd/M/yy HH:mm:ss", "dd/M/yy HHmm:ss",
                "dd/M/yyyy HH:mm", "dd/M/yyyy HHmm", "dd/M/yyyy HH:mm:ss", "dd/M/yyyy HHmm:ss",

                // Date & Time Permutations with AM/PM (with 24-hour times)
                "dd/M/yy h:mm a", "dd/M/yy h:mma", "dd/M/yy h.mma", "dd/M/yy h:mm a, MMM d yyyy", "dd/M/yy h.mma, MMM d yyyy",
                "dd/M/yyyy h:mm a", "dd/M/yyyy h:mma", "dd/M/yyyy h.mma", "dd/M/yyyy h:mm a, MMM d yyyy", "dd/M/yyyy h.mma, MMM d yyyy",

                // **New Formats with Time Separating Hour and Minute by Period (e.g., "6.30pm")**
                "yyyy-MM-dd h.mma", "dd/MM/yyyy h.mma", "d/M/yyyy h.mma", "MM-dd-yyyy h.mma", "M-d-yyyy h.mma",
                "dd/MM/yy h.mma", "d/M/yy h.mma", "MMM d yyyy h.mma", "MMMM d yyyy h.mma", "d MMM yyyy h.mma",
                "MMMM dd yyyy h.mma", "d MMMM yyyy h.mma", "h.mma", "h.mma, MMM d yyyy", "h.mma, d MMM yyyy",
                "h.mma, MMMM d yyyy", "h.mma, MMMM dd yyyy", "h.mma, dd MMM yyyy", "h.mma, dd/MM/yyyy",
                "h.mma, d/M/yyyy", "h.mma, MM/dd/yyyy", "h.mma, M/d/yyyy",

                // **New Patterns for "6pm" (Only Hour Specified)**
                "yyyy-MM-dd h a", "dd/MM/yyyy h a", "d/M/yyyy h a", "MM-dd-yyyy h a", "M-d-yyyy h a",
                "dd/MM/yy h a", "d/M/yy h a", "MMM d yyyy h a", "MMMM d yyyy h a", "d MMM yyyy h a",
                "MMMM dd yyyy h a", "d MMMM yyyy h a", "h a", "h a, MMM d yyyy", "h a, d MMM yyyy",
                "h a, MMMM d yyyy", "h a, MMMM dd yyyy", "h a, dd MMM yyyy", "h a, dd/MM/yyyy",
                "h a, d/M/yyyy", "h a, MM/dd/yyyy", "h a, M/d/yyyy",

                // Permutations for "h a" with Date Formats (with single or two digits in day and month)
                "dd/M/yyyy h a", "d/MM/yyyy h a", "dd/M/yy h a", "d/M/yy h a", "dd/MM/yyyy h a, MMM d yyyy",
                "dd/M/yyyy h a, MMM d yyyy", "dd/MM/yy h a, MMM d yyyy", "d/M/yy h a, MMM d yyyy",

                // International & Alternative Styles
                "yyyy年MM月dd日", "d MMM yyyy HH:mm:ss", "d MMM yyyy h:mm a", "MMMM d yyyy, h:mm a",
                "dd.MM.yyyy", "d.MM.yyyy", "d.M.yyyy", "dd.MM.yy", "d.MM.yy", "MMM d yyyy HH:mm"
        );


        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                if (pattern.contains("HH") || pattern.contains("mm")) {
                    return LocalDateTime.parse(date, formatter);
                } else {
                    LocalDate localDate = LocalDate.parse(date, formatter);
                    return localDate.atTime(LocalTime.MIDNIGHT); // Add default time midnight if no date specified
                }
            } catch (DateTimeParseException ignored) {
                // Ignore the exception as other formats may be correct
                // Continue to loop through the list of formats
            }
        }
        // Returns null if no formatter pattern matches
        return null;

    }


}
