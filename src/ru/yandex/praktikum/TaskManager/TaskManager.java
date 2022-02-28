package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
import ru.yandex.praktikum.Task.UnderTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int indetifierNumber = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epics = new HashMap<>();
    private final HashMap<Integer, UnderTask> underTasks = new HashMap<>();


    public HashMap<Integer, EpicTask> getEpics() {
        return new HashMap<>(epics);
    }

    public HashMap<Integer, Task> getTasks() {
        return new HashMap<>(tasks);
    }

    public HashMap<Integer, UnderTask> getUnderTasks() {
        return new HashMap<>(underTasks);
    }

    //Статусы у задач по умолчанию NEW
    //Проверка наличия подзадач и статусов задач в методах обновления(update)
    public void createTask(Task task) {
        tasks.put(indetifierNumber, task);
        task.setId(indetifierNumber);
        indetifierNumber++;
    }

    public void createEpicTask(EpicTask epicTask) {
        epics.put(indetifierNumber, epicTask);
        epicTask.setId(indetifierNumber);
        indetifierNumber++;
    }

    public void createUnderTask(UnderTask underTask) {
        underTasks.put(indetifierNumber, underTask);
        underTask.setId(indetifierNumber);
        EpicTask epicTask = epics.get(underTask.getIdEpicTask());
        epicTask.setIdUnderTasks(indetifierNumber);
        indetifierNumber++;
    }

    public void printAllTask() {
        System.out.println("Задачи: ");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        System.out.println("Сложные Задачи: ");
        for (int idEpic : epics.keySet()) {
            EpicTask epic = epics.get(idEpic);
            System.out.println(epic + System.lineSeparator() + "подзадачи(" + epic.getNameTask() + "):");
            for (int idUnderTask : epic.getIdUnderTasks()) {
                UnderTask underTask = underTasks.get(idUnderTask);
                ArrayList<UnderTask> listUnderTask = new ArrayList<>();
                listUnderTask.add(underTask);
                for (UnderTask underTask1 : listUnderTask) {
                    System.out.println(underTask1);
                }
            }
        }
    }

    public void clearAllTask() {
        tasks.clear();
        epics.clear();
        underTasks.clear();
    }

    public Task getTaskFromId(int id) {
        Task task = new Task();
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else {
            System.out.println("Нет такой задачи");
        }
        return task;
    }

    public EpicTask getEpicTaskFromId(int id) {
        EpicTask epic = new EpicTask();
        if (epics.containsKey(id)) {
            epic = epics.get(id);
        } else {
            System.out.println("Нет такой задачи");
        }
        return epic;
    }

    public UnderTask getUnderTaskFromId(int id) {
        UnderTask underTask = new UnderTask();
        if (underTasks.containsKey(id)) {
            underTask = underTasks.get(id);
        } else {
            System.out.println("Нет такой задачи");
        }
        return underTask;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateUnderTask(UnderTask underTask) {
        underTasks.put(underTask.getId(), underTask);
        EpicTask epic = epics.get(underTask.getIdEpicTask());
        ArrayList<String> statusDone = new ArrayList<>();
        for (int x : epic.getIdUnderTasks()) {
            UnderTask underTask1 = underTasks.get(x);
            if (underTask1.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (underTask1.getStatus().equals(TaskStatus.DONE)) {
                statusDone.add("Done");
                if (statusDone.size() == epic.getIdUnderTasks().size()) {
                    epic.setStatus(TaskStatus.DONE);
                }
            } else epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void deleteTasksOnId(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (underTasks.containsKey(id)) {
            underTasks.remove(id);
        } else System.out.println("Такой задачи нет");
    }

    public ArrayList<UnderTask> getListFromEpic(int idEpicTask) {
        ArrayList<UnderTask> listUnderTask = new ArrayList<>();
        EpicTask epic = epics.get(idEpicTask);
        for (int x : epic.getIdUnderTasks()) {
            listUnderTask.add(underTasks.get(x));
        }
        return listUnderTask;
    }
}