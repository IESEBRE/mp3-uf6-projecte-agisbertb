package org.example.controller;

import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.PropietariDAOImpl;
import org.example.view.Vista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Controlador per a la gestió de propietaris.
 *
 * Aquest controlador s'encarrega de la gestió de propietaris, incloent la creació,
 * modificació i eliminació de propietaris, així com la interacció amb la vista
 * associada i la gestió d'excepcions.
 *
 * @author Andreu Gisbert Bel
 * @version 1.0
 */

public class PropietariController {
    private final PropietariDAOImpl propietariDAO;
    private final Vista view;
    private final PropertyChangeSupport propertyChangeSupport;
    private final ViewController viewController;

    /**
     * Constructor del controlador de propietaris.
     *
     * Inicialitza les dependències necessàries, configura els escoltadors d'esdeveniments
     * i inicialitza la vista.
     *
     * @param propietariDAO Implementació del DAO per a propietaris
     * @param view Vista associada a aquest controlador
     * @param viewController Controlador de la vista principal
     */

    public PropietariController(PropietariDAOImpl propietariDAO, Vista view, ViewController viewController) {
        this.propietariDAO = propietariDAO;
        this.view = view;
        this.viewController = viewController;
        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(new ExceptionController());
        initView();
        initListeners();
    }

    private void setExcepcio(DAOException excepcio) {
        propertyChangeSupport.firePropertyChange(ExceptionController.PROP_EXCEPCIO, null, excepcio);
    }

    /**
     * Inicialitza la vista carregant les dades de propietaris i configurant components.
     */

