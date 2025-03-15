package com.dry_code_snippets.commands.snippets;

import com.dry_code_snippets.util.RequestHandler;
import picocli.CommandLine.Command;
import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.debugPrint;
import static com.dry_code_snippets.util.RequestHandler.addQueryParam;

@Command(name = "list-snippets", description = "Lists all code snippets that match the filters")
public class ListSnippets implements Runnable {
    public void run() {
        String tags = multiLineInput("(Optional) Enter tags to filter by (each tag on its own line)", 2048, true);
        String codingLanguage = singleLineInput("(Optional) Enter the coding language to filter by", 256, true, false);

        String queryParams = "";
        queryParams = addQueryParam(queryParams, "tags", tags.replaceAll("\n", ";"));
        queryParams = addQueryParam(queryParams, "coding_language", codingLanguage);

        String response = RequestHandler.getRequest("/snippets", queryParams);
        debugPrint("RESPONSE: " + response);
    }
}
