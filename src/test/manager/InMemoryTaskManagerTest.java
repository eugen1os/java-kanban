package manager;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;  // Добавьте этот импорт

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void testTaskEqualityById() {
        Task task1 = new Task(1, "Task 1", "Description", Status.NEW);
        Task task2 = new Task(1, "Task 2", "New description", Status.IN_PROGRESS);
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void testSubtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic(1, "Epic", "Description");
        manager.createEpic(epic);

        Subtask subtask = new Subtask(1, "Subtask", "Description", Status.NEW, 1);
        assertThrows(IllegalArgumentException.class, () -> manager.createSubtask(subtask),
                "Подзадача не может быть своим же эпиком");
    }

    @Test
    void testEpicCannotAddToItself() {
        Epic epic = new Epic(1, "Epic", "Description");
        manager.createEpic(epic);

        Subtask subtask = new Subtask(1, "Subtask", "Description", Status.NEW, 1);
        assertThrows(IllegalArgumentException.class, () -> manager.createSubtask(subtask),
                "Подзадача не может быть своим же эпиком");
    }

    @Test
    void testManagerInitialization() {
        assertNotNull(manager.getAllTasks(), "Менеджер задач должен быть проинициализирован");
        assertNotNull(manager.getAllEpics(), "Менеджер эпиков должен быть проинициализирован");
        assertNotNull(manager.getAllSubtasks(), "Менеджер подзадач должен быть проинициализирован");
    }

    @Test
    void testAddAndFindDifferentTaskTypes() {
        Task task = new Task(1, "Task", "Description", Status.NEW);
        manager.createTask(task);

        Epic epic = new Epic(2, "Epic", "Description");
        manager.createEpic(epic);

        Subtask subtask = new Subtask(3, "Subtask", "Description", Status.NEW, 2);
        manager.createSubtask(subtask);

        assertEquals(task, manager.getTaskById(1), "Задача должна находиться по id");
        assertEquals(epic, manager.getEpicById(2), "Эпик должен находиться по id");
        assertEquals(subtask, manager.getSubtaskById(3), "Подзадача должна находиться по id");
    }

    @Test
    void testTaskImmutabilityWhenAdded() {
        Task original = new Task(1, "Original", "Description", Status.NEW);
        manager.createTask(original);

        Task fromManager = manager.getTaskById(1);
        assertEquals(original.getName(), fromManager.getName(), "Имя задачи не должно изменяться");
        assertEquals(original.getDescription(), fromManager.getDescription(), "Описание задачи не должно изменяться");
        assertEquals(original.getStatus(), fromManager.getStatus(), "Статус задачи не должен изменяться");
    }

    @Test
    void testGeneratedIdDoesNotConflict() {
        Task task1 = new Task(0, "Task 1", "Description", Status.NEW);
        manager.createTask(task1);

        Task task2 = new Task(0, "Task 2", "Description", Status.NEW);
        manager.createTask(task2);

        assertNotEquals(task1.getId(), task2.getId(),
                "Автоматически сгенерированные id не должны конфликтовать");
    }

    @Test
    void testHistoryPreservation() {
        Task task = new Task(1, "Task", "Description", Status.NEW);
        manager.createTask(task);
        manager.getTaskById(1);

        assertEquals(1, manager.getHistory().size(), "История должна содержать 1 задачу");
        assertEquals(task, manager.getHistory().get(0), "Задача в истории должна соответствовать добавленной");
    }

    // Добавленные тесты
    @Test
    void testEpicStatusCalculation() {
        Epic epic = new Epic(1, "Epic", "Description");
        manager.createEpic(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Статус нового эпика должен быть NEW");

        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description", Status.NEW, 1);
        manager.createSubtask(subtask1);
        assertEquals(Status.NEW, manager.getEpicById(1).getStatus(),
                "Эпик с подзадачами NEW должен иметь статус NEW");

        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(1).getStatus(),
                "Эпик с подзадачами IN_PROGRESS должен иметь статус IN_PROGRESS");

        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description", Status.DONE, 1);
        manager.createSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(1).getStatus(),
                "Эпик с подзадачами разных статусов должен иметь статус IN_PROGRESS");

        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        assertEquals(Status.DONE, manager.getEpicById(1).getStatus(),
                "Эпик со всеми подзадачами DONE должен иметь статус DONE");
    }

    @Test
    void testDeleteAllTasks() {
        manager.createTask(new Task(1, "Task 1", "Description", Status.NEW));
        manager.createTask(new Task(2, "Task 2", "Description", Status.NEW));

        manager.deleteAllTasks();

        assertTrue(manager.getAllTasks().isEmpty(), "Список задач должен быть пуст после удаления");
        assertEquals(0, manager.getHistory().size(), "История должна быть пуста после удаления задач");
    }

    @Test
    void testDeleteAllEpicsWithSubtasks() {
        Epic epic = new Epic(1, "Epic", "Description");
        manager.createEpic(epic);
        manager.createSubtask(new Subtask(2, "Subtask 1", "Description", Status.NEW, 1));
        manager.createSubtask(new Subtask(3, "Subtask 2", "Description", Status.IN_PROGRESS, 1));

        manager.deleteAllEpics();

        assertTrue(manager.getAllEpics().isEmpty(), "Список эпиков должен быть пуст");
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пуст");
        assertEquals(0, manager.getHistory().size(), "История должна быть пуста после удаления");
    }

    @Test
    void testUpdateTask() {
        Task task = new Task(1, "Original", "Original desc", Status.NEW);
        manager.createTask(task);

        Task updated = new Task(1, "Updated", "Updated desc", Status.DONE);
        manager.updateTask(updated);

        Task fromManager = manager.getTaskById(1);
        assertEquals("Updated", fromManager.getName(), "Имя задачи должно обновиться");
        assertEquals("Updated desc", fromManager.getDescription(), "Описание задачи должно обновиться");
        assertEquals(Status.DONE, fromManager.getStatus(), "Статус задачи должен обновиться");
    }

    @Test
    void testHistoryManagerOrderAndLimit() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task(i, "Task " + i, "Description", Status.NEW);
            manager.createTask(task);
        }

        for (int i = 1; i <= 15; i++) {
            manager.getTaskById(i);
        }

        List<Task> history = manager.getHistory();
        assertEquals(10, history.size(), "История должна содержать максимум 10 задач");

        for (int i = 6; i <= 15; i++) {
            assertEquals(i, history.get(i-6).getId(),
                    "История должна содержать задачи в порядке их просмотра");
        }
    }
}