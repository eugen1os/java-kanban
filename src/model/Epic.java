package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksIds;

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        this.subtasksIds = new ArrayList<>();
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.subtasksIds = new ArrayList<>();
    }

    public List<Integer> getSubtasksIds() {
        return new ArrayList<>(subtasksIds);
    }

    public void addSubtaskId(int subtaskId) {
        if (!subtasksIds.contains(subtaskId)) {
            subtasksIds.add(subtaskId);
        }
    }

    public void removeSubtaskId(int subtaskId) {
        subtasksIds.remove(Integer.valueOf(subtaskId));
    }

    public void clearSubtasks() {
        subtasksIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}