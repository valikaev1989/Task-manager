package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
import ru.yandex.praktikum.Task.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager, Managers {
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private long indetifierNumber = 0;
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, EpicTask> epics = new HashMap<>();
    private HashMap<Long, SubTask> subTasks = new HashMap<>();

    public HistoryManager getHistoryManager() { //метод возвращения списка истории просмотров, строка 180.
        return historyManager;
    }

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    public HashMap<Long, EpicTask> getEpics() {
        return epics;
    }

    public HashMap<Long, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public long generateID() {
        indetifierNumber++;
        return indetifierNumber;
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateID());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        epicTask.setId(generateID());
        epics.put(epicTask.getId(), epicTask);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(generateID());
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        epicTask.setIdSubTasks(subTask.getId());
    }

    @Override
    public void clearAllTask() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTask(long id) {
        Task task = new Task();
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
            historyManager.add(task); //добавление задачи в список истории просмотров
        } else {
            System.out.println("Нет такой задачи");
        }
        return task;
    }

    @Override
    public EpicTask getEpicTask(long id) {
        EpicTask epic = new EpicTask();
        if (epics.containsKey(id)) {
            epic = epics.get(id);
            historyManager.add(epic); //добавление задачи в список истории просмотров
        } else {
            System.out.println("Нет такой задачи");
        }
        return epic;
    }

    @Override
    public SubTask getSubTask(long id) {
        SubTask subTask = new SubTask();
        if (subTasks.containsKey(id)) {
            subTask = subTasks.get(id);
            historyManager.add(subTask); //добавление задачи в список истории просмотров
        } else {
            System.out.println("Нет такой задачи");
        }
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        updateEpicTask(epicTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
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
                }
            } else epicTask.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public void deleteTasksOnId(long id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            EpicTask epicTask = epics.get(id);
            for (long idSubTask : epicTask.getIdSubTasks()) {
                subTasks.remove(idSubTask);
            }
            epicTask.getIdSubTasks().clear();
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            EpicTask epicTask = epics.get(subTask.getIdEpicTask());
            epicTask.getIdSubTasks().remove(id);
            subTasks.remove(id);
            updateEpicTask(epicTask);
        } else System.out.println("Такой задачи нет");
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

    public List<Task> getListHistory() {
        return historyManager.getHistory();
    }
}