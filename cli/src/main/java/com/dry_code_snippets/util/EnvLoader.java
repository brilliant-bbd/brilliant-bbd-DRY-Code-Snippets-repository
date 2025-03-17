package com.dry_code_snippets.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;

import static com.dry_code_snippets.util.OutputHelper.changeTextWhite;
import static com.dry_code_snippets.util.OutputHelper.cliPrintError;

public class EnvLoader {
    private static String clientId;
    private static String clientSecret;
    private static String apiBaseUrl;
    private static String debug;

    public static boolean loadEnv() {

        File envFile = new File(".env");
        if (!envFile.exists()) {
            cliPrintError(".env file is required for the CLI to run ");
            changeTextWhite();
            System.out.print("\r");
            return false;
        }

        Dotenv dotenv = Dotenv.load();

        clientId = dotenv.get("GOOGLE_CLIENT_ID", "");
        clientSecret = dotenv.get("GOOGLE_CLIENT_SECRET", "");
        apiBaseUrl = dotenv.get("API_BASE_URL", "");
        debug = dotenv.get("DEBUG", "false");

        if (clientId.isEmpty()) {
            cliPrintError("Missing GOOGLE_CLIENT_ID in .env file");
            changeTextWhite();
            System.out.print("\r");
            return false;
        }

        if (clientSecret.isEmpty()) {
            cliPrintError("Missing GOOGLE_CLIENT_SECRET in .env file");
            changeTextWhite();
            System.out.print("\r");
            return false;
        }

        if (apiBaseUrl.isEmpty()) {
            cliPrintError("Missing API_BASE_URL in .env file");
            changeTextWhite();
            System.out.print("\r");
            return false;
        }
        return true;
    }
    public static String getClientId() {
        return clientId;
    }

    public static String getClientSecret() {
        return clientSecret;
    }

    public static String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public static boolean getDebug() {
        return debug.equalsIgnoreCase("true");
    }

}