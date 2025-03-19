package com.dry_code_snippets.commands.comments;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;

import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "add-comment", description = "Comment on a code snippet")
public class AddComment implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to comment on", 19, false, true);
        String comment = multiLineInput("Enter the comment", 256, false);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("comment", comment);

        String queryParams = addQueryParam("", "snippetId", snippetId);

        debugPrint("JSON BODY: " + jsonBody);
        HttpResponse<String> response = RequestHandler.postRequest("/api/snippets/comments", queryParams, jsonBody.toString());
        handleResponse(response);
    }

    private void handleResponse(HttpResponse<String> response) {
        debugPrint("RESPONSE: " + response);

        if (response == null) {
            cliPrintError("ERROR: request failed");
        } else if (checkValidResponse(response)) {
            debugPrint(response.body());
            if (response.statusCode() == 200) {
                cliPrint("Comment added successfully");
            }
        }

    }
}
