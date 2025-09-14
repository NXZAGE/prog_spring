package com.itmo.nxzage.server.services.db.dao;

import java.lang.reflect.AccessFlag.Location;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.services.db.DB;

public final class LocationDao implements Dao<Location> {
    private final Logger logger = ServerLogger.getLogger("LocationDAO");
    @Override
    public Optional<Location> get(int id) {
        String querry = """
                SELECT x, y, z
                FROM location
                WHERE id=?
                """;
        try (var conn = DB.getConnection();
             var stmt = conn.prepareStatement(querry);
        ) {
            stmt.setInt(1, id);
            try (ResultSet result = stmt.executeQuery()) {
                
            }
        } catch (SQLException e) {
            logger.info("Failed to get: " + e.getMessage());
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Location> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer save(Location element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(int id, Location element) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
