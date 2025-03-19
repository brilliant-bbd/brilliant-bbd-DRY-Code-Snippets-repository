package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;
import java.net.http.HttpResponse;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.OutputHelper.printWrapperBottom;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "explain-snippet", description = "Explains code snippet using AI")
public class ExplainSnippet implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to an explanation of", 19, false, true);

        HttpResponse<String> response = RequestHandler.getRequest("/api/snippets/" + snippetId + "/explain", "");
        handleResponse(response);
    }

    private void handleResponse(HttpResponse<String> response) {
        debugPrint("RESPONSE: " + response);

        if (response == null) {

            cliPrintError("ERROR: request failed");

        } else if (checkValidResponse(response)) {

            debugPrint(response.body());
            printWrapperTop("SNIPPET EXPLANATION");
            cliPrint("\n" + response.body() + "\n");
            printWrapperBottom();

        }
    }
}
