package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();
    void deleteAllTasks();
    Task getTask(int id);
    Task createTask(Task task);
    void updateTask(Task task);
    void deleteTask(int id);

    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();
    Epic getEpic(int id);
    Subtask getSubtask(int id);
    Epic createEpic(Epic epic);
    Subtask createSubtask(Subtask subtask);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);
    void deleteEpic(int id);
    void deleteSubtask(int id);
    List<Subtask> getSubtasksByEpic(int epicId);

    List<Task> getHistory();
}