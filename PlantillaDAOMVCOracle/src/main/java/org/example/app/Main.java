package org.example.app;

import org.example.controller.MainController;

import javax.swing.*;
import java.util.Locale;

/**
 * Classe principal de l'aplicació.
 *
 * Aquesta classe configura la localització per defecte de l'aplicació a Català (Espanya)
 * i inicia el controlador principal de l'aplicació.
 *
 * @author Andreu Gisbert Bel
 * @version 1.0
 */

public class Main {

    /**
     * El mètode principal que llança l'aplicació.
     *
     * Aquest mètode estableix el locale per defecte de l'aplicació i inicia el MainController
     * en un nou fil d'execució de Swing.
     *
     * @param args arguments de la línia de comandes (no utilitzats)
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Locale.setDefault(new Locale("ca","ES"));
            new MainController();
        });
    }
}