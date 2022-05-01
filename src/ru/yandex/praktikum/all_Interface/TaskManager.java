package ru.yandex.praktikum.all_Interface;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager extends Managers {
    long generateID();

    void createTask(Task task) throws ManagerSaveException;

    void createEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void createSubTask(SubTask subTask) throws ManagerSaveException;

    void clearAllTask() throws ManagerSaveException;

    Task getTask(long id) throws ManagerSaveException;

    EpicTask getEpicTask(long id) throws ManagerSaveException;

    SubTask getSubTask(long id) throws ManagerSaveException;

    void updateTask(Task task) throws ManagerSaveException;

    void updateSubTask(SubTask subTask) throws ManagerSaveException;

    void updateEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void deleteTasksOnId(long id) throws ManagerSaveException;

    ArrayList<SubTask> getListSubTaskFromEpic(long idEpicTask);

    ArrayList<Task> getListTask();

    ArrayList<EpicTask> getListEpicTask();

    List<Task> getHistoryList() throws ManagerSaveException; // метод возврата списка последних просмотренных заданий

    public HashMap<Long, Task> getTasks();

    public HashMap<Long, EpicTask> getEpics();

    public HashMap<Long, SubTask> getSubTasks();

    ArrayList<Task> getAllTasks() throws ManagerSaveException;
}