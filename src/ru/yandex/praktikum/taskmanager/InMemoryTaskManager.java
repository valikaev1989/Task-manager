package ru.yandex.praktikum.taskmanager;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.allinterface.HistoryManager;
import ru.yandex.praktikum.allinterface.Managers;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.task.TaskStatus;
import ru.yandex.praktikum.task.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();// получение экземпляра InMemoryHistoryManager
    private long identifierNumber = 0;
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, EpicTask> epics = new HashMap<>();
    private final HashMap<Long, SubTask> subTasks = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Task::compareTo);

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
    public List<Task> getHistoryList() throws IOException, InterruptedException { // переписанный метод возврата списка последних 10 просмотренных задач
        List<Task> historyList;
        historyList = historyManager.getHistory();
        return historyList;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void removeTaskInHistory(long id) {
        historyManager.remove(id);
    }

    protected void setIndetifierNumber(long identifierNumber) {
        this.identifierNumber = identifierNumber;
    }

    public long getIndetifierNumber() {
        return identifierNumber;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private long generateID() {
        identifierNumber++;
        return identifierNumber;
    }

    private boolean checkAddTime(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }
        Task higher = prioritizedTasks.ceiling(task);
        Task lower = prioritizedTasks.floor(task);
        try {
            if (lower != null && lower.getEndTime().isAfter(task.getStartTime())) {
                return false;
            }
            if (higher != null && task.getEndTime().isAfter(higher.getStartTime())) {
                return false;
            }
        } catch (NullPointerException ignored) {
        }
        return true;
    }

    private void setEpicTime(EpicTask epicTask) {  //----расчёт startTime,endTime,duration
        TreeSet<Task> subtaskInEpic = new TreeSet<>();
        SubTask subTask;
        if (!epicTask.getIdSubTasks().isEmpty()) {
            for (Long id : epicTask.getIdSubTasks()) {
                subTask = subTasks.get(id);
                if (subTask.getStartTime() != null) {
                    subtaskInEpic.add(subTask);
                }
            }
            if (!subtaskInEpic.isEmpty()) {
                Duration duration = Duration.between(subtaskInEpic.first().getStartTime(), subtaskInEpic.last().getEndTime());
                int durationEpicTask = (int) duration.toMinutes();
                epicTask.setStartTime(subtaskInEpic.first().getStartTime());
                epicTask.setDuration(durationEpicTask);
                epicTask.setEpicEndTime(subtaskInEpic.last().getEndTime());
            }
        } else {
            epicTask.setStartTime(null);
            epicTask.setDuration(0);
            epicTask.setEpicEndTime(null);
        }
    }

    @Override
    public void addInDateList(Task task) {
        if (checkAddTime(task)) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createTask(Task task) throws IOException, InterruptedException {
        if (task != null) {
            if (task.getClass().equals(Task.class)) {
                task.setId(generateID());
                addInDateList(task);
                tasks.put(task.getId(), task);
            } else {
                throw new IllegalArgumentException("задача не соответствует типу Task");
            }
        } else {
            throw new NullPointerException("переданный task = null");
        }
    }


    @Override
    public void createEpicTask(EpicTask epicTask) throws IOException, InterruptedException {
        if (epicTask != null) {
            if (epicTask.getClass().equals(EpicTask.class)) {
                epicTask.setId(generateID());
                epics.put(epicTask.getId(), epicTask);
            } else {
                throw new IllegalArgumentException("задача не соответствует типу EpicTask");
            }
        } else {
            throw new NullPointerException("переданный epicTask = null");
        }
    }

    @Override
    public void createSubTask(SubTask subTask) throws IOException, InterruptedException {
        if (subTask != null) {
            if (subTask.getClass().equals(SubTask.class)) {
                if (epics.containsKey(subTask.getIdEpicTask())) {
                    subTask.setId(generateID());
                    addInDateList(subTask);
                    subTasks.put(subTask.getId(), subTask);
                    EpicTask epicTask = epics.get(subTask.getIdEpicTask());
                    epicTask.setIdSubTasks(subTask.getId());
                    setEpicTime(epicTask);
                    updateEpicTask(epicTask);
                } else {
                    throw new IllegalArgumentException("id EpicTask записанный в subTask отсутствует в хранилище");
                }
            } else {
                throw new IllegalArgumentException("задача не соответствует типу SubTask");
            }
        } else {
            throw new NullPointerException("переданный subTask = null");
        }
    }

    @Override
    public void clearAllTask() throws IOException, InterruptedException {

        for (Task task : getListTask()) {
            if (getHistoryList().contains(task)) {
                removeTaskInHistory(task.getId());
            }
        }
        for (Task task : getListEpicTask()) {
            if (getHistoryList().contains(task)) {
                removeTaskInHistory(task.getId());
            }
        }
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTask(Long id) throws IOException, InterruptedException {
        if (id != null) {
            if (id > 0) {
                if (tasks.containsKey(id)) {
                    Task task = tasks.get(id);
                    historyManager.add(task);
                    return task;
                } else {
                    throw new IllegalArgumentException("задача с idTask:" + id + " отсутствует");
                }
            } else {
                throw new IllegalArgumentException("idTask с отрицательным значением");
            }
        } else {
            throw new NullPointerException("запрошенный idTask = null");
        }
    }

    @Override
    public EpicTask getEpicTask(Long id) throws IOException, InterruptedException {
        if (id != null) {
            if (id > 0) {
                if (epics.containsKey(id)) {
                    EpicTask epic = epics.get(id);
                    historyManager.add(epic);
                    return epic;//добавление задачи в список истории просмотров
                } else {
                    throw new IllegalArgumentException("задача с idEpicTask:" + id + " отсутствует");
                }
            } else {
                throw new IllegalArgumentException("idEpicTask с отрицательным значением");
            }
        } else {
            throw new NullPointerException("запрошенный idEpicTask = null");
        }
    }

    @Override
    public SubTask getSubTask(Long id) throws IOException, InterruptedException {
        if (id != null) {
            if (id > 0) {
                if (subTasks.containsKey(id)) {
                    SubTask subTask = subTasks.get(id);
                    historyManager.add(subTask);
                    return subTask;//добавление задачи в список истории просмотров
                } else {
                    throw new IllegalArgumentException("задача с idSubTask:" + id + " отсутствует");
                }
            } else {
                throw new IllegalArgumentException("idSubTask с отрицательным значением");
            }
        } else {
            throw new NullPointerException("запрошенный idSubTask = null");
        }
    }

    @Override
    public void updateTask(Task task) throws IOException, InterruptedException {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            throw new NullPointerException("task = null");
        }

    }

    @Override
    public void updateSubTask(SubTask subTask) throws IOException, InterruptedException {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            EpicTask epicTask = epics.get(subTask.getIdEpicTask());
            updateEpicTask(epicTask);
            setEpicTime(epicTask);
        } else {
            throw new NullPointerException("subTask = null");
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws IOException, InterruptedException {
        if (epics.containsKey(epicTask.getId())) {
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
        } else {
            throw new NullPointerException("epicTask = null");
        }
    }

    @Override
    public void deleteTasksOnId(Long id) throws IOException, InterruptedException {
        if (id != null) {
            if (id > 0) {
                if (tasks.containsKey(id)) {
                    deleteTask(id);
                } else if (epics.containsKey(id)) {
                    deleteEpicTask(id);
                } else if (subTasks.containsKey(id)) {
                    deleteSubTask(id);
                } else {
                    throw new IllegalArgumentException("задача с id:" + id + " для удаления отсутствует");
                }
            } else {
                throw new IllegalArgumentException("id с отрицательным значением");
            }
        } else {
            throw new NullPointerException("id = null");
        }
    }

    @Override
    public void deleteTask(Long id) throws IOException, InterruptedException {
        Task task = tasks.get(id);
        if (getHistoryList().contains(task)) {
            historyManager.remove(id);
        }
        tasks.remove(id);
    }

    @Override
    public void deleteEpicTask(Long id) throws IOException, InterruptedException {
        EpicTask epicTask = epics.get(id);
        for (long idSubTask : epicTask.getIdSubTasks()) {
            SubTask subTask = subTasks.get(idSubTask);
            if (getHistoryList().contains(subTask)) {
                historyManager.remove(id);
            }
            subTasks.remove(idSubTask);
        }
        epicTask.getIdSubTasks().clear();
        if (getHistoryList().contains(epicTask)) {
            historyManager.remove(id);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubTask(Long id) throws IOException, InterruptedException {
        SubTask subTask = subTasks.get(id);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        epicTask.getIdSubTasks().remove(id);
        subTasks.remove(id);
        updateEpicTask(epicTask);
        if (getHistoryList().contains(subTask)) {
            historyManager.remove(id);
        }
    }

    @Override
    public ArrayList<SubTask> getListSubTaskFromEpic(Long idEpicTask) {
        if (idEpicTask != null) {
            if (epics.containsKey(idEpicTask)) {
                ArrayList<SubTask> listSubTask = new ArrayList<>();
                EpicTask epic = epics.get(idEpicTask);
                for (long x : epic.getIdSubTasks()) {
                    listSubTask.add(subTasks.get(x));
                }
                return listSubTask;
            } else {
                throw new IllegalArgumentException("эпик с id:" + idEpicTask + " отсутствует");
            }
        } else {
            throw new NullPointerException("idEpicTask = null");
        }
    }

    @Override
    public ArrayList<SubTask> getListSubTask() {
        return new ArrayList<>(subTasks.values());
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