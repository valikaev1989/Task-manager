package ru.yandex.praktikum.TaskManager;

public interface Managers {
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
