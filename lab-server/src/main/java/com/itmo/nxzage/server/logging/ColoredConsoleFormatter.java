package com.itmo.nxzage.server.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ColoredConsoleFormatter extends Formatter {
    private final String module;

    public ColoredConsoleFormatter(String module) {
            this.module = module;
        }

    @Override
    public String format(LogRecord record) {
        String color = switch (record.getLevel().getName()) {
            case "SEVERE" -> "\u001B[31m"; // red
            case "WARNING" -> "\u001B[33m"; // yellow
            case "INFO" -> "\u001B[32m"; // green
            default -> "\u001B[37m"; // white
        };
        String reset = "\u001B[0m";
        return String.format("%s[%s] %s - %s%s%n", color, record.getLevel(), module,
                formatMessage(record), reset);
    }
}

