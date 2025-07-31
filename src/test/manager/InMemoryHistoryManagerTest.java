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
    void testHistoryOrder() {
        Task task1 = new Task(1, "Task 1", "Description", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description", Status.IN_PROGRESS);
        Task task3 = new Task(3, "Task 3", "Description", Status.DONE);

        // Добавляем задачи в разном порядке
        historyManager.add(task1);
        historyManager.add(task3);
        historyManager.add(task2);
        historyManager.add(task1); // Повторно добавляем первую задачу

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 уникальные задачи");

        // Проверяем порядок: последняя добавленная задача должна быть последней в списке
        assertEquals(task3, history.get(1), "Неверный порядок задач в истории");
        assertEquals(task2, history.get(2), "Неверный порядок задач в истории");
        assertEquals(task1, history.get(0), "Первая задача должна переместиться в конец при повторном добавлении");
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

    @Test
    void testTaskDataPreservedInHistory() {
        Task original = new Task(1, "Original", "Description", Status.NEW);
        historyManager.add(original);

        // Изменяем задачу после добавления в историю
        original.setName("Modified");
        original.setStatus(Status.DONE);

        Task fromHistory = historyManager.getHistory().get(0);
        assertEquals("Original", fromHistory.getName(),
                "История должна сохранять оригинальное название задачи");
        assertEquals(Status.NEW, fromHistory.getStatus(),
                "История должна сохранять оригинальный статус задачи");
    }
}