package com.dry_code_snippets.commands;

import com.dry_code_snippets.util.GoogleAuthHandler;
import picocli.CommandLine;
import java.io.IOException;
import java.net.URISyntaxException;

@CommandLine.Command(name = "login", description = "Login with Google")
public class Login implements Runnable {

    public void run() {
        try {
            GoogleAuthHandler.Authenticate();
        } catch (IOException | URISyntaxException ignored) {
        }
    }

}

