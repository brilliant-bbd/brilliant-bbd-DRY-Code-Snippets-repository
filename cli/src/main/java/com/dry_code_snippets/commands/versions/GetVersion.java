package com.dry_code_snippets.commands.versions;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;

import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.cliPrintError;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "get-version", description = "Displays a version of a snippet snippet")
public class GetVersion implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to view a version of", 19, false, true);
        String version = singleLineInput("Enter the version of the snippet you want to view", 10, false, true);

        String queryParams = addQueryParam("", "snippetId", snippetId);
        queryParams = addQueryParam(queryParams, "version", version);

        HttpResponse<String> response = RequestHandler.getRequest("/api/snippets/versions/byVersion", queryParams);
        handleResponse(response);
    }

    private void handleResponse(HttpResponse<String> response) {
        debugPrint("RESPONSE: " + response);

        if (response == null) {
            cliPrintError("ERROR: request failed");
        } else if (checkValidResponse(response)) {
            debugPrint(response.body());
        }

    }

}
