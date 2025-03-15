package com.dry_code_snippets.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static com.dry_code_snippets.util.OutputHelper.cliPrint;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

public class RequestHandler {
    private static final String BASE_URL =  Dotenv.load().get("API_BASE_URL");
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private static String buildUrl(String path, String queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            debugPrint("API CALL MADE: " + BASE_URL + path);
            return BASE_URL + path;
        }
        debugPrint("API CALL MADE: " + BASE_URL + path + "?" + queryParams);
        return BASE_URL + path + "?" + queryParams;
    }

    private static String encodedQueryParam(String key, String value) {
        try {
            if (value != null && !value.isBlank()) {
                return URLEncoder.encode(key, StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(value, StandardCharsets.UTF_8);
            }
            return "";
        } catch (Exception e) {
            return "";        }
    }

    public static String addQueryParam(String queryParams, String key, String value) {
        String encodedParam = encodedQueryParam(key, value);
        if (encodedParam.isEmpty()) {
            return queryParams;
        }
        if (queryParams == null || queryParams.isBlank()) {
            return encodedParam;
        }
        return queryParams + "&" + encodedParam;
    }

    private static String getResponse(HttpResponse<String> response) {
        return switch (response.statusCode()) {
            case 200 -> response.body();
            case 400 -> "ERROR: Invalid Request";
            case 401 -> "ERROR: Unauthorized";
            case 404 -> "ERROR: Not Found";
            default -> "ERROR: Request Failed";
        };
    }

    public static String getRequest(String path, String queryParams) {
        debugPrint("GET REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "ERROR: Request Failed";
        }
    }

    public static String postRequest(String path, String queryParams, String body) {
        debugPrint("POST REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "ERROR: Request Failed";
        }
    }

    public static String putRequest(String path, String queryParams, String body) {
        debugPrint("PUT REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "ERROR: Request Failed";
        }
    }

    public static String deleteRequest(String path, String queryParams) {
        debugPrint("DELETE REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + GoogleAuthHandler.jwtToken)
                    .DELETE()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return getResponse(response);

        } catch (Exception e) {
            return "ERROR: Request Failed";
        }
    }
}