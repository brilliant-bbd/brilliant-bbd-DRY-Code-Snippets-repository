package com.dry_code_snippets.commands.comments;

import com.dry_code_snippets.models.Comment;
import com.dry_code_snippets.util.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;
import java.util.Arrays;

import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "list-snippet-comments", description = "Lists all comments of a snippet")
public class ListComments implements Runnable {

    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want the comments of", 19, false, true);
        String queryParams = addQueryParam("", "snippetId", snippetId);
        HttpResponse<String> response = RequestHandler.getRequest("/api/snippets/comments", queryParams);
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
                Comment[] comments = objectMapper.readValue(response.body(), Comment[].class);

                if (comments.length == 0) {
                    cliPrint("There are no comments for that snippet yet");
                    return;
                }

                printWrapperTop("COMMENTS");
                Arrays.stream(comments).forEach(comment -> cliPrint(comment.toString()));
                printWrapperBottom();
            } catch (JsonProcessingException e) {
                cliPrintError("ERROR: Problem parsing response");
            }
        }

    }

}
