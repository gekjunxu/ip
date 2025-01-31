package bob.command;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertInstanceOf(Event.class, task); // Verify that the parsed task is of type Event
        assertEquals("Team meeting", task.toString().substring(7, task.toString().indexOf(" (from:"))); // Ensure the description is correct
    }


    @Test
    void parseTask_invalidTaskType_exceptionThrown() {
        String invalidTaskLine = "[X][ ] Invalid task type (by: Jan 31 2025 23:59)";

        BobException thrown = assertThrows(BobException.class, () -> {
            Parser.parseTask(invalidTaskLine);
        });

        assertEquals("Unknown task type in file: [X][ ] Invalid task type (by: Jan 31 2025 23:59)", thrown.getMessage());
    }




}
