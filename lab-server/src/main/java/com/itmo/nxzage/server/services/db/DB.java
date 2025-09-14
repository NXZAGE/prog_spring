package com.itmo.nxzage.server.services.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import com.itmo.nxzage.server.logging.ServerLogger;

public class DB {
    private static Logger logger = ServerLogger.getLogger("DB");
    public static Connection getConnection() throws SQLException {
        try {
            // Get database credentials from DatabaseConfig class
            var jdbcUrl = DatabaseConfig.getDbUrl();
            var user = DatabaseConfig.getDbUsername();
            var password = DatabaseConfig.getDbPassword();
            // Open a connection
            logger.info("DB shared connection.");
            return DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException  e) {
            logger.warning("Failed to share DB connection: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
