package ru.yandex.praktikum.main;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.*;
import ru.yandex.praktikum.taskmanager.FileBackedTasksManager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class App {
    private final File file = new File("SavedTasks.csv");
    private FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
    private final Scanner scanner = new Scanner(System.in);

    public void start() throws ManagerSaveException {
        System.out.println("1 - первый запуск и запись в файл всех задач из метода check()");
        System.out.println("2- загрузка из файла SavedTasks.csv всех задач");
        System.out.println("3 - проверка даты");
        int command = scanner.nextInt();
        if (command == 1) {
            check();
        } else if (command == 2) {
            fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
            printAllTask();
            System.out.println(" ");
            printHistory();
            createNewTasksAfterLoad();// метод создания новых задач после загрузки
            printAllTask();
            System.out.println(" ");
            printHistory();
            System.out.println("ID:" + fileBackedTasksManager.getIndetifierNumber());
        }else if(command==3){
            fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
            printAllTask();
            System.out.println(" ");
            printHistory();
            System.out.println("");
            System.out.println("приоритет задач");
            for (Task task:fileBackedTasksManager.getPrioritizedTasks()){
                System.out.println(task);
            }
            checkDateTimeTask();
            printAllTask();
            System.out.println(" ");
            printHistory();
            System.out.println("");
            System.out.println("приоритет задач");
            for (Task task:fileBackedTasksManager.getPrioritizedTasks()){
                System.out.println(task);
            }
        }
    }
public void checkDateTimeTask() throws ManagerSaveException {
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
    fileBackedTasksManager.createEpicTask(epic1);
    fileBackedTasksManager.createEpicTask(epic2);
    fileBackedTasksManager.createTask(task1);
    fileBackedTasksManager.createTask(task5);
    fileBackedTasksManager.createTask(task2);
    fileBackedTasksManager.createTask(task6);
    fileBackedTasksManager.createTask(task3);
    fileBackedTasksManager.createTask(task4);
    fileBackedTasksManager.createTask(task8);
    fileBackedTasksManager.createTask(task7);
    fileBackedTasksManager.createTask(task9);

    SubTask subTask1 = new SubTask("sub1", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 26), epic1.getId());
    SubTask subTask5 = new SubTask("sub5", "desc", epic1.getId());
    SubTask subTask2 = new SubTask("sub2", "desc", 2, LocalDateTime.of(2022, 5, 11, 10, 40), epic1.getId());
    SubTask subTask3 = new SubTask("sub3", "desc", 3, LocalDateTime.of(2022, 5, 11, 10, 21), epic2.getId());
    SubTask subTask4 = new SubTask("sub4", "desc", 4, LocalDateTime.of(2022, 5, 11, 10, 37), epic1.getId());
    SubTask subTask6 = new SubTask("sub6", "desc", epic2.getId());
    fileBackedTasksManager.createSubTask(subTask1);
    fileBackedTasksManager.createSubTask(subTask5);
    fileBackedTasksManager.createSubTask(subTask2);
    fileBackedTasksManager.createSubTask(subTask3);
    fileBackedTasksManager.createSubTask(subTask4);
    fileBackedTasksManager.createSubTask(subTask6);

    fileBackedTasksManager.getTask(task1.getId());
    fileBackedTasksManager.getTask(task2.getId());
    fileBackedTasksManager.getTask(task3.getId());
    fileBackedTasksManager.getTask(task4.getId());
    fileBackedTasksManager.getTask(task5.getId());
    fileBackedTasksManager.getSubTask(subTask1.getId());
    fileBackedTasksManager.getSubTask(subTask2.getId());
    fileBackedTasksManager.getSubTask(subTask3.getId());
    fileBackedTasksManager.getSubTask(subTask4.getId());
    fileBackedTasksManager.getEpicTask(epic1.getId());
    fileBackedTasksManager.getEpicTask(epic2.getId());
}
    public void createNewTasksAfterLoad() throws ManagerSaveException {
        Task task1 = new Task("Task1", "description");
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.getTask(getIdTask(task1));

        Task task2 = new Task("Task2", "description");
        fileBackedTasksManager.createTask(task2);
        fileBackedTasksManager.getTask(getIdTask(task2));
        EpicTask epicTask1 = new EpicTask("epictask1", "description");
        fileBackedTasksManager.createEpicTask(epicTask1);
        fileBackedTasksManager.getEpicTask(getIdTask(epicTask1));
        EpicTask epicTask2 = new EpicTask("epictask2", "description");
        fileBackedTasksManager.createEpicTask(epicTask2);
        fileBackedTasksManager.getEpicTask(getIdTask(epicTask2));
        SubTask subTask1 = new SubTask("Subtask1", "description", epicTask1.getId());
        fileBackedTasksManager.createSubTask(subTask1);
        fileBackedTasksManager.getSubTask(getIdTask(subTask1));
        SubTask subTask2 = new SubTask("Subtask2", "description", epicTask1.getId());
        fileBackedTasksManager.createSubTask(subTask2);
        fileBackedTasksManager.getSubTask(getIdTask(subTask2));
        SubTask subTask3 = new SubTask("Subtask3", "description", epicTask2.getId());
        fileBackedTasksManager.createSubTask(subTask3);
        fileBackedTasksManager.getSubTask(getIdTask(subTask3));
        SubTask subTask4 = new SubTask("Subtask4", "description", epicTask2.getId());
        fileBackedTasksManager.createSubTask(subTask4);
        fileBackedTasksManager.getSubTask(getIdTask(subTask4));
    }

    public long getIdTask(Task task) throws ManagerSaveException {//получение id задачи
        long idTask = 0;
        for (Task task1 : fileBackedTasksManager.getAllTasks()) {
            if (task.equals(task1)) {
                task = task1;
                idTask = task.getId();
            }
        }
        return idTask;
    }

    public void check() throws ManagerSaveException {

        // Создаем первую задачу:
        Task task1 = new Task("Придумать сложную задачу", "описание для первой задачи");
        fileBackedTasksManager.createTask(task1);

        // Создаем вторую задачу:
        Task task2 = new Task("описать сложную задачу", "описание для второй задачи");
        fileBackedTasksManager.createTask(task2);

        // Создаем третью задачу:
        Task task3 = new Task("реализовать сложную задачу", "описание для второй задачи");
        fileBackedTasksManager.createTask(task3);

        // Создаем четвертую задачу:
        Task task4 = new Task("Выполнить сложную задачу", "описание для второй задачи");
        fileBackedTasksManager.createTask(task4);

        // Создаем первый эпик и три подзадачи:
        EpicTask epic1 = new EpicTask("Купить слона", "description");
        fileBackedTasksManager.createEpicTask(epic1);
        long idEpic1 = epic1.getId();
        SubTask subTask1 = new SubTask("Заработать на покупку слона", "description", idEpic1);
        SubTask subTask2 = new SubTask("Найти объявление о продаже слона", "description", idEpic1);
        SubTask subTask3 = new SubTask("Приехать за слоном", "description", idEpic1);
        fileBackedTasksManager.createSubTask(subTask1);
        fileBackedTasksManager.createSubTask(subTask2);
        fileBackedTasksManager.createSubTask(subTask3);

        // Создаем второй эпик и три подзадачи:
        EpicTask epic2 = new EpicTask("Продать слона", "description");
        fileBackedTasksManager.createEpicTask(epic2);
        Long idEpic2 = epic2.getId();
        SubTask subTask4 = new SubTask("Создать объявление о продаже слона", "description", idEpic2);
        fileBackedTasksManager.createSubTask(subTask4);
        SubTask subTask5 = new SubTask("Договорится с покупателем о продаже", "description", idEpic2);
        fileBackedTasksManager.createSubTask(subTask5);
        SubTask subTask6 = new SubTask("Передать слона заказчику", "description", idEpic2);
        fileBackedTasksManager.createSubTask(subTask6);

        // печатаем задачи:
        printAllTask();

        // меняем статусы задачи1:
        System.out.println(System.lineSeparator() + "Меняем статус задачи '" + task1.getNameTask() + "' на: "
                + TaskStatus.DONE + ".");
        task1.setStatus(TaskStatus.DONE);
        fileBackedTasksManager.updateTask(task1);

        // меняем статусы задачи2:
        System.out.println("Меняем статус задачи '" + task2.getNameTask() + "'на: " + TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        fileBackedTasksManager.updateTask(task2);

        // меняем статусы подзадачи1(epic1):
        System.out.println("Меняем статус подзадачи1(epic1) '" + subTask1.getNameTask() + "' на: " + TaskStatus.IN_PROGRESS);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        fileBackedTasksManager.updateSubTask(subTask1);

        // меняем статусы подзадачи2(epic1):
        System.out.println("Меняем статус подзадачи2(epic1) '" + subTask2.getNameTask() + "' на: " + TaskStatus.DONE + ".");
        subTask2.setStatus(TaskStatus.DONE);
        fileBackedTasksManager.updateSubTask(subTask2);

        // меняем статусы подзадачи3(epic2):
        System.out.println("Меняем статус подзадачи3(epic2)'" + subTask4.getNameTask() + "' на: " + TaskStatus.DONE + "."
                + System.lineSeparator());
        subTask4.setStatus(TaskStatus.DONE);
        fileBackedTasksManager.updateSubTask(subTask4);

        // печатаем задачи:
        printAllTask();
        System.out.println(" ");

        fileBackedTasksManager.deleteTasksOnId(subTask2.getId());
        System.out.println(System.lineSeparator() + "удаляем подзадачу: 'Найти объявление о продаже слона' со статусом 'DONE' в эпике 'Купить слона'");
        System.out.println("Сложная Задача: ");
        System.out.println(epic1 + System.lineSeparator() + "подзадачи(" + epic1.getNameTask() + "):");
        for (Task task : fileBackedTasksManager.getListSubTaskFromEpic(epic1.getId())) {
            System.out.println(task);
        }

        fileBackedTasksManager.deleteTasksOnId(subTask3.getId());
        System.out.println(System.lineSeparator() + "удаляем подзадачу: 'Приехать за слоном' со статусом 'NEW' в эпике 'Купить слона'");
        System.out.println("Сложная Задача: ");
        System.out.println(epic1 + System.lineSeparator() + "подзадачи(" + epic1.getNameTask() + "):");
        if (epic1.getIdSubTasks().isEmpty()) {
            System.out.println("список пуст");
        } else {
            for (Task task : fileBackedTasksManager.getListSubTaskFromEpic(epic1.getId())) {
                System.out.println(task);
            }
        }
        System.out.println(" " + "\n");
        getTask();// печать истории с учетом удаления двух подзадач выше и удалением в самом методе эпика 5,задачи 3
        System.out.println(" " + "\n");
        printAllTask();//печать оставшихся задач из хранилищ с задачами
    }

    public void printAllTask() {//печать всех задач
        System.out.println("Задачи: ");
        for (Task task : fileBackedTasksManager.getListTask()) {
            System.out.println(task);
        }
        System.out.println(System.lineSeparator() + "Сложные Задачи: ");
        for (EpicTask epic : fileBackedTasksManager.getListEpicTask()) {
            System.out.println(epic + System.lineSeparator() + "подзадачи(" + epic.getNameTask() + "):");
            for (SubTask subTask : fileBackedTasksManager.getListSubTaskFromEpic(epic.getId())) {
                System.out.println(subTask);
            }
            System.out.println(" ");
        }
    }

    public void getTask() throws ManagerSaveException { //метод проверки удаления из истории просмотра дублей
        //и принудительного удаления задач из истории
        fileBackedTasksManager.getTask(1L);
        fileBackedTasksManager.getTask(2L);
        fileBackedTasksManager.getTask(3L);
        fileBackedTasksManager.getTask(4L);
        fileBackedTasksManager.getEpicTask(5L);
        fileBackedTasksManager.getEpicTask(9L);
        fileBackedTasksManager.getSubTask(10L);
        fileBackedTasksManager.getSubTask(11L);
        fileBackedTasksManager.getSubTask(12L);
        fileBackedTasksManager.getTask(1L);
        fileBackedTasksManager.getTask(2L);
        fileBackedTasksManager.getTask(3L);
        fileBackedTasksManager.getTask(4L);
        fileBackedTasksManager.getEpicTask(5L);
        fileBackedTasksManager.getEpicTask(9L);
        fileBackedTasksManager.getSubTask(10L);
        fileBackedTasksManager.getSubTask(6L);
        fileBackedTasksManager.getSubTask(11L);
        fileBackedTasksManager.getSubTask(12L);
        fileBackedTasksManager.removeTaskInHistory(9L);//удаление эпика из истории
        fileBackedTasksManager.removeTaskInHistory(3L);//удаление таск из истории
        printHistory();
    }

    public void printHistory() throws ManagerSaveException {//печать истории просмотра
        List<Task> history = fileBackedTasksManager.getHistoryList();
        if (history.isEmpty()) {
            System.out.println("История просмотров пуста");
        } else {
            System.out.println("История просмотров задач:");
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ": " + history.get(i));
            }
        }
    }
}