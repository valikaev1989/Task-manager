package ru.yandex.praktikum.TaskManager;

import ru.yandex.praktikum.Task.EpicTask;
import ru.yandex.praktikum.Task.Task;
import ru.yandex.praktikum.Task.TaskStatus;
import ru.yandex.praktikum.Task.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int indetifierNumber = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();


    public HashMap<Integer, EpicTask> getEpics() {
        return new HashMap<>(epics);
    }

    public HashMap<Integer, Task> getTasks() {
        return new HashMap<>(tasks);
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return new HashMap<>(subTasks);
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

    public void createSubTask(SubTask subTask) {
        subTasks.put(indetifierNumber, subTask);
        subTask.setId(indetifierNumber);
        EpicTask epicTask = epics.get(subTask.getIdEpicTask());
        epicTask.setIdSubTasks(indetifierNumber);
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
            for (int idSubTask : epic.getIdSubTasks()) {
                SubTask subTask = subTasks.get(idSubTask);
                ArrayList<SubTask> listSubTask = new ArrayList<>();
                listSubTask.add(subTask);
                for (SubTask subTask1 : listSubTask) {
                    System.out.println(subTask1);
                }
            }
        }
    }

    public void clearAllTask() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
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

    public SubTask getSubTaskFromId(int id) {
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
        EpicTask epic = epics.get(subTask.getIdEpicTask());
        ArrayList<String> statusDone = new ArrayList<>();
        for (int x : epic.getIdSubTasks()) {
            SubTask subTask1 = subTasks.get(x);
            if (subTask1.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (subTask1.getStatus().equals(TaskStatus.DONE)) {
                statusDone.add("Done");
                if (statusDone.size() == epic.getIdSubTasks().size()) {
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
        } else if (subTasks.containsKey(id)) {
            subTasks.remove(id);
        } else System.out.println("Такой задачи нет");
    }

    public ArrayList<SubTask> getListFromEpic(int idEpicTask) {
        ArrayList<SubTask> listSubTask = new ArrayList<>();
        EpicTask epic = epics.get(idEpicTask);
        for (int x : epic.getIdSubTasks()) {
            listSubTask.add(subTasks.get(x));
        }
        return listSubTask;
    }
}