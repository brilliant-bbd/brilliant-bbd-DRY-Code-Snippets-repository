package com.dry_code_snippets;

import picocli.CommandLine;
import com.dry_code_snippets.util.Cli;

import java.util.Scanner;

import static com.dry_code_snippets.util.OutputHelper.changeTextGreen;
import static com.dry_code_snippets.util.OutputHelper.changeTextWhite;

public class Main {
    public static void main(String[] args) {

        CommandLine cli = new CommandLine(new Cli());
        Scanner scanner = new Scanner(System.in);

        System.out.println("Google CLI - Type '-help' for help and '-exit' to quit.");
        changeTextGreen();
        System.out.print(" > ");

        while (scanner.hasNextLine()) {

            String input = scanner.nextLine().trim();

            if ("-exit".equalsIgnoreCase(input)) {
                System.out.println("Exiting...");
                break;
            }

            if (!input.isEmpty()) {
                changeTextWhite();
                cli.execute(input.split("\\s+"));
            }

            changeTextGreen();
            System.out.print("\r > ");
        }

        scanner.close();
    }
}