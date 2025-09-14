package com.itmo.nxzage.server.services.db.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import com.itmo.nxzage.server.services.db.models.Credential;

public class CredentialsDAO implements Dao<Credential> {

    @Override
    public boolean delete(int id) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<Credential> get(int id) throws SQLException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Credential> getAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer save(Credential element) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(int id, Credential element) throws SQLException {
        // TODO Auto-generated method stub
        
    }
    
}
