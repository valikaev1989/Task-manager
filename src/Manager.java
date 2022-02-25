import ru.yandex.praktikum.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int indetifierNumber = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Task.EpicTask> epics = new HashMap<>();
    private final HashMap<Integer, Task.UnderTask> underTasks = new HashMap<>();
    private final HashMap<Integer, Object> doneTasks = new HashMap<>();

    public HashMap<Integer, Task.EpicTask> getEpics() {
        return epics;
    }

    public void createTask(Task task) {
        tasks.put(indetifierNumber, task);
        task.setIdTask(indetifierNumber);
        indetifierNumber++;
    }

    public void createEpicTask(Task.EpicTask epicTask) {
        epics.put(indetifierNumber, epicTask);
        epicTask.setIdEpicTask(indetifierNumber);
        indetifierNumber++;
    }

    public void createUnderTask(Task.UnderTask underTask, int idEpicTask) {
        underTasks.put(indetifierNumber, underTask);
        underTask.setIdUnderTask(indetifierNumber);
        underTask.setIdEpicTask(idEpicTask);
        Task.EpicTask epic = epics.get(idEpicTask);
        epic.setIdUnderTasks(indetifierNumber);
        indetifierNumber++;
    }

    public void printAllTask() {
        System.out.println("Задачи: ");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        System.out.println("Сложные Задачи: ");
        for (int idEpic : epics.keySet()) {
            Task.EpicTask epic = epics.get(idEpic);
            System.out.println(epic + System.lineSeparator() + "подзадачи(" + epic.getNameEpicTask() + "):");
            for (int idUnderTask : epic.getIdUnderTasks()) {
                Task.UnderTask underTask = underTasks.get(idUnderTask);
                ArrayList<Task.UnderTask> listUnderTask = new ArrayList<>();
                listUnderTask.add(underTask);
                for (Task.UnderTask underTask1 : listUnderTask) {
                    System.out.println(underTask1);
                }
            }
        }
    }

    public void clearAllTask() {
        tasks.clear();
        epics.clear();
        underTasks.clear();
        doneTasks.clear();
    }

    public Object getTaskFromId(int id) {
        Object object = null;
        if (tasks.containsKey(id)) {
            object = tasks.get(id);
        } else if (underTasks.containsKey(id)) {
            object = underTasks.get(id);
        } else if (epics.containsKey(id)) {
            object = epics.get(id);
        } else {
            System.out.println("Нет такой задачи");
        }
        return object;
    }

    public void updateTask(int id, String status) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            if (status.equals("In_progress")) {
                task.setStatus(status);
                tasks.put(id, task);
            } else if (status.equals("Done")) {
                task.setStatus(status);
                doneTasks.put(id, task);
                tasks.remove(id);
            } else {
                System.out.println("Введен некорректный статус");
            }
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    public void updateUnderTask(int id, String status) {
        if (underTasks.containsKey(id)) {
            if (status.equals("In_progress") || status.equals("Done")) {
                Task.UnderTask underTask = underTasks.get(id);
                underTask.setStatus(status);
                underTasks.put(id, underTask);
                int idEpic = underTask.getIdEpicTask();
                Task.EpicTask epic = epics.get(idEpic);
                ArrayList<String> statusNew = new ArrayList<>();
                ArrayList<String> statusDone = new ArrayList<>();
                for (int x : epic.getIdUnderTasks()) {
                    Task.UnderTask underTask1 = underTasks.get(x);
                    if (underTask1.getStatus().equals("New")) {
                        statusNew.add("New");
                        if (statusNew.size() == epic.getIdUnderTasks().size()) {
                            epic.setStatus("New");
                        }
                    } else if (underTask1.getStatus().equals("Done")) {
                        statusDone.add("Done");
                        if (statusDone.size() == epic.getIdUnderTasks().size()) {
                            epic.setStatus("Done");
                            doneTasks.put(idEpic, epic);
                            epics.remove(idEpic);
                        }
                    } else epic.setStatus("In_progress");
                }
            } else {
                System.out.println("Некорректный статус задачи");
            }
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    public void deleteTasksOnId(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Task.EpicTask epic = epics.get(id);
            epics.remove(id);
        } else if (underTasks.containsKey(id)) {
            underTasks.remove(id);
        } else System.out.println("Такой задачи нет");
    }

    public ArrayList<Task.UnderTask> getListFromEpic(int idEpicTask) {
        ArrayList<Task.UnderTask> listUnderTask = new ArrayList<>();
        Task.EpicTask epic = epics.get(idEpicTask);
        for (int x : epic.getIdUnderTasks()) {
            listUnderTask.add(underTasks.get(x));
        }
        return listUnderTask;
    }

    public ArrayList<Object> getListFinishTasks() {
        return new ArrayList<>(doneTasks.values());
    }
}
