package com.dry_code_snippets.commands.comments;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@Command(name = "list-snippet-comments", description = "Lists all comments of a snippet")
public class ListComments implements Runnable {

    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want the comments of", 10, false, true);
        String response = RequestHandler.getRequest("/snippets/" + snippetId + "/comments", "");
        debugPrint("RESPONSE: " + response);
    }
}
