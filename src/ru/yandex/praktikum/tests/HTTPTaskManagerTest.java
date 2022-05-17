package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.allinterface.Managers;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.api.HTTPTaskManager;
import ru.yandex.praktikum.api.KVServer;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.task.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;

public class HTTPTaskManagerTest {

    @Test
    void create() throws IOException, InterruptedException {
        new KVServer().start();
        final HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");

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
        httpTaskManager.createTask(task1);
        httpTaskManager.createTask(task5);
        httpTaskManager.createTask(task2);
        httpTaskManager.createTask(task6);
        httpTaskManager.createTask(task3);
        httpTaskManager.createTask(task4);
        httpTaskManager.createTask(task8);
        httpTaskManager.createTask(task7);
        httpTaskManager.createTask(task9);
        SubTask subTask1 = new SubTask("sub1", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 40), epic1.getId());
        SubTask subTask5 = new SubTask("sub5", "desc", epic1.getId());
        SubTask subTask2 = new SubTask("sub2", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 45), epic1.getId());
        SubTask subTask3 = new SubTask("sub3", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 50), epic2.getId());
        SubTask subTask4 = new SubTask("sub4", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 55), epic1.getId());
        SubTask subTask6 = new SubTask("sub6", "desc", epic2.getId());
        httpTaskManager.createSubTask(subTask1);
        httpTaskManager.createSubTask(subTask5);
        httpTaskManager.createSubTask(subTask2);
        httpTaskManager.createSubTask(subTask3);
        httpTaskManager.createSubTask(subTask4);
        httpTaskManager.createSubTask(subTask6);
        httpTaskManager.getEpicTask(epic1.getId());
        httpTaskManager.getEpicTask(epic2.getId());
        httpTaskManager.getTask(task1.getId());
        httpTaskManager.getTask(task2.getId());
        httpTaskManager.getTask(task3.getId());
        httpTaskManager.getTask(task4.getId());
        httpTaskManager.getTask(task5.getId());
        httpTaskManager.getTask(task6.getId());
        httpTaskManager.getSubTask(subTask1.getId());
        httpTaskManager.getSubTask(subTask5.getId());
        httpTaskManager.getSubTask(subTask2.getId());
        httpTaskManager.getSubTask(subTask3.getId());
        httpTaskManager.getSubTask(subTask4.getId());
        httpTaskManager.getSubTask(subTask5.getId());
        httpTaskManager.loadFromServer();


    }


}
