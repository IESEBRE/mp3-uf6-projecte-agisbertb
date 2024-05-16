package org.example.controller;

import org.example.model.entities.Bici;
import org.example.model.entities.Revisio;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.RevisioDAOImpl;
import org.example.view.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RevisioController {
    private final RevisioDAOImpl revisioDAO;
    private final Vista view;
    private final PropertyChangeSupport propertyChangeSupport;
    private final ViewController viewController;

    public RevisioController(RevisioDAOImpl revisioDAO, Vista view, ViewController viewController) {
        this.revisioDAO = revisioDAO;
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
            List<Revisio> revisions = revisioDAO.getAll();
            updateRevisioTable(revisions);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    private void initListeners() {
        viewController.addActionListener(e -> {
            if (viewController.modeRevisio()) {
                if (e.getSource() == view.getInsertarButton()) inserirRevisio();
                else if (e.getSource() == view.getModificarButton()) modificarRevisio();
                else if (e.getSource() == view.getBorrarButton()) eliminarRevisio();
            }
        });

        viewController.addListSelectionListenerToRevisions(e -> {
            if (!e.getValueIsAdjusting() && viewController.modeRevisio()) {
                mostrarDadesRevisio();
            }
        });
    }

    private Revisio getRevisioDadesVista() {
        try {
            String dataStr = view.getCampDataRevisio().getText().trim();
            String descripcio = view.getCampDescripcioRevisio().getText().trim();
            String preuText = view.getCampPreuRevisio().getText().trim();
            Bici bici = (Bici) view.getComboBici().getSelectedItem();

            if (dataStr.isEmpty() || descripcio.isEmpty() || preuText.isEmpty() || bici == null) {
                JOptionPane.showMessageDialog(view, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            double preu = Double.parseDouble(preuText);

            return new Revisio(dataStr, descripcio, preu, bici);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Preu ha de ser un número", "Error de format", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private boolean validarRevisio(Revisio revisio) throws DAOException {
        String regex = "^[A-ZÀ-ÚÑÇ][a-zà-úñç]*(\\s+[A-ZÀ-ÚÑÇ][a-zà-úñç]*)*$";

        try {
            LocalDate.parse(revisio.getData());
        } catch (DateTimeParseException e) {
            throw new DAOException(31);
        }

        if (revisio.getDescripcio() == null || revisio.getDescripcio().trim().isEmpty() || !revisio.getDescripcio().matches(regex)) {
            throw new DAOException(32);
        }
        if (revisio.getPreu() <= 0) {
            throw new DAOException(33);
        }
        return true;
    }

    private void inserirRevisio() {
        try {
            Revisio novaRevisio = getRevisioDadesVista();
            if (novaRevisio == null || !validarRevisio(novaRevisio)) return;

            revisioDAO.save(novaRevisio);
            updateRevisioTable(revisioDAO.getAll());

            JOptionPane.showMessageDialog(view, "Revisió afegida correctament", "Afegir revisió", JOptionPane.INFORMATION_MESSAGE);

            llimpiarDadesRevisio();

        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Data en format incorrecte", "Error de format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarRevisio() {
        try {
            int fila = view.getTaulaRevisions().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaRevisions().getModel();
                Revisio revisio = (Revisio) tableModel.getValueAt(fila, 3);

                Revisio revisioModificada = getRevisioDadesVista();
                if (revisioModificada == null || !validarRevisio(revisioModificada)) return;

                revisio.setData(revisioModificada.getData());
                revisio.setDescripcio(revisioModificada.getDescripcio());
                revisio.setPreu(revisioModificada.getPreu());
                revisio.setBici(revisioModificada.getBici());

                revisioDAO.update(revisio);
                updateRevisioTable(revisioDAO.getAll());

                JOptionPane.showMessageDialog(view, "Revisió modificada correctament", "Modificar revisió", JOptionPane.INFORMATION_MESSAGE);

                llimpiarDadesRevisio();
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Data en format incorrecte", "Error de format", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRevisio() {
        try {
            int fila = view.getTaulaRevisions().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaRevisions().getModel();
                Revisio revisio = (Revisio) tableModel.getValueAt(fila, 3);
                revisioDAO.delete(revisio.getId());
                updateRevisioTable(revisioDAO.getAll());
                llimpiarDadesRevisio();

                JOptionPane.showMessageDialog(view, "Revisió eliminada correctament", "Eliminar revisió", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    public void updateRevisioTable(List<Revisio> revisions) {
        DefaultTableModel model = (DefaultTableModel) view.getTaulaRevisions().getModel();
        model.setRowCount(0);
        for (Revisio revisio : revisions) {
            model.addRow(new Object[]{revisio.getData(), revisio.getDescripcio(), revisio.getPreu(), revisio});
        }
    }

    private void llimpiarDadesRevisio() {
        view.getCampDataRevisio().setText("");
        view.getCampDescripcioRevisio().setText("");
        view.getCampPreuRevisio().setText("");
        view.getComboBici().setSelectedIndex(0);
    }

    private void mostrarDadesRevisio() {
        int fila = view.getTaulaRevisions().getSelectedRow();
        if (fila != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaRevisions().getModel();
            Revisio revisio = (Revisio) tableModel.getValueAt(fila, 3);
            view.getCampDataRevisio().setText(revisio.getData());
            view.getCampDescripcioRevisio().setText(revisio.getDescripcio());
            view.getCampPreuRevisio().setText(String.valueOf(revisio.getPreu()));
            view.getComboBici().setSelectedItem(revisio.getBici());
        }
    }
}
