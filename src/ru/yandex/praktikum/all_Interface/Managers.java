package ru.yandex.praktikum.all_Interface;

import ru.yandex.praktikum.historyManager.InMemoryHistoryManager;
import ru.yandex.praktikum.taskManager.InMemoryTaskManager;

public interface Managers {
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}