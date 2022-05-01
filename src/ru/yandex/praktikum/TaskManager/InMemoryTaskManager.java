package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.Interface.HistoryManager;
import ru.yandex.praktikum.Interface.Managers;
import ru.yandex.praktikum.Interface.TaskManager;
import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
import ru.yandex.praktikum.Task.SubTask;

import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();// получение экземпляра InMemoryHistoryManager
    private long identifierNumber = 0;
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, EpicTask> epics = new HashMap<>();
    private final HashMap<Long, SubTask> subTasks = new HashMap<>();

    @Override
    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Long, EpicTask> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Long, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public List<Task> getHistoryList() throws ManagerSaveException { // переписанный метод возврата списка последних 10 просмотренных задач
        List<Task> historyList;
        historyList = historyManager.getHistory();
        return historyList;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void removeTaskInHistory(long id) throws ManagerSaveException {
        deleteTasksOnId(id);
    }

    public void setIndetifierNumber(long identifierNumber) {
        this.identifierNumber = identifierNumber;
    }

    public long getIndetifierNumber() {
        return identifierNumber;
    }

    @Override
    public long generateID() {
        identifierNumber++;
        return identifierNumber;
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        task.setId(generateID());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws ManagerSaveException {
        epicTask.setId(generateID());
        epics.put(epicTask.getId(), epicTask);
    }

    @Override
    public void createSubTask(SubTask subTask) throws ManagerSaveException {
        subTask.setId(generateID());
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        epicTask.setIdSubTasks(subTask.getId());
    }

    @Override
    public void clearAllTask() throws ManagerSaveException {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTask(long id) throws ManagerSaveException {
        Task task = new Task();
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
            historyManager.add(task); //добавление задачи в список истории просмотров
        } else {
            System.out.println("задача с id:"+id+" отсутствует");
        }
        return task;
    }

    @Override
    public EpicTask getEpicTask(long id) throws ManagerSaveException {
        EpicTask epic = new EpicTask();
        if (epics.containsKey(id)) {
            epic = epics.get(id);
            historyManager.add(epic); //добавление задачи в список истории просмотров
        } else {
            System.out.println("эпика с id:"+id+" отсутствует");
        }
        return epic;
    }

    @Override
    public SubTask getSubTask(long id) throws ManagerSaveException {
        SubTask subTask = new SubTask();
        if (subTasks.containsKey(id)) {
            subTask = subTasks.get(id);
            historyManager.add(subTask); //добавление задачи в список истории просмотров
        } else {
            System.out.println("подзадача с id:"+id+" отсутствует");
        }
        return subTask;
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        updateEpicTask(epicTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerSaveException {
        ArrayList<String> statusDone = new ArrayList<>();
        ArrayList<String> statusNew = new ArrayList<>();
        for (long idSubTask : epicTask.getIdSubTasks()) {
            SubTask subTask = subTasks.get(idSubTask);
            if (epicTask.getIdSubTasks().isEmpty()) {
                epicTask.setStatus(TaskStatus.NEW);
            } else if (subTask.getStatus().equals(TaskStatus.NEW)) {
                statusNew.add("New");
                if (statusNew.size() == epicTask.getIdSubTasks().size()) {
                    epicTask.setStatus(TaskStatus.NEW);
                }
            } else if (subTask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                epicTask.setStatus(TaskStatus.IN_PROGRESS);
            } else if (subTask.getStatus().equals(TaskStatus.DONE)) {
                statusDone.add("Done");
                if (statusDone.size() == epicTask.getIdSubTasks().size()) {
                    epicTask.setStatus(TaskStatus.DONE);
                } else epicTask.setStatus(TaskStatus.IN_PROGRESS);
            } else epicTask.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public void deleteTasksOnId(long id) throws ManagerSaveException {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            EpicTask epicTask = epics.get(id);
            for (long idSubTask : epicTask.getIdSubTasks()) {
                historyManager.remove(idSubTask);
                subTasks.remove(idSubTask);
            }
            epicTask.getIdSubTasks().clear();
            historyManager.remove(id);
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            EpicTask epicTask = epics.get(subTask.getIdEpicTask());
            epicTask.getIdSubTasks().remove(id);
            subTasks.remove(id);
            updateEpicTask(epicTask);
            historyManager.remove(id);
        } else System.out.println("задача с id:" + id + " для удаления отсутствует");
    }

    @Override
    public ArrayList<SubTask> getListSubTaskFromEpic(long idEpicTask) {
        ArrayList<SubTask> listSubTask = new ArrayList<>();
        EpicTask epic = epics.get(idEpicTask);
        for (long x : epic.getIdSubTasks()) {
            listSubTask.add(subTasks.get(x));
        }
        return listSubTask;
    }

    @Override
    public ArrayList<Task> getListTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<EpicTask> getListEpicTask() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasks() throws ManagerSaveException {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subTasks.values());
        return allTasks;
    }

}