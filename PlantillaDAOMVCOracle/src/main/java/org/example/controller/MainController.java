package org.example.controller;

import org.example.model.impls.BiciDAOImpl;
import org.example.model.impls.PropietariDAOImpl;
import org.example.model.impls.RevisioDAOImpl;
import org.example.view.ModelComponentsVisuals;
import org.example.view.Vista;

public class MainController {
    private BiciDAOImpl bicicletaDAO;
    private PropietariDAOImpl propietariDAO;
    private RevisioDAOImpl revisioDAO;
    private Vista view;
    private ModelComponentsVisuals modelComponentsVisuals;
    private ViewController viewController;

    public MainController() {
        bicicletaDAO = new BiciDAOImpl();
        propietariDAO = new PropietariDAOImpl();
        revisioDAO = new RevisioDAOImpl();
        view = new Vista();
        modelComponentsVisuals = new ModelComponentsVisuals();
        viewController = new ViewController(view, modelComponentsVisuals);

        new BiciController(bicicletaDAO, view, viewController);
        new PropietariController(propietariDAO, view, viewController);
        new RevisioController(revisioDAO, view, viewController);

        view.setVisible(true);
    }
}
