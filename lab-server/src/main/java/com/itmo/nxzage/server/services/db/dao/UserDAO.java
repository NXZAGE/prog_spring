package com.itmo.nxzage.server.services.db.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import com.itmo.nxzage.common.util.data.User;

public class UserDAO implements Dao<User> {

    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<User> get(int id) throws SQLException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    public Optional<User> get(String name) throws  SQLException {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<User> getAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer save(User element) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(int id, User element) throws SQLException {
        // TODO Auto-generated method stub
        
    }    
}
