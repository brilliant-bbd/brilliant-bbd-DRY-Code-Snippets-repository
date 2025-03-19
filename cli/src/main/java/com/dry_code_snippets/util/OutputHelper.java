package com.dry_code_snippets.util;

public class OutputHelper {

    private static final boolean DEBUG = false;

    public static void cliPrint(String text) {
        if (text.startsWith("ERROR: ")) {
            cliPrintError(text);
            return;
        }
        changeTextYellow();
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

    public static void debugPrint(String text) {
        if (DEBUG) {
            changeTextBlue();
            System.out.println("\r" + text);
            changeTextGreen();
            System.out.print("\r > ");
        }
    }

    public static void printWrapperTop(String header) {
        cliPrint("\r----------------------------------------\n" + header + ": \n");
    }

    public static void printWrapperBottom() {
        cliPrint("\r----------------------------------------");
    }

    public static void changeTextGreen() {
        System.out.print("\u001B[32m");
    }

    public static void changeTextWhite() {
        System.out.print("\u001B[0m");
    }

    public static void changeTextYellow() {
        System.out.print("\u001B[33m");
    }

    public static void changeTextRed() {
        System.out.print("\u001B[31m");
    }

    public static void changeTextBlue() {
        System.out.print("\u001B[34m");
    }

}
