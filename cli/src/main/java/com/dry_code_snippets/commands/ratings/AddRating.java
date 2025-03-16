package com.dry_code_snippets.commands.ratings;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.cliPrintError;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;

@Command(name = "add-rating", description = "Rate a code snippet")
public class AddRating implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to rate ", 19, false, true);
        String rating = getValidRating();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("rating", rating);

        String queryParams = addQueryParam("", "snippetId", snippetId);

        debugPrint("JSON BODY: " + jsonBody);
        String response = RequestHandler.postRequest("/api/snippets/rating", queryParams, jsonBody.toString());
        debugPrint("RESPONSE: " + response);
    }

    private static String getValidRating() {
        String rating = "";
        while (true){
            rating = singleLineInput("Enter the rating (Integer between 0 and 10)", 256, false, true);
            try {
                int intRating = Integer.parseInt(rating);
                if (intRating >= 0 && intRating <= 10) {
                    return rating;
                } else {
                    cliPrintError("ERROR: The rating must be an integer between 0 and 10");
                }
            } catch (NumberFormatException e) {
                cliPrintError("ERROR: The rating must be an integer between 0 and 10");
            }
        }
    }
}
