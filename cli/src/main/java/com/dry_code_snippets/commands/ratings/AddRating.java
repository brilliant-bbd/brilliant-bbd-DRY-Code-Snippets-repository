package com.dry_code_snippets.commands.ratings;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;

@Command(name = "add-rating", description = "Rate a code snippet")
public class AddRating implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to rate ", 10, false, true);
        String rating = singleLineInput("Enter the rating", 256, false, true);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("rating", rating);

        debugPrint("JSON BODY: " + jsonBody);
        String response = RequestHandler.postRequest("/snippets/" + snippetId + "/rating", "", jsonBody.toString());
        debugPrint("RESPONSE: " + response);
    }
}
