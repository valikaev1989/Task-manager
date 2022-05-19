package ru.yandex.praktikum.api;

import com.google.gson.*;
import ru.yandex.praktikum.allinterface.HistoryManager;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.taskmanager.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager {
    private final Gson gson;
    private static KVClient kvClient;

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super(new File("SavedTasks.csv"));
        kvClient = new KVClient(url);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static HTTPTaskManager loadInServerFromSavedTasks() throws IOException, InterruptedException {
        final HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        httpTaskManager.loadFromServer();
        return httpTaskManager;

    }


    public void save() throws IOException, InterruptedException {
        String q = gson.toJson(getListTask());
        String w = gson.toJson(getListEpicTask());
        String e = gson.toJson(getListSubTask());
        String r = gson.toJson(getHistoryList());
        String a = gson.toJson(getPrioritizedTasks());
        kvClient.put("tasks", q);
        kvClient.put("epicTasks", w);
        kvClient.put("subTasks", e);
        kvClient.put("historyTasks", r);
        kvClient.put("priorityTasks", a);
    }

    public String loadFromKeyKVServer(String key) throws IOException, InterruptedException {
        return kvClient.load(key);
    }

    public void loadFromServer() throws IOException, InterruptedException {
        if (!(kvClient.load("tasks").equals("null"))) {
            ArrayList<Task> tasksList = parseTasksJsonToTasksList(kvClient.load("tasks"));
            for (Task task : tasksList) {
                getTasks().put(task.getId(), task);
            }
        }
        if (!(kvClient.load("epicTasks").equals("null"))) {
            ArrayList<Task> epicsList = parseTasksJsonToTasksList(kvClient.load("epicTasks"));
            for (Task epicTask : epicsList) {
                getEpics().put(epicTask.getId(), (EpicTask) epicTask);
            }
        }
        if (!(kvClient.load("subTasks").equals("null"))) {
            ArrayList<Task> subTaskList = parseTasksJsonToTasksList(kvClient.load("subTasks"));
            for (Task task : subTaskList) {
                getSubTasks().put(task.getId(), (SubTask) task);
            }
        }
        long id = getAllTasks().size();
        setIndetifierNumber(id);
        if (!(kvClient.load("historyTasks").equals("null"))) {
            ArrayList<Task> history = parseTasksJsonToTasksList(kvClient.load("historyTasks"));
            for (Task task : history) {
                getHistoryManager().add(task);
            }

        }
        if (!(kvClient.load("priorityTasks").equals("null"))) {
            ArrayList<Task> priority = parseTasksJsonToTasksList(kvClient.load("priorityTasks"));
            for (Task task : priority) {
                addInDateList(task);
            }
        }
    }

    private ArrayList<Task> parseTasksJsonToTasksList(String json) {
        ArrayList<Task> result = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        for (JsonElement element : jsonArray) {

            String type = element.getAsJsonObject().get("type").getAsString();

            switch (type) {
                case "TASK":
                    result.add(gson.fromJson(element, Task.class));
                    break;
                case "SUBTASK":
                    result.add(gson.fromJson(element, SubTask.class));
                    break;
                case "EPICTASK":
                    result.add(gson.fromJson(element, EpicTask.class));
                    break;
            }
        }
        return result;
    }

    @Override
    public Task getAnyTaskById(Long id) {
        return super.getAnyTaskById(id);
    }

    @Override
    public void createTask(Task task) throws IOException, InterruptedException {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws IOException, InterruptedException {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) throws IOException, InterruptedException {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void clearAllTask() throws IOException, InterruptedException {
        super.clearAllTask();
        save();
    }

    @Override
    public Task getTask(Long id) throws IOException, InterruptedException {
        Task task = super.getTask(id);
        save();
        return task;

    }

    @Override
    public EpicTask getEpicTask(Long id) throws IOException, InterruptedException {
        EpicTask epicTask = super.getEpicTask(id);
        save();
        return epicTask;
    }

    @Override
    public SubTask getSubTask(Long id) throws IOException, InterruptedException {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) throws IOException, InterruptedException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException, InterruptedException {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws IOException, InterruptedException {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void deleteTasksOnId(Long id) throws IOException, InterruptedException {
        super.deleteTasksOnId(id);
        save();
    }

    @Override
    public void deleteTask(Long id) throws IOException, InterruptedException {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpicTask(Long id) throws IOException, InterruptedException {
        super.deleteEpicTask(id);
        save();
    }

    @Override
    public void deleteSubTask(Long id) throws IOException, InterruptedException {
        super.deleteSubTask(id);
        save();
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
    public void removeTaskInHistory(long id) throws IOException, InterruptedException {
        super.removeTaskInHistory(id);
        save();
    }

    @Override
    public void addInDateList(Task task) throws IOException, InterruptedException {
        super.addInDateList(task);
        save();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }
}


