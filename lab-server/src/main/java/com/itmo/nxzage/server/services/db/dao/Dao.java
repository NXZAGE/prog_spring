package com.itmo.nxzage.server.services.db.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(int id) throws SQLException;
    List<T> getAll() throws SQLException;
    Integer save(T element) throws SQLException;
    void update(int id, T element) throws SQLException;
    boolean delete(int id) throws SQLException;
} 
