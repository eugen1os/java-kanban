package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testTaskEquality() {
        Task task1 = new Task(1, "Task 1", "Description", Status.NEW);
        Task task2 = new Task(1, "Task 2", "New description", Status.IN_PROGRESS);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void testEpicEquality() {
        Epic epic1 = new Epic(1, "Epic 1", "Description");
        Epic epic2 = new Epic(1, "Epic 2", "New description");

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }
}