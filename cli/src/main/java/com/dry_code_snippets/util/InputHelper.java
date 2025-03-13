package com.dry_code_snippets.util;

import java.util.Scanner;

public class InputHelper {

    public static String singleLineInput(String message) {
        System.out.println(message + " - Press enter to submit");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String multiLineInput(String message) {

        System.out.println(message + " - Type '$DONE' on a new line to finish:");

        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("$DONE")) break;
            input.append(line).append("\n");
        }

        return input.toString().trim();
    }

}
