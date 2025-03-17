package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@Command(name = "delete-snippet", description = "Delete a snippet")
public class DeleteSnippet implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to delete", 19, false, true);

        String response = RequestHandler.deleteRequest("/api/snippets/" + snippetId, "");
        debugPrint("RESPONSE: " + response);
    }
}
