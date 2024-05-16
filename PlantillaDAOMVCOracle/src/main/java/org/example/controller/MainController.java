package org.example.controller;

import org.example.model.impls.BiciDAOImpl;
import org.example.model.impls.PropietariDAOImpl;
import org.example.model.impls.RevisioDAOImpl;
import org.example.view.ModelComponentsVisuals;
import org.example.view.Vista;

public class MainController {
    private final BiciDAOImpl bicicletaDAO;
    private final PropietariDAOImpl propietariDAO;
    private final RevisioDAOImpl revisioDAO;
    private final Vista view;
    private final ViewController viewController;

    public MainController() {
        bicicletaDAO = new BiciDAOImpl();
        propietariDAO = new PropietariDAOImpl();
        revisioDAO = new RevisioDAOImpl();
        view = new Vista();
        ModelComponentsVisuals modelComponentsVisuals = new ModelComponentsVisuals();
        viewController = new ViewController(view, modelComponentsVisuals);
        initControllers();
        view.setVisible(true);
    }

    private void initControllers() {
        new BiciController(bicicletaDAO, view, viewController);
        new PropietariController(propietariDAO, view, viewController);
        new RevisioController(revisioDAO, view, viewController);
    }
}