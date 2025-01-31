package bob.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {

    @Test
    public void toString_success() {
        // Task with 1 word prints successfully
        assertEquals("[T][ ] eat", new Todo("eat").toString());

        // Task with 2 words prints successfully
        assertEquals("[T][ ] read book", new Todo("read book").toString());

        // Task with special symbols prints successfully
        assertEquals("[T][ ] read book!!!@#$%^&*()", new Todo("read book!!!@#$%^&*()").toString());

        // Task with special symbols prints successfully
        assertEquals("[T][ ] read book!!!@#$%^&*()", new Todo("read book!!!@#$%^&*()").toString());
    }

    @Test
    public void markDone_success() {
        Todo todo = new Todo("finish homework");
        todo.markDone();
        assertEquals("[T][X] finish homework", todo.toString());
    }

    @Test
    public void unMarkDone_success() {
        Todo todo = new Todo("finish homework");
        todo.markDone();
        assertEquals("[T][X] finish homework", todo.toString());
        todo.unMarkDone();
        assertEquals("[T][ ] finish homework", todo.toString());
    }


}
