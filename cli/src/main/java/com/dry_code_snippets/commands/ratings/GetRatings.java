package com.dry_code_snippets.commands.ratings;

import com.dry_code_snippets.models.Rating;
import com.dry_code_snippets.models.Version;
import com.dry_code_snippets.util.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import picocli.CommandLine.Command;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "get-ratings", description = "Lists all ratings of the snippet")
public class GetRatings implements Runnable {

    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want the ratings of", 19, false, true);

        String queryParams = addQueryParam("", "snippetId", snippetId);

        HttpResponse<String> response = RequestHandler.getRequest("/api/snippets/ratings", queryParams);
        handleResponse(response);
    }

    private void handleResponse(HttpResponse<String> response) {
        debugPrint("RESPONSE: " + response);

        if (response == null) {
            cliPrintError("ERROR: request failed");
        } else if (checkValidResponse(response)) {
            debugPrint(response.body());

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Rating[] ratings = objectMapper.readValue(response.body(), Rating[].class);
                if (ratings.length == 0) {
                    cliPrint("There are no ratings for that snippet yet");
                    return;
                }

                printWrapperTop("RATINGS");

                String result = Arrays.stream(ratings)
                        .map(rating -> String.valueOf(rating.getRating()))
                        .collect(Collectors.joining(","));

                cliPrint(result + " \n");

                printWrapperBottom();

            } catch (JsonProcessingException e) {
                cliPrintError("ERROR: Problem parsing response");
            }
        }
    }
}
