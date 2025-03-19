package com.dry_code_snippets.util;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import static com.dry_code_snippets.util.GoogleAuthHandler.getJwt;
import static com.dry_code_snippets.util.OutputHelper.cliPrintError;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

public class RequestHandler {
//  private static final String BASE_URL = "http://dry-code-snippets-tfoject-prod.eba-ryhpdtkm.af-south-1.elasticbeanstalk.com";
    private static final String BASE_URL = "http://localhost:8080";

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
            if (value != null) {
                return URLEncoder.encode(key, StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(value, StandardCharsets.UTF_8);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
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

    public static boolean checkValidResponse(HttpResponse<String> response) {
        if (response.statusCode() >= 400 && response.statusCode() < 600 ) {
            switch (response.statusCode()) {
                case 400 -> cliPrintError("ERROR: 400 Invalid Request");
                case 401 -> cliPrintError("ERROR: 401 Unauthorized");
                case 403 -> cliPrintError("ERROR: 403 Forbidden");
                case 404 -> cliPrintError("ERROR: 404 Not Found");
                default -> cliPrintError("ERROR: Request Failed");
            };
            return false;
        }
        return true;
    }

    public static HttpResponse<String> getRequest(String path, String queryParams) {
        debugPrint("GET REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + getJwt())
                    .GET()
                    .build();

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            return null;
        }
    }

    public static HttpResponse<String> postRequest(String path, String queryParams, String body) {
        debugPrint("POST REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + getJwt())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            return null;
        }
    }

    public static HttpResponse<String> loginPostRequest(String authCode) {
        debugPrint("LOGIN POST REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl("/api/login", "")))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(authCode, StandardCharsets.UTF_8))
                    .build();

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            return null;
        }
    }


    public static HttpResponse<String> putRequest(String path, String queryParams, String body) {
        debugPrint("PUT REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + getJwt())
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            return null;
        }
    }

    public static HttpResponse<String> deleteRequest(String path, String queryParams) {
        debugPrint("DELETE REQUEST");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(buildUrl(path, queryParams)))
                    .header("Authorization", "Bearer " + getJwt())
                    .DELETE()
                    .build();

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            return null;
        }
    }
}