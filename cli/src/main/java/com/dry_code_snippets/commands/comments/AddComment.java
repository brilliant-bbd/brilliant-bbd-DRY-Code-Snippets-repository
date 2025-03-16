package com.dry_code_snippets.commands.comments;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;

@Command(name = "add-comment", description = "Comment on a code snippet")
public class AddComment implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to comment on", 19, false, true);
        String comment = multiLineInput("Enter the comment", 256, false);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("comment", comment);

        String queryParams = addQueryParam("", "snippetId", snippetId);

        debugPrint("JSON BODY: " + jsonBody);
        String response = RequestHandler.postRequest("/api/snippets/comments", queryParams, jsonBody.toString());
        debugPrint("RESPONSE: " + response);
    }
}
