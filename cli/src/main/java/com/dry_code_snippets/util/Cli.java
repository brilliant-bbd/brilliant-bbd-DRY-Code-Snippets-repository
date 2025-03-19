package com.dry_code_snippets.util;

import com.dry_code_snippets.commands.comments.AddComment;
import com.dry_code_snippets.commands.comments.ListComments;
import com.dry_code_snippets.commands.ratings.AddRating;
import com.dry_code_snippets.commands.ratings.GetRatings;
import com.dry_code_snippets.commands.snippets.*;
import com.dry_code_snippets.commands.Login;
import com.dry_code_snippets.commands.versions.AddVersion;
import com.dry_code_snippets.commands.versions.GetVersion;
import com.dry_code_snippets.commands.versions.ListVersions;
import picocli.CommandLine.Command;

@Command(
        name = "dry",
        description = "A CLI tool for the dry-code-snippets API",
        mixinStandardHelpOptions = true,
        subcommands = { Login.class,
                ListSnippets.class,
                GetSnippet.class,
                ExplainSnippet.class,
                AddSnippet.class,
                DeleteSnippet.class,
                ListVersions.class,
                GetVersion.class,
                AddVersion.class,
                ListComments.class,
                AddComment.class,
                GetRatings.class,
                AddRating.class,
                }
)
public class Cli implements Runnable {
    public void run() {
    }
}

