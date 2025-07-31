package manager;

import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        history.remove(task); // Удаляем дубликаты
        history.add(task);
        if (history.size() > 10) {
            history.remove(0); // Удаляем самый старый элемент
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history); // Возвращаем копию
    }
}