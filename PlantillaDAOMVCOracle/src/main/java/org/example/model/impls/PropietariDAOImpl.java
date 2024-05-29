package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;
import org.example.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementació de l'interfície DAO per a objectes Propietari.
 * Proporciona la gestió de persistència per a objectes Propietari a la base de dades.
 */

public class PropietariDAOImpl implements DAO<Propietari> {

    /**
     * Recupera un propietari pel seu ID.
     *
     * @param id El ID del propietari a recuperar.
     * @return El propietari si es troba, o null si no es troba.
     * @throws DAOException si ocorre un error de base de dades.
     */

    @Override
    public Propietari get(Long id) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Propietari propietari = null;

        try {
            con = DBUtil.getConnection();
            st = con.prepareStatement("SELECT * FROM Propietaris WHERE propietari_id = ?");
            st.setLong(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                propietari = new Propietari(
                        rs.getLong("propietari_id"),
                        rs.getString("nom"),
                        rs.getString("cognoms"),
                        rs.getString("telefon"),
                        rs.getString("email")
                );
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
        return propietari;
    }

    /**
     * Recupera tots els propietaris de la base de dades.
     *
     * @return Una llista de tots els propietaris.
     * @throws DAOException si ocorre un error de base de dades.
     */

    @Override
    public List<Propietari> getAll() throws DAOException {
        List<Propietari> propietaris = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM Propietaris");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                propietaris.add(new Propietari(
                        rs.getLong("propietari_id"),
                        rs.getString("nom"),
                        rs.getString("cognoms"),
                        rs.getString("telefon"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
        return propietaris;
    }

    /**
     * Guarda un nou propietari a la base de dades.
     *
     * @param propietari El propietari a guardar.
     * @throws DAOException si ocorre un error de base de dades o si no es pot guardar.
     */

    @Override
    public void save(Propietari propietari) throws DAOException {
        String insertSQL = "INSERT INTO Propietaris (nom, cognoms, telefon, email) VALUES (?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement(insertSQL, new String[] { "propietari_id" })) {

            st.setString(1, propietari.getNom());
            st.setString(2, propietari.getCognoms());
            st.setString(3, propietari.getTelefon());
            st.setString(4, propietari.getEmail());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException(1);
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    propietari.setId(generatedKeys.getLong(1));
                } else {
                    throw new DAOException(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw new DAOException(1, e.getMessage());
        }
    }

    /**
     * Actualitza un propietari existent a la base de dades.
     *
     * @param propietari El propietari a actualitzar.
     * @throws DAOException si el propietari és null, el seu ID és null, o si ocorre un error de base de dades.
     */

    @Override
    public void update(Propietari propietari) throws DAOException {
        String updateSQL = "UPDATE Propietaris SET nom = ?, cognoms = ?, telefon = ?, email = ? WHERE propietari_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement(updateSQL)) {

            st.setString(1, propietari.getNom());
            st.setString(2, propietari.getCognoms());
            st.setString(3, propietari.getTelefon());
            st.setString(4, propietari.getEmail());
            st.setLong(5, propietari.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw new DAOException(1, e.getMessage());
        }
    }

    /**
     * Elimina un propietari de la base de dades pel seu ID.
     *
     * @param id El ID del propietari a eliminar.
     * @throws DAOException si l'ID és null o si ocorre un error de base de dades.
     */

    @Override
    public void delete(Long id) throws DAOException {
        String deleteSQL = "DELETE FROM Propietaris WHERE propietari_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement(deleteSQL)) {

            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw new DAOException(1, e.getMessage());
        }
    }
}