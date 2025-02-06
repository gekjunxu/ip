package bob.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;

public class ParserTest {

    @Test
    public void parseTask_validTodoTask_success() throws BobException {
        String input = "[T][ ] Buy groceries"; // Valid Todo task
        Task task = Parser.parseTask(input); // Parsing the task

        assertNotNull(task);
        assertInstanceOf(Todo.class, task); // Verify that the parsed task is of type Todo
        assertEquals("Buy groceries", task.toString().substring(7)); // Ensure the description is correct
    }

    @Test
    public void parseTask_validDeadlineTask_success() throws BobException {
        String input = "[D][ ] Submit assignment (by: 2025-01-31 23:59)"; // Valid Deadline task
        Task task = Parser.parseTask(input); // Parsing the task

        assertNotNull(task);
        assertInstanceOf(Deadline.class, task); // Verify that the parsed task is of type Deadline
        assertEquals("Submit assignment", task.toString()
                .substring(7, task.toString().indexOf(" (by:"))); // Ensure the description is correct
    }

    @Test
    public void parseTask_validEventTask_success() throws BobException {
        String input = "[E][ ] Team meeting (from: 2025-01-31 14:00 to: 2025-01-31 16:00)"; // Valid Event task
        Task task = Parser.parseTask(input); // Parsing the task

        assertNotNull(task);
        // Verify that the parsed task is of type Event
        assertInstanceOf(Event.class, task);
        // Ensure the description is correct
        assertEquals("Team meeting", task.toString().substring(7, task.toString().indexOf(" (from:")));
    }


    @Test
    void parseTask_invalidTaskType_exceptionThrown() {
        String invalidTaskLine = "[X][ ] Invalid task type (by: Jan 31 2025 23:59)";

        BobException thrown = assertThrows(BobException.class, () -> Parser.parseTask(invalidTaskLine));

        assertEquals("Unknown task type in file: [X][ ] Invalid task type (by: Jan 31 2025 23:59)",
                thrown.getMessage());
    }


    @Test
    public void parseDate_validDate_success() {
        String validDate = "2025-01-31"; // Valid date format (yyyy-MM-dd)

        LocalDateTime result = Parser.parseDate(validDate); // Parsing the date

        assertNotNull(result); // Ensure that the result is not null
        assertEquals(2025, result.getYear()); // Check if the year is correct
        assertEquals(1, result.getMonthValue()); // Check if the month is correct
        assertEquals(31, result.getDayOfMonth()); // Check if the day is correct
    }


    @Test
    public void parseDate_invalidDate_returnsNull() {
        String invalidDate = "2025-13-32"; // Invalid date (month 13, day 32)

        LocalDateTime result = Parser.parseDate(invalidDate); // Parsing the invalid date

        assertNull(result); // The result should be null since the date is invalid
    }


    @Test
    public void parseDate_validDateWithTime_success() {
        String validDateWithTime = "2025-01-31 14:30"; // Valid date and time format (yyyy-MM-dd HH:mm)

        LocalDateTime result = Parser.parseDate(validDateWithTime); // Parsing the date with time

        assertNotNull(result); // Ensure that the result is not null
        assertEquals(2025, result.getYear()); // Check if the year is correct
        assertEquals(1, result.getMonthValue()); // Check if the month is correct
        assertEquals(31, result.getDayOfMonth()); // Check if the day is correct
        assertEquals(14, result.getHour()); // Check if the hour is correct
        assertEquals(30, result.getMinute()); // Check if the minute is correct
    }


    @Test
    public void parseDate_invalidFormat_returnsNull() {
        String invalidFormat = "31+02+2025"; // Invalid format (incorrect separator)

        LocalDateTime result = Parser.parseDate(invalidFormat); // Parsing the invalid date format

        assertNull(result); // The result should be null since the format doesn't match any expected pattern
    }


}
