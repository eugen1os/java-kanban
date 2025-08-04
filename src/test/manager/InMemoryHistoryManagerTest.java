package manager;

import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddTaskToHistory() {
        Task task = new Task(1, "Test task", "Description", Status.NEW);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertFalse(history.isEmpty(), "История не должна быть пустой после добавления задачи");
        assertEquals(1, history.size(), "История должна содержать ровно одну задачу");
        assertEquals(task, history.get(0), "Задача в истории должна соответствовать добавленной");
    }

    @Test
    void testNoDuplicatesInHistory() {
        Task task = new Task(1, "Test task", "Description", Status.NEW);

        // Добавляем одну и ту же задачу три раза
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не должна содержать дубликатов задач");
    }

    @Test
    void testHistoryLimit() {
        // Добавляем больше задач, чем лимит истории (10)
        for (int i = 1; i <= 15; i++) {
            Task task = new Task(i, "Task " + i, "Description", Status.NEW);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История не должна превышать лимит в 10 задач");

        // Проверяем, что остались последние добавленные задачи
        for (int i = 6; i <= 15; i++) {
            assertEquals(i, history.get(i-6).getId(),
                    "История должна содержать последние добавленные задачи");
        }
    }

    @Test
    void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой при инициализации");
    }


}