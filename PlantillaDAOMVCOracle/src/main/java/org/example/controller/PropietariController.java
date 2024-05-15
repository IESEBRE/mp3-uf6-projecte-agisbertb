package org.example.controller;

import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.PropietariDAOImpl;
import org.example.view.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class PropietariController {
    private PropietariDAOImpl propietariDAO;
    private Vista view;
    private PropertyChangeSupport propertyChangeSupport;
    private ViewController viewController;

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

    private void initView() {
        try {
            List<Propietari> propietaris = propietariDAO.getAll();
            updatePropietariTable(propietaris);
            updatePropietariComboBox(propietaris);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void initListeners() {
        ActionListener actionListener = e -> {
            if (viewController.modePropietari()) {
                if (e.getSource() == view.getInsertarButton()) inserirPropietari();
                else if (e.getSource() == view.getModificarButton()) modificarPropietari();
                else if (e.getSource() == view.getBorrarButton()) eliminarPropietari();
            }
        };

        viewController.addActionListener(actionListener);
        viewController.addListSelectionListenerToPropietaris(e -> {
            if (!e.getValueIsAdjusting() && viewController.modePropietari()) {
                mostrarDadesPropietari();
            }
        });
    }

    private void inserirPropietari() {
        try {
            String nom = view.getCampNomPropietari().getText().trim();
            String cognoms = view.getCampCognomsPropietari().getText().trim();
            String telefon = view.getCampTelefonPropietari().getText().trim();
            String email = view.getCampEmailPropietari().getText().trim();

            if (nom.isEmpty() || cognoms.isEmpty() || telefon.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Propietari nouPropietari = new Propietari(nom, cognoms, telefon, email);
            propietariDAO.save(nouPropietari);
            List<Propietari> propietaris = propietariDAO.getAll();
            updatePropietariTable(propietaris);
            updatePropietariComboBox(propietaris);

            JOptionPane.showMessageDialog(view, "Propietari afegit correctament", "Afegir propietari", JOptionPane.INFORMATION_MESSAGE);

            llimpiarDadesPropietari();

        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void modificarPropietari() {
        try {
            int fila = view.getTaulaPropietaris().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaPropietaris().getModel();
                Propietari propietari = (Propietari) tableModel.getValueAt(fila, 4);

                String nom = view.getCampNomPropietari().getText().trim();
                String cognoms = view.getCampCognomsPropietari().getText().trim();
                String telefon = view.getCampTelefonPropietari().getText().trim();
                String email = view.getCampEmailPropietari().getText().trim();

                if (nom.isEmpty() || cognoms.isEmpty() || telefon.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                propietari.setNom(nom);
                propietari.setCognoms(cognoms);
                propietari.setTelefon(telefon);
                propietari.setEmail(email);

                propietariDAO.update(propietari);
                List<Propietari> propietaris = propietariDAO.getAll();
                updatePropietariTable(propietaris);
                updatePropietariComboBox(propietaris);

                JOptionPane.showMessageDialog(view, "Propietari modificat correctament", "Modificar propietari", JOptionPane.INFORMATION_MESSAGE);

                llimpiarDadesPropietari();
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void eliminarPropietari() {
        try {
            int fila = view.getTaulaPropietaris().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaPropietaris().getModel();
                Propietari propietari = (Propietari) tableModel.getValueAt(fila, 4);
                propietariDAO.delete(propietari);
                List<Propietari> propietaris = propietariDAO.getAll();
                updatePropietariTable(propietaris);
                updatePropietariComboBox(propietaris);

                JOptionPane.showMessageDialog(view, "Propietari eliminat correctament", "Eliminar propietari", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    public void updatePropietariTable(List<Propietari> propietaris) {
        DefaultTableModel model = (DefaultTableModel) view.getTaulaPropietaris().getModel();
        model.setRowCount(0);
        for (Propietari propietari : propietaris) {
            model.addRow(new Object[]{propietari.getNom(), propietari.getCognoms(), propietari.getTelefon(), propietari.getEmail(), propietari});
        }
    }

    public void updatePropietariComboBox(List<Propietari> propietaris) {
        viewController.updatePropietariComboBox(propietaris);
    }

    private void llimpiarDadesPropietari() {
        view.getCampNomPropietari().setText("");
        view.getCampCognomsPropietari().setText("");
        view.getCampTelefonPropietari().setText("");
        view.getCampEmailPropietari().setText("");
    }

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
