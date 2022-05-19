package ru.yandex.praktikum.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.api.*;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest {

    @Test
    void checkHTTPManagerSaveLoad() throws IOException, InterruptedException {
        new KVServer().start();
        final HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        Task task1 = new Task("Task1", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 15));
        Task task5 = new Task("Task5", "description");
        Task task2 = new Task("Task2", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 20));
        Task task6 = new Task("Task6", "description");
        Task task3 = new Task("Task3", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 25));
        Task task4 = new Task("Task4", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 30));
        Task task8 = new Task("Task8", "description");
        Task task7 = new Task("Task7", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 35));
        Task task9 = new Task("Task9", "description");
        EpicTask epic1 = new EpicTask("epic1", "desc");
        EpicTask epic2 = new EpicTask("epic2", "desc");

        httpTaskManager.createEpicTask(epic1);
        httpTaskManager.createEpicTask(epic2);
        for (Task task10 : Arrays.asList(task1, task5, task2, task6, task3, task4, task8, task7, task9)) {
            httpTaskManager.createTask(task10);
        }
        SubTask subTask1 = new SubTask("sub1", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 40), epic1.getId());
        SubTask subTask5 = new SubTask("sub5", "desc", epic1.getId());
        SubTask subTask2 = new SubTask("sub2", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 45), epic1.getId());
        SubTask subTask3 = new SubTask("sub3", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 50), epic2.getId());
        SubTask subTask4 = new SubTask("sub4", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 55), epic1.getId());
        SubTask subTask6 = new SubTask("sub6", "desc", epic2.getId());
        for (SubTask subTask7 : Arrays.asList(subTask1, subTask5, subTask2, subTask3, subTask4, subTask6)) {
            httpTaskManager.createSubTask(subTask7);
        }
        httpTaskManager.getEpicTask(epic1.getId());
        httpTaskManager.getEpicTask(epic2.getId());
        for (Task task : Arrays.asList(task1, task2, task3, task4, task5, task6)) {
            httpTaskManager.getTask(task.getId());
        }
        for (SubTask subTask : Arrays.asList(subTask1, subTask5, subTask2, subTask3, subTask4, subTask5)) {
            httpTaskManager.getSubTask(subTask.getId());
        }

        String q = gson.toJson(httpTaskManager.getListTask());
        String w = gson.toJson(httpTaskManager.getListEpicTask());
        String e = gson.toJson(httpTaskManager.getListSubTask());
        String r = gson.toJson(httpTaskManager.getHistoryList());
        String a = gson.toJson(httpTaskManager.getPrioritizedTasks());
        assertEquals(q, httpTaskManager.loadFromKeyKVServer("tasks"));
        assertEquals(w, httpTaskManager.loadFromKeyKVServer("epicTasks"));
        assertEquals(e, httpTaskManager.loadFromKeyKVServer("subTasks"));
        assertEquals(r, httpTaskManager.loadFromKeyKVServer("historyTasks"));
        assertEquals(a, httpTaskManager.loadFromKeyKVServer("priorityTasks"));
    }
}