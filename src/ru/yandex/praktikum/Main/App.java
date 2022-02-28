package ru.yandex.praktikum.Main;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
import ru.yandex.praktikum.Task.UnderTask;
import ru.yandex.praktikum.TaskManager.TaskManager;

public class App {
    TaskManager taskManager = new TaskManager();

    public void start() {
        cycleCheckTechTask();
    }

    public void cycleCheckTechTask() {

        // Создаем первую задачи:
        Task task1 = new Task();
        task1.setNameTask("Придумать сложную задачу");
        task1.setDescription("описание для первой задачи");
        taskManager.createTask(task1);

        // Создаем вторую задачи:
        Task task2 = new Task();
        task2.setNameTask("Выполнить сложную задачу");
        task2.setDescription("описание для второй задачи");
        taskManager.createTask(task2);

        // Создаем первый эпик и две подзадачи:
        EpicTask epic1 = new EpicTask();
        epic1.setNameTask("Купить слона");
        taskManager.createEpicTask(epic1);
        int idEpic1 = epic1.getId();
        UnderTask underTask1 = new UnderTask();
        underTask1.setIdEpicTask(idEpic1);
        underTask1.setNameTask("Заработать на покупку слона");
        UnderTask underTask2 = new UnderTask();
        underTask2.setIdEpicTask(idEpic1);
        underTask2.setNameTask("Найти объявление о продаже слона");
        taskManager.createUnderTask(underTask1);
        taskManager.createUnderTask(underTask2);

        // Создаем второй эпик и одну подзадачу:
        EpicTask epic2 = new EpicTask();
        epic2.setNameTask("Продать слона");
        taskManager.createEpicTask(epic2);
        int idEpic2 = epic2.getId();
        UnderTask underTask3 = new UnderTask();
        underTask3.setIdEpicTask(idEpic2);
        underTask3.setNameTask("Создать объявление о продаже слона");
        taskManager.createUnderTask(underTask3);

        // печатаем задачи:
        taskManager.printAllTask();

        // меняем статусы задачи1:
        System.out.println("Меняем статус задачи '" + task1.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        // меняем статусы задачи2:
        System.out.println("Меняем статус задачи '" + task2.getNameTask() + "'на: " + TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task2);

        // меняем статусы подзадачи1(epic1):
        System.out.println("Меняем статус подзадачи '" + underTask1.getNameTask() + "' на: " + TaskStatus.IN_PROGRESS);
        underTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateUnderTask(underTask1);

        // меняем статусы подзадачи2(epic1):
        System.out.println("Меняем статус подзадачи '" + underTask2.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        underTask2.setStatus(TaskStatus.DONE);
        taskManager.updateUnderTask(underTask2);

        // меняем статусы подзадачи3(epic2):
        System.out.println("Меняем статус подзадачи '" + underTask3.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        underTask3.setStatus(TaskStatus.DONE);
        taskManager.updateUnderTask(underTask3);
        // печатаем задачи:
        taskManager.printAllTask();
    }
}