package manager;

import java.util.List;

public interface HistoryManager {
    void add(model.Task task);
    void remove(int id);
    List<model.Task> getHistory();
}