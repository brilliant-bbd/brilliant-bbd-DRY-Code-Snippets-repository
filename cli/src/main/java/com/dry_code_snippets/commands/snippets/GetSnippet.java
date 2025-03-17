package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@Command(name = "get-snippet", description = "Displays a code snippet")
public class GetSnippet implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to view", 19, false, true);

        String response = RequestHandler.getRequest("/api/snippets/" + snippetId, "");
        debugPrint("RESPONSE: " + response);
    }
}
