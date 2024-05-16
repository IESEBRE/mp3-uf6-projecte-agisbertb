package org.example.controller;

import org.example.model.impls.BiciDAOImpl;
import org.example.model.impls.PropietariDAOImpl;
import org.example.model.impls.RevisioDAOImpl;
import org.example.view.ModelComponentsVisuals;
import org.example.view.Vista;

public class MainController {
    protected BiciDAOImpl bicicletaDAO;
    protected PropietariDAOImpl propietariDAO;
    protected RevisioDAOImpl revisioDAO;
    protected Vista view;
    protected ViewController viewController;

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