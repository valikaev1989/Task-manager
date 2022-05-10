package ru.yandex.praktikum.main;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.taskmanager.InMemoryTaskManager;

import java.time.LocalDateTime;

public class qwe {
    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static void main(String[] args) throws ManagerSaveException, InterruptedException {
        Task task1 = new Task("Task1", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 15));
        Task task5 = new Task("Task5", "description");
        Task task2 = new Task("Task2", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 25));
        Task task6 = new Task("Task6", "description");
        Task task3 = new Task("Task3", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 30));
        Task task4 = new Task("Task4", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 35));
        Task task8 = new Task("Task8", "description");
        Task task7 = new Task("Task7", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 33));
        Task task9 = new Task("Task9", "description");

        EpicTask epic1 = new EpicTask("epic1", "desc");
        EpicTask epic2 = new EpicTask("epic2", "desc");
        inMemoryTaskManager.createEpicTask(epic1);
        inMemoryTaskManager.createEpicTask(epic2);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task5);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task6);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createTask(task4);
        inMemoryTaskManager.createTask(task8);
        inMemoryTaskManager.createTask(task7);
        inMemoryTaskManager.createTask(task9);

        SubTask subTask1 = new SubTask("sub1", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 26), epic1.getId());
        SubTask subTask5 = new SubTask("sub5", "desc", epic1.getId());
        SubTask subTask2 = new SubTask("sub2", "desc", 2, LocalDateTime.of(2022, 5, 11, 10, 40), epic1.getId());
        SubTask subTask3 = new SubTask("sub3", "desc", 3, LocalDateTime.of(2022, 5, 11, 10, 21), epic2.getId());
        SubTask subTask4 = new SubTask("sub4", "desc", 4, LocalDateTime.of(2022, 5, 11, 10, 37), epic1.getId());
        SubTask subTask6 = new SubTask("sub6", "desc", epic2.getId());
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask5);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        inMemoryTaskManager.createSubTask(subTask4);
        inMemoryTaskManager.createSubTask(subTask6);
        System.out.println(task1);

        System.out.println("epic1start " + epic1.getStartTime());
        System.out.println("epic1End " + epic1.getEndTime());
        System.out.println("epic1Duration " + epic1.getDuration());
        System.out.println("epic2start " + epic2.getStartTime());
        System.out.println("epic2End " + epic2.getEndTime());
        System.out.println("epic2Duration " + epic2.getDuration());
        subTask6.setStartTime(LocalDateTime.of(2022, 5, 11, 10, 50));
        subTask6.setDuration(12);
        inMemoryTaskManager.updateSubTask(subTask6);
        System.out.println("epic2start " + epic2.getStartTime());
        System.out.println("epic2End " + epic2.getEndTime());
        System.out.println("epic2Duration " + epic2.getDuration());
    }
}