    private void initView() {
        try {
            List<Propietari> propietaris = propietariDAO.getAll();
            updatePropietariTable(propietaris);
            updatePropietariComboBox(propietaris);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    /**
     * Configura els escoltadors d'esdeveniments per la interfície d'usuari.
     */

    private void initListeners() {
        viewController.addActionListener(e -> {
            if (viewController.modePropietari()) {
                if (e.getSource() == view.getInsertarButton()) inserirPropietari();
                else if (e.getSource() == view.getModificarButton()) modificarPropietari();
                else if (e.getSource() == view.getBorrarButton()) eliminarPropietari();
            }
        });

        viewController.addListSelectionListenerToPropietaris(e -> {
            if (!e.getValueIsAdjusting() && viewController.modePropietari()) {
                mostrarDadesPropietari();
            }
        });
    }

    /**
     * Recupera les dades d'un propietari des de la vista.
     *
     * @return Propietari amb les dades provinents dels camps de la vista o null si hi ha errors
     */

    private Propietari getPropietariDadesVista() {
        try {
            String nom = view.getCampNomPropietari().getText().trim();
            String cognoms = view.getCampCognomsPropietari().getText().trim();
            String telefon = view.getCampTelefonPropietari().getText().trim();
            String email = view.getCampEmailPropietari().getText().trim();

            if (nom.isEmpty() || cognoms.isEmpty() || telefon.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            return new Propietari(nom, cognoms, telefon, email);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al llegir les dades", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Valida les dades d'un propietari abans de la seva inserció o modificació.
     *
     * @param propietari Propietari a validar
     * @return true si el propietari és vàlid, false en cas contrari
     * @throws DAOException si hi ha un error de validació
     */

    private boolean validarPropietari(Propietari propietari) throws DAOException {
        String regexNomCognoms = "^[A-ZÀ-ÚÑÇ][a-zà-úñç]*(\\s+[A-ZÀ-ÚÑÇ][a-zà-úñç]*)*$";
        String regexTelefon = "^[0-9]{9}$";
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";

        if (propietari.getNom() == null || propietari.getNom().trim().isEmpty() || !propietari.getNom().matches(regexNomCognoms)) {
            throw new DAOException(11);
        }
        if (propietari.getCognoms() == null || propietari.getCognoms().trim().isEmpty() || !propietari.getCognoms().matches(regexNomCognoms)) {
            throw new DAOException(12);
        }
        if (propietari.getTelefon() == null || propietari.getTelefon().trim().isEmpty() || !propietari.getTelefon().matches(regexTelefon)) {
            throw new DAOException(13);
        }
        if (propietari.getEmail() == null || propietari.getEmail().trim().isEmpty() || !propietari.getEmail().matches(regexEmail)) {
            throw new DAOException(14);
        }
        return true;
    }

    /**
     * Insereix un nou propietari a la base de dades.
     */

    private void inserirPropietari() {
        try {
            Propietari nouPropietari = getPropietariDadesVista();
            if (nouPropietari == null || !validarPropietari(nouPropietari)) return;

            propietariDAO.save(nouPropietari);
            updatePropietariTable(propietariDAO.getAll());
            updatePropietariComboBox(propietariDAO.getAll());

            JOptionPane.showMessageDialog(view, "Propietari afegit correctament", "Afegir propietari", JOptionPane.INFORMATION_MESSAGE);

            llimpiarDadesPropietari();

        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    /**
     * Modifica un propietari existent.
     */

    private void modificarPropietari() {
        try {
            int fila = view.getTaulaPropietaris().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaPropietaris().getModel();
                Propietari propietari = (Propietari) tableModel.getValueAt(fila, 4);

                Propietari propietariModificat = getPropietariDadesVista();
                if (propietariModificat == null || !validarPropietari(propietariModificat)) return;

                propietari.setNom(propietariModificat.getNom());
                propietari.setCognoms(propietariModificat.getCognoms());
                propietari.setTelefon(propietariModificat.getTelefon());
                propietari.setEmail(propietariModificat.getEmail());

                propietariDAO.update(propietari);
                updatePropietariTable(propietariDAO.getAll());
                updatePropietariComboBox(propietariDAO.getAll());

                JOptionPane.showMessageDialog(view, "Propietari modificat correctament", "Modificar propietari", JOptionPane.INFORMATION_MESSAGE);

                llimpiarDadesPropietari();
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    /**
     * Elimina un propietari de la base de dades.
     */

    private void eliminarPropietari() {
        try {
            int fila = view.getTaulaPropietaris().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaPropietaris().getModel();
                Propietari propietari = (Propietari) tableModel.getValueAt(fila, 4);
                propietariDAO.delete(propietari.getId());

                updatePropietariTable(propietariDAO.getAll());
                updatePropietariComboBox(propietariDAO.getAll());

                JOptionPane.showMessageDialog(view, "Propietari eliminat correctament", "Eliminar propietari", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    /**
     * Actualitza la taula de propietaris amb les dades actuals.
     *
     * @param propietaris Llista de propietaris per mostrar a la taula
     */

    public void updatePropietariTable(List<Propietari> propietaris) {
        DefaultTableModel model = (DefaultTableModel) view.getTaulaPropietaris().getModel();
        model.setRowCount(0);
        for (Propietari propietari : propietaris) {
            model.addRow(new Object[]{propietari.getNom(), propietari.getCognoms(), propietari.getTelefon(), propietari.getEmail(), propietari});
        }
    }

    /**
     * Actualitza la taula de propietaris amb les dades actuals.
     *
     * @param propietaris Llista de propietaris per mostrar a la taula
     */

    public void updatePropietariComboBox(List<Propietari> propietaris) {
        viewController.updatePropietariComboBox(propietaris);
    }

    /**
     * Neteja els camps de dades del propietari a la vista.
     */

    private void llimpiarDadesPropietari() {
        view.getCampNomPropietari().setText("");
        view.getCampCognomsPropietari().setText("");
        view.getCampTelefonPropietari().setText("");
        view.getCampEmailPropietari().setText("");
    }

    /**
     * Mostra les dades d'un propietari seleccionat a la taula.
     */

    private void mostrarDadesPropietari() {
        int fila = view.getTaulaPropietaris().getSelectedRow();
        if (fila != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaPropietaris().getModel();
            Propietari propietari = (Propietari) tableModel.getValueAt(fila, 4);
            view.getCampNomPropietari().setText(propietari.getNom());
            view.getCampCognomsPropietari().setText(propietari.getCognoms());
            view.getCampTelefonPropietari().setText(propietari.getTelefon());
            view.getCampEmailPropietari().setText(propietari.getEmail());
        }
    }
}
