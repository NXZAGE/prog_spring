package com.itmo.nxzage.server.services.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import com.itmo.nxzage.server.logging.ServerLogger;

public final class DatabaseConfig {
    private static final Properties properties = new Properties();
    private static final Logger logger = ServerLogger.getLogger("DatabaseConfig");
    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                logger.severe("Sorry, unable to find db.properties");
                System.exit(1);
            }
            // Load the properties file
            properties.load(input); 
            logger.info("Database properties loaded successfully");
        } catch (IOException e) {
            logger.severe("Failed to load prperties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }
    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }
}

