package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;

import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.cliPrintError;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "add-snippet", description = "Adds a new code snippet")
public class AddSnippet implements Runnable {
    public void run() {
        String title = singleLineInput("Enter a title", 256, false, false);
        String codingLanguage = singleLineInput("Enter the coding language used", 256, false, false);
        String tags = multiLineInput("Enter the tags (each tag on its own line)", 2048, false);
        String description = multiLineInput("Enter a description", 1000, false);
        String code = multiLineInput("Enter the code", 10000, false);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("title", title);
        jsonBody.put("language", codingLanguage);
        jsonBody.put("tags", tags.split("\n"));
        jsonBody.put("description", description);
        jsonBody.put("code", code);

        debugPrint("JSON BODY: " + jsonBody);
        HttpResponse<String> response = RequestHandler.postRequest("/api/snippets", "", jsonBody.toString());
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
