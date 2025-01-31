package bob.storage;

import bob.command.Parser;
import bob.exception.BobException;
import bob.task.Task;
import bob.task.TaskList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Storage {

    protected Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public boolean directoryExists() {
        return Files.exists(filePath);
    }

    public void createDirectory() throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);
    }

    public ArrayList<Task> loadTasks() throws IOException, BobException {
        ArrayList<Task> tasks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
        String line;
        while ((line = reader.readLine()) != null) {
            Task task = Parser.parseTask(line); // Parse each line into a Task
            tasks.add(task);
        }
        return tasks;
    }

    public void writeTasksToFile(TaskList taskList) throws IOException {
        File file = filePath.toFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < taskList.size(); i++) {
            writer.write(taskList.get(i).toString());
            writer.newLine();
        }
        writer.close();
    }

}
