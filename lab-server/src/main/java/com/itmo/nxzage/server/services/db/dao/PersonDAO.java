package com.itmo.nxzage.server.services.db.dao;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.data.Coordinates;
import com.itmo.nxzage.common.util.data.Country;
import com.itmo.nxzage.common.util.data.Location;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.data.User;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.services.db.DB;

public final class PersonDAO implements Dao<Person> {
    private final Logger logger = ServerLogger.getLogger("PersonDAO");
    public Person mapRow(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Date creationDate = rs.getTimestamp("creation_date");
        String name = rs.getString("name");
        var coordinates = new Coordinates(
            rs.getDouble("coordinates_x"), 
            rs.getFloat("coordinates_y"));
        Float height = rs.getFloat("height");
        Long weight = rs.getLong("weight");
        String passport = rs.getString("passport");
        var nationality = Country.valueOf(rs.getString("nationality"));
        var location = new Location(
            rs.getFloat("location_x"),
            rs.getInt("location_y"),
            rs.getLong("location_z"),
            rs.getString("location_name")
        );
        var owner = new User(rs.getInt("owner_id"), rs.getString("owner_name"));
        Person person = new Person(name, coordinates, height, weight, passport, nationality, location);
        person.setID(id);
        person.setCreationDate(creationDate);
        person.setOwner(owner);
        return person;
    }

