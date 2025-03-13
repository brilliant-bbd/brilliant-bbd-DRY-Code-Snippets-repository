package com.dry_code_snippets.util;

public class OutputHelper {

    public static void cliPrint(String text) {
        changeTextWhite();
        System.out.println("\r" + text);
        changeTextGreen();
        System.out.print("\r > ");
    }

    public static void cliPrintError(String text) {
        changeTextRed();
        System.out.println("\r" + text);
        changeTextGreen();
        System.out.print("\r > ");
    }

    public static void changeTextGreen() {
        System.out.print("\u001B[32m");
    }

    public static void changeTextWhite() {
        System.out.print("\u001B[0m");
    }

    public static void changeTextRed() {
        System.out.print("\u001B[31m");
    }

}
