package com.dry_code_snippets.commands.versions;

import com.dry_code_snippets.models.Version;
import com.dry_code_snippets.util.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import picocli.CommandLine.Command;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Comparator;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;
import static com.dry_code_snippets.util.RequestHandler.checkValidResponse;

@Command(name = "list-snippet-versions", description = "Lists all versions of the code snippet specified by id")
public class ListVersions implements Runnable {

    public void run() {
        String snippetId = singleLineInput("Enter the id of the snippet you want the versions of", 19, false, true);

        String queryParams = addQueryParam("", "snippetId", snippetId);

        HttpResponse<String> response = RequestHandler.getRequest("/api/snippets/versions", queryParams);
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
                Version[] versions = objectMapper.readValue(response.body(), Version[].class);
                if (versions.length == 0) {
                    cliPrintError("ERROR: There is no snippet with that id");
                    return;
                }

                printWrapperTop("VERSION RANGE");
                Version highestVersion = Arrays.stream(versions).max(Comparator.comparingInt(Version::getVersion)).orElse(null);

                if (highestVersion.getVersion() >= 1) {
                    cliPrint("1.." + highestVersion.getVersion());
                }
                printWrapperBottom();

            } catch (JsonProcessingException e) {
                cliPrintError("ERROR: Problem parsing response");
            }
        }
    }
}
