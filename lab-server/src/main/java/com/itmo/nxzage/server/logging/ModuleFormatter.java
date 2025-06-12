package com.itmo.nxzage.server.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ModuleFormatter extends Formatter {
    private final String module;

    public ModuleFormatter(String module) {
        this.module = module;
    }

    @Override
    public String format(LogRecord record) {
        return String.format("[%s] %s - %s%n", record.getLevel(), module, formatMessage(record));
    }

}
