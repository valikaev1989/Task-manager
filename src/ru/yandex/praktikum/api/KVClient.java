package ru.yandex.praktikum.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    HttpClient httpClient;
    String API_Token;
    String URL;

    public KVClient(String URL) throws IOException, InterruptedException {
        this.URL = URL;
        httpClient = HttpClient.newHttpClient();

        URI url = URI.create(this.URL + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        API_Token = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI url = URI.create(this.URL + "/save/" + key + "?API_TOKEN=" + API_Token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI url = URI.create(this.URL + "/load/" + key + "?API_TOKEN=" + API_Token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}