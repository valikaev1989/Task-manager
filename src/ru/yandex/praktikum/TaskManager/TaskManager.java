package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.SubTask;
import ru.yandex.praktikum.Task.Task;

import java.util.ArrayList;

public interface TaskManager extends Managers{
    long generateID();

    void createTask(Task task);

    void createEpicTask(EpicTask epicTask);

    void createSubTask(SubTask subTask);

    void clearAllTask();

    Task getTaskFromId(long id);

    EpicTask getEpicTaskFromId(long id);

    SubTask getSubTaskFromId(long id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpicTask(EpicTask epicTask);

    void deleteTasksOnId(long id);

    ArrayList<SubTask> getListSubTaskFromEpic(long idEpicTask);

    ArrayList<Task> getListTask();

    ArrayList<EpicTask> getListEpicTask();
}
