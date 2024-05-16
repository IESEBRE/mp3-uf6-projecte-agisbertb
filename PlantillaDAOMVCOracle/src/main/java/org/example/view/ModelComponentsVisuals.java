package org.example.view;

import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.model.entities.Revisio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class ModelComponentsVisuals {

    private DefaultTableModel modelTaulaBicis;
    private DefaultTableModel modelTaulaPropietaris;
    private DefaultTableModel modelTaulaRevisions;

    private DefaultComboBoxModel<Bici.TipoBici> comboBoxModelTipusBici;
    private DefaultComboBoxModel<Bici.Carboni> comboBoxModelCarboni;
    private DefaultComboBoxModel<Propietari> comboBoxModelPropietari;
    private DefaultComboBoxModel<Bici> comboBoxModelBici;

    public ModelComponentsVisuals() {
        initializeBiciModel();
        initializePropietariModel();
        initializeRevisioModel();

        comboBoxModelTipusBici = new DefaultComboBoxModel<>(Bici.TipoBici.values());
        comboBoxModelCarboni = new DefaultComboBoxModel<>(Bici.Carboni.values());
        comboBoxModelPropietari = new DefaultComboBoxModel<>();
        comboBoxModelBici = new DefaultComboBoxModel<>();
    }

    private void initializeBiciModel() {
        modelTaulaBicis = new DefaultTableModel(new Object[]{"Marca", "Model", "Pes", "Any", "Tipus", "Carboni", "Propietari", "Object"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return String.class;
                    case 1: return String.class;
                    case 2: return Double.class;
                    case 3: return Integer.class;
                    case 4: return Bici.TipoBici.class;
                    case 5: return Bici.Carboni.class;
                    case 6: return Propietari.class;
                    default: return Object.class;
                }
            }
        };
    }

    private void initializePropietariModel() {
        modelTaulaPropietaris = new DefaultTableModel(new Object[]{"Nom", "Cognom", "Telefon", "Email", "Object"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return String.class;
                    case 1: return String.class;
                    case 2: return String.class;
                    case 3: return String.class;
                    default: return Object.class;
                }
            }
        };
    }

    private void initializeRevisioModel() {
        modelTaulaRevisions = new DefaultTableModel(new Object[]{"Data", "Descripcio", "Bici" ,"Object"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return LocalDate.class;
                    case 1: return String.class;
                    case 2: return Bici.class;
                    default: return Object.class;
                }
            }
        };
    }

    // Método para actualizar el ComboBoxModel con la lista de propietarios
    public void setPropietarisComboBoxModel(List<Propietari> propietaris) {
        comboBoxModelPropietari.removeAllElements();
        for (Propietari propietari : propietaris) {
            comboBoxModelPropietari.addElement(propietari);
        }
    }

    // Getters
    public DefaultTableModel getModelTaulaBicis() {
        return modelTaulaBicis;
    }

    public DefaultTableModel getModelTaulaPropietaris() {
        return modelTaulaPropietaris;
    }

    public DefaultTableModel getModelTaulaRevisions() {
        return modelTaulaRevisions;
    }

    public ComboBoxModel<Bici.TipoBici> getComboBoxModelTipusBici() {
        return comboBoxModelTipusBici;
    }

    public ComboBoxModel<Bici.Carboni> getComboBoxModelCarboni() {
        return comboBoxModelCarboni;
    }

    public ComboBoxModel<Propietari> getComboBoxModelPropietari() {
        return comboBoxModelPropietari;
    }

    public ComboBoxModel<Bici> getComboBoxModelBici() {
        return comboBoxModelBici;
    }
}
