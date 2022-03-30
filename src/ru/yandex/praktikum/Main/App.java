package ru.yandex.praktikum.Main;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.SubTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
import ru.yandex.praktikum.TaskManager.InMemoryTaskManager;

import java.util.List;

public class App {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public void start() {
        cycleCheckTechTask(); // печать истории просмотра, строка 137
    }

    public void cycleCheckTechTask() {

        // Создаем первую задачу:
        Task task1 = new Task();
        task1.setNameTask("Придумать сложную задачу");
        task1.setDescription("описание для первой задачи");
        inMemoryTaskManager.createTask(task1);

        // Создаем вторую задачу:
        Task task2 = new Task();
        task2.setNameTask("описать сложную задачу");
        task2.setDescription("описание для второй задачи");
        inMemoryTaskManager.createTask(task2);

        // Создаем третью задачу:
        Task task3 = new Task();
        task3.setNameTask("реализовать сложную задачу");
        task3.setDescription("описание для второй задачи");
        inMemoryTaskManager.createTask(task3);

        // Создаем четвертую задачу:
        Task task4 = new Task();
        task4.setNameTask("Выполнить сложную задачу");
        task4.setDescription("описание для второй задачи");
        inMemoryTaskManager.createTask(task4);

        // Создаем первый эпик и три подзадачи:
        EpicTask epic1 = new EpicTask();
        epic1.setNameTask("Купить слона");
        inMemoryTaskManager.createEpicTask(epic1);
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
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);

        // Создаем второй эпик и одну подзадачу:
        EpicTask epic2 = new EpicTask();
        epic2.setNameTask("Продать слона");
        inMemoryTaskManager.createEpicTask(epic2);
        long idEpic2 = epic2.getId();
        SubTask subTask4 = new SubTask();
        subTask4.setIdEpicTask(idEpic2);
        subTask4.setNameTask("Создать объявление о продаже слона");
        inMemoryTaskManager.createSubTask(subTask4);
        SubTask subTask5 = new SubTask();
        subTask5.setIdEpicTask(idEpic2);
        subTask5.setNameTask("Договорится с покупателем о продаже");
        inMemoryTaskManager.createSubTask(subTask5);
        SubTask subTask6 = new SubTask();
        subTask6.setIdEpicTask(idEpic2);
        subTask6.setNameTask("Передать слона заказчику");
        inMemoryTaskManager.createSubTask(subTask6);

        // печатаем задачи:
        printAllTask();

