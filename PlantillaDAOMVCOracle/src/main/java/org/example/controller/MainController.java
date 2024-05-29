package org.example.controller;

import org.example.model.impls.BiciDAOImpl;
import org.example.model.impls.PropietariDAOImpl;
import org.example.model.impls.RevisioDAOImpl;
import org.example.view.ModelComponentsVisuals;
import org.example.view.Vista;

/**
 * Controlador principal de l'aplicació.
 *
 * Aquest controlador inicialitza tots els components principals de l'aplicació,
 * incloent els DAOs per a les bicicletes, els propietaris i les revisions, així com la vista principal.
 * Configura també els controladors secundaris per a cada entitat gestionada.
 *
 * @author Andreu Gisbert Bel
 * @version 1.0
 */

public class MainController {
    protected BiciDAOImpl bicicletaDAO;
    protected PropietariDAOImpl propietariDAO;
    protected RevisioDAOImpl revisioDAO;
    protected Vista view;
    protected ViewController viewController;

    /**
     * Constructor del controlador principal.
     *
     * Crea instàncies de les implementacions DAO per a bicicletes, propietaris i revisions,
     * així com la vista principal i els components visuals del model. Inicialitza els controladors
     * específics per a cada entitat i mostra la vista principal.
     */

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

    /**
     * Inicialitza els controladors per a les entitats gestionades: bicicletes, propietaris i revisions.
     *
     * Crea i assigna un controlador per a cada entitat, configurant les interaccions necessàries
     * entre la vista i els models a través del ViewController.
     */

    private void initControllers() {
        new BiciController(bicicletaDAO, view, viewController);
        new PropietariController(propietariDAO, view, viewController);
        new RevisioController(revisioDAO, view, viewController);
    }
}