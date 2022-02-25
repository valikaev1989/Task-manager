import ru.yandex.praktikum.Task;

import java.util.ArrayList;

public class App {
    Manager manager = new Manager();

    public void start() {
        cycleCheckTechTask();
    }

    private void cycleCheckTechTask() {
        Task task1 = new Task();
        task1.setNameTask("Придумать сложную задачу");
        manager.createTask(task1);

        Task task2 = new Task();
        task2.setNameTask("Выполнить сложную задачу");
        manager.createTask(task2);

        Task.EpicTask epic1 = new Task.EpicTask();
        epic1.setNameEpicTask("Купить слона");
        manager.createEpicTask(epic1);
        int idEpic1 = epic1.getId();
        Task.UnderTask underTask1 = new Task.UnderTask();
        underTask1.setNameUnderTask("Заработать на покупку слона");
        Task.UnderTask underTask2 = new Task.UnderTask();
        underTask2.setNameUnderTask("Найти объявление о продаже слона");
        manager.createUnderTask(underTask1, idEpic1);
        manager.createUnderTask(underTask2, idEpic1);

        Task.EpicTask epic2 = new Task.EpicTask();
        epic2.setNameEpicTask("Продать слона");
        manager.createEpicTask(epic2);
        int idEpic2 = epic2.getId();
        Task.UnderTask underTask3 = new Task.UnderTask();
        underTask3.setNameUnderTask("Создать объявление о продаже слона");
        manager.createUnderTask(underTask3, idEpic2);

        manager.printAllTask();

        int idTask1 = task1.getId();
        System.out.println("Меняем статус задачи '" + task1.getNameTask() + "' на 'Done'."
                + System.lineSeparator() + "    Задача завершена и перемещана в список завершенных задач.");
        manager.updateTask(idTask1, "Done");
        int idTask2 = task2.getId();
        System.out.println("Меняем статус задачи '" + task2.getNameTask() + "' на 'In_progress'.");
        manager.updateTask(idTask2, "In_progress");

        int idUnderTask1 = underTask1.getId();
        System.out.println("Меняем статус подзадачи '" + underTask1.getNameUnderTask() + "' на 'In_progress'.");
        manager.updateUnderTask(idUnderTask1, "In_progress");
        int idUnderTask2 = underTask2.getId();
        System.out.println("Меняем статус подзадачи '" + underTask2.getNameUnderTask() + "' на 'Done'.");
        manager.updateUnderTask(idUnderTask2, "Done");

        int idUnderTask3 = underTask3.getId();
        int idEpicUnderTask3 = underTask3.getIdEpicTask();
        Task.EpicTask epic = manager.getEpics().get(idEpicUnderTask3);
        System.out.println("Меняем статус подзадачи '" + underTask3.getNameUnderTask() + "' на 'Done'."
                + System.lineSeparator() + "    Подзадача завершена и " + "сложная задача: '"
                + epic.getNameEpicTask() + "' перемещана в список завершенных задач.");
        manager.updateUnderTask(idUnderTask3, "Done");

        manager.printAllTask();
        ArrayList<Object> doneTask = manager.getListFinishTasks();
        System.out.println("Завершенные задачи:");
        for (Object object : doneTask) {
            System.out.println(object);
        }
    }
}
