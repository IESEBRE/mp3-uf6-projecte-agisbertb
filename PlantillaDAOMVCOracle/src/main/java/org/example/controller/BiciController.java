package org.example.controller;

import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.BiciDAOImpl;
import org.example.view.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador per a la gestió de bicicletes.
 *
 * Aquest controlador s'encarrega de la gestió de bicicletes, incloent la creació,
 * modificació i eliminació de bicicletes, així com la interacció amb la vista
 * associada i la gestió d'excepcions.
 *
 * @author Andreu Gisbert Bel
 * @version 1.0
 */

public class BiciController {
    private final BiciDAOImpl bicicletaDAO;
    private final Vista view;
    private final PropertyChangeSupport propertyChangeSupport;
    private final ViewController viewController;

    /**
     * Constructor del controlador de bicicletes.
     *
     * Inicialitza les dependències necessàries, configura els escoltadors d'esdeveniments
     * i inicialitza la vista.
     *
     * @param bicicletaDAO Implementació del DAO per a bicicletes
     * @param view Vista associada a aquest controlador
     * @param viewController Controlador de la vista principal
     */

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

    /**
     * Inicialitza la vista carregant les dades de bicicletes i configurant components.
     */

    private void initView() {
        try {
            List<Bici> bicis = bicicletaDAO.getAll();
            updateBiciTable(bicis);
            updateBiciComboBox(bicis);
        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        }
    }

    /**
     * Configura els escoltadors d'esdeveniments per la interfície d'usuari.
     */

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

    /**
     * Recupera les dades d'una bicicleta des de la vista.
     *
     * @return Bici amb les dades provinents dels camps de la vista o null si hi ha errors
     */

    private Bici getBiciDadesVista() {
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
                return null;
            }

            double pes = Double.parseDouble(pesText);
            int any = Integer.parseInt(anyText);

            return new Bici(marca, model, any, pes, tipus, carboni, propietari);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Pes i Any han de ser números", "Error de format", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Valida les dades d'una bicicleta abans de la seva inserció o modificació.
     *
     * @param bici Bici a validar
     * @return true si la bicicleta és vàlida, false en cas contrari
     * @throws DAOException si hi ha un error de validació
     */

    private boolean validarBici(Bici bici) throws DAOException {
        String regex = "^[A-ZÀ-ÚÑÇ][a-zà-úñç]*(\\s+[A-ZÀ-ÚÑÇ][a-zà-úñç]*)*$";

        if (bici.getMarca() == null || bici.getMarca().trim().isEmpty() || !bici.getMarca().matches(regex)) {
            throw new DAOException(21);
        }
        if (bici.getModelBici() == null || bici.getModelBici().trim().isEmpty() || !bici.getModelBici().matches(regex)) {
            throw new DAOException(22);
        }
        if (bici.getAnyFabricacio() < 1990 || bici.getAnyFabricacio() > LocalDate.now().getYear()) {
            throw new DAOException(23);
        }
        if (bici.getPes() <= 0) {
            throw new DAOException(24);
        }
        return true;
    }

    /**
     * Insereix una nova bicicleta a la base de dades.
     */

    private void inserirBici() {
        try {
            Bici novaBici = getBiciDadesVista();
            if (novaBici == null || !validarBici(novaBici)) return;

            bicicletaDAO.save(novaBici);
            updateBiciTable(bicicletaDAO.getAll());
            updateBiciComboBox(bicicletaDAO.getAll());

            JOptionPane.showMessageDialog(view, "Bici afegida correctament", "Afegir bici", JOptionPane.INFORMATION_MESSAGE);

            llimpiarDadesBici();

        } catch (DAOException e) {
            setExcepcio(new DAOException(e.getTipo()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Pes i Any han de ser números", "Error de format", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Modifica una bicicleta existent.
     */

    private void modificarBici() {
        try {
            int fila = view.getTaulaBicis().getSelectedRow();
            if (fila != -1) {
                DefaultTableModel tableModel = (DefaultTableModel) view.getTaulaBicis().getModel();
                Bici bici = (Bici) tableModel.getValueAt(fila, 7);

                Bici biciModificada = getBiciDadesVista();
                if (biciModificada == null || !validarBici(biciModificada)) return;

                bici.setMarca(biciModificada.getMarca());
                bici.setModelBici(biciModificada.getModelBici());
                bici.setPes(biciModificada.getPes());
                bici.setAnyFabricacio(biciModificada.getAnyFabricacio());
                bici.setTipo(biciModificada.getTipo());
                bici.setCarboni(biciModificada.getCarboni());
                bici.setPropietari(biciModificada.getPropietari());

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

    /**
     * Elimina una bicicleta de la base de dades.
     */

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

    /**
     * Actualitza la taula de bicicletes amb les dades actuals.
     *
     * @param bicis Llista de bicicletes per mostrar a la taula
     */

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

    /**
     * Neteja els camps de dades de la bicicleta a la vista.
     */

    private void llimpiarDadesBici() {
        view.getCampMarca().setText("");
        view.getCampModelBici().setText("");
        view.getCampPes().setText("");
        view.getCampAnyFabricacio().setText("");
        view.getComboTipus().setSelectedIndex(0);
        view.getComboCarboni().setSelectedIndex(0);
        view.getComboPropietari().setSelectedIndex(0);
    }

    /**
     * Mostra les dades d'una bicicleta seleccionada a la taula.
     */

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