package org.example.view;

import org.example.model.entities.Bici;
import org.example.model.entities.Propietari;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * Aquesta classe representa la interfície gràfica principal de l'aplicació,
 * inclou pestanyes i taulers per gestionar bicicletes, propietaris i revisions.
 */

public class Vista extends JFrame {

    private JPanel panel;
    private JTabbedPane pestanyes;

    private JTable taulaBicis;
    private JTable taulaPropietaris;
    private JTable taulaRevisions;

    private JScrollPane scrollPane1;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;

    private JPanel Propietaris;
    private JTextField campNomPropietari;
    private JTextField campCognomsPropietari;
    private JTextField campTelefonPropietari;
    private JTextField campEmailPropietari;

    private JPanel Bicis;
    private JTextField campMarca;
    private JTextField campModelBici;
    private JTextField campAnyFabricacio;
    private JTextField campPes;
    private JComboBox<Bici.TipoBici> comboTipus;
    private JComboBox<Bici.Carboni> comboCarboni;
    private JComboBox<Propietari> comboPropietari;
    private JComboBox<Bici> comboBici;

    private JPanel Revisions;
    private JTextField campDataRevisio;
    private JTextField campDescripcioRevisio;
    private JTextField campPreuRevisio;

    // Getters

    public JTable getTaulaBicis() {
        return taulaBicis;
    }

    public JTable getTaulaPropietaris() {
        return taulaPropietaris;
    }

    public JTable getTaulaRevisions() {
        return taulaRevisions;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JTextField getCampMarca() {
        return campMarca;
    }

    public JTextField getCampModelBici() {
        return campModelBici;
    }

    public JTextField getCampAnyFabricacio() {
        return campAnyFabricacio;
    }

    public JTextField getCampPes() {
        return campPes;
    }

    public JComboBox<Bici.TipoBici> getComboTipus() {
        return comboTipus;
    }

    public JComboBox<Bici.Carboni> getComboCarboni() {
        return comboCarboni;
    }

    public JComboBox<Propietari> getComboPropietari() {
        return comboPropietari;
    }

    public JComboBox<Bici> getComboBici() {
        return comboBici;
    }

    public JTextField getCampNomPropietari() {
        return campNomPropietari;
    }

    public JTextField getCampCognomsPropietari() {
        return campCognomsPropietari;
    }

    public JTextField getCampTelefonPropietari() {
        return campTelefonPropietari;
    }

    public JTextField getCampEmailPropietari() {
        return campEmailPropietari;
    }

    public JTextField getCampDataRevisio() {
        return campDataRevisio;
    }

    public JTextField getCampDescripcioRevisio() {
        return campDescripcioRevisio;
    }

    public JTextField getCampPreuRevisio() {
        return campPreuRevisio;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    /**
     * Constructor que inicialitza la finestra principal i configura els components bàsics.
     */

    public Vista() {
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

    /**
     * Crea components d'UI personalitzats, configurant taulers i pestanyes.
     */

    private void createUIComponents() {
        scrollPane1 = new JScrollPane();
        taulaBicis = new JTable();
        taulaPropietaris = new JTable();
        taulaRevisions = new JTable();
        pestanyes = new JTabbedPane();
        taulaBicis.setModel(new DefaultTableModel());
        taulaBicis.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taulaBicis);
        comboCarboni = new JComboBox<>(Bici.Carboni.values());
        comboTipus = new JComboBox<>(Bici.TipoBici.values());

    }
}