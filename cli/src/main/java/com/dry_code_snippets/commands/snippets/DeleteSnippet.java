package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;

import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "delete-snippet", description = "Delete a snippet")
public class DeleteSnippet implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to delete", 19, false, true);

        HttpResponse<String> response = RequestHandler.deleteRequest("/api/snippets/" + snippetId, "");
        handleResponse(response);
    }

    private void handleResponse(HttpResponse<String> response) {
        debugPrint("RESPONSE: " + response);

        if (response == null) {
            cliPrintError("ERROR: request failed");
        } else if (checkValidResponse(response)) {
            debugPrint(response.body());
            if (response.statusCode() == 204) {
                cliPrint("Snippet deleted successfully");
            }
        }

    }

}
