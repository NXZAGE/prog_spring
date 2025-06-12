package com.itmo.nxzage.server.logging;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerLogger {
    private static final Map<String, Logger> loggers = new HashMap<>();
    private static final boolean ENABLE_CONSOLE = Boolean.parseBoolean(System.getProperty("log.console", "true"));
    private static final String LOG_DIR = "./logs";
    private static final Level DEFAULT_LEVEL = Level.INFO;

    public static Logger getLogger(String moduleName) {
        if (loggers.containsKey(moduleName)) {
            return loggers.get(moduleName);
        }

        Logger logger = Logger.getLogger(moduleName);
        logger.setUseParentHandlers(false);
        logger.setLevel(DEFAULT_LEVEL);

        try {
            FileHandler fileHandler = new FileHandler(LOG_DIR + "/" + moduleName.toLowerCase() + ".log", true);
            fileHandler.setEncoding("UTF-8");
            fileHandler.setFormatter(new ModuleFormatter(moduleName));
            logger.addHandler(fileHandler);

            if (ENABLE_CONSOLE) {
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(new ColoredConsoleFormatter(moduleName));
                consoleHandler.setLevel(DEFAULT_LEVEL);
                logger.addHandler(consoleHandler);
            }

        } catch (IOException e) {
            System.err.println("Could not initialize logger for " + moduleName + ": " + e.getMessage() + ";" + System.getProperty("user.dir"));
        }

        loggers.put(moduleName, logger);
        return logger;
    }
}

