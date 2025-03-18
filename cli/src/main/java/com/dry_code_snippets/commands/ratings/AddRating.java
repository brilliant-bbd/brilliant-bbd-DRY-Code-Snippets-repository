package com.dry_code_snippets.commands.ratings;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;

import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "add-rating", description = "Rate a code snippet")
public class AddRating implements Runnable {
    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want to rate ", 19, false, true);
        String rating = getValidRating();

        String queryParams = addQueryParam("", "snippetId", snippetId);

        debugPrint("Rating: " + rating);
        HttpResponse<String> response = RequestHandler.postRequest("/api/snippets/ratings", queryParams, rating);
        handleResponse(response);
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

    private void handleResponse(HttpResponse<String> response) {
        debugPrint("RESPONSE: " + response);

        if (response == null) {
            cliPrintError("ERROR: request failed");
        } else if (checkValidResponse(response)) {
            debugPrint(response.body());
            if (response.statusCode() == 201) {
                cliPrint("Rating added successfully");
            }
        }

    }

}
