package bob.task;

import java.util.ArrayList;

/**
 * An abstraction for the list of tasks.
 */
public class TaskList {

    private ArrayList<Task> tasks;

    /**
     * Creates a new TaskList instance with no tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a new TaskList instance with the specified tasks.
     *
     * @param tasks The existing list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds tasks to the existing list of tasks.
     *
     * @param task The new task to be added.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Deletes tasks from the existing list of tasks.
     *
     * @param index The index of the task to be deleted
     */
    public void deleteTask(int index) {
        System.out.println("\t   " + tasks.get(index).toString());
        this.tasks.remove(index);
    }

    /**
     * Lists out the tasks in the list of tasks.
     */
    public void listTasks() {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns true if there are no tasks in the list, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the task object at the specified index.
     * @param i index of the task.
     */
    public Task get(int i) {
        return tasks.get(i);
    }
}
