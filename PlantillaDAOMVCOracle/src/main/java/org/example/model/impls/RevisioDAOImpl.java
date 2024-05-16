package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Revisio;
import org.example.model.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RevisioDAOImpl implements DAO<Revisio> {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/xe";
    private static final String DB_USER = "C##HR";
    private static final String DB_PASSWORD = "HR";

    @Override
    public Revisio get(Long id) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Revisio revisio = null;

        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            st = con.prepareStatement("SELECT * FROM Revisions WHERE revisio_id = ?");
            st.setLong(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                revisio = new Revisio(
                        rs.getDate("data").toString(),
                        rs.getString("descripcio"),
                        rs.getLong("bici_id")
                );
                revisio.setId(rs.getLong("revisio_id"));
            }
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1, e.getMessage());
            }
        }
        return revisio;
    }

    @Override
    public List<Revisio> getAll() throws DAOException {
        List<Revisio> revisions = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement("SELECT * FROM Revisions");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Revisio revisio = new Revisio(
                        rs.getDate("data").toString(),
                        rs.getString("descripcio"),
                        rs.getLong("bici_id")
                );
                revisio.setId(rs.getLong("revisio_id"));
                revisions.add(revisio);
            }
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
        return revisions;
    }

    @Override
    public void save(Revisio revisio) throws DAOException {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement(
                     "INSERT INTO Revisions (data, descripcio, bici_id) VALUES (?, ?, ?)",
                     new String[] { "revisio_id" })) {

            st.setDate(1, Date.valueOf(revisio.getData()));
            st.setString(2, revisio.getDescripcio());
            st.setLong(3, revisio.getBiciId());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException(1, "No rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    revisio.setId(generatedKeys.getLong(1));
                } else {
                    throw new DAOException(1, "Failed to retrieve generated key.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException(1, e.getMessage());
        }
    }

    @Override
    public void update(Revisio revisio) throws DAOException {
        if (revisio == null || revisio.getId() == null) {
            throw new DAOException(1, "La revisió o el seu ID és null");
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement(
                     "UPDATE Revisions SET data = ?, descripcio = ?, bici_id = ? WHERE revisio_id = ?")) {

            st.setDate(1, Date.valueOf(revisio.getData()));
            st.setString(2, revisio.getDescripcio());
            st.setLong(3, revisio.getBiciId());
            st.setLong(4, revisio.getId());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException(1, "No s'ha actualitzat cap fila");
            }
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws DAOException {
        if (id == null) {
            throw new DAOException(1, "L'ID de la revisió és null.");
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement("DELETE FROM Revisions WHERE revisio_id = ?")) {

            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
    }
}
