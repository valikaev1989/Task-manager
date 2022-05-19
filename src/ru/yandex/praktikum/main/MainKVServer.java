package ru.yandex.praktikum.main;

import ru.yandex.praktikum.api.HTTPTaskManager;
import ru.yandex.praktikum.api.HTTPTaskServer;
import ru.yandex.praktikum.api.KVClient;
import ru.yandex.praktikum.api.KVServer;

import java.io.IOException;

public class MainKVServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        new HTTPTaskServer().start();
    }
}

class OldTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        new App().start();
    }
}

class MainHTTPTaskManager {
    public static void main(String[] args) throws IOException, InterruptedException {
        new App2().start();
    }
}

class MainKVClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVClient kvClient = new KVClient("http://localhost:8078");
        kvClient.put("1", "test1");
        kvClient.put("2", "test2");
        kvClient.put("3", "test3");
        kvClient.put("4", "test4");
        kvClient.put("1", "test5");
        System.out.println(kvClient.load("1"));
        System.out.println(kvClient.load("2"));
        System.out.println(kvClient.load("3"));
        System.out.println(kvClient.load("4"));
    }
}