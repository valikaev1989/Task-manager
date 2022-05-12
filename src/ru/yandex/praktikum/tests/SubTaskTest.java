package ru.yandex.praktikum.tests;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubTaskTest {
    private final EpicTask epicTask = new EpicTask("epicTask1", "desc");
    private final SubTask subTask = new SubTask("subTask1", "desc", epicTask.getId());

    @Test
    public void checkSetIdEpicTask() {
        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> subTask.setIdEpicTask(null));
        assertEquals("переданный idEpicTask = null", ex2.getMessage());

        Long q = (long) -1;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> subTask.setIdEpicTask(q));
        assertEquals("idEpicTask имеет отрицательное значение", ex.getMessage());
    }

    @Test
    public void checkGetIdEpicTask() {
        NullPointerException ex1 = assertThrows(NullPointerException.class, subTask::getIdEpicTask);
        assertEquals("idEpicTask = null", ex1.getMessage());

    }
}