        // меняем статусы задачи1:
        System.out.println(System.lineSeparator() + "Меняем статус задачи '" + task1.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        task1.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateTask(task1);

        // меняем статусы задачи2:
        System.out.println("Меняем статус задачи '" + task2.getNameTask() + "'на: " + TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task2);

        // меняем статусы подзадачи1(epic1):
        System.out.println("Меняем статус подзадачи '" + subTask1.getNameTask() + "' на: " + TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateSubTask(subTask1);

        // меняем статусы подзадачи2(epic1):
        System.out.println("Меняем статус подзадачи '" + subTask2.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        subTask2.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateSubTask(subTask2);

        // меняем статусы подзадачи3(epic2):
        System.out.println("Меняем статус подзадачи '" + subTask4.getNameTask() + "' на: " + TaskStatus.DONE + "."
                + System.lineSeparator());
        subTask4.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateSubTask(subTask4);
        // печатаем задачи:
        printAllTask();
        System.out.println(" ");
        printHistory(); // печать истории просмотра, строка 137
        System.out.println(" ");
        printHistory2();//удаление эпика №9


        //удаляем задачи из эпика "Купить слона":
        inMemoryTaskManager.deleteTasksOnId(subTask1.getId());
        System.out.println(System.lineSeparator() + "удаляем подзадачу: 'Заработать на слона' со статусом 'IN_PROGRESS' в эпике 'Купить слона'");
        System.out.println("Сложная Задача: ");
        System.out.println(epic1 + System.lineSeparator() + "подзадачи(" + epic1.getNameTask() + "):");
        for (Task task : inMemoryTaskManager.getListSubTaskFromEpic(epic1.getId())) {
            System.out.println(task);
        }

        inMemoryTaskManager.deleteTasksOnId(subTask2.getId());
        System.out.println(System.lineSeparator() + "удаляем подзадачу: 'Найти объявление о продаже слона' со статусом 'DONE' в эпике 'Купить слона'");
        System.out.println("Сложная Задача: ");
        System.out.println(epic1 + System.lineSeparator() + "подзадачи(" + epic1.getNameTask() + "):");
        for (Task task : inMemoryTaskManager.getListSubTaskFromEpic(epic1.getId())) {
            System.out.println(task);
        }

        inMemoryTaskManager.deleteTasksOnId(subTask3.getId());
        System.out.println(System.lineSeparator() + "удаляем подзадачу: 'Приехать за слоном' со статусом 'NEW' в эпике 'Купить слона'");
        System.out.println("Сложная Задача: ");
        System.out.println(epic1 + System.lineSeparator() + "подзадачи(" + epic1.getNameTask() + "):");
        if (epic1.getIdSubTasks().isEmpty()) {
            System.out.println("список пуст");
        } else {
            for (Task task : inMemoryTaskManager.getListSubTaskFromEpic(epic1.getId())) {
                System.out.println(task);
            }
        }
    }

    public void printAllTask() {
        System.out.println("Задачи: ");
        for (Task task : inMemoryTaskManager.getListTask()) {
            System.out.println(task);
        }
        System.out.println(System.lineSeparator() + "Сложные Задачи: ");
        for (EpicTask epic : inMemoryTaskManager.getListEpicTask()) {
            System.out.println(epic + System.lineSeparator() + "подзадачи(" + epic.getNameTask() + "):");
            for (Task task : inMemoryTaskManager.getListSubTaskFromEpic(epic.getId())) {
                System.out.println(task);
            }
        }
    }

    public void printHistory() { //метод проверки дублирования истории просмотра
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getTask(4);
        inMemoryTaskManager.getEpicTask(5);
        inMemoryTaskManager.getEpicTask(9);
        inMemoryTaskManager.getSubTask(6);
        inMemoryTaskManager.getSubTask(7);
        inMemoryTaskManager.getSubTask(8);
        inMemoryTaskManager.getSubTask(10);
        inMemoryTaskManager.getSubTask(11);
        inMemoryTaskManager.getSubTask(12);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getTask(4);
        inMemoryTaskManager.getEpicTask(5);
        inMemoryTaskManager.getEpicTask(9);
        inMemoryTaskManager.getSubTask(10);
        inMemoryTaskManager.getSubTask(11);
        inMemoryTaskManager.getSubTask(12);


        List<Task> history = inMemoryTaskManager.getHistoryList();
        if (history.isEmpty()) {
            System.out.println("История просмотров пуста");
        } else {
            System.out.println("История просмотров задач:");
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ": " + history.get(i));
            }
        }
    }

    public void printHistory2() { //метод проверки удаления истории просмотра
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getTask(4);
        inMemoryTaskManager.getEpicTask(5);
        inMemoryTaskManager.getEpicTask(9);
        inMemoryTaskManager.getSubTask(6);
        inMemoryTaskManager.getSubTask(7);
        inMemoryTaskManager.getSubTask(8);
        inMemoryTaskManager.getSubTask(10);
        inMemoryTaskManager.getSubTask(11);
        inMemoryTaskManager.getSubTask(12);
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getTask(4);
        inMemoryTaskManager.getEpicTask(5);
        inMemoryTaskManager.getEpicTask(9);
        inMemoryTaskManager.getSubTask(10);
        inMemoryTaskManager.getSubTask(11);
        inMemoryTaskManager.getSubTask(12);
        inMemoryTaskManager.removeTaskInHistory(9);//удаление эпика из истории
        inMemoryTaskManager.removeTaskInHistory(3);//удаление таск из истории

        List<Task> history = inMemoryTaskManager.getHistoryList();
        if (history.isEmpty()) {
            System.out.println("История просмотров пуста");
        } else {
            System.out.println("История просмотров задач с удалением эпик с id 9, таск с id 3:");
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ": " + history.get(i));
            }
        }
    }
}