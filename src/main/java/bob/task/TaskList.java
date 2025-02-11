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
        assert tasks != null : "A valid ArrayList must be passed in";
        this.tasks = tasks;
    }

    /**
     * Adds tasks to the existing list of tasks.
     *
     * @param task The new task to be added.
     */
    public void addTask(Task task) {
        assert task != null : "Task cannot be null";
        this.tasks.add(task);
    }

    /**
     * Deletes tasks from the existing list of tasks.
     *
     * @param index The index of the task to be deleted
     */
    public void deleteTask(int index) {
        assert index >= 0 && index < this.tasks.size() : "Task index out of bounds";
        this.tasks.remove(index);
    }

    /**
     * Lists out the tasks in the list of tasks.
     */
    public String listTasks() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < this.tasks.size(); i++) {
            output.append(i + 1).append(". ").append(this.tasks.get(i).toString()).append("\n");
        }
        return output.toString();
    }

    /**
     * Takes in an input sequence and returns a TaskList containing matching
     * tasks.
     *
     * @param name To find in description of tasks.
     * @return TaskList containing the found tasks.
     */
    public TaskList findTasks(String name) {
        assert name != null : "Task name cannot be null";
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.description.contains(name)) {
                foundTasks.add(task);
            }
        }
        return new TaskList(foundTasks);
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
     *
     * @param i index of the task.
     */
    public Task get(int i) {
        return tasks.get(i);
    }
}
