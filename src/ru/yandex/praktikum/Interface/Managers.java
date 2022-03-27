package ru.yandex.praktikum.Interface;

import ru.yandex.praktikum.HistoryManager.InMemoryHistoryManager;
import ru.yandex.praktikum.TaskManager.InMemoryTaskManager;

public interface Managers {
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}