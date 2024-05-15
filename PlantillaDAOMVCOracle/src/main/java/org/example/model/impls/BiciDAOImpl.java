package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.model.entities.Revisio;
import org.example.model.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                bici.setRevisions(getRevisions(rs.getLong("bici_id"), con));
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
                bici.setRevisions(getRevisions(rs.getLong("bici_id"), con));
                bicicletes.add(bici);
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
        return bicicletes;
    }

    @Override
    public void save(Bici bici) throws DAOException {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement(
                     "INSERT INTO Bicicletes (marca, model, any_fabricacio, pes, tipus, carboni, propietari_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, bici.getMarca());
            st.setString(2, bici.getModelBici());
            st.setInt(3, bici.getAnyFabricacio());
            st.setDouble(4, bici.getPes());
            st.setString(5, bici.getTipo().name());
            st.setString(6, bici.getCarboni().name());
            st.setLong(7, bici.getPropietari().getId());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException(1);
            }

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long biciId = generatedKeys.getLong(1);
                    saveRevisions(biciId, bici.getRevisions(), con);
                } else {
                    throw new DAOException(1);
                }
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

    @Override
    public void update(Bici bici) throws DAOException {
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

            st.executeUpdate();

            updateRevisions(bici.getId(), bici.getRevisions(), con);
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

    @Override
    public void delete(Long id) throws DAOException {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement st = con.prepareStatement("DELETE FROM Bicicletes WHERE bici_id = ?")) {

            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException throwables) {
            throw new DAOException(1);
        }
    }

    public void delete(Bici bici) throws DAOException {
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

    private Set<Revisio> getRevisions(Long biciId, Connection con) throws SQLException {
//        Set<Revisio> revisions = new HashSet<>();
//        try (PreparedStatement st = con.prepareStatement("SELECT * FROM Revisions WHERE bici_id = ?")) {
//            st.setLong(1, biciId);
//            try (ResultSet rs = st.executeQuery()) {
//                while (rs.next()) {
//                    revisions.add(new Revisio(
//                            rs.getLong("revisio_id"),
//                            rs.getDate("data").toLocalDate(),
//                            rs.getString("descripcio"),
//                            rs.getLong("bici_id")
//                    ));
//                }
//            }
//        }
        return null;
    }

    private void saveRevisions(Long biciId, Set<Revisio> revisions, Connection con) throws SQLException {
        for (Revisio revisio : revisions) {
            try (PreparedStatement st = con.prepareStatement(
                    "INSERT INTO Revisions (data, descripcio, bici_id) VALUES (?, ?, ?)")) {
                st.setDate(1, Date.valueOf(revisio.getData()));
                st.setString(2, revisio.getDescripcio());
                st.setLong(3, biciId);
                st.executeUpdate();
            }
        }
    }

    private void updateRevisions(Long biciId, Set<Revisio> revisions, Connection con) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM Revisions WHERE bici_id = ?")) {
            st.setLong(1, biciId);
            st.executeUpdate();
        }
        saveRevisions(biciId, revisions, con);
    }
}
