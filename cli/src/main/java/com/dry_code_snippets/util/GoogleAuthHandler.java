package com.dry_code_snippets.util;

import com.sun.net.httpserver.HttpServer;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dry_code_snippets.util.OutputHelper.cliPrint;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

public class GoogleAuthHandler {
    private static final String CLIENT_ID = "664830995974-t1ql2bsusulrkuvu59473qd2mkc8ummd.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:9090/callback";
    public static String jwtToken = "";

    public static String getJwt() {
        return jwtToken;
    }

    public static void Authenticate() throws IOException, URISyntaxException {
        String authUrl = "https://accounts.google.com/o/oauth2/auth?client_id=" + CLIENT_ID +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8) + "&response_type=code" +
                "&scope=" + URLEncoder.encode("openid email profile", StandardCharsets.UTF_8) + "&access_type=offline";

        Desktop desktop = java.awt.Desktop.getDesktop();
        desktop.browse(new URI(authUrl));
        getJwtToken();
    }

    private static void getJwtToken() throws IOException {
        HttpServer callbackServer = HttpServer.create(new InetSocketAddress(9090), 0);
        callbackServer.createContext("/callback", exchange -> {

            try {
                String output = "Login unsuccessful. Try again";
                String callbackResponse = exchange.getRequestURI().getQuery();
                if (callbackResponse != null) {

                    Matcher authCodeMatcher = Pattern.compile("code=([^&]*)").matcher(callbackResponse);

                    if (authCodeMatcher.find()) {
                        String authCode = authCodeMatcher.group(1);
                        if (authCode == null) {
                            output = "ERROR: Login unsuccessful. Try again";
                        } else {
                            HttpResponse<String> response = RequestHandler.loginPostRequest(authCode);
                            if (response != null && response.statusCode() == 200 ) {
                                output = "Login Successful";
                                jwtToken = response.body();
                            } else {
                                output = "Login Unsuccessful";
                            }
                            debugPrint("RESPONSE: " + response);
                            debugPrint("JWT: " + jwtToken);
                        }
                    } else {
                        output = "ERROR: Login unsuccessful. Try again";
                    }

                }
                exchange.sendResponseHeaders(200, output.length());
                exchange.getResponseBody().write(output.getBytes());
                cliPrint(output);
                callbackServer.stop(0);

            } catch (Exception e) {
                cliPrint("ERROR: Login unsuccessful. Try again");
                callbackServer.stop(0);
            }

        });
        callbackServer.start();
    }
}
