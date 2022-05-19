package ru.yandex.praktikum.main;

import ru.yandex.praktikum.api.HTTPTaskManager;
import ru.yandex.praktikum.api.HTTPTaskServer;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App2 {
    private final String URL = "http://localhost:8078";
    private HTTPTaskManager httpTaskManager = new HTTPTaskManager(URL);
    private final Scanner scanner = new Scanner(System.in);

    public App2() throws IOException, InterruptedException {
    }

    public void start() throws IOException, InterruptedException {
        System.out.println("1 - запуск клиента и запись на сервер с помощью метода check()");
        System.out.println("2 - загрузка из файла SavedTasks.csv на сервер");
        System.out.println("3  - старт HTTPServer");
        int command = scanner.nextInt();
        switch (command) {
            case 1:
                check();
                httpTaskManager.loadFromServer();
                break;
            case 2:
                httpTaskManager = HTTPTaskManager.loadInServerFromSavedTasks();
                printAllTask();
                System.out.println(" ");
                printHistory();
                printPriority();
                System.out.println("ID:" + httpTaskManager.getIndetifierNumber());
                httpTaskManager.loadFromServer();
                System.out.println(" \n\n\n");
                check();
                httpTaskManager.loadFromServer();
                break;
            case 3:
                new HTTPTaskServer().start();
                break;

            default:
                System.out.println("не верный выбор");
        }
    }

    private void check() throws IOException, InterruptedException {
        create();
        printAllTask();
        System.out.println(" ");
        printHistory();
        System.out.println(" ");
        printPriority();
        System.out.println(" ");
        System.out.println("ID:" + httpTaskManager.getIndetifierNumber());
    }

    private void create() throws IOException, InterruptedException {
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
        for (Task task : Arrays.asList(task1, task5, task2, task6, task3, task4, task8, task7, task9)) {
            httpTaskManager.createTask(task);
        }
        SubTask subTask1 = new SubTask("sub1", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 40), epic1.getId());
        SubTask subTask5 = new SubTask("sub5", "desc", epic1.getId());
        SubTask subTask2 = new SubTask("sub2", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 45), epic1.getId());
        SubTask subTask3 = new SubTask("sub3", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 50), epic2.getId());
        SubTask subTask4 = new SubTask("sub4", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 55), epic1.getId());
        SubTask subTask6 = new SubTask("sub6", "desc", epic2.getId());
        for (SubTask subTask : Arrays.asList(subTask1, subTask5, subTask2, subTask3, subTask4, subTask6)) {
            httpTaskManager.createSubTask(subTask);
        }
        httpTaskManager.getEpicTask(epic1.getId());
        httpTaskManager.getEpicTask(epic2.getId());
        for (Task task : Arrays.asList(task1, task2, task3, task4, task5, task6)) {
            httpTaskManager.getTask(task.getId());
        }
        for (SubTask subTask : Arrays.asList(subTask1, subTask5, subTask2, subTask3, subTask4, subTask5)) {
            httpTaskManager.getSubTask(subTask.getId());
        }
    }


    public void printHistory() throws IOException, InterruptedException {//печать истории просмотра
        List<Task> history = httpTaskManager.getHistoryList();
        if (history.isEmpty()) {
            System.out.println("История просмотров пуста");
        } else {
            System.out.println("История просмотров задач:");
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ": " + history.get(i));
            }
        }
    }

    public void printAllTask() {//печать всех задач
        System.out.println("Задачи: ");
        for (Task task : httpTaskManager.getListTask()) {
            System.out.println(task);
        }
        System.out.println(System.lineSeparator() + "Сложные Задачи: ");
        for (EpicTask epic : httpTaskManager.getListEpicTask()) {
            System.out.println(epic + System.lineSeparator() + "подзадачи(" + epic.getNameTask() + "):");
            for (SubTask subTask : httpTaskManager.getListSubTaskFromEpic(epic.getId())) {
                System.out.println(subTask);
            }
            System.out.println(" ");
        }
    }

    public void printPriority() {//печать истории просмотра
        int number = 0;
        System.out.println("приоритет задач:");
        for (Task task : httpTaskManager.getPrioritizedTasks()) {
            number++;
            System.out.println(number + ": " + task);
        }
    }
}

