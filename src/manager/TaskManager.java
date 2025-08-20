package manager;

import java.util.List;

public interface TaskManager {
    List<model.Task> getAllTasks();
    void deleteAllTasks();
    model.Task getTask(int id);
    model.Task createTask(model.Task task);
    void updateTask(model.Task task);
    void deleteTask(int id);

    List<model.Epic> getAllEpics();
    List<model.Subtask> getAllSubtasks();
    model.Epic getEpic(int id);
    model.Subtask getSubtask(int id);
    model.Epic createEpic(model.Epic epic);
    model.Subtask createSubtask(model.Subtask subtask);
    void updateEpic(model.Epic epic);
    void updateSubtask(model.Subtask subtask);
    void deleteEpic(int id);
    void deleteSubtask(int id);
    List<model.Subtask> getSubtasksByEpic(int epicId);

    List<model.Task> getHistory();
}