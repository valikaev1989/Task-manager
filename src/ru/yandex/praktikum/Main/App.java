package ru.yandex.praktikum.Main;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.SubTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
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

        // Создаем первый эпик и три подзадачи:
        EpicTask epic1 = new EpicTask();
        epic1.setNameTask("Купить слона");
        taskManager.createEpicTask(epic1);
        long idEpic1 = epic1.getId();
        SubTask subTask1 = new SubTask();
        subTask1.setIdEpicTask(idEpic1);
        subTask1.setNameTask("Заработать на покупку слона");
        SubTask subTask2 = new SubTask();
        subTask2.setIdEpicTask(idEpic1);
        subTask2.setNameTask("Найти объявление о продаже слона");
        SubTask subTask3 = new SubTask();
        subTask3.setIdEpicTask(idEpic1);
        subTask3.setNameTask("Приехать за слоном");
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        // Создаем второй эпик и одну подзадачу:
        EpicTask epic2 = new EpicTask();
        epic2.setNameTask("Продать слона");
        taskManager.createEpicTask(epic2);
        long idEpic2 = epic2.getId();
        SubTask subTask4 = new SubTask();
        subTask4.setIdEpicTask(idEpic2);
        subTask4.setNameTask("Создать объявление о продаже слона");
        taskManager.createSubTask(subTask4);

        // печатаем задачи:
        printAllTask();

        // меняем статусы задачи1:
        System.out.println("Меняем статус задачи '" + task1.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        // меняем статусы задачи2:
        System.out.println("Меняем статус задачи '" + task2.getNameTask() + "'на: " + TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task2);

        // меняем статусы подзадачи1(epic1):
        System.out.println("Меняем статус подзадачи '" + subTask1.getNameTask() + "' на: " + TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);

        // меняем статусы подзадачи2(epic1):
        System.out.println("Меняем статус подзадачи '" + subTask2.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        subTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask2);

        // меняем статусы подзадачи3(epic2):
        System.out.println("Меняем статус подзадачи '" + subTask4.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        subTask4.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask4);
        // печатаем задачи:
        printAllTask();

        //удаляем задачу "Заработать на слона со статусом"
        taskManager.deleteTasksOnId(subTask1.getId());
        System.out.println(System.lineSeparator() + "удаляем подзадачу: 'Заработать на слона' со статусом IN_PROGRESS в эпике 'Купить слона'");
        System.out.println("Сложная Задача: ");
        System.out.println(epic1 + System.lineSeparator() + "подзадачи(" + epic1.getNameTask() + "):");
        for (long idSubTask : epic1.getIdSubTasks()) {
            System.out.println(taskManager.getSubTaskFromId(idSubTask));
        }
    }

    public void printAllTask() {
        System.out.println("Задачи: ");
        for (Task task : taskManager.getListTask()) {
            System.out.println(task);
        }
        System.out.println("Сложные Задачи: ");
        for (EpicTask epic : taskManager.getListEpicTask()) {
            System.out.println(epic + System.lineSeparator() + "подзадачи(" + epic.getNameTask() + "):");
            for (long idSubTask : epic.getIdSubTasks()) {
                System.out.println(taskManager.getSubTaskFromId(idSubTask));
            }
        }
    }
}