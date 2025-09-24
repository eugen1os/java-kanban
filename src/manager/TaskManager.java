package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    // Методы для Task
    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    // Методы для Epic
    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    // Методы для Subtask
    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    // Дополнительные методы
    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();
}