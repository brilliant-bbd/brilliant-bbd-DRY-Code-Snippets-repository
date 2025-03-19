package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.models.Comment;
import com.dry_code_snippets.models.Snippet;
import com.dry_code_snippets.util.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine.Command;

import java.net.http.HttpResponse;
import java.util.Arrays;

import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.OutputHelper.printWrapperBottom;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "list-snippets", description = "Lists all code snippets that match the filters")
public class ListSnippets implements Runnable {
    public void run() {
        String codingLanguage = singleLineInput("(Optional) Enter the coding language to filter by", 256, true, false);
        String tags = multiLineInput("(Optional) Enter tags to filter by (each tag on its own line)", 2048, true);

        String queryParams = addQueryParam("", "tags", tags.replaceAll("\n", ";"));
        queryParams = addQueryParam(queryParams, "language", codingLanguage);

        HttpResponse<String> response = RequestHandler.getRequest("/api/snippets", queryParams);
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

                if (response.statusCode() == 204) {
                    cliPrint("There are no snippets for those filters");
                    return;
                }
                Snippet[] snippets = objectMapper.readValue(response.body(), Snippet[].class);
                if (snippets.length == 0) {
                    cliPrint("There are no snippets for those filters");
                    return;
                }

                printWrapperTop("SNIPPETS");
                Arrays.stream(snippets).forEach(snippet -> cliPrint(snippet.getSimpleSnippet()));
                printWrapperBottom();

            } catch (JsonProcessingException e) {
                cliPrintError("ERROR: Problem parsing response");
            }

        }


    }

}
