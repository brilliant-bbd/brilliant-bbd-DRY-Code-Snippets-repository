package com.dry_code_snippets.commands;

import com.dry_code_snippets.util.GoogleAuthHandler;
import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@CommandLine.Command(name = "login", description = "Login with Google")
public class Login implements Runnable {

    public void run() {
        try {
            GoogleAuthHandler.Authenticate();
        } catch (IOException | URISyntaxException ignored) {
        }
    }

}

