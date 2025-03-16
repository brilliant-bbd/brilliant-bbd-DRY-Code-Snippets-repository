package com.dry_code_snippets.util;

import com.dry_code_snippets.commands.comments.AddComment;
import com.dry_code_snippets.commands.comments.ListComments;
import com.dry_code_snippets.commands.ratings.AddRating;
import com.dry_code_snippets.commands.snippets.AddSnippet;
import com.dry_code_snippets.commands.Login;
import com.dry_code_snippets.commands.snippets.DeleteSnippet;
import com.dry_code_snippets.commands.snippets.ListSnippets;
import com.dry_code_snippets.commands.snippets.GetSnippet;
import com.dry_code_snippets.commands.versions.AddVersion;
import com.dry_code_snippets.commands.versions.GetVersion;
import com.dry_code_snippets.commands.versions.ListVersions;
import picocli.CommandLine.Command;

@Command(
        name = "dry",
        description = "A CLI tool for the dry-code-snippets API",
        mixinStandardHelpOptions = true,
        subcommands = { Login.class,
                AddSnippet.class,
                ListSnippets.class,
                GetSnippet.class,
                DeleteSnippet.class,
                ListVersions.class,
                GetVersion.class,
                AddVersion.class,
                ListComments.class,
                AddComment.class,
                AddRating.class}
)
public class Cli implements Runnable {
    public void run() {
    }
}

