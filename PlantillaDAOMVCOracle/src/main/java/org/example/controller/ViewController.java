package org.example.controller;

import org.example.view.ModelComponentsVisuals;
import org.example.view.Vista;

import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class ViewController {
    private Vista view;
    private ModelComponentsVisuals modelComponentsVisuals;

    public ViewController(Vista view, ModelComponentsVisuals modelComponentsVisuals) {
        this.view = view;
        this.modelComponentsVisuals = modelComponentsVisuals;
        lligaVistaModel();
    }

    private void lligaVistaModel() {
        configurarTaula(view.getTaulaBicis(), modelComponentsVisuals.getModelTaulaBicis(), 7);
        configurarTaula(view.getTaulaPropietaris(), modelComponentsVisuals.getModelTaulaPropietaris(), 4);
        configurarTaula(view.getTaulaRevisions(), modelComponentsVisuals.getModelTaulaRevisions(), 2);

        view.getComboTipus().setModel(modelComponentsVisuals.getComboBoxModelTipusBici());
        view.getComboCarboni().setModel(modelComponentsVisuals.getComboBoxModelCarboni());
        view.getComboPropietari().setModel(modelComponentsVisuals.getComboBoxModelPropietari());
    }

    private void configurarTaula(JTable tabla, DefaultTableModel model, int columnaOculta) {
        tabla.setModel(model);
        var columnModel = tabla.getColumnModel().getColumn(columnaOculta);
        columnModel.setMinWidth(0);
        columnModel.setMaxWidth(0);
        columnModel.setPreferredWidth(0);
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

    public void addActionListener(ActionListener actionListener) {
        view.getInsertarButton().addActionListener(actionListener);
        view.getModificarButton().addActionListener(actionListener);
        view.getBorrarButton().addActionListener(actionListener);
    }

    public void addListSelectionListenerToPropietaris(ListSelectionListener listSelectionListener) {
        view.getTaulaPropietaris().getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void addListSelectionListenerToBicis(ListSelectionListener listSelectionListener) {
        view.getTaulaBicis().getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void addListSelectionListenerToRevisions(ListSelectionListener listSelectionListener) {
        view.getTaulaRevisions().getSelectionModel().addListSelectionListener(listSelectionListener);
    }
}
