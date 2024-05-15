package org.example.controller;

import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.BiciDAOImpl;
import org.example.view.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class BiciController {
    private BiciDAOImpl biciDAO;
    private Vista view;
    private PropertyChangeSupport propertyChangeSupport;
    private ViewController viewController;

    public BiciController(BiciDAOImpl biciDAO, Vista view, ViewController viewController) {
        this.biciDAO = biciDAO;
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
            List<Bici> bicis = biciDAO.getAll();
            updateBiciTable(bicis);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void initListeners() {
        viewController.addActionListener(e -> {
            if (viewController.modeBici()) {
                if (e.getSource() == view.getInsertarButton()) inserirBici();
                else if (e.getSource() == view.getModificarButton()) modificarBici();
                else if (e.getSource() == view.getBorrarButton()) eliminarBici();
            }
        });

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
            String pesStr = view.getCampPes().getText().trim();
            String anyStr = view.getCampAnyFabricacio().getText().trim();
            Bici.TipoBici tipo = (Bici.TipoBici) view.getComboTipus().getSelectedItem();
            Bici.Carboni carboni = (Bici.Carboni) view.getComboCarboni().getSelectedItem();
            Propietari propietari = (Propietari) view.getComboPropietari().getSelectedItem();

            if (marca.isEmpty() || model.isEmpty() || pesStr.isEmpty() || anyStr.isEmpty() || propietari == null) {
                JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double pes = Double.parseDouble(pesStr);
            int any = Integer.parseInt(anyStr);

            Bici novaBici = new Bici(marca, model, any, pes, tipo, carboni, propietari);
            biciDAO.save(novaBici);
            updateBiciTable(biciDAO.getAll());

            JOptionPane.showMessageDialog(view, "Bici afegida correctament", "Afegir bici", JOptionPane.INFORMATION_MESSAGE);

            llimpiarDadesBici();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Error en el format dels camps numèrics", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void modificarBici() {
        try {
            int fila = view.getTaulaBicis().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaBicis().getModel();
                Bici bici = (Bici) tableModel.getValueAt(fila, 7);

                String marca = view.getCampMarca().getText().trim();
                String model = view.getCampModelBici().getText().trim();
                String pesStr = view.getCampPes().getText().trim();
                String anyStr = view.getCampAnyFabricacio().getText().trim();
                Bici.TipoBici tipo = (Bici.TipoBici) view.getComboTipus().getSelectedItem();
                Bici.Carboni carboni = (Bici.Carboni) view.getComboCarboni().getSelectedItem();
                Propietari propietari = (Propietari) view.getComboPropietari().getSelectedItem();

                if (marca.isEmpty() || model.isEmpty() || pesStr.isEmpty() || anyStr.isEmpty() || propietari == null) {
                    JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double pes = Double.parseDouble(pesStr);
                int any = Integer.parseInt(anyStr);

                bici.setMarca(marca);
                bici.setModelBici(model);
                bici.setPes(pes);
                bici.setAnyFabricacio(any);
                bici.setTipo(tipo);
                bici.setCarboni(carboni);
                bici.setPropietari(propietari);

                biciDAO.update(bici);
                updateBiciTable(biciDAO.getAll());

                JOptionPane.showMessageDialog(view, "Bici modificada correctament", "Modificar bici", JOptionPane.INFORMATION_MESSAGE);

                llimpiarDadesBici();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Error en el format dels camps numèrics", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void eliminarBici() {
        try {
            int fila = view.getTaulaBicis().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaBicis().getModel();
                Bici bici = (Bici) tableModel.getValueAt(fila, 7);
                biciDAO.delete(bici);
                updateBiciTable(biciDAO.getAll());

                JOptionPane.showMessageDialog(view, "Bici eliminada correctament", "Eliminar bici", JOptionPane.INFORMATION_MESSAGE);
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
