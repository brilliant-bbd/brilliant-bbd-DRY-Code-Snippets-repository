package com.dry_code_snippets.commands.versions;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@Command(name = "list-snippet-versions", description = "Lists all versions of the code snippet specified by id")
public class ListVersions implements Runnable {

    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want the versions of", 10, false, true);
        String response = RequestHandler.getRequest("/snippets/" + snippetId + "/versions", "");
        debugPrint("RESPONSE: " + response);
    }
}
