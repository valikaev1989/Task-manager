package ru.yandex.praktikum.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.api.*;
import ru.yandex.praktikum.task.EpicTask;
import ru.yandex.praktikum.task.SubTask;
import ru.yandex.praktikum.task.Task;
import ru.yandex.praktikum.task.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HTTPTaskManagerTest {
   private HTTPTaskManager taskManager;
   private KVServer kvServer;
   private HTTPTaskServer httpTaskServer;
   private Gson gson = new Gson();

   private Task task1 = new Task("Task1", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 15));
   private Task task2 = new Task("Task2", "description", 1, LocalDateTime.of(2022, 5, 11, 10, 20));
   private EpicTask epic1 = new EpicTask("epic1", "desc");
   private EpicTask epic2 = new EpicTask("epic2", "desc");

    @BeforeEach()
    void create() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HTTPTaskServer();
        httpTaskServer.start();
        taskManager = (HTTPTaskManager) httpTaskServer.getTaskManager();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();
    }

    @AfterEach
    void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void checkTaskPost() throws IOException, InterruptedException {
        String taskQ = gson.toJson(task1);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskQ))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = client.send(request1, handler);
        assertEquals(200, response1.statusCode());
        assertEquals("задача создана", response1.body());
        String taskW = gson.toJson(task2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskW))
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals(200, response2.statusCode());
        assertEquals("задача создана", response2.body());

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response4 = client.send(request4, handler);
        Type collectionTasks = new TypeToken<Collection<Task>>() {
        }.getType();
        ArrayList<Task> tasks = gson.fromJson(response4.body(), collectionTasks);
        for (Task task : tasks) {
            assertTrue(taskManager.getListTask().contains(task), " запрашиваемые задачи не соответствуют коллекции");
        }
    }

    @Test
    void checkTaskGet() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=4"))
                .GET()
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals("нет такой задачи", response.body());

        String taskQ = gson.toJson(task1);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskQ))
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        assertEquals(200, response1.statusCode());
        assertEquals("задача создана", response1.body());

        String taskW = gson.toJson(task2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskW))
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals(200, response2.statusCode());
        assertEquals("задача создана", response2.body());

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=2"))
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, handler);
        Task task = gson.fromJson(response3.body(), Task.class);
        assertEquals(taskManager.getTask(2L), task);

    }

    @Test
    void checkDeleteTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=4"))
                .DELETE()
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(404, response.statusCode());

        String task = gson.toJson(task1);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        assertEquals(200, response1.statusCode());

        assertEquals("задача создана", response1.body());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=1"))
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals(200, response2.statusCode());
    }

    @Test
    void checkPutTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        String task = gson.toJson(task1);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task))
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        assertEquals(200, response1.statusCode());

        Task task2 = taskManager.getTask(1L);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        String taskToJson = gson.toJson(task2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(taskToJson))
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        Task taskFromJson = gson.fromJson(response2.body(), Task.class);
        assertEquals(task2.getStatus(), taskFromJson.getStatus());
        assertEquals(200, response2.statusCode());
    }

    @Test
    void checkEpicTasksPost() throws IOException, InterruptedException {
        String epicQ = gson.toJson(epic1);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicQ))
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        assertEquals(200, response1.statusCode());
        assertEquals("задача создана", response1.body());

        String epicW = gson.toJson(epic2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicW))
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals(200, response2.statusCode());
        assertEquals("задача создана", response2.body());

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response4 = client.send(request4, handler);
        Type collectionTasks = new TypeToken<Collection<EpicTask>>() {
        }.getType();
        ArrayList<EpicTask> tasks = gson.fromJson(response4.body(), collectionTasks);
        for (EpicTask task : tasks) {
            assertTrue(taskManager.getListEpicTask().contains(task), " запрашиваемые задачи не соответствуют коллекции");
        }
    }

    @Test
    void checkEpicTaskGet() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=4"))
                .GET()
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals("нет такой задачи", response.body());

        String taskQ = gson.toJson(epic1);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskQ))
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        assertEquals(200, response1.statusCode());
        assertEquals("задача создана", response1.body());

        String taskW = gson.toJson(epic2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskW))
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals(200, response2.statusCode());
        assertEquals("задача создана", response2.body());

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=2"))
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, handler);
        EpicTask task = gson.fromJson(response3.body(), EpicTask.class);
        assertEquals(taskManager.getEpicTask(2L), task);
    }

    @Test
    void checkEpicTaskDelete() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=4"))
                .DELETE()
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(404, response.statusCode());
        checkEpicTasksPost();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=1"))
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals(200, response2.statusCode());
    }

    @Test
    void checkSubTaskPost() throws IOException, InterruptedException {
        String epicQ = gson.toJson(epic1);
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicQ))
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        assertEquals(200, response1.statusCode());
        assertEquals("задача создана", response1.body());
        SubTask subTask1 = new SubTask("sub1", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 40), 1L);
        URI url1 = URI.create("http://localhost:8080/tasks/subtask");
        String subTaskQ = gson.toJson(subTask1);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url1)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskQ))
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals(200, response2.statusCode());
        assertEquals("задача создана", response2.body());

        SubTask subTask2 = new SubTask("sub2", "desc", 1, LocalDateTime.of(2022, 5, 11, 10, 45), 1L);
        String subTaskW = gson.toJson(subTask2);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskW))
                .build();
        HttpResponse<String> response3 = client.send(request3, handler);
        assertEquals(200, response3.statusCode());
        assertEquals("задача создана", response3.body());

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(URI.create(url1 + "?list=1"))
                .GET()
                .build();
        HttpResponse<String> response4 = client.send(request4, handler);
        Type collectionTasks = new TypeToken<Collection<SubTask>>() {
        }.getType();
        ArrayList<SubTask> tasks = gson.fromJson(response4.body(), collectionTasks);
        for (SubTask task : tasks) {
            assertTrue(taskManager.getSubTasks().containsValue(task), " запрашиваемые задачи не соответствуют коллекции");
        }
    }

    @Test
    void checkSubTaskPUT() throws IOException, InterruptedException {
        checkSubTaskPost();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpClient client = HttpClient.newHttpClient();
        SubTask subTask = taskManager.getSubTask(2L);
        EpicTask epicTask = taskManager.getEpicTask(subTask.getIdEpicTask());
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask);
        String changeSubTask = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(changeSubTask))
                .build();
        HttpResponse<String> response = client.send(request, handler);
        SubTask subTaskResp = gson.fromJson(response.body(), SubTask.class);

        assertEquals(subTask.getStatus(), subTaskResp.getStatus());
        EpicTask epicTaskAfterChangeStatus = taskManager.getEpicTask(subTask.getIdEpicTask());
        assertEquals(epicTask.getStatus(), epicTaskAfterChangeStatus.getStatus());
    }

    @Test
    void checkSubTaskDelete() throws IOException, InterruptedException {
        checkSubTaskPUT();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals("задача удалена", response.body());
    }

    @Test
    void checkTasksGet() throws IOException, InterruptedException {
        checkSubTaskPost();
        checkTaskPost();
        checkEpicTasksPost();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?sorted=true"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        String priority = gson.toJson(taskManager.getPrioritizedTasks());
        assertEquals(priority, response.body());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url + "?sorted=false"))
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        String allTasks = gson.toJson(taskManager.getAllTasks());
        assertEquals(allTasks, response2.body());
    }

    @Test
    void checkHistory() throws IOException, InterruptedException {
        checkSubTaskPost();
        checkTaskPost();
        checkEpicTasksPost();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        URI url1 = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create(url1 + "?id=4"))
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, handler);
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(URI.create(url1 + "?id=5"))
                .GET()
                .build();
        HttpResponse<String> response4 = client.send(request4, handler);

        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, handler);
        String history = gson.toJson(taskManager.getHistoryList());
        assertEquals(history, response.body());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=4"))
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request2, handler);
        assertEquals("задача удалена из истории", response2.body());


        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create(url + "?id=10"))
                .DELETE()
                .build();
        HttpResponse<String> response3 = client.send(request3, handler);
        assertEquals("задача не удалена из истории", response3.body());
    }
}