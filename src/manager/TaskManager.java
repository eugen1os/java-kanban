import java.util.*;

public class TaskManager {
    private int nextId = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    // Методы для Task
    public List<Task> getAllTasks() { return new ArrayList<>(tasks.values()); }
    public void deleteAllTasks() { tasks.clear(); }
    public Task getTaskById(int id) { return tasks.get(id); }
    public void createTask(Task task) { task.id = nextId++; tasks.put(task.id, task); }
    public void updateTask(Task task) { if (tasks.containsKey(task.id)) tasks.put(task.id, task); }
    public void deleteTaskById(int id) { tasks.remove(id); }

    // Методы для Epic
    public List<Epic> getAllEpics() { return new ArrayList<>(epics.values()); }
    public void deleteAllEpics() { epics.clear(); subtasks.clear(); }
    public Epic getEpicById(int id) { return epics.get(id); }
    public void createEpic(Epic epic) { epic.id = nextId++; epics.put(epic.id, epic); }
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.id)) {
            Epic savedEpic = epics.get(epic.id);
            savedEpic.name = epic.name;
            savedEpic.description = epic.description;
        }
    }
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    // Методы для Subtask
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(subtasks.values()); }
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic.id);
        }
    }
    public Subtask getSubtaskById(int id) { return subtasks.get(id); }
    public void createSubtask(Subtask subtask) {
        subtask.id = nextId++;
        subtasks.put(subtask.id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.id);
            updateEpicStatus(epic.id);
        }
    }
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.id)) {
            subtasks.put(subtask.id, subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic.id);
            }
        }
    }

    // Дополнительные методы
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null || epic.getSubtaskIds().isEmpty()) return;

        boolean allDone = true;
        boolean allNew = true;

        for (int subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) continue;

            if (subtask.status != Status.DONE) allDone = false;
            if (subtask.status != Status.NEW) allNew = false;
        }

        if (allDone) epic.status = Status.DONE;
        else if (allNew) epic.status = Status.NEW;
        else epic.status = Status.IN_PROGRESS;
    }
}