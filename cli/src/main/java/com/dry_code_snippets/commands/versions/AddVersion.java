package com.dry_code_snippets.commands.versions;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@Command(name = "add-version", description = "Adds new version to a snippet")
public class AddVersion implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to add a version to", 10, false, true);
        String code = multiLineInput("Enter the code", 4096, false);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("code", code);

        debugPrint("JSON BODY: " + jsonBody);
        String response = RequestHandler.postRequest("/snippets/" + snippetId + "/version", "", jsonBody.toString());
        debugPrint("RESPONSE: " + response);
    }
}
