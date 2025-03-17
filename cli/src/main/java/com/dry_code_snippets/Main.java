package com.dry_code_snippets;

import picocli.CommandLine;
import com.dry_code_snippets.util.Cli;

import static com.dry_code_snippets.util.EnvLoader.loadEnv;
import static com.dry_code_snippets.util.InputHelper.runCommandScanner;

public class Main {
    public static void main(String[] args) {
        if (!loadEnv()) {
            return;
        }
        CommandLine cli = new CommandLine(new Cli());
        runCommandScanner(cli);
    }
}