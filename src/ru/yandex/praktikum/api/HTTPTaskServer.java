package ru.yandex.praktikum.api;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.praktikum.allinterface.TaskManager;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.taskmanager.Managers;

import java.io.*;
import java.net.InetSocketAddress;


public class HTTPTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;
    private static Gson gson = new Gson();
    private final TaskManager taskManager;

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public HTTPTaskServer() throws IOException, InterruptedException {
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
        taskManager = Managers.getDefault();
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
    }

    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks запроса от клиента.");
            String URL = httpExchange.getRequestURI().toString();
            System.out.println(URL);

            String response = "";
            int responseCode = 200;
            switch (method) {
                case "GET":
                    System.out.println("Вы использовали метод GET!");
                    if (URL.contains("sorted")) {
                        String isSorted = URL.split("=")[1];
                        if (isSorted.equals("true")) {
                            response = gson.toJson(taskManager.getPrioritizedTasks());
                        } else {
                            response = gson.toJson(taskManager.getAllTasks());
                        }
                    }
                    break;
                case "DELETE":
                    System.out.println("Вы использовали метод DELETE!");
                    try {
                        taskManager.clearAllTask();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (taskManager.getAllTasks().isEmpty() && taskManager.getHistoryList().isEmpty()) {
                            response = "менеджер очищен";
                        } else {
                            response = "ошибка очищения менеджера";
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    response = "Такой метод не используется!";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
                os.write(response.getBytes());
            }
        }
    }

    class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/history запроса от клиента.");
            String URL = httpExchange.getRequestURI().toString();
            String response = "";
            int responseCode = 200;
            switch (method) {
                case "GET":
                    try {
                        response = gson.toJson(taskManager.getHistoryList());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "DELETE":
                    if (URL.contains("id")) {
                        String id = URL.split("=")[1];
                        try {
                            Task task = taskManager.getAnyTaskById(Long.valueOf(id));
                            if (taskManager.getHistoryList().contains(task)) {
                                taskManager.removeTaskInHistory(Long.parseLong(id));
                                response = "задача удалена из истории";
                            } else {
                                response = "задача не удалена из истории";
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        responseCode = 400;
                    }
                    break;
                default:
                    response = "Такой метод не используется!";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
                os.write(response.getBytes());
            }
        }
    }


    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/task запроса от клиента.");
            String URL = httpExchange.getRequestURI().toString();
            String response = "";
            int responseCode = 200;
            switch (method) {
                case "POST":
                    if (URL.equals("/tasks/task")) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String json = new String(is.readAllBytes());
                            Task task = gson.fromJson(json, Task.class);
                            taskManager.createTask(task);
                            if (taskManager.getTasks().containsKey(task.getId())) {
                                response = "задача создана";
                            } else {
                                responseCode = 204;
                                response = "ошибка создания task";
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        responseCode = 204;
                        response = "ошибка создания task";
                    }
                    break;
                case "GET":
                    if (URL.contains("id")) {
                        Long id = Long.parseLong(URL.split("=")[1]);
                        try {
                            if (taskManager.getTasks().containsKey(id)) {
                                response = gson.toJson(taskManager.getTask(id));
                            } else {
                                response = "нет такой задачи";
                            }
                        } catch (InterruptedException e) {
                            System.out.println("ошибка запроса id");
                        }
                    } else if (URL.equals("/tasks/task")) {
                        response = gson.toJson(taskManager.getListTask());
                    }
                    break;
                case "PUT":
                    if (URL.equals("/tasks/task")) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String json = new String(is.readAllBytes());
                            Task task = gson.fromJson(json, Task.class);
                            taskManager.updateTask(task);
                            response = gson.toJson(taskManager.getTask(task.getId()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        response = "ошибка изменения задачи";
                    }
                    break;
                case "DELETE":
                    if (URL.contains("id")) {
                        Long id = Long.parseLong(URL.split("=")[1]);
                        try {
                            if (taskManager.getTasks().containsKey(id)) {
                                taskManager.deleteTask(id);
                                response = "задача удалена";
                            } else {
                                responseCode = 404;
                                response = "задача не удалена!";
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    response = "Такой метод не используется!";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
                os.write(response.getBytes());
            }
        }
    }


    class EpicTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/epic запроса от клиента.");
            String URL = httpExchange.getRequestURI().toString();
            String response = "";
            int responseCode = 200;
            switch (method) {
                case "POST":
                    if (URL.equals("/tasks/epic")) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String json = new String(is.readAllBytes());
                            EpicTask epicTask = gson.fromJson(json, EpicTask.class);
                            taskManager.createEpicTask(epicTask);
                            if (taskManager.getEpics().containsKey(epicTask.getId())) {
                                response = "задача создана";
                            } else {
                                responseCode = 204;
                                response = "ошибка создания task";
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        responseCode = 204;
                        response = "ошибка создания task";
                    }
                    break;
                case "GET":
                    if (URL.contains("id")) {
                        Long id = Long.parseLong(URL.split("=")[1]);
                        try {
                            if (taskManager.getEpics().containsKey(id)) {
                                response = gson.toJson(taskManager.getEpicTask(id));
                            } else {
                                response = "нет такой задачи";
                                responseCode = 404;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (URL.equals("/tasks/epic")) {
                        response = gson.toJson(taskManager.getListEpicTask());
                    }
                    break;
                case "DELETE":
                    if (URL.contains("id")) {
                        Long id = Long.parseLong(URL.split("=")[1]);
                        try {
                            if (taskManager.getEpics().containsKey(id)) {
                                taskManager.deleteEpicTask(id);
                                response = "задача удалена";
                            } else {
                                responseCode = 404;
                                response = "задача не удалена!";
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    response = "Такой метод не используется!";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
                os.write(response.getBytes());
            }
        }
    }

    class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/task запроса от клиента.");
            String URL = httpExchange.getRequestURI().toString();
            String response = "";
            int responseCode = 200;
            switch (method) {
                case "POST":
                    if (URL.equals("/tasks/subtask")) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String json = new String(is.readAllBytes());
                            SubTask subTask = gson.fromJson(json, SubTask.class);
                            taskManager.createSubTask(subTask);
                            if (taskManager.getSubTasks().containsKey(subTask.getId())) {
                                response = "задача создана";
                            } else {
                                responseCode = 204;
                                response = "ошибка создания task";
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        responseCode = 204;
                        response = "ошибка создания task";
                    }
                    break;
                case "GET":
                    if (URL.contains("list")) {
                        Long id = Long.parseLong(URL.split("=")[1]);
                        EpicTask epicTask = taskManager.getEpics().get(id);
                        response = gson.toJson(taskManager.getListSubTaskFromEpic(epicTask.getId()));
                    } else if (URL.contains("id")) {
                        Long id = Long.parseLong(URL.split("=")[1]);
                        try {
                            response = gson.toJson(taskManager.getSubTask(id));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "PUT":
                    if (URL.equals("/tasks/subtask")) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String json = new String(is.readAllBytes());
                            SubTask subTask = gson.fromJson(json, SubTask.class);
                            taskManager.updateSubTask(subTask);
                            response = gson.toJson(taskManager.getSubTasks().get(subTask.getId()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        response = "ошибка изменения задачи";
                    }
                    break;
                case "DELETE":
                    if (URL.contains("id")) {
                        Long id = Long.parseLong(URL.split("=")[1]);
                        try {
                            if (taskManager.getSubTasks().containsKey(id)) {
                                taskManager.deleteSubTask(id);
                                response = "задача удалена";
                            } else {
                                responseCode = 404;
                                response = "задача не удалена!";
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    response = "Такой метод не используется!";
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                httpExchange.sendResponseHeaders(responseCode, response.getBytes().length);
                os.write(response.getBytes());
            }
        }
    }
}