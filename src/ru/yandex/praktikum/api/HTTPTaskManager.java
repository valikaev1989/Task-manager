package ru.yandex.praktikum.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.praktikum.allinterface.HistoryManager;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.taskmanager.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager {
    String url;
    Gson gson;
    KVClient kvClient;

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super(new File("SavedTasks.csv"));
        this.url = url;
        kvClient = new KVClient(url);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();
    }


    public static HTTPTaskManager loadInServerFromSavedTasks() throws IOException, InterruptedException {
        final HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        httpTaskManager.readForHTTPTaskManager();
        httpTaskManager.saveOnServer();
        return httpTaskManager;
    }

    public void saveOnServer() throws IOException, InterruptedException {
        String managerTasks;
        managerTasks = "Tasks:" + gson.toJson(getListTask());
        managerTasks = managerTasks + "\n EpicTasks:" + gson.toJson(getListEpicTask());
        managerTasks = managerTasks + "\n SubTasks:" + gson.toJson(getListSubTask());
        managerTasks = managerTasks + "\n History:" + gson.toJson(getHistoryList());
        managerTasks = managerTasks + "\n PriorityTask:" + gson.toJson(getPrioritizedTasks());
        kvClient.put("manager", managerTasks);
    }

    public void loadFromServer() throws IOException, InterruptedException {
        kvClient.load("manager");
        System.out.println(kvClient.load("manager"));
    }

    @Override
    public void createTask(Task task) throws IOException, InterruptedException {
        super.createTask(task);
        saveOnServer();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws IOException, InterruptedException {
        super.createEpicTask(epicTask);
        saveOnServer();
    }

    @Override
    public void createSubTask(SubTask subTask) throws IOException, InterruptedException {
        super.createSubTask(subTask);
        saveOnServer();
    }

    @Override
    public void clearAllTask() throws IOException, InterruptedException {
        super.clearAllTask();
        saveOnServer();
    }

    @Override
    public Task getTask(Long id) throws IOException, InterruptedException {
        Task task = super.getTask(id);
        saveOnServer();
        return task;
    }

    @Override
    public EpicTask getEpicTask(Long id) throws IOException, InterruptedException {
        EpicTask epicTask = super.getEpicTask(id);
        saveOnServer();
        return epicTask;
    }

    @Override
    public SubTask getSubTask(Long id) throws IOException, InterruptedException {
        SubTask subTask = super.getSubTask(id);
        saveOnServer();
        return subTask;
    }

    @Override
    public void updateTask(Task task) throws IOException, InterruptedException {
        super.updateTask(task);
        saveOnServer();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException, InterruptedException {
        super.updateSubTask(subTask);
        saveOnServer();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws IOException, InterruptedException {
        super.updateEpicTask(epicTask);
        saveOnServer();
    }

    @Override
    public void deleteTasksOnId(Long id) throws IOException, InterruptedException {
        super.deleteTasksOnId(id);
        saveOnServer();
    }

    @Override
    public void deleteTask(Long id) throws IOException, InterruptedException {
        super.deleteTask(id);
        saveOnServer();
    }

    @Override
    public void deleteEpicTask(Long id) throws IOException, InterruptedException {
        super.deleteEpicTask(id);
        saveOnServer();
    }

    @Override
    public void deleteSubTask(Long id) throws IOException, InterruptedException {
        super.deleteSubTask(id);
        saveOnServer();
    }

    @Override
    public ArrayList<SubTask> getListSubTaskFromEpic(Long idEpicTask) {
        return super.getListSubTaskFromEpic(idEpicTask);
    }

    @Override
    public ArrayList<SubTask> getListSubTask() {
        return super.getListSubTask();
    }

    @Override
    public ArrayList<Task> getListTask() {
        return super.getListTask();
    }

    @Override
    public ArrayList<EpicTask> getListEpicTask() {
        return super.getListEpicTask();
    }

    @Override
    public List<Task> getHistoryList() throws IOException, InterruptedException {
        return super.getHistoryList();
    }


    @Override
    public HashMap<Long, EpicTask> getEpics() {
        return super.getEpics();
    }

    @Override
    public HashMap<Long, SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public ArrayList<Task> getAllTasks() throws ManagerSaveException {
        return super.getAllTasks();

    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }

    @Override
    public void removeTaskInHistory(long id) {
        super.removeTaskInHistory(id);
    }

    @Override
    public void addInDateList(Task task) {
        super.addInDateList(task);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }
}
