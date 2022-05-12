package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.task.TaskStatus;


import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
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

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @AfterEach
    void clearCollections() throws ManagerSaveException {
        taskManager.clearAllTask();
    }

    @Test
    void checkAddTask() throws ManagerSaveException {
        taskManager.createTask(task1);
        assertEquals(task1, taskManager.getTasks().get(task1.getId()));
        assertNotNull(taskManager.getTasks(), "Задачи на возвращаются.");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(epicTask1));
        assertEquals("задача не соответствует типу Task", ex.getMessage());

        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> taskManager.createTask(null));
        assertEquals("переданный task = null", ex1.getMessage());

    }

    @Test
    void checkAddEpicTask() throws ManagerSaveException {
        taskManager.createEpicTask(epicTask1);
        assertEquals(epicTask1, taskManager.getEpics().get(epicTask1.getId()));

        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> taskManager.createEpicTask(null));
        assertEquals("переданный epicTask = null", ex1.getMessage());
        assertNotNull(taskManager.getEpics(), "Задачи на возвращаются.");
    }

    @Test
    void checkAddSubTask() throws ManagerSaveException {
        taskManager.createEpicTask(epicTask1);
        subTask1.setIdEpicTask(epicTask1.getId());
        taskManager.createSubTask(subTask1);
        assertEquals(subTask1, taskManager.getSubTasks().get(subTask1.getId()));

        assertNotNull(taskManager.getSubTasks(), "Задачи на возвращаются.");

        subTask2.setIdEpicTask(123L);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> taskManager.createSubTask(subTask2));
        assertEquals("id EpicTask записанный в subTask отсутствует в хранилище", ex.getMessage());

        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> taskManager.createSubTask(null));
        assertEquals("переданный subTask = null", ex1.getMessage());
    }

    @Test
    public void checkGetTask() throws ManagerSaveException {
        taskManager.createTask(task1);
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> taskManager.getTask(null));
        assertEquals("запрошенный idTask = null", ex1.getMessage());

        Task newTask = taskManager.getTask(task1.getId());
        assertNotNull(newTask, "Задача не найдена.");
        assertEquals(task1, newTask, "Задачи не совпадают.");

        Long getId1 = (long) 10;
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> taskManager.getTask(getId1));
        assertEquals("задача с idTask:" + getId1 + " отсутствует", ex2.getMessage());

        Long getId = (long) -1;
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> taskManager.getTask(getId));
        assertEquals("idTask с отрицательным значением", ex3.getMessage());
    }

    @Test
    public void checkGetEpicTask() throws ManagerSaveException {
        taskManager.createEpicTask(epicTask1);
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> taskManager.getEpicTask(null));
        assertEquals("запрошенный idEpicTask = null", ex1.getMessage());

        EpicTask newTask = taskManager.getEpicTask(epicTask1.getId());
        assertNotNull(newTask, "Задача не найдена.");
        assertEquals(epicTask1, newTask, "Задачи не совпадают.");

        Long getId1 = (long) 10;
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> taskManager.getEpicTask(getId1));
        assertEquals("задача с idEpicTask:" + getId1 + " отсутствует", ex2.getMessage());

        Long getId = (long) -1;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> taskManager.getEpicTask(getId));
        assertEquals("idEpicTask с отрицательным значением", ex.getMessage());
    }

    @Test
    public void checkGetSubTask() throws ManagerSaveException {
        taskManager.createEpicTask(epicTask1);
        subTask1.setIdEpicTask(epicTask1.getId());
        taskManager.createSubTask(subTask1);
        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> taskManager.getSubTask(null));
        assertEquals("запрошенный idSubTask = null", ex1.getMessage());

        SubTask newTask = taskManager.getSubTask(subTask1.getId());
        assertNotNull(newTask, "Задача не найдена.");
        assertEquals(subTask1, newTask, "Задачи не совпадают.");

        Long getId1 = (long) 10;
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> taskManager.getSubTask(getId1));
        assertEquals("задача с idSubTask:" + getId1 + " отсутствует", ex2.getMessage());

        Long getId = (long) -1;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> taskManager.getSubTask(getId));
        assertEquals("idSubTask с отрицательным значением", ex.getMessage());
    }

    @Test
    void checkUpdateTask() throws ManagerSaveException {
        Task task = new Task();
        NullPointerException ex = assertThrows(NullPointerException.class, () -> taskManager.updateTask(task));
        assertEquals("task = null", ex.getMessage());

        taskManager.createTask(task1);
        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);
        assertEquals(task1, taskManager.getTasks().get(task1.getId()));
    }

    @Test
    void checkUpdateSubTask() throws ManagerSaveException {
        SubTask subTask = new SubTask();
        NullPointerException ex = assertThrows(NullPointerException.class, () -> taskManager.updateSubTask(subTask));
        assertEquals("subTask = null", ex.getMessage());

        NullPointerException ex1 = assertThrows(NullPointerException.class, () -> taskManager.createSubTask(subTask1));
        assertEquals("idEpicTask = null", ex1.getMessage());

        taskManager.createEpicTask(epicTask1);
        subTask1.setIdEpicTask(1L);
        taskManager.createSubTask(subTask1);
        subTask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        assertEquals(subTask1, taskManager.getSubTasks().get(subTask1.getId()));
        assertNotEquals(TaskStatus.NEW, epicTask1.getStatus());
    }

    @Test
    void checkUpdateEpicTask() {
        EpicTask epicTask = new EpicTask();
        NullPointerException ex = assertThrows(NullPointerException.class, () -> taskManager.updateEpicTask(epicTask));
        assertEquals("epicTask = null", ex.getMessage());
    }

    @Test
    void checkDeleteTasksOnId() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> taskManager.deleteTasksOnId(null));
        assertEquals("id = null", ex.getMessage());

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> taskManager.deleteTasksOnId(123L));
        assertEquals("задача с id:" + 123L + " для удаления отсутствует", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> taskManager.deleteTasksOnId(-1L));
        assertEquals("id с отрицательным значением", ex2.getMessage());
    }

    @Test
    void checkGetListSubTaskFromEpic() throws ManagerSaveException {
        taskManager.createEpicTask(epicTask1);
        subTask1.setIdEpicTask(epicTask1.getId());
        subTask2.setIdEpicTask(epicTask1.getId());
        subTask3.setIdEpicTask(epicTask1.getId());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        assertEquals(3, taskManager.getListSubTaskFromEpic(epicTask1.getId()).size());

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> taskManager.getListSubTaskFromEpic(-1L));
        assertEquals("эпик с id:" + -1L + " отсутствует", ex1.getMessage());

        NullPointerException ex = assertThrows(NullPointerException.class, () -> taskManager.getListSubTaskFromEpic(null));
        assertEquals("idEpicTask = null", ex.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> taskManager.getListSubTaskFromEpic(123L));
        assertEquals("эпик с id:" + 123L + " отсутствует", ex3.getMessage());
    }

    @Test
    void checkGetListTask() throws ManagerSaveException {
        assertTrue(taskManager.getListTask().isEmpty(), "Список не пустой");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        assertEquals(3, taskManager.getListTask().size(), "размер ListTask отличается от созданных задач");
    }

    @Test
    void checkGetListEpicTask() throws ManagerSaveException {
        assertTrue(taskManager.getListEpicTask().isEmpty(), "Список не пустой");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        taskManager.createEpicTask(epicTask3);
        assertEquals(3, taskManager.getListEpicTask().size(), "размер ListTask отличается от созданных задач");
    }

    @Test
    void checkGetHistoryList() throws ManagerSaveException {
        assertTrue(taskManager.getHistoryList().isEmpty(), "Список не пустой");
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

        assertEquals(13, taskManager.getAllTasks().size(), "размер AllTasks() отличается"
                + " от запрошенных задач задач");
        assertEquals(13, taskManager.getHistoryList().size(), "размер HistoryList() отличается"
                + " от запрошенных задач задач");
    }

    @Test
    void checkGetTasks() throws ManagerSaveException {
        assertTrue(taskManager.getTasks().isEmpty(), "tasks не пустой");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        assertEquals(3, taskManager.getTasks().size(), "размер tasks отличается от созданных задач");
    }

    @Test
    void checkGetEpics() throws ManagerSaveException {
        assertTrue(taskManager.getEpics().isEmpty(), "epics не пустой");
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        taskManager.createEpicTask(epicTask3);
        assertEquals(3, taskManager.getEpics().size(), "размер epics отличается от созданных задач");

    }

    @Test
    void checkGetSubTasks() throws ManagerSaveException {
        assertTrue(taskManager.getSubTasks().isEmpty(), "subTasks не пустой");
        taskManager.createEpicTask(epicTask1);
        subTask1.setIdEpicTask(epicTask1.getId());
        subTask2.setIdEpicTask(epicTask1.getId());
        subTask3.setIdEpicTask(epicTask1.getId());
        subTask4.setIdEpicTask(epicTask1.getId());
        subTask5.setIdEpicTask(epicTask1.getId());
        subTask6.setIdEpicTask(epicTask1.getId());
        subTask7.setIdEpicTask(epicTask1.getId());

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subTask4);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        taskManager.createSubTask(subTask7);
        assertEquals(7, taskManager.getSubTasks().size(), "размер subTasks отличается от созданных задач");
    }

    @Test
    void checkGetAllTasks() throws ManagerSaveException {
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список не пустой");
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
        assertEquals(13, taskManager.getAllTasks().size(), "размер AllTasks() отличается от созданных задач");
    }
}
