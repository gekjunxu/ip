package bob.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import bob.command.Parser;
import bob.exception.BobException;
import bob.task.Task;
import bob.task.TaskList;


/**
 * Handles loading and saving tasks to a file.
 * This class is responsible for managing file operations such as reading tasks from a file,
 * writing tasks to a file, and ensuring the required storage directory exists.
 */
public class Storage {

    protected Path filePath;

    /**
     * Creates a Storage instance with the specified file path.
     *
     * @param filePath The path to the storage file.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Checks if the storage file exists.
     *
     * @return {@code true} if the file exists, {@code false} otherwise.
     */
    public boolean directoryExists() {
        return Files.exists(filePath);
    }

    /**
     * Creates the necessary directory and storage file if they do not exist.
     *
     * @throws IOException If an I/O error occurs while creating the directory or file.
     */
    public void createDirectory() throws IOException {
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return A list of tasks read from the file.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws BobException If the file contains an invalid task format.
     */
    public ArrayList<Task> loadTasks() throws IOException, BobException {
        ArrayList<Task> tasks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
        String line;
        while ((line = reader.readLine()) != null) {
            Task task = Parser.parseTask(line); // Parse each line into a Task
            tasks.add(task);
        }
        reader.close();
        return tasks;
    }

    /**
     * Writes the given task list to the storage file.
     *
     * @param taskList The list of tasks to be written to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
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
