package com.dry_code_snippets.util;

import picocli.CommandLine;

import java.util.Scanner;

import static com.dry_code_snippets.util.OutputHelper.*;
import static com.dry_code_snippets.util.OutputHelper.changeTextGreen;

public class InputHelper {

    public static void runCommandScanner(CommandLine cli) {

        Scanner scanner = new Scanner(System.in);

        changeTextYellow();
        System.out.println("╔═══╦═══╦╗──╔╗\n╚╗╔╗║╔═╗║╚╗╔╝║\n─║║║║╚═╝╠╗╚╝╔╝\n─║║║║╔╗╔╝╚╗╔╝\n╔╝╚╝║║║╚╗─║║\n╚═══╩╝╚═╝─╚╝");
        System.out.println("Code Snippets CLI - Type '-help' for help and '-exit' to quit.");
        changeTextGreen();
        System.out.print(" > ");

        while (scanner.hasNextLine()) {

            String input = scanner.nextLine().trim();

            if ("-exit".equalsIgnoreCase(input)) {
                System.out.println("Exiting...");
                break;
            }

            if (!input.isEmpty()) {
                changeTextYellow();
                cli.execute(input.split("\\s+"));
            }

            changeTextGreen();
            System.out.print("\r > ");
        }

        scanner.close();

    }

    public static String singleLineInput(String message, int maxLength, boolean allowEmpty, boolean integerOnly ) {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (input.isBlank()) {
            changeTextYellow();
            System.out.println(message + " - Press enter to submit");
            changeTextWhite();
            input = scanner.nextLine();
            if (input.trim().length() > maxLength) {
                input = "";
                changeTextRed();
                System.out.println("ERROR: Input has exceeded the maximum allowed length (" + maxLength + " characters)");
            } else if (input.isBlank()) {
                if (allowEmpty) break;
                input = "";
                changeTextRed();
                System.out.println("ERROR: Input is required");
            } else if (integerOnly){
                try {
                    Integer.parseInt(input.trim());
                } catch (NumberFormatException e) {
                    input = "";
                    changeTextRed();
                    System.out.println("ERROR: Input must be an integer");
                }

            }
        }
        return input.trim();
    }

    public static String multiLineInput(String message, int maxLength, boolean allowEmpty) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder input = new StringBuilder();

        while (input.toString().isBlank()) {
            changeTextYellow();
            System.out.println(message + " - Type '$DONE' on a new line to finish:");
            changeTextWhite();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.toUpperCase().startsWith("$DONE")) break;
                input.append(line).append("\n");
            }

            String inputString = input.toString();
            if (inputString.trim().length() > maxLength) {
                input.setLength(0);
                changeTextRed();
                System.out.println("ERROR: Input has exceeded the maximum allowed length (" + maxLength + " characters)");
            } else if (inputString.isBlank()) {
                if (allowEmpty) break;
                input.setLength(0);
                changeTextRed();
                System.out.println("ERROR: Input is required");
            }
        }
        return input.toString().trim();
    }

}
