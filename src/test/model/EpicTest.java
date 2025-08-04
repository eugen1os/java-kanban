package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic(1, "Epic 1", "Description 1");
        Epic epic2 = new Epic(1, "Epic 2", "Description 2");
        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }
}