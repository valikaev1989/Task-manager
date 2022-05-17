package ru.yandex.praktikum.allinterface;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager extends Managers {

    void createTask(Task task) throws IOException, InterruptedException;

    void createEpicTask(EpicTask epicTask) throws IOException, InterruptedException;

    void createSubTask(SubTask subTask) throws IOException, InterruptedException;

    void clearAllTask() throws IOException, InterruptedException;

    Task getTask(Long id) throws IOException, InterruptedException;

    EpicTask getEpicTask(Long id) throws IOException, InterruptedException;

    SubTask getSubTask(Long id) throws IOException, InterruptedException;

    void updateTask(Task task) throws IOException, InterruptedException;

    void updateSubTask(SubTask subTask) throws IOException, InterruptedException;

    void updateEpicTask(EpicTask epicTask) throws IOException, InterruptedException;

    void deleteTasksOnId(Long id) throws IOException, InterruptedException;

    void deleteTask(Long id) throws IOException, InterruptedException;

    void deleteEpicTask(Long id) throws IOException, InterruptedException;

    void deleteSubTask(Long id) throws IOException, InterruptedException;

    ArrayList<SubTask> getListSubTaskFromEpic(Long idEpicTask);
    ArrayList<SubTask> getListSubTask();

    ArrayList<Task> getListTask();

    ArrayList<EpicTask> getListEpicTask();

    List<Task> getHistoryList() throws IOException, InterruptedException; // метод возврата списка последних просмотренных заданий

    HashMap<Long, Task> getTasks();

    HashMap<Long, EpicTask> getEpics();

    HashMap<Long, SubTask> getSubTasks();

    ArrayList<Task> getAllTasks() throws ManagerSaveException;

    HistoryManager getHistoryManager();

    void removeTaskInHistory(long id);

    void addInDateList(Task task);

    TreeSet<Task> getPrioritizedTasks();

}