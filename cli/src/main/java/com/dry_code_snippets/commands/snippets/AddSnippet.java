package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@Command(name = "add-snippet", description = "Adds a new code snippet")
public class AddSnippet implements Runnable {
    public void run() {
        String title = singleLineInput("Enter a title", 256, false, false);
        String codingLanguage = singleLineInput("Enter the coding language used", 256, false, false);
        String tags = multiLineInput("Enter the tags (each tag on its own line)", 2048, false);
        String description = multiLineInput("Enter a description", 2048, false);
        String code = multiLineInput("Enter the code", 4096, false);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("title", title);
        jsonBody.put("coding_language", codingLanguage);
        jsonBody.put("tags", tags.split("\n"));
        jsonBody.put("description", description);
        jsonBody.put("code", code);

        debugPrint("JSON BODY: " + jsonBody);
        String response = RequestHandler.postRequest("/snippets", "", jsonBody.toString());
        debugPrint("RESPONSE: " + response);
    }
}
