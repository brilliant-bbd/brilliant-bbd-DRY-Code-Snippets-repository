package com.dry_code_snippets.util;

import java.util.Scanner;

import static com.dry_code_snippets.util.OutputHelper.changeTextWhite;
import static com.dry_code_snippets.util.OutputHelper.changeTextYellow;

public class InputHelper {

    public static String singleLineInput(String message) {
        System.out.println(message + " - Press enter to submit");
        changeTextWhite();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        changeTextYellow();
        return input;
    }

    public static String multiLineInput(String message) {

        System.out.println(message + " - Type '$DONE' on a new line to finish:");
        changeTextWhite();

        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("$DONE")) break;
            input.append(line).append("\n");
        }
        
        changeTextYellow();
        return input.toString().trim();
    }

}
