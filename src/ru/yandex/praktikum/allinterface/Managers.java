package ru.yandex.praktikum.allinterface;

import ru.yandex.praktikum.historymanager.InMemoryHistoryManager;
import ru.yandex.praktikum.taskmanager.InMemoryTaskManager;

public interface Managers {
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}