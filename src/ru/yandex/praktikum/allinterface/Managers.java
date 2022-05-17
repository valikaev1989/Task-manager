package ru.yandex.praktikum.allinterface;

import ru.yandex.praktikum.api.HTTPTaskManager;
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

    static HTTPTaskManager getHTTPTaskManager() throws IOException, InterruptedException {
        return new HTTPTaskManager("http://localhost:8078");
    }

    static HTTPTaskManager getHTTPTaskManagerLoad() throws IOException, InterruptedException {
        return HTTPTaskManager.loadInServerFromSavedTasks();
    }

}