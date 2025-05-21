package com.itmo.nxzage.client.io;

import java.util.Scanner;

/**
 * Обертка для стандартного ввода
 */
public final class ConsoleInput implements InputSource {
    private Scanner scanner;

    public ConsoleInput() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String nextLine() {
        // TODO разобраться с вылетающими ошибками
        return scanner.nextLine();
    }

    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    @Override
    public boolean isInteractive() {
        return true;
    }

    @Override
    public void close() {
        scanner.close();
    }

    @Override
    public String info() {
        return "Main console input";
    }
}
