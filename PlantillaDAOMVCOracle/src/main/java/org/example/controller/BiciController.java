package org.example.controller;

import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.BiciDAOImpl;
import org.example.view.Vista;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class BiciController {
    private final BiciDAOImpl bicicletaDAO;
    private final Vista view;
    private final PropertyChangeSupport propertyChangeSupport;
    private final ViewController viewController;

    public BiciController(BiciDAOImpl bicicletaDAO, Vista view, ViewController viewController) {
        this.bicicletaDAO = bicicletaDAO;
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
            List<Bici> bicis = bicicletaDAO.getAll();
            updateBiciTable(bicis);
            updateBiciComboBox(bicis);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void initListeners() {
        ActionListener actionListener = e -> {
            if (viewController.modeBici()) {
                if (e.getSource() == view.getInsertarButton()) inserirBici();
                else if (e.getSource() == view.getModificarButton()) modificarBici();
                else if (e.getSource() == view.getBorrarButton()) eliminarBici();
            }
        };

        viewController.addActionListener(actionListener);
        viewController.addListSelectionListenerToBicis(e -> {
            if (!e.getValueIsAdjusting() && viewController.modeBici()) {
                mostrarDadesBici();
            }
        });
    }

    private void inserirBici() {
        try {
            String marca = view.getCampMarca().getText().trim();
            String model = view.getCampModelBici().getText().trim();
            String pesText = view.getCampPes().getText().trim();
            String anyText = view.getCampAnyFabricacio().getText().trim();
            Bici.TipoBici tipus = (Bici.TipoBici) view.getComboTipus().getSelectedItem();
            Bici.Carboni carboni = (Bici.Carboni) view.getComboCarboni().getSelectedItem();
            Propietari propietari = (Propietari) view.getComboPropietari().getSelectedItem();

            if (marca.isEmpty() || model.isEmpty() || pesText.isEmpty() || anyText.isEmpty() || tipus == null || carboni == null || propietari == null) {
                JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double pes = Double.parseDouble(pesText);
            int any = Integer.parseInt(anyText);

            Bici novaBici = new Bici(marca, model, any, pes, tipus, carboni, propietari);
            bicicletaDAO.save(novaBici);

            List<Bici> bicis = bicicletaDAO.getAll();
            updateBiciTable(bicis);
            updateBiciComboBox(bicis);

            JOptionPane.showMessageDialog(view, "Bici afegida correctament", "Afegir bici", JOptionPane.INFORMATION_MESSAGE);

            llimpiarDadesBici();

        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Pes i Any han de ser números", "Error de format", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void modificarBici() {
        try {
            int fila = view.getTaulaBicis().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaBicis().getModel();
                Bici bici = (Bici) tableModel.getValueAt(fila, 7);

                String marca = view.getCampMarca().getText().trim();
                String modelBici = view.getCampModelBici().getText().trim();
                int anyFabricacio = Integer.parseInt(view.getCampAnyFabricacio().getText().trim());
                double pes = Double.parseDouble(view.getCampPes().getText().trim());
                Bici.TipoBici tipo = (Bici.TipoBici) view.getComboTipus().getSelectedItem();
                Bici.Carboni carboni = (Bici.Carboni) view.getComboCarboni().getSelectedItem();
                Propietari propietari = (Propietari) view.getComboPropietari().getSelectedItem();

                if (marca.isEmpty() || modelBici.isEmpty() || view.getCampAnyFabricacio().getText().trim().isEmpty() || view.getCampPes().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                bici.setMarca(marca);
                bici.setModelBici(modelBici);
                bici.setAnyFabricacio(anyFabricacio);
                bici.setPes(pes);
                bici.setTipo(tipo);
                bici.setCarboni(carboni);
                bici.setPropietari(propietari);

                bicicletaDAO.update(bici);
                updateBiciTable(bicicletaDAO.getAll());
                updateBiciComboBox(bicicletaDAO.getAll());

                JOptionPane.showMessageDialog(view, "Bici modificada correctament", "Modificar bici", JOptionPane.INFORMATION_MESSAGE);
                llimpiarDadesBici();
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Format de número incorrecte", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void eliminarBici() {
        try {
            int fila = view.getTaulaBicis().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaBicis().getModel();
                Bici bici = (Bici) tableModel.getValueAt(fila, 7);
                if (bici != null && bici.getId() != null) {
                    bicicletaDAO.delete(bici);

                    updateBiciTable(bicicletaDAO.getAll());
                    updateBiciComboBox(bicicletaDAO.getAll());

                    JOptionPane.showMessageDialog(view, "Bici eliminada correctament", "Eliminar bici", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "La bici o el seu ID és null", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }


    public void updateBiciTable(List<Bici> bicis) {
        DefaultTableModel model = (DefaultTableModel) view.getTaulaBicis().getModel();
        model.setRowCount(0);
        for (Bici bici : bicis) {
            model.addRow(new Object[]{bici.getMarca(), bici.getModelBici(), bici.getPes(), bici.getAnyFabricacio(), bici.getTipo(), bici.getCarboni(), bici.getPropietari(), bici});
        }
    }

    public void updateBiciComboBox(List<Bici> bicis) {
        viewController.updateBiciComboBox(bicis);
    }

    private void llimpiarDadesBici() {
        view.getCampMarca().setText("");
        view.getCampModelBici().setText("");
        view.getCampPes().setText("");
        view.getCampAnyFabricacio().setText("");
        view.getComboTipus().setSelectedIndex(0);
        view.getComboCarboni().setSelectedIndex(0);
        view.getComboPropietari().setSelectedIndex(0);
    }

    private void mostrarDadesBici() {
        int fila = view.getTaulaBicis().getSelectedRow();
        if (fila != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaBicis().getModel();
            Bici bici = (Bici) tableModel.getValueAt(fila, 7);
            view.getCampMarca().setText(bici.getMarca());
            view.getCampModelBici().setText(bici.getModelBici());
            view.getCampPes().setText(String.valueOf(bici.getPes()));
            view.getCampAnyFabricacio().setText(String.valueOf(bici.getAnyFabricacio()));
            view.getComboTipus().setSelectedItem(bici.getTipo());
            view.getComboCarboni().setSelectedItem(bici.getCarboni());
            view.getComboPropietari().setSelectedItem(bici.getPropietari());
        }
    }
}