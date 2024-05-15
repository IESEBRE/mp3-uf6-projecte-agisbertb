package org.example.controller;

import org.example.model.entities.Propietari;
import org.example.model.exceptions.DAOException;
import org.example.model.impls.BiciDAOImpl;
import org.example.model.impls.PropietariDAOImpl;
import org.example.model.impls.RevisioDAOImpl;
import org.example.view.ModelComponentsVisuals;
import org.example.view.Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class MainController {
    private BiciDAOImpl bicicletaDAO;
    private PropietariDAOImpl propietariDAO;
    private RevisioDAOImpl revisioDAO;
    private Vista view;
    private ModelComponentsVisuals modelComponentsVisuals;

    public MainController() {
        bicicletaDAO = new BiciDAOImpl();
        propietariDAO = new PropietariDAOImpl();
        revisioDAO = new RevisioDAOImpl();
        view = new Vista();
        modelComponentsVisuals = new ModelComponentsVisuals();

        initViewController();
        initControllers();

        view.setVisible(true);
    }

    private void initViewController() {
        try {
            // Cargar propietarios y configurar el ComboBoxModel
            List<Propietari> propietaris = propietariDAO.getAll();
            modelComponentsVisuals.setPropietarisComboBoxModel(propietaris);
        } catch (DAOException e) {
            e.printStackTrace();
        }

        new ViewController(view, modelComponentsVisuals);
    }

    private void initControllers() {
        new BiciController(bicicletaDAO, view, new ViewController(view, modelComponentsVisuals));
        new PropietariController(propietariDAO, view, new ViewController(view, modelComponentsVisuals));
        new RevisioController(revisioDAO, view, new ViewController(view, modelComponentsVisuals));
    }
}
