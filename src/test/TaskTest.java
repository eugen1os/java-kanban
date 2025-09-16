package test;

import manager.Managers;
import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void testTaskCreationAndRetrieval() {
        Task task = new Task(0, "Изучить Java", "Пройти курс по Java", Status.NEW);
        manager.createTask(task);

        assertEquals(1, manager.getAllTasks().size());

        Task retrieved = manager.getTaskById(task.getId());
        assertNotNull(retrieved);
        assertEquals("Изучить Java", retrieved.getName());
        assertEquals("Пройти курс по Java", retrieved.getDescription());
        assertEquals(Status.NEW, retrieved.getStatus());
    }

    @Test
    void testTaskStatusFlow() {
        Task task = new Task(0, "Тестовая задача", "Описание", Status.NEW);
        manager.createTask(task);

        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus());

        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    void testEpicWithMultipleSubtasks() {
        Epic epic = new Epic(0, "Разработать приложение", "Создать трекер задач");
        manager.createEpic(epic);

        Subtask design = new Subtask(0, "Дизайн", "Спроектировать интерфейс", Status.NEW, epic.getId());
        Subtask development = new Subtask(0, "Разработка", "Написать код", Status.NEW, epic.getId());
        Subtask testing = new Subtask(0, "Тестирование", "Протестировать приложение", Status.NEW, epic.getId());

        manager.createSubtask(design);
        manager.createSubtask(development);
        manager.createSubtask(testing);

        assertEquals(3, manager.getSubtasksByEpicId(epic.getId()).size());
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void testEpicStatusTransitions() {
        Epic epic = new Epic(0, "Эпик со статусами", "Тест смены статусов");
        manager.createEpic(epic);

        Subtask subtask = new Subtask(0, "Тестовая подзадача", "Описание", Status.NEW, epic.getId());
        manager.createSubtask(subtask);

        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask);
        assertEquals(Status.DONE, epic.getStatus());

        Subtask newSubtask = new Subtask(0, "Новая подзадача", "Описание", Status.NEW, epic.getId());
        manager.createSubtask(newSubtask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testHistoryOrderAndNoDuplicates() {
        Task task1 = new Task(0, "Задача 1", "Описание", Status.NEW);
        Task task2 = new Task(0, "Задача 2", "Описание", Status.NEW);
        Epic epic = new Epic(0, "Эпик", "Описание");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic.getId());
        manager.getTaskById(task1.getId());

        List<Task> history = manager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task2.getId(), history.get(0).getId());
        assertEquals(epic.getId(), history.get(1).getId());
        assertEquals(task1.getId(), history.get(2).getId());
    }

    @Test
    void testHistoryAfterDeletion() {
        Task task1 = new Task(0, "Задача 1", "Описание", Status.NEW);
        Task task2 = new Task(0, "Задача 2", "Описание", Status.NEW);

        manager.createTask(task1);
        manager.createTask(task2);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        manager.deleteTaskById(task1.getId());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2.getId(), history.get(0).getId());
    }

    @Test
    void testInvalidOperations() {
        assertNull(manager.getTaskById(999));
        assertNull(manager.getEpicById(999));
        assertNull(manager.getSubtaskById(999));

        Task nonExistent = new Task(999, "Несуществующая", "Описание", Status.NEW);
        manager.updateTask(nonExistent);
        assertNull(manager.getTaskById(999));
    }

    @Test
    void testSubtaskWithNonExistentEpic() {
        Subtask subtask = new Subtask(0, "Подзадача", "Описание", Status.NEW, 999);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.createSubtask(subtask);
        });
    }

    @Test
    void testEmptyManagers() {
        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    void testMassOperations() {
        for (int i = 1; i <= 50; i++) {
            Task task = new Task(0, "Задача " + i, "Описание " + i, Status.NEW);
            manager.createTask(task);
        }

        assertEquals(50, manager.getAllTasks().size());

        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }
}