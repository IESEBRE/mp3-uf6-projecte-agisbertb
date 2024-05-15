package org.example.view;

import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import org.example.model.entities.Revisio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

public class ModelComponentsVisuals {

    private DefaultTableModel modelTaulaBicis;
    private DefaultTableModel modelTaulaPropietaris;
    private DefaultTableModel modelTaulaRevisions;

    private ComboBoxModel<Bici.TipoBici> comboBoxModelTipusBici;
    private ComboBoxModel<Bici.Carboni> comboBoxModelCarboni;
    private ComboBoxModel<Propietari> comboBoxModelPropietari;

    public ModelComponentsVisuals() {
        initializeBiciModel();
        initializePropietariModel();
        initializeRevisioModel();

        comboBoxModelTipusBici = new DefaultComboBoxModel<>(Bici.TipoBici.values());
        comboBoxModelCarboni = new DefaultComboBoxModel<>(Bici.Carboni.values());
        comboBoxModelPropietari = new DefaultComboBoxModel<>(); // Assuming owners are loaded from the database
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
        modelTaulaRevisions = new DefaultTableModel(new Object[]{"Data", "Descripcio", "Object"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return LocalDate.class;
                    case 1: return String.class;
                    default: return Object.class;
                }
            }
        };
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
}
