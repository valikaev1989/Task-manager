package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.task.TaskStatus;
import ru.yandex.praktikum.taskmanager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    private final Task task1 = new Task("Test Task1", "Test description1");
    private final Task task2 = new Task("Test Task2", "Test description2", TaskStatus.IN_PROGRESS);
    private final Task task3 = new Task("Test Task3", "Test description3", TaskStatus.DONE);
    private final EpicTask epicTask1 = new EpicTask("Test epicTask1", "Test description4");
    private final EpicTask epicTask2 = new EpicTask("Test epicTask2", "Test description5");
    private final EpicTask epicTask3 = new EpicTask("Test epicTask3", "Test description6");
    private final SubTask subTask1 = new SubTask("sub1", "desc", TaskStatus.NEW, epicTask1.getId());
    private final SubTask subTask2 = new SubTask("sub2", "desc", TaskStatus.IN_PROGRESS, epicTask1.getId());
    private final SubTask subTask3 = new SubTask("sub3", "desc", TaskStatus.DONE, epicTask1.getId());
    private final SubTask subTask4 = new SubTask("sub4", "desc", TaskStatus.NEW, epicTask1.getId());
    private final SubTask subTask5 = new SubTask("sub5", "desc", TaskStatus.DONE, epicTask2.getId());
    private final SubTask subTask6 = new SubTask("sub6", "desc", TaskStatus.NEW, epicTask2.getId());
    private final SubTask subTask7 = new SubTask("sub7", "desc", TaskStatus.DONE, epicTask3.getId());


    @BeforeEach
    void create() throws ManagerSaveException {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        taskManager.createEpicTask(epicTask3);

        subTask1.setIdEpicTask(epicTask1.getId());
        subTask2.setIdEpicTask(epicTask1.getId());
        subTask3.setIdEpicTask(epicTask1.getId());
        subTask4.setIdEpicTask(epicTask1.getId());
        subTask5.setIdEpicTask(epicTask2.getId());
        subTask6.setIdEpicTask(epicTask2.getId());
        subTask7.setIdEpicTask(epicTask3.getId());

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subTask4);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        taskManager.createSubTask(subTask7);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());

        taskManager.getEpicTask(epicTask1.getId());
        taskManager.getEpicTask(epicTask2.getId());
        taskManager.getEpicTask(epicTask3.getId());

        taskManager.getSubTask(subTask1.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getSubTask(subTask3.getId());
        taskManager.getSubTask(subTask4.getId());
        taskManager.getSubTask(subTask5.getId());
        taskManager.getSubTask(subTask6.getId());
        taskManager.getSubTask(subTask7.getId());
    }

    @Test
    void checkEmptyHistoryList() throws ManagerSaveException {
        assertFalse(taskManager.getHistoryList().isEmpty(), "В списке истории нет задач");
        taskManager.clearAllTask();
        assertTrue(taskManager.getHistoryList().isEmpty(), "В списке истории есть задачи");
    }

    @Test
    void checkDublicateHistoryList() throws ManagerSaveException {
        int check = 0;
        assertFalse(taskManager.getHistoryList().isEmpty(), "В списке истории нет задач");
        Task task = taskManager.getHistoryList().get(0);
        taskManager.getTask(task.getId());
        taskManager.getTask(task.getId());
        taskManager.getTask(task.getId());
        for (Task task1 : taskManager.getHistoryList()) {
            if (task1.equals(task)) {
                check++;
            }
        }
        assertEquals(1, check, "больше одного раза");
    }

    @Test
    void checkRemoveFromHistoryList() throws ManagerSaveException {
        int check1 = taskManager.getHistoryList().size();
        taskManager.removeTaskInHistory(taskManager.getHistoryList().get(0).getId());
        int check2 = taskManager.getHistoryList().size();
        assertNotEquals(check1,check2,"Одинаковый размер списка истории после удаления");
    }
}    //Для HistoryManager — тесты для всех методов интерфейса. Граничные условия:
//a. Пустая история задач.
//b. Дублирование.
//с. Удаление из истории: начало, середина, конец.
