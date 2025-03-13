package com.dry_code_snippets.util;

import com.dry_code_snippets.commands.AddSnippet;
import com.dry_code_snippets.commands.Login;
import picocli.CommandLine.Command;

@Command(
        name = "Dry-code-snippets-cli",
        description = "A CLI tool for the dry-code-snippets API",
        mixinStandardHelpOptions = true,
        subcommands = { Login.class, AddSnippet.class }
)
public class Cli implements Runnable {
    public void run() {
    }
}

