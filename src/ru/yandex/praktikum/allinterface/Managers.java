package ru.yandex.praktikum.allinterface;

import ru.yandex.praktikum.api.HTTPTaskServer;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.historymanager.InMemoryHistoryManager;
import ru.yandex.praktikum.taskmanager.FileBackedTasksManager;
import ru.yandex.praktikum.taskmanager.InMemoryTaskManager;

import java.io.File;
import java.io.IOException;

public interface Managers {
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    static FileBackedTasksManager getFileBackedTasksManager() throws ManagerSaveException {
        return FileBackedTasksManager.loadFromFile(new File("SavedTasks.csv"));
    }

    static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    static TaskManager getHTTPTaskServer() throws IOException {
        return new HTTPTaskServer();
    }

}