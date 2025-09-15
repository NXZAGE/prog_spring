package com.itmo.nxzage.server.services.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import com.itmo.nxzage.common.util.data.User;
import com.itmo.nxzage.server.services.db.models.Credential;

public class AuthModule {
    public boolean isUserExists(String username) {
        try {
            getUser(username);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void registerUser(String username, String passwordhash, String salt) {
        try (var conn = DB.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                int userId = insertUser(conn, username);
                insertCredentials(conn, userId, passwordhash, salt);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }

    public Credential getUserCredentials(String username) {
        String sql = """
            SELECT 
                credentials.user_id AS id,
                credentials.password_hash AS passport_hash
                credentials.salt AS salt
            FROM
                credentials
            JOIN
                users
            ON
                users.id=credentials.user_id
            WHERE
                users.name=?

        """;
        try (var conn = DB.getConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Credential(rs.getInt("id"), rs.getString("password_hash"), rs.getString("salt"));
                } else {
                    throw new NoSuchElementException("There is no credential data for user with username=%s".formatted(username));
                }
            }
        } catch (SQLException e) {
            throw new NoSuchElementException("There is no credential data for user with username=%s".formatted(username));
        }
    }

    public Credential getUserCredentials(Integer id) {
        String sql = """
            SELECT
                user_id AS id,
                password_hash,
                salt        
            FROM
                credentials
            WHERE
                user_id=?
        """;
        try (var conn = DB.getConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Credential(rs.getInt("id"), rs.getString("password_hash"), rs.getString("salt"));
                } else {
                    throw new NoSuchElementException("There is no credential data for user with id=%d".formatted(id));
                }
            }
        } catch (SQLException e) {
            throw new NoSuchElementException("There is no credential data for user with id=%d".formatted(id));
        }
    }

    public User getUser(Integer id) {
        String sql = """
            SELECT
                id,
                username
            FROM
                users
            WHERE
                id=?
        """;
        try (var conn = DB.getConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"));
                } else {
                    throw new NoSuchElementException("There is no user with id=%d".formatted(id));
                }
            }
        } catch (SQLException e) {
            throw new NoSuchElementException("There is no user with id=%d".formatted(id));
        }
    }

    public User getUser(String username) {
        String sql = """
            SELECT
                id,
                username
            FROM
                users
            WHERE
                username=?
        """;
        try (var conn = DB.getConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"));
                } else {
                    throw new NoSuchElementException("There is no user with username=%s".formatted(username));
                }
            }
        } catch (SQLException e) {
            throw new NoSuchElementException("There is no user with username=%s".formatted(username));
        }
    }

    private int insertUser(Connection conn, String username) throws SQLException {
        String sql = """
            INSERT INTO 
            users (username) 
            VALUES (?) 
            RETURNING id
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            throw new SQLException("Не удалось получить ID пользователя");
        }
    }

    private void insertCredentials(Connection conn, int userId, String passwordHash, String salt) throws SQLException {
        String sql = """
            INSERT INTO 
            credentials (user_id, password_hash, salt) 
            VALUES 
            (?, ?, ?)
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, passwordHash);
            stmt.setString(3, salt);
            stmt.executeUpdate();
        }
    }
}
