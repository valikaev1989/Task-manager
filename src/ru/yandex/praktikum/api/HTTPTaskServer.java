package ru.yandex.praktikum.api;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.allinterface.HistoryManager;
import ru.yandex.praktikum.allinterface.Managers;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.main.App;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.taskmanager.FileBackedTasksManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class HTTPTaskServer implements TaskManager {
    private static final int PORT = 8081;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static HttpServer httpServer;
    private static Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static TaskManager taskManager;


    public HTTPTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicTaskHandler());
        httpServer.createContext("/tasks/subtask", new SubTaskHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();
        taskManager = Managers.getFileBackedTasksManager();
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    private static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks запроса от клиента.");

            String response;
            switch (method) {
                case "GET":
                    System.out.println("Вы использовали метод GET!");
                    response = gson.toJson(taskManager.getAllTasks());
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(200, 0);
                        os.write(response.getBytes());
                    }
                    break;
                case "DELETE":
                    System.out.println("Вы использовали метод DELETE!");
                    taskManager.clearAllTask();
                    response = gson.toJson(taskManager.getAllTasks());
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(200, 0);
                        os.write(response.getBytes());
                    }
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }
        }
    }

    private static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/history запроса от клиента.");

            String response;
            switch (method) {
                case "POST":
                    response = "Вы использовали метод POST!";
                    break;
                case "GET":
                    response = "Вы использовали метод GET!";
                    break;
                case "DELETE":
                    response = "Вы использовали метод DELETE!";
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }


            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(200, 0);
                os.write(response.getBytes());
            }
        }
    }


    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/task запроса от клиента.");
            String response;
            switch (method) {
                case "POST":
                    response = "Вы использовали метод POST!";
                    break;
                case "GET":
//                    response = gson.toJson(taskManager.getAllTasks());
                    response = "Вы использовали метод GET!";
                    break;
                case "DELETE":
                    response = "Вы использовали метод DELETE!";
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        httpExchange.sendResponseHeaders(200, 0);
                        os.write(response.getBytes());
                    }
            }
        }
    }


    static class EpicTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/EpicTask запроса от клиента.");

            String response;
            switch (method) {
                case "POST":
                    response = "Вы использовали метод POST!";
                    break;
                case "GET":
                    response = "Вы использовали метод GET!";
                    break;
                case "DELETE":
                    response = "Вы использовали метод DELETE!";
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(200, 0);
                os.write(response.getBytes());
            }
        }
    }

    private static class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/SubTask запроса от клиента.");

            String response;
            switch (method) {
                case "POST":
                    response = "Вы использовали метод POST!";
                    break;
                case "GET":
                    response = "Вы использовали метод GET!";
                    break;
                case "DELETE":
                    response = "Вы использовали метод DELETE!";
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }


            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(200, 0);
                os.write(response.getBytes());
            }
        }
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {

    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws ManagerSaveException {

    }

    @Override
    public void createSubTask(SubTask subTask) throws ManagerSaveException {

    }

    @Override
    public void clearAllTask() throws ManagerSaveException {

    }

    @Override
    public Task getTask(Long id) throws ManagerSaveException {
        return null;
    }

    @Override
    public EpicTask getEpicTask(Long id) throws ManagerSaveException {
        return null;
    }

    @Override
    public SubTask getSubTask(Long id) throws ManagerSaveException {
        return null;
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {

    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {

    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerSaveException {

    }

    @Override
    public void deleteTasksOnId(Long id) throws ManagerSaveException {

    }

    @Override
    public void deleteTask(Long id) throws ManagerSaveException {

    }

    @Override
    public void deleteEpicTask(Long id) throws ManagerSaveException {

    }

    @Override
    public void deleteSubTask(Long id) throws ManagerSaveException {

    }

    @Override
    public ArrayList<SubTask> getListSubTaskFromEpic(Long idEpicTask) {
        return null;
    }

    @Override
    public ArrayList<Task> getListTask() {
        return null;
    }

    @Override
    public ArrayList<EpicTask> getListEpicTask() {
        return null;
    }

    @Override
    public List<Task> getHistoryList() throws ManagerSaveException {
        return null;
    }

    @Override
    public HashMap<Long, Task> getTasks() {
        return null;
    }

    @Override
    public HashMap<Long, EpicTask> getEpics() {
        return null;
    }

    @Override
    public HashMap<Long, SubTask> getSubTasks() {
        return null;
    }

    @Override
    public ArrayList<Task> getAllTasks() throws ManagerSaveException {
        return null;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return null;
    }

    @Override
    public void removeTaskInHistory(long id) {

    }

    @Override
    public void addInDateList(Task task) {

    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return null;
    }
}