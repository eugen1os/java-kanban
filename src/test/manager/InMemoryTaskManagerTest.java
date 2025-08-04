package manager;

import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager = Managers.getDefault();

    @Test
    void epicCannotBeSubtaskToItself() {
        Epic epic = new Epic(1, "Epic", "Description");
        manager.createEpic(epic);

        Subtask subtask = new Subtask(1, "Subtask", "Description", Status.NEW, 1);
        assertThrows(IllegalArgumentException.class, () -> manager.createSubtask(subtask),
                "Эпик не может быть подзадачей самого себя");
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        Subtask subtask = new Subtask(1, "Subtask", "Description", Status.NEW, 1);
        assertThrows(IllegalArgumentException.class, () -> manager.createSubtask(subtask),
                "Подзадача не может быть своим же эпиком");
    }

    @Test
    void managerCanAddAndFindDifferentTaskTypes() {
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
    void generatedIdsDoNotConflict() {
        Task task1 = new Task(0, "Task 1", "Description", Status.NEW);
        manager.createTask(task1);

        Task task2 = new Task(0, "Task 2", "Description", Status.NEW);
        manager.createTask(task2);

        assertNotEquals(task1.getId(), task2.getId(), "ID должны быть уникальными");
    }

    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task original = new Task(1, "Original", "Original desc", Status.NEW);
        manager.createTask(original);

        Task fromManager = manager.getTaskById(1);
        assertEquals(original.getName(), fromManager.getName(), "Имя не должно изменяться");
        assertEquals(original.getDescription(), fromManager.getDescription(), "Описание не должно изменяться");
        assertEquals(original.getStatus(), fromManager.getStatus(), "Статус не должен изменяться");
    }
}