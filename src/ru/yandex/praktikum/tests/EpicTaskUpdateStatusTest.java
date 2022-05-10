package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.TaskStatus;
import ru.yandex.praktikum.taskmanager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskUpdateStatusTest {
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static final EpicTask epic1 = new EpicTask("epic1", "desc");
    private final SubTask subTask1 = new SubTask("sub1", "desc", TaskStatus.NEW, epic1.getId());
    private final SubTask subTask2 = new SubTask("sub2", "desc", TaskStatus.DONE, epic1.getId());
    private final SubTask subTask3 = new SubTask("sub3", "desc", TaskStatus.IN_PROGRESS, epic1.getId());
    private final SubTask subTask4 = new SubTask("sub4", "desc", TaskStatus.NEW, epic1.getId());

    @BeforeAll
    static void shouldCheckEmptyListIdSubTask() throws ManagerSaveException {
        inMemoryTaskManager.createEpicTask(epic1);
        assertEquals(TaskStatus.NEW, epic1.getStatus());
    }

    @Test
    public void shouldCheckNewStatusEpicTask() throws ManagerSaveException {
        inMemoryTaskManager.createSubTask(subTask1);
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask1.getId());
    }

    @Test
    public void shouldCheckInProgressStatusEpicTask() throws ManagerSaveException {
        inMemoryTaskManager.createSubTask(subTask3);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask3.getId());
    }

    @Test
    public void shouldCheckDoneStatusEpicTask() throws ManagerSaveException {
        inMemoryTaskManager.createSubTask(subTask2);
        assertEquals(TaskStatus.DONE, epic1.getStatus());
        inMemoryTaskManager.createSubTask(subTask1);
        subTask1.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateSubTask(subTask1);
        assertEquals(TaskStatus.DONE, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask2.getId());
        inMemoryTaskManager.deleteTasksOnId(subTask1.getId());
    }

    @Test
    public void shouldCheckDoneStatusEpicTask2() throws ManagerSaveException {
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        inMemoryTaskManager.createSubTask(subTask4);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask1.getId());
        inMemoryTaskManager.deleteTasksOnId(subTask3.getId());
        inMemoryTaskManager.deleteTasksOnId(subTask4.getId());
        assertEquals(TaskStatus.DONE, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask2.getId());
    }
    @Test
    public void shouldCheckInProgressStatusEpicTask2() throws ManagerSaveException {
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        inMemoryTaskManager.createSubTask(subTask4);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask3.getId());
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask1.getId());
        inMemoryTaskManager.deleteTasksOnId(subTask2.getId());
        inMemoryTaskManager.deleteTasksOnId(subTask4.getId());
    }
    @Test
    public void shouldCheckNewStatusEpicTask2() throws ManagerSaveException {
        inMemoryTaskManager.createSubTask(subTask1);
        inMemoryTaskManager.createSubTask(subTask2);
        inMemoryTaskManager.createSubTask(subTask3);
        inMemoryTaskManager.createSubTask(subTask4);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask3.getId());
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask2.getId());
        assertEquals(TaskStatus.NEW, epic1.getStatus());
        inMemoryTaskManager.deleteTasksOnId(subTask1.getId());
        inMemoryTaskManager.deleteTasksOnId(subTask4.getId());
    }
}