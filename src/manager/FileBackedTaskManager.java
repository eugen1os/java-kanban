package manager;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.loadFromFile();
        return manager;
    }

    private void loadFromFile() {
        if (!file.exists()) {
            return;
        }

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            if (lines.length <= 1) {
                return;
            }

            boolean readingHistory = false;
            List<Integer> historyIds = new ArrayList<>();
            int maxId = 0;

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    readingHistory = true;
                    continue;
                }

                if (!readingHistory) {
                    Task task = fromString(line);
                    if (task != null) {
                        if (!hasTaskWithId(task.getId())) {
                            addTaskWithoutSaving(task);
                            maxId = Math.max(maxId, task.getId());
                        }
                    }
                } else {
                    String[] historyParts = line.split(",");
                    for (String idStr : historyParts) {
                        try {
                            int id = Integer.parseInt(idStr.trim());
                            historyIds.add(id);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }

            nextId = maxId + 1;

            for (int id : historyIds) {
                Task task = findTaskById(id);
                if (task != null) {
                    historyManager.add(task);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }
    }

    private boolean hasTaskWithId(int id) {
        return tasks.containsKey(id) || epics.containsKey(id) || subtasks.containsKey(id);
    }

    private Task findTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        }
        return null;
    }

    private void addTaskWithoutSaving(Task task) {
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            epics.put(epic.getId(), epic);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtaskId(subtask.getId());
            }
        } else {
            tasks.put(task.getId(), task);
        }
    }

    private Task fromString(String value) {
        String[] fields = value.split(",", -1);
        if (fields.length < 5) {
            return null;
        }

        try {
            int id = Integer.parseInt(fields[0]);
            TaskType type = TaskType.valueOf(fields[1]);
            String name = fields[2];
            Status status = Status.valueOf(fields[3]);
            String description = fields[4].isEmpty() ? "" : fields[4];

            switch (type) {
                case TASK:
                    return new Task(id, name, description, status);
                case EPIC:
                    Epic epic = new Epic(id, name, description);
                    epic.setStatus(status);
                    return epic;
                case SUBTASK:
                    if (fields.length >= 6 && !fields[5].isEmpty()) {
                        int epicId = Integer.parseInt(fields[5]);
                        return new Subtask(id, name, description, status, epicId);
                    }
                    return null;
                default:
                    return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("id,type,name,status,description,epic");

            for (Task task : getAllTasks()) {
                writer.println(toString(task));
            }
            for (Epic epic : getAllEpics()) {
                writer.println(toString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.println(toString(subtask));
            }

            writer.println();
            List<Task> history = getHistory();
            if (!history.isEmpty()) {
                StringBuilder historyLine = new StringBuilder();
                for (Task task : history) {
                    if (historyLine.length() > 0) {
                        historyLine.append(",");
                    }
                    historyLine.append(task.getId());
                }
                writer.println(historyLine);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }

    private String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%d,%s,%s,%s,%s,%d",
                    task.getId(),
                    TaskType.SUBTASK,
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    subtask.getEpicId());
        } else if (task instanceof Epic) {
            return String.format("%d,%s,%s,%s,%s,",
                    task.getId(),
                    TaskType.EPIC,
                    task.getName(),
                    task.getStatus(),
                    task.getDescription());
        } else {
            return String.format("%d,%s,%s,%s,%s,",
                    task.getId(),
                    TaskType.TASK,
                    task.getName(),
                    task.getStatus(),
                    task.getDescription());
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return super.getSubtaskById(id);
    }
}