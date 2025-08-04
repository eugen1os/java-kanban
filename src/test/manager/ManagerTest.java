package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void getDefaultReturnsInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Менеджер задач не должен быть null");
        assertNotNull(manager.getAllTasks(), "Список задач должен быть инициализирован");
    }

    @Test
    void getDefaultHistoryReturnsInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории не должен быть null");
        assertNotNull(historyManager.getHistory(), "История должна быть инициализирована");
    }
}