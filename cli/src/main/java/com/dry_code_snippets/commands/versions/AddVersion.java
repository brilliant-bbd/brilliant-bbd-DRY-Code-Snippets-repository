package com.dry_code_snippets.commands.versions;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;

import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.cliPrintError;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "add-version", description = "Adds new version to a snippet")
public class AddVersion implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to add a version to", 19, false, true);
        String code = multiLineInput("Enter the code", 10000, false);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("code", code);

        String queryParams = addQueryParam("", "snippetId", snippetId);

        debugPrint("JSON BODY: " + jsonBody);
        HttpResponse<String> response = RequestHandler.postRequest("/api/snippets/version", queryParams, jsonBody.toString());
        handleResponse(response);
    }

    private void handleResponse(HttpResponse<String> response) {
        debugPrint("RESPONSE: " + response);

        if (response == null) {
            cliPrintError("ERROR: request failed");
        } else if (checkValidResponse(response)) {

        }

    }

}
