package com.dry_code_snippets;

import picocli.CommandLine;
import com.dry_code_snippets.util.Cli;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        CommandLine cli = new CommandLine(new Cli());
        Scanner scanner = new Scanner(System.in);

        System.out.println("Google CLI - Type '-help' for help and '-exit' to quit.");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if ("-exit".equalsIgnoreCase(input)) {
                System.out.println("Exiting...");
                break;
            }

            if (!input.isEmpty()) {
                System.out.println();
                cli.execute(input.split("\\s+"));
                System.out.println();
            }
        }

        scanner.close();
    }
}