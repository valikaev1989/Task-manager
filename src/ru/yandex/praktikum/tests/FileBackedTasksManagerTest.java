package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.task.TaskStatus;
import ru.yandex.praktikum.taskmanager.FileBackedTasksManager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class FileBackedTasksManagerTest extends TaskManagerTest {
    public FileBackedTasksManagerTest() throws ManagerSaveException {
        super(new FileBackedTasksManager(new File("SavedTasks.csv")));
    }
    private final File file = new File("TestSaveTasks.csv");
    private final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
    private final FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(file);
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




    @Test
    void checkSaveEmptyFileName()  {
        final Task task10 = new Task("Test Task1", "Test description1");
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(new File(""));
        ManagerSaveException ex = assertThrows(ManagerSaveException.class, () -> fileBackedTasksManager1.createTask(task10));
        assertEquals("Произошла ошибка во время записи в файл.", ex.getMessage());
    }

    @Test
    void checkAddInFile() throws IOException, InterruptedException {
        assertTrue(fileBackedTasksManager.getHistoryList().isEmpty(), "Список не пустой");
        assertTrue(fileBackedTasksManager.getAllTasks().isEmpty(), "Список не пустой");
        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);
        fileBackedTasksManager.createTask(task3);

        fileBackedTasksManager.createEpicTask(epicTask1);
        fileBackedTasksManager.createEpicTask(epicTask2);
        fileBackedTasksManager.createEpicTask(epicTask3);

        subTask1.setIdEpicTask(epicTask1.getId());
        subTask2.setIdEpicTask(epicTask1.getId());
        subTask3.setIdEpicTask(epicTask1.getId());
        subTask4.setIdEpicTask(epicTask1.getId());
        subTask5.setIdEpicTask(epicTask2.getId());
        subTask6.setIdEpicTask(epicTask2.getId());
        subTask7.setIdEpicTask(epicTask3.getId());

        fileBackedTasksManager.createSubTask(subTask1);
        fileBackedTasksManager.createSubTask(subTask2);
        fileBackedTasksManager.createSubTask(subTask3);
        fileBackedTasksManager.createSubTask(subTask4);
        fileBackedTasksManager.createSubTask(subTask5);
        fileBackedTasksManager.createSubTask(subTask6);
        fileBackedTasksManager.createSubTask(subTask7);

        fileBackedTasksManager.getTask(task1.getId());
        fileBackedTasksManager.getTask(task2.getId());
        fileBackedTasksManager.getTask(task3.getId());

        fileBackedTasksManager.getEpicTask(epicTask1.getId());
        fileBackedTasksManager.getEpicTask(epicTask2.getId());
        fileBackedTasksManager.getEpicTask(epicTask3.getId());

        fileBackedTasksManager.getSubTask(subTask1.getId());
        fileBackedTasksManager.getSubTask(subTask2.getId());
        fileBackedTasksManager.getSubTask(subTask3.getId());
        fileBackedTasksManager.getSubTask(subTask4.getId());
        fileBackedTasksManager.getSubTask(subTask5.getId());
        fileBackedTasksManager.getSubTask(subTask6.getId());
        fileBackedTasksManager.getSubTask(subTask7.getId());
        assertEquals(13, fileBackedTasksManager.getAllTasks().size(), "размер AllTasks() отличается"
                + " от запрошенных задач задач");
        assertEquals(13, fileBackedTasksManager.getHistoryList().size(), "размер HistoryList() отличается"
                + " от запрошенных задач задач");
    }

    @Test
    void checkLoadFromFile() throws IOException, InterruptedException {
        assertFalse(fileBackedTasksManager1.getHistoryList().isEmpty(), "Список истории пустой после загрузки из файла");
        assertFalse(fileBackedTasksManager1.getAllTasks().isEmpty(), "Список задач пустой после загрузки из файла");
        fileBackedTasksManager1.createTask(task1);
        fileBackedTasksManager1.createTask(task2);
        fileBackedTasksManager1.createTask(task3);
        fileBackedTasksManager1.getTask(task1.getId());
        fileBackedTasksManager1.getTask(task2.getId());
        fileBackedTasksManager1.getTask(task3.getId());
    }
}