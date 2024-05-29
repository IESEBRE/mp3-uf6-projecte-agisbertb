package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Bici;
import org.example.model.entities.Revisio;
import org.example.model.exceptions.DAOException;
import org.example.utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementació de l'interfície DAO per a objectes Revisio.
 * Proporciona la gestió de persistència per a objectes Revisio a la base de dades.
 */

public class RevisioDAOImpl implements DAO<Revisio> {

    /**
     * Recupera una revisió pel seu ID.
     *
     * @param id El ID de la revisió a recuperar.
     * @return La revisió si es troba, o null si no es troba.
     * @throws DAOException si ocorre un error de base de dades.
     */

    @Override
    public Revisio get(Long id) throws DAOException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Revisio revisio = null;

        try {
            con = DBUtil.getConnection();
            st = con.prepareStatement("SELECT * FROM Revisions WHERE revisio_id = ?");
            st.setLong(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Bici bici = getBici(rs.getLong("bici_id"), con);
                revisio = new Revisio(
                        rs.getDate("data").toString(),
                        rs.getString("descripcio"),
                        rs.getDouble("preu"),
                        bici
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

    /**
     * Recupera totes les revisions de la base de dades.
     *
     * @return Una llista de totes les revisions.
     * @throws DAOException si ocorre un error de base de dades.
     */

    @Override
    public List<Revisio> getAll() throws DAOException {
        List<Revisio> revisions = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement("SELECT * FROM Revisions");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Bici bici = getBici(rs.getLong("bici_id"), con);
                Revisio revisio = new Revisio(
                        rs.getDate("data").toString(),
                        rs.getString("descripcio"),
                        rs.getDouble("preu"),
                        bici
                );
                revisio.setId(rs.getLong("revisio_id"));
                revisions.add(revisio);
            }
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
        return revisions;
    }

    /**
     * Guarda una nova revisió a la base de dades.
     *
     * @param revisio La revisió a guardar.
     * @throws DAOException si ocorre un error de base de dades o si no es pot guardar.
     */

    @Override
    public void save(Revisio revisio) throws DAOException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement(
                     "INSERT INTO Revisions (data, descripcio, preu, bici_id) VALUES (?, ?, ?, ?)",
                     new String[] { "revisio_id" })) {

            st.setDate(1, Date.valueOf(revisio.getData()));
            st.setString(2, revisio.getDescripcio());
            st.setDouble(3, revisio.getPreu());
            st.setLong(4, revisio.getBici().getId());

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

    /**
     * Actualitza una revisió existent a la base de dades.
     *
     * @param revisio La revisió a actualitzar.
     * @throws DAOException si la revisió és null, el seu ID és null, o si ocorre un error de base de dades.
     */

    @Override
    public void update(Revisio revisio) throws DAOException {
        if (revisio == null || revisio.getId() == null) {
            throw new DAOException(1, "La revisió o el seu ID és null");
        }

        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement(
                     "UPDATE Revisions SET data = ?, descripcio = ?, preu = ?, bici_id = ? WHERE revisio_id = ?")) {

            st.setDate(1, Date.valueOf(revisio.getData()));
            st.setString(2, revisio.getDescripcio());
            st.setDouble(3, revisio.getPreu());
            st.setLong(4, revisio.getBici().getId());
            st.setLong(5, revisio.getId());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException(1, "No s'ha actualitzat cap fila");
            }
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
    }

    /**
     * Elimina una revisió de la base de dades pel seu ID.
     *
     * @param id El ID de la revisió a eliminar.
     * @throws DAOException si l'ID és null o si ocorre un error de base de dades.
     */

    @Override
    public void delete(Long id) throws DAOException {
        if (id == null) {
            throw new DAOException(1, "L'ID de la revisió és null.");
        }

        try (Connection con = DBUtil.getConnection();
             PreparedStatement st = con.prepareStatement("DELETE FROM Revisions WHERE revisio_id = ?")) {

            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException throwables) {
            throw new DAOException(1, throwables.getMessage());
        }
    }

    /**
     * Recupera una bicicleta pel seu ID utilitzant una connexió existent.
     *
     * @param biciId El ID de la bicicleta a recuperar.
     * @param con La connexió de base de dades activa.
     * @return La bicicleta si es troba, o null si no es troba.
     * @throws SQLException si ocorre un error de base de dades durant la consulta.
     */

    private Bici getBici(Long biciId, Connection con) throws SQLException {
        if (biciId == null) return null;
        Bici bici = null;
        try (PreparedStatement st = con.prepareStatement("SELECT * FROM Bicicletes WHERE bici_id = ?")) {
            st.setLong(1, biciId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    bici = new Bici(
                            rs.getString("marca"),
                            rs.getString("model"),
                            rs.getInt("any_fabricacio"),
                            rs.getDouble("pes"),
                            Bici.TipoBici.valueOf(rs.getString("tipus")),
                            Bici.Carboni.valueOf(rs.getString("carboni")),
                            null
                    );
                    bici.setId(rs.getLong("bici_id"));
                }
            }
        }
        return bici;
    }
}