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

/**
 * Controlador que gestiona les interaccions entre la vista i els models de components.
 *
 * Aquest controlador centralitza la configuració dels components de l'interfície gràfica,
 * com taules i comboBoxes, i facilita la gestió d'accions i seleccions d'aquests components.
 *
 * @author Andreu Gisbert Bel
 * @version 1.0
 */

public class ViewController {
    private final Vista view;
    private final ModelComponentsVisuals modelComponentsVisuals;

    /**
     * Constructor que associa la vista i els models de components visuals.
     *
     * @param view La vista principal de l'aplicació
     * @param modelComponentsVisuals Model de components visuals que conté les configuracions de l'UI
     */

    public ViewController(Vista view, ModelComponentsVisuals modelComponentsVisuals) {
        this.view = view;
        this.modelComponentsVisuals = modelComponentsVisuals;
        lligaVistaModel();
    }

    /**
     * Configura la vista inicial associant els models de dades amb els components de l'UI.
     */

    private void lligaVistaModel() {
        configurarTabla(view.getTaulaBicis(), modelComponentsVisuals.getModelTaulaBicis(), 7);
        configurarTabla(view.getTaulaPropietaris(), modelComponentsVisuals.getModelTaulaPropietaris(), 4);
        configurarTabla(view.getTaulaRevisions(), modelComponentsVisuals.getModelTaulaRevisions(), 4);

        view.getComboTipus().setModel(modelComponentsVisuals.getComboBoxModelTipusBici());
        view.getComboCarboni().setModel(modelComponentsVisuals.getComboBoxModelCarboni());
        view.getComboPropietari().setModel(modelComponentsVisuals.getComboBoxModelPropietari());
        view.getComboBici().setModel(modelComponentsVisuals.getComboBoxModelBici());
    }

    /**
     * Configura una taula amb un model de dades i amaga una columna específica.
     *
     * @param tabla La taula a configurar
     * @param modelo El model de dades per a la taula
     * @param columnaOculta Índex de la columna a amagar
     */

    private void configurarTabla(JTable tabla, DefaultTableModel modelo, int columnaOculta) {
        tabla.setModel(modelo);
        var columnModel = tabla.getColumnModel().getColumn(columnaOculta);
        columnModel.setMinWidth(0);
        columnModel.setMaxWidth(0);
        columnModel.setPreferredWidth(0);
    }

    /**
     * Afegeix un escoltador d'acció als botons de la interfície gràfica.
     *
     * @param actionListener El escoltador d'accions a afegir
     */

    public void addActionListener(ActionListener actionListener) {
        view.getInsertarButton().addActionListener(actionListener);
        view.getModificarButton().addActionListener(actionListener);
        view.getBorrarButton().addActionListener(actionListener);
    }

    /**
     * Afegeix un escoltador de selecció de llista a la taula de propietaris.
     *
     * @param listener El escoltador de selecció de llista a afegir
     */

    public void addListSelectionListenerToPropietaris(ListSelectionListener listener) {
        view.getTaulaPropietaris().getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * Afegeix un escoltador de selecció de llista a la taula de bicicletes.
     *
     * @param listener El escoltador de selecció de llista a afegir
     */

    public void addListSelectionListenerToBicis(ListSelectionListener listener) {
        view.getTaulaBicis().getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * Afegeix un escoltador de selecció de llista a la taula de revisions.
     *
     * @param listener El escoltador de selecció de llista a afegir
     */

    public void addListSelectionListenerToRevisions(ListSelectionListener listener) {
        view.getTaulaRevisions().getSelectionModel().addListSelectionListener(listener);
    }

    /**
     * Determina si el mode actual és de gestió de propietaris.
     *
     * @return true si el mode actual és de gestió de propietaris, false en cas contrari
     */

    public boolean modePropietari() {
        return view.getPestanyes().getSelectedIndex() == 0;
    }

    /**
     * Determina si el mode actual és de gestió de bicicletes.
     *
     * @return true si el mode actual és de gestió de bicicletes, false en cas contrari
     */

    public boolean modeBici() {
        return view.getPestanyes().getSelectedIndex() == 1;
    }

    /**
     * Determina si el mode actual és de gestió de revisions.
     *
     * @return true si el mode actual és de gestió de revisions, false en cas contrari
     */

    public boolean modeRevisio() {
        return view.getPestanyes().getSelectedIndex() == 2;
    }

    /**
     * Actualitza el comboBox de propietaris amb una llista actualitzada de propietaris.
     *
     * @param propietaris Llista de propietaris a mostrar en el comboBox
     */

    public void updatePropietariComboBox(List<Propietari> propietaris) {
        DefaultComboBoxModel<Propietari> comboBoxModel = (DefaultComboBoxModel<Propietari>) view.getComboPropietari().getModel();
        comboBoxModel.removeAllElements();
        for (Propietari propietari : propietaris) {
            comboBoxModel.addElement(propietari);
        }
    }

    /**
     * Actualitza el comboBox de bicicletes amb una llista actualitzada de bicicletes.
     *
     * @param bicis Llista de bicicletes a mostrar en el comboBox
     */

    public void updateBiciComboBox(List<Bici> bicis) {
        DefaultComboBoxModel<Bici> comboBoxModel = (DefaultComboBoxModel<Bici>) view.getComboBici().getModel();
        comboBoxModel.removeAllElements();
        for (Bici bici : bicis) {
            comboBoxModel.addElement(bici);
        }
    }

}
