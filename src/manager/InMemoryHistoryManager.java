package manager;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final LinkedList<Task> historyOrder = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) return;

        taskMap.put(task.getId(), task);
        historyOrder.remove(task);
        historyOrder.addLast(task);

        if (historyOrder.size() > 10) {
            Task removed = historyOrder.removeFirst();
            if (!historyOrder.contains(removed)) {
                taskMap.remove(removed.getId());
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        for (Task task : historyOrder) {
            Task latestVersion = taskMap.get(task.getId());
            if (latestVersion != null) {
                result.add(latestVersion);
            }
        }
        return result;
    }
}