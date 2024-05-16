package org.example.controller;

import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.view.ModelComponentsVisuals;
import org.example.view.Vista;

import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

public class ViewController {
    private final Vista view;
    private final ModelComponentsVisuals modelComponentsVisuals;

    public ViewController(Vista view, ModelComponentsVisuals modelComponentsVisuals) {
        this.view = view;
        this.modelComponentsVisuals = modelComponentsVisuals;
        lligaVistaModel();
    }

    private void lligaVistaModel() {
        configurarTabla(view.getTaulaBicis(), modelComponentsVisuals.getModelTaulaBicis(), 7);
        configurarTabla(view.getTaulaPropietaris(), modelComponentsVisuals.getModelTaulaPropietaris(), 4);
        configurarTabla(view.getTaulaRevisions(), modelComponentsVisuals.getModelTaulaRevisions(), 4);

        view.getComboTipus().setModel(modelComponentsVisuals.getComboBoxModelTipusBici());
        view.getComboCarboni().setModel(modelComponentsVisuals.getComboBoxModelCarboni());
        view.getComboPropietari().setModel(modelComponentsVisuals.getComboBoxModelPropietari());
        view.getComboBici().setModel(modelComponentsVisuals.getComboBoxModelBici());
    }

    private void configurarTabla(JTable tabla, DefaultTableModel modelo, int columnaOculta) {
        tabla.setModel(modelo);
        var columnModel = tabla.getColumnModel().getColumn(columnaOculta);
        columnModel.setMinWidth(0);
        columnModel.setMaxWidth(0);
        columnModel.setPreferredWidth(0);
    }

    public void addActionListener(ActionListener actionListener) {
        view.getInsertarButton().addActionListener(actionListener);
        view.getModificarButton().addActionListener(actionListener);
        view.getBorrarButton().addActionListener(actionListener);
    }

    public void addListSelectionListenerToPropietaris(ListSelectionListener listener) {
        view.getTaulaPropietaris().getSelectionModel().addListSelectionListener(listener);
    }

    public void addListSelectionListenerToBicis(ListSelectionListener listener) {
        view.getTaulaBicis().getSelectionModel().addListSelectionListener(listener);
    }

    public void addListSelectionListenerToRevisions(ListSelectionListener listener) {
        view.getTaulaRevisions().getSelectionModel().addListSelectionListener(listener);
    }

    public boolean modePropietari() {
        return view.getPestanyes().getSelectedIndex() == 0;
    }

    public boolean modeBici() {
        return view.getPestanyes().getSelectedIndex() == 1;
    }

    public boolean modeRevisio() {
        return view.getPestanyes().getSelectedIndex() == 2;
    }

    public void updatePropietariComboBox(List<Propietari> propietaris) {
        DefaultComboBoxModel<Propietari> comboBoxModel = (DefaultComboBoxModel<Propietari>) view.getComboPropietari().getModel();
        comboBoxModel.removeAllElements();
        for (Propietari propietari : propietaris) {
            comboBoxModel.addElement(propietari);
        }
    }

    public void updateBiciComboBox(List<Bici> bicis) {
        DefaultComboBoxModel<Bici> comboBoxModel = (DefaultComboBoxModel<Bici>) view.getComboBici().getModel();
        comboBoxModel.removeAllElements();
        for (Bici bici : bicis) {
            comboBoxModel.addElement(bici);
        }
    }

}
