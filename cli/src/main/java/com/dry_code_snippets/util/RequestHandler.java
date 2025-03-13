package com.dry_code_snippets.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class RequestHandler {
    private static final String BASE_URL = "http://localhost:8080";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private static String buildUrl(String path, String queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return BASE_URL + path;
        }
        return BASE_URL + path + "?" + queryParams;
    }

    private static String getResponse(HttpResponse<String> response) {
        return switch (response.statusCode()) {
            case 200 -> response.body();
            case 400 -> "Invalid Request";
            case 401 -> "Unauthorized";
            case 404 -> "Not Found";
            default -> "Request Failed";
        };
    }

    public static String getRequest(String path, String queryParams) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "Request Failed";
        }
    }

    public static String postRequest(String path, String queryParams, String body) {
        String url = buildUrl(path, queryParams);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "Request Failed";
        }
    }

    public static String putRequest(String path, String queryParams, String body) {
        String url = buildUrl(path, queryParams);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "Request Failed";
        }
    }

    public static String deleteRequest(String path, String queryParams) {
        String url = buildUrl(path, queryParams);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .DELETE()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "Request Failed";
        }
    }
}