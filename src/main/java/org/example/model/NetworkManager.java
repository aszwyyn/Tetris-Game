package org.example.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NetworkManager {

    public static String checkConnection() {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://example.com"))
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            return "Network response: " + response.statusCode();

        } catch (IOException | InterruptedException e) {
            return "Network connection failed: " + e.getMessage();
        }
    }
}