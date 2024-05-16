package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BiciDAOImpl implements DAO<Bici> {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/xe";
    private static final String DB_USER = "C##HR";
    private static final String DB_PASSWORD = "HR";

    @Override
    public Bici get(Long id) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Bici bici = null;

        try {
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            st = con.prepareStatement("SELECT * FROM Bicicletes WHERE bici_id = ?");
            st.setLong(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                bici = new Bici(
                        rs.getString("marca"),
                        rs.getString("model"),
                        rs.getInt("any_fabricacio"),
                        rs.getDouble("pes"),
                        Bici.TipoBici.valueOf(rs.getString("tipus")),
                        Bici.Carboni.valueOf(rs.getString("carboni")),
                        getPropietari(rs.getLong("propietari_id"), con)
                );
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }
        }
        return bici;
    }

    @Override
    public List<Bici> getAll() throws DAOException {
        List<Bici> bicicletes = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement("SELECT * FROM Bicicletes");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Bici bici = new Bici(
                        rs.getString("marca"),
                        rs.getString("model"),
                        rs.getInt("any_fabricacio"),
                        rs.getDouble("pes"),
                        Bici.TipoBici.valueOf(rs.getString("tipus")),
                        Bici.Carboni.valueOf(rs.getString("carboni")),
                        getPropietari(rs.getLong("propietari_id"), con)
                );
                bici.setId(rs.getLong("bici_id"));
                bicicletes.add(bici);
            }
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
        return bicicletes;
    }


    @Override
    public void save(Bici bici) throws DAOException {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement(
                     "INSERT INTO Bicicletes (marca, model, any_fabricacio, pes, tipus, carboni, propietari_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                     new String[] { "bici_id" })) {

            st.setString(1, bici.getMarca());
            st.setString(2, bici.getModelBici());
            st.setInt(3, bici.getAnyFabricacio());
            st.setDouble(4, bici.getPes());
            st.setString(5, bici.getTipo().name());
            st.setString(6, bici.getCarboni().name());
            st.setLong(7, bici.getPropietari().getId());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException(1, "No rows affected.");
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bici.setId(generatedKeys.getLong(1));
                } else {
                    throw new DAOException(1, "Failed to retrieve generated key.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException(1, e.getMessage());
        }
    }


    @Override
    public void update(Bici bici) throws DAOException {
        if (bici == null || bici.getId() == null) {
            throw new DAOException(1, "La bici o el seu ID és null");
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement(
                     "UPDATE Bicicletes SET marca = ?, model = ?, any_fabricacio = ?, pes = ?, tipus = ?, carboni = ?, propietari_id = ? WHERE bici_id = ?")) {

            st.setString(1, bici.getMarca());
            st.setString(2, bici.getModelBici());
            st.setInt(3, bici.getAnyFabricacio());
            st.setDouble(4, bici.getPes());
            st.setString(5, bici.getTipo().name());
            st.setString(6, bici.getCarboni().name());
            st.setLong(7, bici.getPropietari().getId());
            st.setLong(8, bici.getId());

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
            throw new DAOException(1, "L'ID de la bici és null.");
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement("DELETE FROM Bicicletes WHERE bici_id = ?")) {

            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
    }

    public void delete(Bici bici) throws DAOException {
        if (bici == null) {
            throw new DAOException(1, "La bici és null.");
        }
        delete(bici.getId());
    }

    private Propietari getPropietari(Long propietariId, Connection con) throws SQLException {
        if (propietariId == null) return null;
        Propietari propietari = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM Propietaris WHERE propietari_id = ?")) {
            st.setLong(1, propietariId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    propietari = new Propietari(
                            rs.getString("nom"),
                            rs.getString("cognoms"),
                            rs.getString("telefon"),
                            rs.getString("email")
                    );
                }
            }
        }
        return propietari;
    }
}
