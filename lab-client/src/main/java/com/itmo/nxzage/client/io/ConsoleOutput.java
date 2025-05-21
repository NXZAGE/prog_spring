package com.itmo.nxzage.client.io;

import java.util.Collection;

/**
 * Оболочка стандарного вывода
 */
public class ConsoleOutput implements OutputHandler {
    public static class Colors {
        public static final String RESET = "\u001B[0m";
        public static final String BLACK = "\u001B[30m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";

        public static String makeRed(String message) {
            return RED + message + RESET;
        }

        public static String makeCyan(String message) {
            return CYAN + message + RESET;
        }

        public static String makePurple(String message) {
            return PURPLE + message + RESET;
        }

        public static String makeBlue(String message) {
            return BLUE + message + RESET;
        }
    }

    private boolean silenceMode = false;
    private boolean promptsEnabled = true;

    private static String makeItalic(String message) {
        return "\033[3m" + message + "\033[0m";
    }

    private void print(String message) {
        if (silenceMode) {
            return;
        }

        System.out.print(message);
    }


    @Override
    public void printMessage(String message) {
        print(Colors.makeCyan(message));
    }

    @Override
    public void printCollection(Collection<Object> collection) {
        collection.stream().map((elem) -> Colors.makePurple(elem.toString() + "\n"))
                .forEach(this::print);
    }

    @Override
    public void printSpecial(String message) {
        print(Colors.makeBlue(message));
    }

    @Override
    public void printPrompt(String prompt) {
        if (!promptsEnabled) {
            return;
        }

        print(makeItalic(prompt));
    }

    @Override
    public void printError(String errorMessage) {
        print(Colors.makeRed(errorMessage));
    }

    @Override
    public void enablePrompts() {
        promptsEnabled = true;
    }

    @Override
    public void disablePrompts() {
        promptsEnabled = false;
    }

    @Override
    public void mute() {
        silenceMode = true;
    }


    @Override
    public void unmute() {
        silenceMode = false;
    }
}
