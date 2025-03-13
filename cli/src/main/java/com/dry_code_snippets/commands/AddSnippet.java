package com.dry_code_snippets.commands;

import com.dry_code_snippets.util.RequestHandler;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.Scanner;

import static com.dry_code_snippets.util.InputHelper.multiLineInput;
import static com.dry_code_snippets.util.InputHelper.singleLineInput;
import static com.dry_code_snippets.util.OutputHelper.cliPrint;

@Command(name = "add-snippet", description = "Adds a new code snippet")
public class AddSnippet implements Runnable {

//    @Parameters(index = "0", description = "The title of the snippet")
//    private String title;
//
//    @Parameters(index = "1", description = "The coding language of the code snippet")
//    private String codingLanguage;

    public void run() {

        String title = singleLineInput("Enter a title for the code snippet");
        String codingLanguage = singleLineInput("Enter the coding language for the code snippet");
        String description = multiLineInput("Enter a description for the code snippet");
        String code = multiLineInput("Paste the code snippet");
        saveSnippet(title, description, code, codingLanguage);

    }

    private void saveSnippet(String title, String description, String code, String codingLanguage) {

        JSONObject json = new JSONObject().put("title", title);
        json.put("description", description);
        json.put("coding_language", codingLanguage);
        json.put("code", code);

//        System.out.println(json);
        String response = RequestHandler.postRequest("/api/data", "", json.toString());
        cliPrint("POST Response: " + response);

    }
}
