package com.dry_code_snippets.util;

import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dry_code_snippets.util.OutputHelper.cliPrint;

public class GoogleAuthHandler {

    private static String CLIENT_ID;
    private static  String CLIENT_SECRET;
    private static final String REDIRECT_URI = "http://localhost:9090/callback";

    public static String jwtToken = "";

    public static void Authenticate() throws IOException, URISyntaxException {
        Dotenv dotenv = Dotenv.load();
        CLIENT_ID = dotenv.get("GOOGLE_CLIENT_ID");
        CLIENT_SECRET = dotenv.get("GOOGLE_CLIENT_SECRET");
        String authUrl = "https://accounts.google.com/o/oauth2/auth?client_id=" + CLIENT_ID +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) + "&response_type=code" +
                "&scope=" + URLEncoder.encode("openid email profile", StandardCharsets.UTF_8) + "&access_type=offline";

        Desktop desktop = java.awt.Desktop.getDesktop();
        desktop.browse(new URI(authUrl));
        startCallbackServer();
    }

    private static void startCallbackServer() throws IOException {
        HttpServer callbackServer = HttpServer.create(new InetSocketAddress(9090), 0);
        callbackServer.createContext("/callback", exchange -> {

            try {
                String output = "Login unsuccessful. Try again";
                String callbackResponse = exchange.getRequestURI().getQuery();
                if (callbackResponse != null) {

                    Matcher authCodeMatcher = Pattern.compile("code=([^&]*)").matcher(callbackResponse);

                    if (authCodeMatcher.find()) {
                        String authCode = authCodeMatcher.group(1);
                        jwtToken = getJwtToken(authCode);
                        System.out.println(jwtToken);
                        output = "Login successful.";
                    } else {
                        output = "Login unsuccessful. Try again";
                    }

                }
                exchange.sendResponseHeaders(200, output.length());
                exchange.getResponseBody().write(output.getBytes());
                cliPrint(output);
                callbackServer.stop(0);

            } catch (Exception e) {
                System.out.println("Login unsuccessful. Try again");
                callbackServer.stop(0);
            }

        });
        callbackServer.start();
    }

    private static String getJwtToken(String authCode) throws IOException {
        String tokenRequest = "code=" + authCode +
                "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + REDIRECT_URI + "&grant_type=authorization_code";

        HttpURLConnection tokenConnection = (HttpURLConnection) URI.create("https://oauth2.googleapis.com/token").toURL().openConnection();
        tokenConnection.setRequestMethod("POST");
        tokenConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        tokenConnection.setDoOutput(true);

        OutputStream postOutputStream = tokenConnection.getOutputStream();
        postOutputStream.write(tokenRequest.getBytes());

        InputStream inputStream = tokenConnection.getInputStream();

        String tokenObject = new String(inputStream.readAllBytes());
        Matcher matcher = Pattern.compile("\"id_token\": \"([^\"]*)").matcher(tokenObject);
        if (matcher.find()) {
            // Matches the JWT Token
            return matcher.group(1);
        } else {
            return "No JWT found";
        }

    }

}
