package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Revisio;
import org.example.model.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RevisioDAOImpl implements DAO<Revisio> {
    @Override
    public Revisio get(Long id) throws DAOException {
        return null;
    }

    @Override
    public List<Revisio> getAll() throws DAOException {
        return List.of();
    }

    @Override
    public void save(Revisio obj) throws DAOException {

    }

    @Override
    public void update(Revisio obj) throws DAOException {

    }

    @Override
    public void delete(Long id) throws DAOException {

    }
    // Implementació dels mètodes CRUD
}
