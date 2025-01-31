package bob.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {

    @Test
    public void toString_success() {
        // Test 2 words
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 31, 23, 59);
        Deadline deadline = new Deadline("Finish project", dateTime);
        String expected = "[D][ ] Finish project (by: Jan 31 2025 23:59)";
        assertEquals(expected, deadline.toString());

        // Test 5 words
        LocalDateTime dateTime1 = LocalDateTime.of(2099, 1, 31, 23, 59);
        Deadline deadline1 = new Deadline("Deadline task with five words", dateTime1);
        String expected1 = "[D][ ] Deadline task with five words (by: Jan 31 2099 23:59)";
        assertEquals(expected1, deadline1.toString());
    }

    @Test
    public void markDone_success() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 31, 23, 59);
        Deadline deadline = new Deadline("Finish project", dateTime);

        // Mark the task as done
        deadline.markDone();

        String expected = "[D][X] Finish project (by: Jan 31 2025 23:59)";
        assertEquals(expected, deadline.toString());
    }

    @Test
    public void unMarkDone_success() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 31, 23, 59);
        Deadline deadline = new Deadline("Finish project", dateTime);
        deadline.markDone();

        // Unmark the task as done
        deadline.unMarkDone();

        String expected = "[D][ ] Finish project (by: Jan 31 2025 23:59)";
        assertEquals(expected, deadline.toString());
    }

}
