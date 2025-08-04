package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Desc 1", Status.NEW, 10);
        Subtask subtask2 = new Subtask(1, "Subtask 2", "Desc 2", Status.DONE, 20);

        assertEquals(subtask1, subtask2);
        assertEquals(subtask1.hashCode(), subtask2.hashCode());
    }

    @Test
    void subtasksWithDifferentIdShouldNotBeEqual() {
        Subtask subtask1 = new Subtask(1, "Subtask", "Desc", Status.NEW, 10);
        Subtask subtask2 = new Subtask(2, "Subtask", "Desc", Status.NEW, 10);

        assertNotEquals(subtask1, subtask2);
    }
}