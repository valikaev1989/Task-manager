package ru.yandex.praktikum.allinterface;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager extends Managers {
    long generateID();

    void createTask(Task task) throws ManagerSaveException;

    void createEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void createSubTask(SubTask subTask) throws ManagerSaveException;

    void clearAllTask() throws ManagerSaveException;

    Task getTask(Long id) throws ManagerSaveException;

    EpicTask getEpicTask(Long id) throws ManagerSaveException;

    SubTask getSubTask(Long id) throws ManagerSaveException;

    void updateTask(Task task) throws ManagerSaveException;

    void updateSubTask(SubTask subTask) throws ManagerSaveException;

    void updateEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void deleteTasksOnId(Long id) throws ManagerSaveException;

    void deleteTask(Long id) throws ManagerSaveException;

    void deleteEpicTask(Long id) throws ManagerSaveException;

    void deleteSubTask(Long id) throws ManagerSaveException;

    ArrayList<SubTask> getListSubTaskFromEpic(Long idEpicTask);

    ArrayList<Task> getListTask();

    ArrayList<EpicTask> getListEpicTask();

    List<Task> getHistoryList() throws ManagerSaveException; // метод возврата списка последних просмотренных заданий

    public HashMap<Long, Task> getTasks();

    public HashMap<Long, EpicTask> getEpics();

    public HashMap<Long, SubTask> getSubTasks();

    ArrayList<Task> getAllTasks() throws ManagerSaveException;

    LocalDateTime createStartTimeTask();
}