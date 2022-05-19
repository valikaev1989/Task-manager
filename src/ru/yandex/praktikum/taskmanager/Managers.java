package ru.yandex.praktikum.taskmanager;

import ru.yandex.praktikum.allinterface.HistoryManager;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.api.HTTPTaskManager;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.historymanager.InMemoryHistoryManager;
import ru.yandex.praktikum.taskmanager.FileBackedTasksManager;
import ru.yandex.praktikum.taskmanager.InMemoryTaskManager;

import java.io.File;
import java.io.IOException;

public class Managers {
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

   public static FileBackedTasksManager getFileBackedTasksManager() throws ManagerSaveException {
        return FileBackedTasksManager.loadFromFile(new File("SavedTasks.csv"));
    }

   public static TaskManager getDefault() throws IOException, InterruptedException {
        return HTTPTaskManager.loadInServerFromSavedTasks();
    }
}