    @Override
    public Optional<Person> get(int id) throws SQLException {
        String sql = """
                SELECT 
                    person.id AS id,
                    person.creation_date AS creation_date,
                    person.name AS name,
                    coordinates.x AS coordinates_x,
                    coordinates.y AS coordinates_y,
                    person.height AS height,
                    person.weight AS weight,
                    person.passport AS passport,
                    person.nationality AS nationality,
                    location.x AS location_x,
                    location.y AS location_y,
                    location.z AS location_z,
                    location.name AS location_name,
                    users.id AS owner_id,
                    users.username AS owner_name
                FROM person
                JOIN coordinates ON person.coordinates_id=coordinates.id
                JOIN location ON person.location_id=location.id
                JOIN users ON person.owner_id=users.id
                WHERE person.id=?
                """;

            try (Connection conn = DB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
                stmt.setInt(1, id);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Person element = mapRow(rs);
                        logger.info("Element with id=%d was found: %s".formatted(id, element.toString()));
                        return Optional.of(mapRow(rs));
                    } else {
                        logger.info("Element with id=%d was not found.".formatted(id));
                        return Optional.empty();
                    }
                }
            } catch (SQLException e) {
                logger.severe("[Get by id querry] was not successful: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
    }

    @Override
    public List<Person> getAll() throws SQLException {
        String sql = """
                SELECT 
                    person.id AS id,
                    person.creation_date AS creation_date,
                    person.name AS name,
                    coordinates.x AS coordinates_x,
                    coordinates.y AS coordinates_y,
                    person.height AS height,
                    person.weight AS weight,
                    person.passport AS passport,
                    person.nationality AS nationality,
                    location.x AS location_x,
                    location.y AS location_y,
                    location.z AS location_z,
                    location.name AS location_name,
                    users.id AS owner_id,
                    users.username AS owner_name
                FROM person
                JOIN users ON person.owner_id=users.id
                JOIN coordinates ON person.coordinates_id=coordinates.id
                JOIN location ON person.location_id=location.id
                """;
        try (var conn = DB.getConnection();
            var stmt = conn.prepareStatement(sql);) {
            try (ResultSet rs = stmt.executeQuery();) {
                List<Person> elements = new ArrayList<>();
                while (rs.next()) {
                    elements.add(mapRow(rs));
                }   
                logger.info("%d elements loaded from DB".formatted(elements.size()));
                return elements;
            }
        } catch (SQLException e) {
            logger.warning("SQL Excpetion duaring getting elements from DB");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Integer save(Person element) {
        try (var conn = DB.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                int coordinates_id = saveCoordinates(conn, element.getCoordinates());
                int location_id = saveLocation(conn, element.getLocation());
                int newID = savePerson(conn, element, coordinates_id, location_id);
                conn.commit();
                return Integer.valueOf(newID);
            } catch (SQLException e) {
                logger.warning("Transaction failed: SQlException occured: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException e) {
            logger.warning("Save operation failed: SQLException occured: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int id, Person element) {
        try (var conn = DB.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // TODO перепеисать на updateCoordinates updateLocation
            try {
                int coordinatesId = saveCoordinates(conn, element.getCoordinates());
                int locationId = saveLocation(conn, element.getLocation());
                updatePerson(conn, id, element, coordinatesId, locationId);
            } catch (SQLException e) {
                logger.info("Failed update person: " + e.getMessage());
                throw new RuntimeException(e);   
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException e) {
            logger.warning("Failed to upadte person: SQLExcpetion occured: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = """
            DELETE FROM
                person
            WHERE id=?;
        """;

        try (var conn = DB.getConnection();
            var stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, id);
            int affectedRaws = stmt.executeUpdate();
            if (affectedRaws == 1) {
                logger.info("Person with id=%d successfully deleted.".formatted(id));
            } else if (affectedRaws == 0) {
                logger.info("Deletion failed: no person with id=%d".formatted(id));
            } else {
                logger.severe("Deletion failed: Expected 0 or 1 row affected by deletion, but %d got".formatted(affectedRaws));
                throw new IllegalStateException("Expected 0 or 1 row affected by deletion, but %d got".formatted(affectedRaws));
            }
            return affectedRaws == 1;
        } catch (SQLException e) {
            logger.warning("Deletion failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private int saveCoordinates(Connection conn, Coordinates coordinates) throws SQLException {
        String sql = """
            INSERT INTO
                coordinates(x, y)
            VALUES
                (?, ?)
            RETURNING id;        
        """;
        try (var stmt = conn.prepareStatement(sql);){
            stmt.setDouble(1, coordinates.getX());
            stmt.setFloat(2, coordinates.getY());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new RuntimeException("Failed to save coordinates");
                }
            }
        } catch (SQLException e) {
            logger.warning("Failed to save %s to the DB: %s".formatted(coordinates.toString(), e.getMessage()));
            throw e;
        }
    }

    private int saveLocation(Connection conn, Location location) throws SQLException {
        String sql = """
            INSERT INTO
                location(x, y, z, name)
            VALUES
                (?, ?, ?, ?)
            RETURNING id;        
        """;
        try (var stmt = conn.prepareStatement(sql);){
            stmt.setFloat(1, location.getX());
            stmt.setInt(2, location.getY());
            stmt.setLong(3, location.getZ());
            stmt.setString(4, location.getName());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new RuntimeException("Failed to save location");
                }
            }
        } catch (SQLException e) {
            logger.warning("Failed to save %s to the DB: %s".formatted(location.toString(), e.getMessage()));
            throw e;
        }
    }

    private int savePerson(Connection conn, Person person, int coordintesId, int locationId) throws SQLException {
        String sql = """
            INSERT INTO
                person(
                    owner_id, 
                    name,
                    coordinates_id,
                    height,
                    weight,
                    passport,
                    nationality,
                    location_id
                )
            VALUES
                (?, ?, ?, ?, ?, ?, ?::country, ?)
            RETURNING id;        
        """;
        try (var stmt = conn.prepareStatement(sql);){
            stmt.setInt(1, person.getOwner().getId());
            stmt.setString(2, person.getName());
            stmt.setInt(3, coordintesId);
            stmt.setFloat(4, person.getHeight());
            stmt.setLong(5, person.getWeight());
            stmt.setString(6, person.getPassportID());
            stmt.setString(7, person.getNationality().name());
            stmt.setInt(8, locationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new RuntimeException("Failed to save person");
                }
            }
        } catch (SQLException e) {
            logger.warning("Failed to save person %s to the DB: %s".formatted(person.getName(), e.getMessage()));
            throw e;
        }
    }

    private void updatePerson(Connection conn, int id, Person person, int coordintesId, int locationId) {
        String sql = """
            UPDATE
                person
            SET
                name=?,
                coordinates_id=?,
                height=?,
                weight=?,
                passport=?,
                nationality=?::country,
                location_id=?
            WHERE
                id=?;
        """;
        try (var stmt = conn.prepareStatement(sql);){
            stmt.setString(1, person.getName());
            stmt.setInt(2, coordintesId);
            stmt.setFloat(3, person.getHeight());
            stmt.setLong(4, person.getWeight());
            stmt.setString(5, person.getPassportID());
            stmt.setString(6, person.getNationality().name());
            stmt.setInt(7, locationId);
            stmt.setInt(8, id);
            int affectedRaws = stmt.executeUpdate();
            if (affectedRaws == 1) {
                logger.info("Person with id=%d successfully updated.".formatted(id));
            } else if (affectedRaws == 0) {
                logger.info("Update failed: no person with id=%d".formatted(id));
            } else {
                logger.severe("Update failed: Expected 0 or 1 row affected by update, but %d got".formatted(affectedRaws));
                throw new IllegalStateException("Expected 0 or 1 row affected by update, but %d got".formatted(affectedRaws));
            }
        } catch (SQLException e) {
            logger.warning("Failed to update person with id=%d: %s".formatted(id, e.getMessage()));
            throw new RuntimeException(e);
        }
    }
}
