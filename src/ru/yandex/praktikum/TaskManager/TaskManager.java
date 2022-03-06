package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
import ru.yandex.praktikum.Task.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private long indetifierNumber = 0;
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, EpicTask> epics = new HashMap<>();
    private final HashMap<Long, SubTask> subTasks = new HashMap<>();

    private long generateID() {
        indetifierNumber++;
        return indetifierNumber;
    }

    //Статусы у задач по умолчанию NEW
    //Проверка наличия подзадач и статусов задач в методах обновления(update)
    public void createTask(Task task) {
        task.setId(generateID());
        tasks.put(task.getId(), task);
    }

    public void createEpicTask(EpicTask epicTask) {
        epicTask.setId(generateID());
        epics.put(epicTask.getId(), epicTask);
    }

    public void createSubTask(SubTask subTask) {
        subTask.setId(generateID());
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        epicTask.setIdSubTasks(subTask.getId());
    }

    public void clearAllTask() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    public Task getTaskFromId(long id) {
        Task task = new Task();
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else {
            System.out.println("Нет такой задачи");
        }
        return task;
    }

    public EpicTask getEpicTaskFromId(long id) {
        EpicTask epic = new EpicTask();
        if (epics.containsKey(id)) {
            epic = epics.get(id);
        } else {
            System.out.println("Нет такой задачи");
        }
        return epic;
    }

    public SubTask getSubTaskFromId(long id) {
        SubTask subTask = new SubTask();
        if (subTasks.containsKey(id)) {
            subTask = subTasks.get(id);
        } else {
            System.out.println("Нет такой задачи");
        }
        return subTask;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        updateEpicTask(epicTask);
    }

    // условия статуса в задании для эпиков:
    // 1.если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW(по умолчанию NEW).
    // 2.если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE, строка 99-102.
    // 3.если одна из подзадач имеет статус IN_PROGRESS, эпик со статусом DONE.
    // 4.во всех остальных случаях статус должен быть IN_PROGRESS.
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

    public void deleteTasksOnId(long id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) { //удаление эпика из мапы и очистка листа с ид SubTask в EpicTask
            EpicTask epicTask = epics.get(id);
            epicTask.getIdSubTasks().clear();
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            EpicTask epicTask = epics.get(subTask.getIdEpicTask());
            epicTask.getIdSubTasks().remove(id); //удаление подзадачи из списка ид SubTask в EpicTask
            subTasks.remove(id);
            updateEpicTask(epicTask);
        } else System.out.println("Такой задачи нет");
    }

    public ArrayList<SubTask> getListSubTaskFromEpic(long idEpicTask) {
        ArrayList<SubTask> listSubTask = new ArrayList<>();
        EpicTask epic = epics.get(idEpicTask);
        for (long x : epic.getIdSubTasks()) {
            listSubTask.add(subTasks.get(x));
        }
        return listSubTask;
    }

    public ArrayList<Task> getListTask() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<EpicTask> getListEpicTask() {
        return new ArrayList<>(epics.values());
    }

}