package ru.yandex.praktikum.Interface;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.SubTask;
import ru.yandex.praktikum.Task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager extends Managers {
    long generateID();

    void createTask(Task task);

    void createEpicTask(EpicTask epicTask);

    void createSubTask(SubTask subTask);

    void clearAllTask();

    Task getTask(long id);

    EpicTask getEpicTask(long id);

    SubTask getSubTask(long id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpicTask(EpicTask epicTask);

    void deleteTasksOnId(long id);

    ArrayList<SubTask> getListSubTaskFromEpic(long idEpicTask);

    ArrayList<Task> getListTask();

    ArrayList<EpicTask> getListEpicTask();

    List<Task> getHistoryList(); // метод возврата списка последних просмотренных заданий

    public HashMap<Long, Task> getTasks();
    public HashMap<Long, EpicTask> getEpics();
    public HashMap<Long, SubTask> getSubTasks();

}