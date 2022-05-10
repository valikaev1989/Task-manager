package ru.yandex.praktikum.taskmanager;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.allinterface.HistoryManager;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.praktikum.utils.CSVutil.splitter;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final File fileName;// путь к файлу сохранения задач

    public FileBackedTasksManager(File fileName) {
        this.fileName = fileName;
    }

    public void save() throws ManagerSaveException {//метод записи в файл сохранения
        try (Writer fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            fileWriter.write("id" + splitter + "type" + splitter + "name" + splitter + "status" + splitter
                    + "description" + splitter
                    + "startTime" + splitter + "duration" + splitter + "endTime" + splitter + "epic\n");
            for (Task task : getAllTasks()) {//перебор всех задач
                String taskToString = task.toString();
                fileWriter.write(taskToString + "\n");//запись в файл
            }
            fileWriter.write(" " + "\n");
            fileWriter.write(toString(getHistoryManager()));//последовательная запись id задач истории в файл
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи в файл.", e);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        final FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        fileBackedTasksManager.readFile();
        return fileBackedTasksManager;
    }

    private void readFile() throws ManagerSaveException {//заполнение менеджера задач и истории просмотра из файла
        long identifierNumber = 0;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {

            while (fileReader.ready()) {
                String q = fileReader.readLine();
                String firstLine = "id;type;name;status;description;startTime;duration;endTime;epic";
                String w = "Task";
                if (q.isBlank() || firstLine.equals(q)) {//пропуск при чтении первой и пустых строк
                } else if (q.contains(w)) {//заполнение коллекций менеджера задачами из файла
                    Task taskFromFile = fromString(q);
                    Task task = new Task();
                    SubTask subTask = new SubTask();
                    EpicTask epicTask = new EpicTask();
                    if (taskFromFile.getClass() == task.getClass()) {
                        getTasks().put(taskFromFile.getId(), taskFromFile);
                    } else if (taskFromFile.getClass() == epicTask.getClass()) {
                        epicTask = (EpicTask) taskFromFile;
                        getEpics().put(taskFromFile.getId(), epicTask);
                    } else if (taskFromFile.getClass() == subTask.getClass()) {
                        subTask = (SubTask) taskFromFile;
                        for (EpicTask epicTask1 : getListEpicTask())
                            if (subTask.getIdEpicTask() == epicTask1.getId()) {
                                epicTask1.setIdSubTasks(subTask.getId());
                            }
                        getSubTasks().put(taskFromFile.getId(), subTask);
                    }
                } else {//заполнение истории просмотра задач
                    List<Long> listId = fromStringList(q);
                    for (Long id : listId) {
                        if (identifierNumber < id) {
                            identifierNumber = id;
                            setIndetifierNumber(identifierNumber);//установка максимального значения id из файла
                            // для не дублирования id у задач при создании новых задач
                        }
                    }
                    for (long id : listId) {//заполнение истории просмотров
                        if (getTasks().containsKey(id)) {
                            getTask(id);
                        } else if (getEpics().containsKey(id)) {
                            getEpicTask(id);
                        } else if (getSubTasks().containsKey(id)) {
                            getSubTask(id);
                        }
                    }
                }

            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.", e);
        }
    }

    private Task fromString(String value) { //преобразование строки в задачу
//id;type;name;status;description;startTime;duration;endTime;epic;
        // параметр в методе записан в формате: idTask;TypeTasks;nameTask;Taskstatus;descriptionTask;epicId
        //String nameTask, String description, TaskStatus status, Long id, int duration, LocalDateTime startTime
        Task task;
        String[] split = value.split(";");
        TypeTasks typeTasks = TypeTasks.valueOf(split[1]);
        Long id = Long.parseLong(split[0]);
        String nameTask = split[2];
        String description = split[4];
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (split[5].equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(split[5]);
        }
        if (split[7].equals("null")) {
            endTime = null;
        } else {
            endTime = LocalDateTime.parse(split[7]);
        }
        int duration = Integer.parseInt(split[6]);
        if (typeTasks.equals(TypeTasks.Task)) {
            task = new Task(nameTask, description, TaskStatus.valueOf(split[3]), id, duration, startTime);
        } else if (typeTasks.equals(TypeTasks.EpicTask)) {
            task = new EpicTask(nameTask, description, TaskStatus.valueOf(split[3]), id, duration, startTime, endTime);
        } else {
            long epicId = Long.parseLong(split[8]);
            task = new SubTask(nameTask, description, TaskStatus.valueOf(split[3]), id, epicId);
        }
        return task;
    }

    private static List<Long> fromStringList(String value) {//создание списка с id задач для истории просмотра задач из строки
        List<Long> idList = new ArrayList<>();
        String[] split = value.split(",");
        for (String stringID : split) {
            long id = Long.parseLong(stringID);
            idList.add(id);
        }
        return idList;
    }

    private static String toString(HistoryManager manager) {//получение строки с id задач в истории просмотра
        return manager.toString();
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) throws ManagerSaveException {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void clearAllTask() throws ManagerSaveException {
        super.clearAllTask();
        save();
    }

    @Override
    public Task getTask(Long id) throws ManagerSaveException {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicTask(Long id) throws ManagerSaveException {
        EpicTask epicTask = super.getEpicTask(id);
        save();
        return epicTask;
    }

    @Override
    public SubTask getSubTask(Long id) throws ManagerSaveException {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void deleteTasksOnId(Long id) throws ManagerSaveException {
        super.deleteTasksOnId(id);
        save();
    }

    @Override
    public ArrayList<SubTask> getListSubTaskFromEpic(Long idEpicTask) {
        return super.getListSubTaskFromEpic(idEpicTask);
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
    public List<Task> getHistoryList() throws ManagerSaveException {
        super.getHistoryList();
        save();
        return super.getHistoryList();

    }

    @Override
    public HashMap<Long, Task> getTasks() {
        return super.getTasks();
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
}
