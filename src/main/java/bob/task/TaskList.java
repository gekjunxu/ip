package bob.task;

import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void deleteTask(int index) {
        System.out.println("\t   " + tasks.get(index).toString());
        this.tasks.remove(index);
    }

    public void listTasks() {
        for (int i = 0; i < this.tasks.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + this.tasks.get(i).toString());
        }
    }
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task get(int i) {
        return tasks.get(i);
    }
}
